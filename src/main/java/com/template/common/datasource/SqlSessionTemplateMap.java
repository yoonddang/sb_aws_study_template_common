package com.template.common.datasource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class SqlSessionTemplateMap implements InitializingBean, ApplicationContextAware {

	private static Logger log = LoggerFactory.getLogger(SqlSessionTemplateMap.class);

	private Map<String, DatasourceMeta> jdbcUrlMap;

	private ApplicationContext applicationContext;

	private HashMap<String, SqlSessionTemplate> sqlSessionTemplateMap;

	private String sqlSessionTemplatePrefix;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setJdbcUrlMap(Map<String, DatasourceMeta> jdbcUrlMap) {
		this.jdbcUrlMap = jdbcUrlMap;
	}

	public HashMap<String, SqlSessionTemplate> getSqlSessionTemplateMap() {
		return sqlSessionTemplateMap;
	}

	public void setSqlSessionTemplatePrefix(String sqlSessionTemplatePrefix) {
		this.sqlSessionTemplatePrefix = sqlSessionTemplatePrefix;
	}

	@Override
    public void afterPropertiesSet() throws Exception {
		sqlSessionTemplateMap = new HashMap<>();
		log.debug("------------------------------------------------------------------------------------------");
		for (String key : jdbcUrlMap.keySet()) {
				SqlSessionTemplate sqlSessionTemplate = applicationContext.getBean(sqlSessionTemplatePrefix + key, SqlSessionTemplate.class);
				log.debug("put !!!! => sqlSessionTemplate.jdbc.{} :: {}", key, sqlSessionTemplate.toString());
				sqlSessionTemplateMap.put(key, sqlSessionTemplate);
		}
		log.debug("------------------------------------------------------------------------------------------");

	}

}
