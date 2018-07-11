package com.template.common.configuration;

import com.atomikos.icatch.config.UserTransactionService;
import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.template.common.datasource.DataSourcesBeanFactoryPostProcessor;
import com.template.common.datasource.DataSourcesBeanMsSqlFactoryPostProcessor;
import com.template.common.datasource.SqlSessionTemplateMap;
import com.template.common.datasource.TemplateDataBeanDefinitionRegistryPostProcessor;
import com.template.common.datasource.properties.JdbcConfig;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.encryption.pbe.config.PBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.Yaml;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@ComponentScan(
        basePackages = {
                "com.template.repository",
                "com.template.service"
        }
)
public class DatasourceConfiguration {

    @Bean
    public JdbcConfig jdbcConfig() throws Exception {
        Yaml yaml = new Yaml();

        try (InputStream in = (new ClassPathResource("config/data-source.yml")).getInputStream()) {
            return yaml.loadAs(in, JdbcConfig.class);
        }
    }

    @Bean
    public PBEConfig pbeConfig() {
        EnvironmentStringPBEConfig environment = new EnvironmentStringPBEConfig();
        environment.setAlgorithm("PBEWithMD5AndDES");
        environment.setPassword("ICs1CCMoT4");
        return environment;
    }

    @Bean
    public StringEncryptor encryptor(PBEConfig pbeConfig) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setConfig(pbeConfig);
        return standardPBEStringEncryptor;
    }

    @Bean
    public DataSourcesBeanFactoryPostProcessor dataSourcesBeanFactoryPostProcessor(StringEncryptor encryptor, JdbcConfig jdbcConfig) {
        DataSourcesBeanFactoryPostProcessor dataSourcesBeanFactoryPostProcessor = new DataSourcesBeanFactoryPostProcessor();
        dataSourcesBeanFactoryPostProcessor.setBaseDataSource(jdbcConfig.getDefaultDatasource());
        dataSourcesBeanFactoryPostProcessor.setEncryptor(encryptor);
        dataSourcesBeanFactoryPostProcessor.setJdbcUrlMap(jdbcConfig.getDatasource());
        return dataSourcesBeanFactoryPostProcessor;
    }

    @Bean
    public DataSourcesBeanMsSqlFactoryPostProcessor dataSourcesBeanMsSqlFactoryPostProcessor(StringEncryptor encryptor, JdbcConfig jdbcConfig) {
        DataSourcesBeanMsSqlFactoryPostProcessor dataSourcesBeanMsSqlFactoryPostProcessor = new DataSourcesBeanMsSqlFactoryPostProcessor();
        dataSourcesBeanMsSqlFactoryPostProcessor.setBaseDataSource(jdbcConfig.getDefaultDatasource2());
        dataSourcesBeanMsSqlFactoryPostProcessor.setEncryptor(encryptor);
        dataSourcesBeanMsSqlFactoryPostProcessor.setJdbcUrlMap(jdbcConfig.getDatasource2());
        return dataSourcesBeanMsSqlFactoryPostProcessor;
    }

    @Bean
    public TemplateDataBeanDefinitionRegistryPostProcessor templateDataBeanDefinitionRegistryPostProcessor(JdbcConfig jdbcConfig) {
        TemplateDataBeanDefinitionRegistryPostProcessor templateDataBeanDefinitionRegistryPostProcessor = new TemplateDataBeanDefinitionRegistryPostProcessor();
        templateDataBeanDefinitionRegistryPostProcessor.setJdbcConfig(jdbcConfig);
        return templateDataBeanDefinitionRegistryPostProcessor;
    }

    @Bean("sqlSessionTemplateMapXa")
    public SqlSessionTemplateMap sqlSessionTemplateMapXa(JdbcConfig jdbcConfig) {
        SqlSessionTemplateMap sqlSessionTemplateMap = new SqlSessionTemplateMap();
        sqlSessionTemplateMap.setJdbcUrlMap(jdbcConfig.getDatasource());
        sqlSessionTemplateMap.setSqlSessionTemplatePrefix("sqlSessionTemplate.xa.");
        return sqlSessionTemplateMap;
    }

    @Bean("sqlSessionTemplateMsSqlMap")
    public SqlSessionTemplateMap sqlSessionTemplateMsSqlMap(JdbcConfig jdbcConfig) {
        SqlSessionTemplateMap sqlSessionTemplateMap = new SqlSessionTemplateMap();
        sqlSessionTemplateMap.setJdbcUrlMap(jdbcConfig.getDatasource2());
        sqlSessionTemplateMap.setSqlSessionTemplatePrefix("sqlSessionTemplate.mssql.");
        return sqlSessionTemplateMap;
    }

    @Bean(name = "userTransactionService", initMethod = "init", destroyMethod = "shutdownForce")
    public UserTransactionService userTransactionService(JdbcConfig jdbcConfig){
        Map<String, String> jta = jdbcConfig.getJdbc().getJta();
        Properties properties = new Properties();

        jta.forEach(properties::setProperty);

        return new UserTransactionServiceImp(properties);
    }

    @Bean
    @DependsOn("userTransactionService")
    public UserTransaction atomikosUserTransaction() throws SystemException {
        UserTransactionImp atomikosUserTransaction = new UserTransactionImp();
        atomikosUserTransaction.setTransactionTimeout(300);
        return atomikosUserTransaction;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @DependsOn("userTransactionService")
    public TransactionManager atomikosTransactionManager() {
        UserTransactionManager atomikosTransactionManager = new UserTransactionManager();
        atomikosTransactionManager.setStartupTransactionService(false);
        atomikosTransactionManager.setForceShutdown(false);

        return atomikosTransactionManager;
    }

    @Bean("transactionManager")
    @DependsOn({"atomikosUserTransaction", "atomikosTransactionManager"})
    public JtaTransactionManager jtaTransactionManager(TransactionManager atomikosTransactionManager, UserTransaction atomikosUserTransaction) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(atomikosTransactionManager);
        jtaTransactionManager.setUserTransaction(atomikosUserTransaction);
        jtaTransactionManager.setAllowCustomIsolationLevels(true);

        return jtaTransactionManager;
    }

    @Bean
    @DependsOn("transactionManager")
    public TransactionTemplate transactionTemplate(JtaTransactionManager jtaTransactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(jtaTransactionManager);

        return transactionTemplate;
    }

}
