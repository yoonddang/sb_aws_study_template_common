package com.template.common.datasource;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.Map;
import java.util.Properties;

/**
 * 멀티 데이터소스 생성 BeanFactory
 */
public class DataSourcesBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private static Logger logger = LoggerFactory.getLogger(DataSourcesBeanFactoryPostProcessor.class);

    private Map<String, DatasourceMeta> jdbcUrlMap;
    private String baseDataSource;

    private StringEncryptor encryptor;

    public void setJdbcUrlMap(Map<String, DatasourceMeta> jdbcUrlMap) {
        this.jdbcUrlMap = jdbcUrlMap;
    }

    public void setBaseDataSource(String baseDataSource) {
        this.baseDataSource = baseDataSource;
    }

    public void setEncryptor(StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        logger.debug("////////////////////  postProcessBeanDefinitionRegistry /////////////////////////");
        DatasourceMeta datasourceMeta = jdbcUrlMap.get(baseDataSource);

        String password = datasourceMeta.getPassword();
        BeanDefinitionBuilder datasourceDefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("dataSource.xa.base");

        datasourceDefinitionBuilder.addPropertyValue("jdbcUrl", datasourceMeta.getUrl());
        datasourceDefinitionBuilder.addPropertyValue("username", datasourceMeta.getUserid());
        datasourceDefinitionBuilder.addPropertyValue("password", password);

        registry.registerBeanDefinition("dataSource", datasourceDefinitionBuilder.getBeanDefinition());

        atomicDefauiltDatasourceDefinitionBuilder(registry, jdbcUrlMap.get(baseDataSource), "dataSource");

        for (String key : jdbcUrlMap.keySet()) {
            /** atomic dataSource **/
            logger.debug("Dynamic Bean Definition !!!! => atomic dataSource {} :: {}", key, jdbcUrlMap.get(key));
            atomicDatasourceDefinitionBuilder(registry, jdbcUrlMap.get(key), key);
        }
    }

    /**
     * 2phase 트랜젝션 datasource 생성
     *
     * @param registry
     * @param datasourceMeta
     * @param key
     */
    private void atomicDefauiltDatasourceDefinitionBuilder(BeanDefinitionRegistry registry, DatasourceMeta datasourceMeta, String key) {
        BeanDefinitionBuilder atomicDatasourceDefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("dataSource.xa.base");

        String dataSourceName = key;
        String sqlSessionFactoryName = "sqlSessionFactory";
        String sqlSessionTemplateName = "sqlSessionTemplate";
        String uniqueResourceName = "MySQLXA.template.default";

        String password = datasourceMeta.getPassword();

        Properties xaProperties = new Properties();
        xaProperties.setProperty("user", datasourceMeta.getUserid());
        xaProperties.setProperty("password", password);
        xaProperties.setProperty("url", datasourceMeta.getUrl());

        atomicDatasourceDefinitionBuilder.addPropertyValue("uniqueResourceName", uniqueResourceName);
        atomicDatasourceDefinitionBuilder.addPropertyValue("xaProperties", xaProperties);
        registry.registerBeanDefinition(dataSourceName, atomicDatasourceDefinitionBuilder.getBeanDefinition());

        BeanDefinitionBuilder sqlSessionXADefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("sqlSessionFactory.xa.base");
        sqlSessionXADefinitionBuilder.addPropertyReference("dataSource", dataSourceName);
        registry.registerBeanDefinition(sqlSessionFactoryName, sqlSessionXADefinitionBuilder.getBeanDefinition());

        BeanDefinitionBuilder sqlSessionXATemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class);
        sqlSessionXATemplateBuilder.addConstructorArgReference(sqlSessionFactoryName);
        registry.registerBeanDefinition(sqlSessionTemplateName, sqlSessionXATemplateBuilder.getBeanDefinition());
    }

    /**
     * 2phase 트랜젝션 datasource 생성
     *
     * @param registry
     * @param datasourceMeta
     * @param key
     */
    private void atomicDatasourceDefinitionBuilder(BeanDefinitionRegistry registry, DatasourceMeta datasourceMeta, String key) {
        BeanDefinitionBuilder atomicDatasourceDefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("dataSource.xa.base");


        String dataSourceName = "dataSource.xa." + key;
        String sqlSessionFactoryName = "sqlSessionFactory.xa." + key;
        String sqlSessionTemplateName = "sqlSessionTemplate.xa." + key;
        String uniqueResourceName = "MySQLXA.template." + key;

        String password = datasourceMeta.getPassword();

        Properties xaProperties = new Properties();
        xaProperties.setProperty("user", datasourceMeta.getUserid());
        xaProperties.setProperty("password", password);
        xaProperties.setProperty("url", datasourceMeta.getUrl());

        atomicDatasourceDefinitionBuilder.addPropertyValue("uniqueResourceName", uniqueResourceName);
        atomicDatasourceDefinitionBuilder.addPropertyValue("xaProperties", xaProperties);
        registry.registerBeanDefinition(dataSourceName, atomicDatasourceDefinitionBuilder.getBeanDefinition());

        BeanDefinitionBuilder sqlSessionXADefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("sqlSessionFactory.xa.base");
        sqlSessionXADefinitionBuilder.addPropertyReference("dataSource", dataSourceName);
        registry.registerBeanDefinition(sqlSessionFactoryName, sqlSessionXADefinitionBuilder.getBeanDefinition());

        BeanDefinitionBuilder sqlSessionXATemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class);
        sqlSessionXATemplateBuilder.addConstructorArgReference(sqlSessionFactoryName);
        registry.registerBeanDefinition(sqlSessionTemplateName, sqlSessionXATemplateBuilder.getBeanDefinition());
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        logger.debug("////////////////////  postProcessBeanFactory /////////////////////////");
    }

}