package com.template.common.restfull;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;

public final class RestUtils {
	private final static String[] OBJECT_METHOD_WITH_NO_PARAMETER = { "clone", "finalize", "hashCode", "toString",
			"getClass", "wait", "notify", "notifyAll" };

	/**
	 * Util 클래스이므로 인스턴스 생성을 막기 위해 생성자를 private으로 선언한다.
	 */
	private RestUtils() {
		// do nothing
	}

	/**
	 * 주어진 메소드가 {@link Object}로부터 상속받은 메소드인지 판단한다.
	 * 
	 * @param method 
	 *            메소드.
	 * @return method가 {@link Object]로부터 상속받은 메소드라면 true. 아니라면 false.
	 */
	public static boolean isMethodOfObject(Method method) {
		String name = method.getName();
		Class<?>[] paramTypes = method.getParameterTypes();

		if (paramTypes.length == 0) {
			return ArrayUtils.contains(OBJECT_METHOD_WITH_NO_PARAMETER, name);
		} else if (paramTypes.length == 1) {
			if (paramTypes[0] == String.class) {
				return "toString".equals(name);
			} else if (paramTypes[0] == long.class) {
				return "wait".equals(name);
			} else if (paramTypes[0] == Object.class) {
				return "equals".equals(name);
			}
		} else if (paramTypes.length == 2) {
			if (paramTypes[0] == long.class && paramTypes[1] == int.class) {
				return "wait".equals(name);
			}
		}

		return false;
	}

	/**
	 * 스텁에 적절한 타입인지 체크한다.
	 * 
	 * @param type
	 *            체크할 타입
	 * @return type이 어노테이션이거나 enum일 경우 false. 아니라면 true.
	 */
	public static boolean isSuitableForStub(Class<?> type) {
		return !type.isAnnotation() && !type.isEnum();
	}

	/**
	 * 프록시를 생성한다.
	 * 
	 * @param <T>
	 *            프록시의 타입
	 * @param interceptor
	 *            프록시의 메소드 호출을 처리할 인터셉터
	 * @param type
	 *            프록시의 타입
	 * @param interfaces
	 *            프록시가 구현할 인터페이스들
	 * @return 프록시
	 */
	public static <T> T createProxy(MethodInterceptor interceptor, Class<T> type, Class<?> interfaces) {
		ProxyFactory factory = new ProxyFactory();
		factory.addAdvice(interceptor);

		if (type.isInterface()) {
			factory.addInterface(type);
		} else {
			factory.setProxyTargetClass(true);
			factory.setTargetClass(type);
		}

		factory.addInterface(interfaces);

		return type.cast(factory.getProxy());
	}
}
