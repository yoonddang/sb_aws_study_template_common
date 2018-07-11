package com.template.common.restfull.invoker;

import com.template.common.restfull.bean.RestUrlInfo;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

public class RestRequest {
	private final RestUrlInfo restUrlInfo;
	final Object arg;
	Map<String, Object> uriVariables;

	public RestRequest(RestUrlInfo restUrlInfo, Object arg, Map<String, Object> uriVariables) {
		this.restUrlInfo = restUrlInfo;
		this.arg = arg;
		this.uriVariables = uriVariables;
	}

	public Object getArg() {
		return arg;
	}

	public Map<String, Object> getUriVariables() {
		return uriVariables;
	}

	@Override
	public String toString() {
		return "RestRequest to URL [" + getUrl() + "] [" + getRequestMehtod() + "] with parameters: " + arg;
	}

	public String getUrl() {
		return restUrlInfo.getUrl();
	}

	public RequestMethod getRequestMehtod() {
		return restUrlInfo.getRequestMethod();
	}

}