package com.template.common.restfull.invoker;

import com.template.common.restfull.RestUtils;
import com.template.common.restfull.bean.RestForSpringProxy;
import com.template.common.restfull.bean.RestRegistry;
import com.template.common.restfull.exception.TypeMismatchException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;

public class RestMethodInterceptor implements MethodInterceptor {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Class<?> targetType;
	protected final RestTemplate restTemplate;
	MultiValueMap<String, String> headers;
	public RestMethodInterceptor(Class<?> targetType, RestTemplate restTemplate) {
		this.targetType = targetType;
		this.restTemplate = restTemplate;
		headers = new HttpHeaders();
		headers.set("user-agent", "template-template-server");
		headers.set("content-type", "application/json");
		headers.set("contentType", "application/json; charset=UTF-8");
		headers.set("Accept", "application/json");
	}

	/**
	 * 프록시의 메소드가 호출됐을 때 호출되는 메소드.
	 * 
	 * {@link Object}나 {@link RestForSpringProxy}의 메소드는 별도로 처리하고, 나머지 메소드는 모두 원격
	 * 자원 호출로 처리한다.
	 */
	public final Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();

		if (RestUtils.isMethodOfObject(method)) {
			return handleObjectMethods(invocation);
		} else if (RestForSpringProxy.class == method.getDeclaringClass()) {
			return handleRestForSpringMethods(invocation);
		}

		Object result = getResult(invocation);
		return handleResult(invocation.getMethod(), result);
	}

	/**
	 * 원격 자원을 호출하고 그 결과를 반환한다.
	 * 
	 * @param invocation
	 *            프록시 메소드 호출 정보
	 * @return 원격 자원의 응답
	 */
	protected Object getResult(MethodInvocation invocation) {

		RestRequest req = RestRegistry.createRequest(invocation.getMethod(), invocation.getArguments());
		Object result = null;
		if (req.getRequestMehtod() == RequestMethod.GET) {
			try {
				ResponseEntity result1 = restTemplate.exchange(req.getUrl(), HttpMethod.GET, new HttpEntity(headers), ParameterizedTypeReference.forType(invocation.getMethod().getGenericReturnType()), req.getUriVariables());
				result =  result1.getBody();
			} catch(HttpServerErrorException e) {
				errorLog(req, e);
				throw e;
			}
		} else if (req.getRequestMehtod() == RequestMethod.POST) {
			try {
				ResponseEntity result1 = restTemplate.exchange(req.getUrl(), HttpMethod.POST, new HttpEntity(req.getArg(), headers), ParameterizedTypeReference.forType(invocation.getMethod().getGenericReturnType()), req.getUriVariables());
				result = result1.getBody();
			} catch(HttpServerErrorException e) {
				errorLog(req, e);
				throw e;
			}
		}
		return result;
	}

	private void errorLog(RestRequest req, HttpServerErrorException e) {
		logger.error("HttpServerError request = {}",req.getUrl());
		logger.error("HttpServerError status  = {}",e.getStatusCode());
		logger.error("HttpServerError status  = {}",e.getResponseBodyAsString());
		logger.error("HttpServerError ", e);
	}

	/**
	 * {@link Object}의 메소드들을 처리한다.
	 * 
	 * {@link Object#toString()}만 직접 처리하고, 나머지는 프록시 대상이 처리하도록 맡긴다.
	 * 
	 * @param invocation
	 *            프록시 메소드 호출 정보
	 * @return 호출 결과
	 * @throws Throwable
	 *             throwable
	 */
	private Object handleObjectMethods(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		if ("toString".equals(method.getName())) {
			return "Rest for Spring Proxy for " + targetType.getCanonicalName();
		}

		return invocation.proceed();
	}

	private Object handleRestForSpringMethods(MethodInvocation invocation) {
		Method method = invocation.getMethod();

		if ("getTargetType".equals(method.getName())) {
			return targetType;
		} else if ("getInvoker".equals(method.getName())) {
			return restTemplate;
		}

		return null;
	}

	/**
	 * 원격 자원 호출 결과를 처리한다.
	 * 
	 * 호출 결과 예외가 반환될 경우 예외를 던진다. 예외가 아닐 경우에는, 호출된 메소드의 리턴타입과 호출 결과과 호환 가능한지 체크한
	 * 후 결과를 반환하낟.
	 * 
	 * @param method
	 *            호출된 메소드
	 * @param result
	 *            원격 자원의 응답
	 * @return 결과값
	 * @throws Throwable
	 *             예외
	 */
	private Object handleResult(Method method, Object result) throws Throwable {
		if (result instanceof Throwable) {
			if ((result instanceof Exception) && !(result instanceof RuntimeException)) {
				for (Class<?> ex : method.getExceptionTypes()) {
					if (ex.isAssignableFrom(result.getClass())) {
						throw (Exception) result;
					}
				}

				throw new TypeMismatchException("The result exception(" + result.getClass().getCanonicalName()
						+ ") is not declared in the stub method's throw clause: " + method);
			}

			throw (Throwable) result;
		}

		Class<?> stubReturnType = method.getReturnType();

		if (void.class.isAssignableFrom(stubReturnType)) {
			return null;
		}

		Class<?> resultType = (result == null) ? null : result.getClass();

		if (!ClassUtils.isAssignable(resultType, stubReturnType, true)) {
			if (result == null) {
				throw new TypeMismatchException("The return type(" + stubReturnType.getName()
						+ ") of the stub method is primitive but the result is null: " + method);
			} else {
				throw new TypeMismatchException("Cannot cast the return value(" + resultType.getName()
						+ ") to the return type(" + stubReturnType.getName() + ") of the stub method: " + method);
			}
		}

		return result;
	}
}
