package com.template.common.restfull.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestStub {
	/**
	 * 호출대상 서버
	 * 
	 * @return PROCESS_SERVER, BOARD_SERVER
	 */
	RestServer restServer() default RestServer.PROCESS_SERVER;
	
	/**
	 * Class RequstMapping
	 * 
	 * @return Class의 RequestMapping url
	 */
	String value() default "";
}
