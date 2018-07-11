package com.template.common.restfull.bean;

import com.template.common.configuration.properties.Servers;
import com.template.common.restfull.annotation.RestStub;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 주어진 클래스패스에서 Rest for Spring을 위한 스텁을 자동으로 찾아내서 Spring 컨테이너에 등록한다.
 * 
 */
public class RestForSpringAutoScanner implements BeanFactoryPostProcessor, ApplicationContextAware {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ApplicationContext context;

	private List<String> packageList;
	private RestTemplate restTemplate;
	private Servers servers;

	/**
	 * 주어진 클래스패스에서 RequestBroker for Spring을 위한 스텁을 자동으로 찾아내서 Spring 컨테이너에 등록한다.
	 * Spring 컨테이너에 의해 호출된다.
	 */
	@SuppressWarnings("serial")
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) factory;

		String[] scanPackages = null;
		if (packageList == null || packageList.isEmpty()) {
			scanPackages = new String[] { "" };
		} else {
			scanPackages = packageList.toArray(new String[packageList.size()]);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("RB for Spring auto scan start. packages: " + packageList);
		}

		try {
			Set<String> uraResourceNameSet = findClassWithAnnotation(RestStub.class, scanPackages);

			if (!uraResourceNameSet.isEmpty()) {
				if (logger.isDebugEnabled()) {
					logger.debug("REST for Spring REST stub candidates: " + uraResourceNameSet);
				}

				Set<String> validSet = removeInvalidRestStubs(uraResourceNameSet);
				registerBeans(validSet, RestProxyFactoryBean.class, beanFactory);
				if (logger.isInfoEnabled()) {
					logger.info("REST stubs registered to the Spring container by REST for Spring auto scanner: "
							+ uraResourceNameSet);
				}
			}

		} catch (Exception e) {
			throw new BeansException("REST for Spring auto scan failed. packages: " + packageList, e) {
			};
		}
	}

	/**
	 * Rest 스텁의 경우, @RestStub의 value 속성값이 지정 되어 있지 않으면 자동 등록이 불가능하기 때문에 이런 스텁을 제거해
	 * 준다.
	 * 
	 * @param beanTypeSet
	 *            점검할 타입 셋
	 * @throws ClassNotFoundException
	 *             클래스 로드 실패
	 */
	private Set<String> removeInvalidRestStubs(Set<String> beanTypeSet) throws ClassNotFoundException {
		Set<String> validSet = new HashSet<String>();

		for (String type : beanTypeSet) {
			Class<?> clazz = context.getClassLoader().loadClass(type);
			RestStub anno = clazz.getAnnotation(RestStub.class);

			if (StringUtils.isEmpty(anno.value())) {
				if (logger.isWarnEnabled()) {
					logger.warn("Cannot register automatically discovered REST stub [" + type
							+ "] because of missing value attribute");
				}

			} else {
				validSet.add(type);
			}
		}

		return validSet;
	}

	/**
	 * beanTypeSet에 주어진 타입들을 Spring 컨테이너에 등록한다. 이때, factoryType으로 주어진 factory
	 * bean을 통해 등록한다.
	 * 
	 * @param beanTypeSet
	 *            등록할 스텁 타입명 목록
	 * @param factoryType
	 *            등록할 때 사용할 factory bean 타입
	 * @param factory
	 *            Spring DefaultListableBeanFactory
	 * @throws IOException
	 *             beanTypeSet에 주어진 타입을 로드할 때 발생할 수 있음
	 * @throws ClassNotFoundException
	 *             beanTypeSet에 주어진 타입을 로드할 때 발생할 수 있음
	 */
	void registerBeans(Set<String> beanTypeSet, Class<? extends FactoryBean<?>> factoryType,
			DefaultListableBeanFactory factory) throws IOException, ClassNotFoundException {
		for (String type : beanTypeSet) {
			Class<?> clazz = context.getClassLoader().loadClass(type);

			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(factoryType);
			builder.addPropertyValue("objectType", clazz);
			builder.addPropertyValue("restTemplate", restTemplate);
			builder.addPropertyValue("servers", servers);

			BeanDefinition beanDefinition = builder.getBeanDefinition();

			factory.registerBeanDefinition(createBeanName(clazz), beanDefinition);
		}
	}

	/**
	 * Spring 컨테이너에 등록할 때 사용할 빈 이름을 생성한다.
	 * 
	 * @param clazz
	 *            스텁 타입
	 * @return 빈 이름
	 */
	String createBeanName(Class<?> clazz) {
		return "Rest for Spring Proxy@" + clazz.getCanonicalName();
	}

	/**
	 * rootPackages들로부터 주어진 어노테이션이 달린 타입을 찾는다. 이 과정에서 클래스가 로드되지 않는다.
	 * 
	 * @param annotationType
	 *            어노테이션 타입
	 * @param rootPackages
	 *            대상 패키지들
	 * @return 어노테이션이 달린 타입 셋
	 * 
	 * @throws IOException
	 *             파일 관련 작업 실패시 발생
	 */
	Set<String> findClassWithAnnotation(final Class<? extends Annotation> annotationType, String[] rootPackages)
			throws IOException {
		final Set<String> found = new HashSet<String>();

		for (String pkg : rootPackages) {
			Resource[] resources = context.getResources("classpath*:" + pkg.replace('.', '/') + "/**/*.class");

			for (Resource resource : resources) {
				ClassReader reader = new ClassReader(resource.getInputStream());
				ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4) {
					private boolean acceptable = false;
					private String currentType = null;

					public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
						currentType = name;
						
						if ((access & Opcodes.ACC_ANNOTATION) == 0 && (access & Opcodes.ACC_ENUM) == 0) {
							acceptable = true;
						}
					}

					public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
						if (acceptable && desc.equals(Type.getDescriptor(annotationType))) {
							found.add(currentType.replace('/', '.'));
						}
						
						return super.visitAnnotation(desc, visible);
					}
					
					
				};
				
				reader.accept(visitor, 0);
			}
		}
		return found;
	}

	public void setPackage(List<String> packageList) {
		this.packageList = packageList;
	}


	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setServers(Servers servers) {
		this.servers = servers;
	}

	@Autowired(required = true)
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}