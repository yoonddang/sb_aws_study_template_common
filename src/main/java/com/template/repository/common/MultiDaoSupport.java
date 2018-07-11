package com.template.repository.common;

import com.template.common.datasource.SqlSessionTemplateMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DaoSupport;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;

import static org.springframework.util.Assert.notNull;

public abstract class MultiDaoSupport extends DaoSupport {

    private Logger logger = LoggerFactory.getLogger(MultiDaoSupport.class);

    @Autowired
    @Qualifier("sqlSessionTemplateMapXa")
    private SqlSessionTemplateMap sqlSessionTemplateMapXa;

    @Autowired
    @Qualifier("sqlSessionTemplateMsSqlMap")
    private SqlSessionTemplateMap sqlSessionTemplateMsSqlMap;

    private HashMap<String, SqlSessionTemplate> sqlSessionTemplate;
    private HashMap<String, SqlSessionTemplate> sqlSessionTemplate2;

    @PostConstruct
    public void init() {
        sqlSessionTemplate = sqlSessionTemplateMapXa.getSqlSessionTemplateMap();
        sqlSessionTemplate2 = sqlSessionTemplateMsSqlMap.getSqlSessionTemplateMap();
    }

    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {
        notNull(sqlSessionTemplateMapXa, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
        notNull(sqlSessionTemplateMsSqlMap, "Property 'sqlSessionFactory' or 'sqlSessionTemplateMsSqlMap' are required");
        notNull(sqlSessionTemplate, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
        notNull(sqlSessionTemplate2, "Property 'sqlSessionFactory' or 'sqlSessionTemplate2' are required");
    }

    protected SqlSessionTemplate sqlSession(String key) {
        if(sqlSessionTemplate.containsKey(key)) {
            return sqlSessionTemplate.get(key);
        } else {
            logStackTrace(key);
            throw new RuntimeException("The sqlSessionMap has not '" + key + "' session");
        }
    }

    protected SqlSessionTemplate sqlMsSqlSession(String key) {
        if(sqlSessionTemplate2.containsKey(key)) {
            return sqlSessionTemplate2.get(key);
        } else {
            logStackTrace(key);
            throw new RuntimeException("The sqlSessionMap has not '" + key + "' session");
        }
    }

    private void logStackTrace(String key) {
        logger.info("Not find key : [{}]", key);

        Thread th = Thread.currentThread();

        StackTraceElement[] elements = th.getStackTrace();
        if (ArrayUtils.isEmpty(elements)) {
            logger.error("Fail to th.getStackTrace()!");
            return;
        }

        Arrays.stream(elements)
                .forEach(element -> {
                    if (StringUtils.isNotEmpty(element.getFileName())) {
                        logger.error("\tat {}.{}({}:{})", element.getClassName(), element.getMethodName(), element.getFileName(), element.getLineNumber());
                    } else {
                        logger.error("\tat {}.{}", element.getClassName(), element.getMethodName());
                    }
                });
    }
}
