package com.template.common.restfull.bean;

import com.template.common.restfull.RestUtils;
import com.template.common.restfull.annotation.RestStubMethod;
import com.template.common.restfull.annotation.RestStubParameter;
import com.template.common.restfull.annotation.RestStubParameterBody;
import com.template.common.restfull.exception.RestForSpringException;
import com.template.common.restfull.invoker.RestRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

public final class RestRegistry {


	private final static Logger logger = LoggerFactory.getLogger(RestRegistry.class);

	private static final Set<Class<?>> STUB_SET = new HashSet<Class<?>>();

	private static final Map<Method, RestUrlInfo> METHOD_URL_MAP = new HashMap<Method, RestUrlInfo>();

	/**
	 * 디폴트 생성자는 막는다.
	 */
	private RestRegistry() {
		// do nothing
	}

	/**
	 * URA 자원 스텁을 등록한다.
	 * 
	 * @param stubType
	 *            URA 자원 스텁 타입
	 * @param uraPrefix
	 *            URA 프리픽스
	 */
	static void registerRestStub(Class<?> stubType, String uraPrefix, String host) {
		for (Method method : findAllNonPrivateMethod(stubType)) {
			registerMethodUrl(method, uraPrefix, host);
		}

		STUB_SET.add(stubType);
	}

	/**
	 * 등록된 스텁 타입인지 확인한다.
	 * 
	 * @param stubType
	 *            스텁 타입
	 * @return 등록 여부
	 */
	static boolean isRegistered(Class<?> stubType) {
		return STUB_SET.contains(stubType);
	}

	/**
	 * 등록된 스텁 메소드인지 확인한다.
	 * 
	 * @param stubMethod
	 *            스텁 메소드
	 * @return 등록 여부
	 */
	static boolean isRegistered(Method stubMethod) {
		return METHOD_URL_MAP.containsKey(stubMethod);
	}

	/**
	 * 스텁 메소드와 메소드 호출 파라미터를 가지고 원격 자원 호출을 위한  객체를 생성한다.
	 * @param stubMethod
	 * @param arg
	 * @return
	 */
	public static RestRequest createRequest(Method stubMethod, Object... arg) {
		RestUrlInfo restUrlInfo = METHOD_URL_MAP.get(stubMethod);
		Map<String, Object> uriVariables = new HashMap<>();
		Object requestBody = null;
		for(int idx = 0 ;idx < stubMethod.getParameters().length ; idx ++) {
			Parameter parameter = stubMethod.getParameters()[idx];
			if(parameter.getAnnotation(RestStubParameter.class) != null) {
				RestStubParameter pathVariable = parameter.getAnnotation(RestStubParameter.class);
				uriVariables.put(pathVariable.name(), arg[idx]);
			} else if(parameter.getAnnotation(RestStubParameterBody.class) != null) {
				requestBody = arg[idx];
			} else {
				if(requestBody == null) {
					requestBody = arg[idx];
				}
			}
		}
		if (restUrlInfo != null) {
			return new RestRequest(restUrlInfo, requestBody, uriVariables);
		}

		throw new RestForSpringException("Requested stub method [" + stubMethod + "] is not registered");
	}

	/**
	 * 하나의 URA 스텁 메소드를 등록한다.
	 * 
	 * @param method
	 *            URA 스텁 메소드
	 * @param urlPrefix
	 *            URA 프리픽스
	 */
	private static void registerMethodUrl(Method method, String urlPrefix, String host) {

		RestStubMethod methodUrlAnno = method.getAnnotation(RestStubMethod.class);

		String urlPostfix;
		if (methodUrlAnno != null && StringUtils.isNotEmpty(methodUrlAnno.value())) {
			urlPostfix = methodUrlAnno.value();
		} else {
			urlPostfix = "/" + method.getName();
		}

		RestUrlInfo info = new RestUrlInfo(urlPrefix + urlPostfix, host, methodUrlAnno.requestMethod());
		METHOD_URL_MAP.put(method, info);

		if (logger.isDebugEnabled()) {
			logger.debug("Registered stub method [" + method + "] for URA: " + info.getUrl());
		}
	}

	/**
	 * 주어진 타입의 모든 non private 메소드를 찾는다.
	 * 
	 * @param type
	 *            탐색할 타입
	 * @return 메소드 컬렉션
	 */
	private static Collection<Method> findAllNonPrivateMethod(Class<?> type) {
		if (type.isInterface()) {
			return Collections.unmodifiableCollection(Arrays.asList(type.getMethods()));
		}

		List<Method> methodList = new ArrayList<Method>();
		for (Method me : type.getMethods()) {
			if (!RestUtils.isMethodOfObject(me)) {
				methodList.add(me);
			}
		}

		Class<?> current = type;

		while (current != Object.class) {
			for (Method method : current.getDeclaredMethods()) {
				int modifier = method.getModifiers();

				if (Modifier.isPrivate(modifier)) {
					continue;
				} else if (Modifier.isPublic(modifier)) {
					continue;
				} else /* if protected or package private */{
					boolean exist = false;
					for (Method me : methodList) {
						if (me.getName().equals(method.getName())
								&& Arrays.equals(me.getParameterTypes(), method.getParameterTypes())) {
							exist = true;
							break;
						}
					}

					if (!exist) {
						methodList.add(method);
					}
				}
			}

			current = current.getSuperclass();
		}

		return Collections.unmodifiableCollection(methodList);
	}

	/**
	 * 등록된 스텁을 모두 제거한다. 단위 테스트를 위한 메소드이다.
	 */
	static void reset() {
		STUB_SET.clear();
		METHOD_URL_MAP.clear();
	}

}
