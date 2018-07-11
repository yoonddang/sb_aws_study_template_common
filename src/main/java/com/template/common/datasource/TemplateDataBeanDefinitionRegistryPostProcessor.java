package com.template.common.datasource;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import com.template.common.datasource.properties.JdbcConfig;
import com.template.common.datasource.properties.Mybatis;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;

import java.util.ArrayList;
import java.util.List;

public class TemplateDataBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private JdbcConfig jdbcConfig;

    public void setJdbcConfig(JdbcConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //registry.registerBeanDefinition("dataSource.jdbc.base", processBaseDatasourceFactoryBean().getBeanDefinition());
        registry.registerBeanDefinition("dataSource.xa.base", processXADatasourceFactoryBean().getBeanDefinition());

        registry.registerBeanDefinition("dataSource.mssql.base", processMsSqlDatasourceFactoryBean().getBeanDefinition());

        //registry.registerBeanDefinition("sqlSessionFactory.jdbc.base", processSqlSessionFactoryBean().getBeanDefinition());
        registry.registerBeanDefinition("sqlSessionFactory.xa.base", processSqlSessionFactoryBean().getBeanDefinition());

        registry.registerBeanDefinition("sqlSessionFactory.mssql.base", processSqlSessionFactoryBean().getBeanDefinition());

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private BeanDefinitionBuilder processSqlSessionFactoryBean() {

        /*SlowQueryPlugin slowQueryPlugin = new SlowQueryPlugin();
        slowQueryPlugin.setSlowQueryTime(3000);*/

        List plugins = new ArrayList();/*
        plugins.add(new BindingLogPlugin());
        plugins.add(new StatementIdAppenderPlugin());
        plugins.add(slowQueryPlugin);*/
        Mybatis mybatis = jdbcConfig.getMybatis();

        BeanDefinitionBuilder sqlSessionFactoryBean = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
        sqlSessionFactoryBean.addPropertyValue("typeAliasesPackage", mybatis.getTypeAliasesPackage());
        sqlSessionFactoryBean.addPropertyValue("configLocation", mybatis.getConfigLocation());
        sqlSessionFactoryBean.addPropertyValue("mapperLocations", mybatis.getMapperLocations());
        sqlSessionFactoryBean.addPropertyValue("failFast", mybatis.isFailFast());
        sqlSessionFactoryBean.addPropertyValue("plugins", plugins);
        sqlSessionFactoryBean.setAbstract(true);
        return sqlSessionFactoryBean;
    }

    private BeanDefinitionBuilder processXADatasourceFactoryBean() {
        Mybatis mybatis = jdbcConfig.getMybatis();

        BeanDefinitionBuilder datasource = BeanDefinitionBuilder.genericBeanDefinition(AtomikosDataSourceBean.class);
        datasource.addPropertyValue("xaDataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        datasource.setInitMethodName("init");
        datasource.setDestroyMethodName("close");
        datasource.addPropertyValue("testQuery", "select 1");
        datasource.setAbstract(true);
        datasource.addDependsOn("userTransactionService");
        return datasource;
    }

    private BeanDefinitionBuilder processMsSqlDatasourceFactoryBean() {
        Mybatis mybatis = jdbcConfig.getMybatis();

        BeanDefinitionBuilder datasource = BeanDefinitionBuilder.genericBeanDefinition(AtomikosNonXADataSourceBean.class);
        datasource.setInitMethodName("init");
        datasource.setDestroyMethodName("close");
        datasource.addPropertyValue("testQuery", "select 1");
        datasource.setAbstract(true);
        datasource.addDependsOn("userTransactionService");
        return datasource;
    }
}