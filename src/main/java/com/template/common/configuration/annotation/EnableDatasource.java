package com.template.common.configuration.annotation;

import com.template.common.configuration.selector.DatasourceConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(DatasourceConfigurationSelector.class)
public @interface EnableDatasource {
}
