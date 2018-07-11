package com.template.common.restfull.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestStubMethod {
	
	/**
	 * Method 호출 방식
	 * 
	 * @return RequestMethod.GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE
	 */
	RequestMethod requestMethod() default RequestMethod.GET;
	
	/**
	 * Method RequstMapping
	 * mehtod name 과 동일하다면 생략 가능하다.
	 * 
	 * @return method의 RequestMapping url
	 */
	String value() default "";
}