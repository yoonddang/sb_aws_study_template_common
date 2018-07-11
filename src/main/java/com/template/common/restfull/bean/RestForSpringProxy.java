package com.template.common.restfull.bean;

import org.springframework.web.client.RestTemplate;

public interface RestForSpringProxy {

	public abstract Class<?> getTargetType(Dummy dummy);

	public abstract RestTemplate getInvoker(Dummy dummy);
}
