package com.template.common.model.common;

import com.template.common.util.JsonUtils;

import java.util.List;
import java.util.Map;

public class ResultInfo {

	private Code code ;
	private String messageKey;
	private String message;
	private Map<String, Object> resultData;
	private int rank;

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getResultData() {
		return resultData;
	}

	public void setResultData(Map<String, Object> resultData) {
		this.resultData = resultData;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
}