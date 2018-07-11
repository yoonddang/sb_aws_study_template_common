package com.template.common.datasource;

import org.jasypt.encryption.StringEncryptor;
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
//AtomikosNonXAConnectionFactory
public class DataSourcesBeanMsSqlFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private static Logger logger = LoggerFactory.getLogger(DataSourcesBeanMsSqlFactoryPostProcessor.class);

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
		BeanDefinitionBuilder datasourceDefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("dataSource.mssql.base");

		datasourceDefinitionBuilder.addPropertyValue("jdbcUrl", datasourceMeta.getUrl());
		datasourceDefinitionBuilder.addPropertyValue("username", datasourceMeta.getUserid());
		datasourceDefinitionBuilder.addPropertyValue("password", password);

		registry.registerBeanDefinition("dataSource", datasourceDefinitionBuilder.getBeanDefinition());

		atomicMsSqlDefauiltDatasourceDefinitionBuilder(registry, jdbcUrlMap.get(baseDataSource), "dataSource");

		for (String key : jdbcUrlMap.keySet()) {
			/** atomic dataSource **/
			logger.debug("Dynamic Bean Definition !!!! => atomic dataSource {} :: {}", key, jdbcUrlMap.get(key));
			atomicDatasourceDefinitionBuilder(registry, jdbcUrlMap.get(key), key);
		}
	}

	private void atomicMsSqlDefauiltDatasourceDefinitionBuilder(BeanDefinitionRegistry registry, DatasourceMeta datasourceMeta, String key) {
		BeanDefinitionBuilder atomicDatasourceDefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("dataSource.mssql.base");

		String dataSourceName = key;
		String sqlSessionFactoryName = "sqlSessionFactory";
		String sqlSessionTemplateName = "sqlSessionTemplate";
		String uniqueResourceName = "MsSQL.template.default";

		String password = datasourceMeta.getPassword();

		atomicDatasourceDefinitionBuilder.addPropertyValue("uniqueResourceName", uniqueResourceName);
		atomicDatasourceDefinitionBuilder.addPropertyValue("driverClassName", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		atomicDatasourceDefinitionBuilder.addPropertyValue("user", datasourceMeta.getUserid());
		atomicDatasourceDefinitionBuilder.addPropertyValue("password", password);
		atomicDatasourceDefinitionBuilder.addPropertyValue("url", datasourceMeta.getUrl());
		registry.registerBeanDefinition(dataSourceName, atomicDatasourceDefinitionBuilder.getBeanDefinition());

		BeanDefinitionBuilder sqlSessionXADefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("sqlSessionFactory.mssql.base");
		sqlSessionXADefinitionBuilder.addPropertyReference("dataSource", dataSourceName);
		registry.registerBeanDefinition(sqlSessionFactoryName, sqlSessionXADefinitionBuilder.getBeanDefinition());

		BeanDefinitionBuilder sqlSessionXATemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class);
		sqlSessionXATemplateBuilder.addConstructorArgReference(sqlSessionFactoryName);
		registry.registerBeanDefinition(sqlSessionTemplateName, sqlSessionXATemplateBuilder.getBeanDefinition());
	}

	private void atomicDatasourceDefinitionBuilder(BeanDefinitionRegistry registry, DatasourceMeta datasourceMeta, String key) {
		BeanDefinitionBuilder atomicDatasourceDefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("dataSource.mssql.base");

		String dataSourceName = "dataSource.mssql." + key;
		String sqlSessionFactoryName = "sqlSessionFactory.mssql." + key;
		String sqlSessionTemplateName = "sqlSessionTemplate.mssql." + key;
		String uniqueResourceName = "MsSQL.template." + key;

		String password = datasourceMeta.getPassword();

		atomicDatasourceDefinitionBuilder.addPropertyValue("uniqueResourceName", uniqueResourceName);
		atomicDatasourceDefinitionBuilder.addPropertyValue("driverClassName", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		atomicDatasourceDefinitionBuilder.addPropertyValue("user", datasourceMeta.getUserid());
		atomicDatasourceDefinitionBuilder.addPropertyValue("password", password);
		atomicDatasourceDefinitionBuilder.addPropertyValue("url", datasourceMeta.getUrl());
		registry.registerBeanDefinition(dataSourceName, atomicDatasourceDefinitionBuilder.getBeanDefinition());

		BeanDefinitionBuilder sqlSessionXADefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition("sqlSessionFactory.mssql.base");
		sqlSessionXADefinitionBuilder.addPropertyReference("dataSource", dataSourceName);
		registry.registerBeanDefinition(sqlSessionFactoryName, sqlSessionXADefinitionBuilder.getBeanDefinition());

		BeanDefinitionBuilder sqlSessionXATemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class);
		sqlSessionXATemplateBuilder.addConstructorArgReference(sqlSessionFactoryName);
		registry.registerBeanDefinition(sqlSessionTemplateName, sqlSessionXATemplateBuilder.getBeanDefinition());

		logger.debug("atomicDatasourceDefinitionBuilder");
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		logger.debug("////////////////////  postProcessBeanFactory /////////////////////////");
	}

}