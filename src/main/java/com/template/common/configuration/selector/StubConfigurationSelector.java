package com.template.common.configuration.selector;

import com.template.common.configuration.StubConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class StubConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{
                StubConfiguration.class.getName()
        };

    }
}
