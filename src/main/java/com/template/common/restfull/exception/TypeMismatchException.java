package com.template.common.restfull.exception;

/**
 * 원격 자원이 반환한 타입이 스텁 메소드의 리턴타입에 대입 불가능할 때 발생하는 예외
 */
@SuppressWarnings("serial")
public class TypeMismatchException extends RestForSpringException {
	/**
	 * 디폴트 생성자는 막아둔다.
	 */
	protected TypeMismatchException() {
		// do nothing
	}
	
	/**
	 * 생성자.
	 * 
	 * @param message 메시지
	 */
	public TypeMismatchException(String message) {
		super(message);
	}
	
	/**
	 * 생성자.
	 * 
	 * @param cause 원인이 된 예외
	 */
	public TypeMismatchException(Throwable cause) {
		super(cause);
	}

	/**
	 * 생성자.
	 * 
	 * @param message 메시지
	 * @param cause 원인이 된 예외
	 */
	public TypeMismatchException(String message, Throwable cause) {
		super(message, cause);
	}
}
