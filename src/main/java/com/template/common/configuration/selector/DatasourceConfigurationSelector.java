package com.template.common.configuration.selector;

import com.template.common.configuration.DatasourceConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class DatasourceConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{
                DatasourceConfiguration.class.getName()
        };

    }
}
