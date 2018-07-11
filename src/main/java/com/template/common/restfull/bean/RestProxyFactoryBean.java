package com.template.common.restfull.bean;

import com.template.common.configuration.properties.Servers;
import com.template.common.restfull.RestUtils;
import com.template.common.restfull.annotation.RestServer;
import com.template.common.restfull.annotation.RestStub;
import com.template.common.restfull.exception.RestForSpringException;
import com.template.common.restfull.invoker.RestMethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.RestTemplate;

/**
 * Rest 자원에 대한 프록시를 생성해 주는 Spring factory bean
 */

public class RestProxyFactoryBean implements FactoryBean<Object>, InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Class<?> objectType;
	private RestTemplate restTemplate;
	private String urlPrefix;
	private RestServer restServer;
	private Servers servers;

	private Object proxyObject = null;

	/**
	 * Spring 컨테이너가 객체 초기화를 위해 호출하는 메소드. 스텁 정보를 등록하고 프록시 객체를 생성한다.
	 */
	public void afterPropertiesSet() throws Exception {
		assignUrlPrefix();
		assignRestServer();

		if (logger.isDebugEnabled()) {
			logger.debug("Creating proxy for Rest stub [" + objectType.getCanonicalName() + "]" + " with URL prefix: "
					+ urlPrefix);
		}

		RestRegistry.registerRestStub(objectType, urlPrefix, servers.getUrl(restServer.getKey()));

		RestMethodInterceptor interceptor = new RestMethodInterceptor(objectType, restTemplate);
		proxyObject = RestUtils.createProxy(interceptor, objectType, RestForSpringProxy.class);

		if (logger.isInfoEnabled()) {
			logger.info("Created proxy for Uest stub [" + objectType.getCanonicalName() + "]" + " with URL prefix: "
					+ urlPrefix);
		}
	}

	private void assignRestServer() {
		RestStub anno = objectType.getAnnotation(RestStub.class);

		if (anno == null) {
			throw new RestForSpringException("URL prefix is not specified for the Rest stub: "
					+ objectType.getCanonicalName());
		}

		restServer = anno.restServer();
	}

	private void assignUrlPrefix() {
		if (StringUtils.isNotEmpty(urlPrefix)) {
			return;
		}
		RestStub anno = objectType.getAnnotation(RestStub.class);

		if (anno == null || StringUtils.isEmpty(anno.value())) {
			throw new RestForSpringException("URL prefix is not specified for the Rest stub: "
					+ objectType.getCanonicalName());
		}

		urlPrefix = anno.value();
	}

	/**
	 * 프록시를 반환한다. Spring 컨테이너가 이 factory bean이 생성한 bean을 가져가기 위해 호출한다.
	 */
	public Object getObject() throws Exception {
		return proxyObject;
	}

	/**
	 * 스텁 타입을 반환한다. Spring 컨테이너가 이 factory bean이 생성하는 bean의 타입을 알아내기 위해 호출한다.
	 */
	public Class<?> getObjectType() {
		return objectType;
	}

	/**
	 * 싱글턴 여부를 반환한다. 항상 true를 리턴한다.
	 */
	public boolean isSingleton() {
		return true;
	}

	@Required
	public void setObjectType(Class<?> objectType) {
		this.objectType = objectType;
	}


	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setServers(Servers servers) {
		this.servers = servers;
	}

	public void setUraPrefix(String uraPrefix) {
		this.urlPrefix = uraPrefix;
	}

	public void setRestServer(RestServer restServerUrl) {
		this.restServer = restServerUrl;
	}
}
