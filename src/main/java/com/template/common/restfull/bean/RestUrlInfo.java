package com.template.common.restfull.bean;

import org.springframework.web.bind.annotation.RequestMethod;

public class RestUrlInfo {

	private final String url;
	private final RequestMethod requestMethod;

	public RestUrlInfo(String url, String host, RequestMethod requestMethod) {
		this.url = host + url;
		this.requestMethod = requestMethod;
	}

	public String getUrl() {
		return url;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}
}