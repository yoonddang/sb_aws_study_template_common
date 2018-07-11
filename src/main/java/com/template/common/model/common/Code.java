package com.template.common.model.common;

public enum Code {

	SUCCESS  ("0"),
	FAIL ("1"),
	ERROR ("2");

	private String value;

	Code(String value) {
		this.value = value;
	}


	public String toString() {
		return this.value;
	}

	public String getValue() {
		return this.value;
	}
}
