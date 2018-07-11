package com.template.common.restfull.exception;

@SuppressWarnings("serial")
public class RestForSpringException extends RuntimeException {
	/**
	 * 디폴트 생성자는 막아둔다.
	 */
	protected RestForSpringException() {
		// do nothing
	}
	
	/**
	 * 생성자.
	 * 
	 * @param message 메시지
	 */
	public RestForSpringException(String message) {
		super(message);
	}
	
	/**
	 * 생성자.
	 * 
	 * @param cause 원인이 된 예외
	 */
	public RestForSpringException(Throwable cause) {
		super(cause);
	}

	/**
	 * 생성자.
	 * 
	 * @param message 메시지
	 * @param cause 원인이 된 예외
	 */
	public RestForSpringException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
