package com.template.common.model.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BaseObject {

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public boolean equals(Object object) {
		return EqualsBuilder.reflectionEquals(this, object);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
