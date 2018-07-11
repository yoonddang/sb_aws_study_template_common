package com.template.repository.templateUser;

import com.template.common.model.user.*;
import com.template.repository.common.DBConstants;
import com.template.repository.common.MultiDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TemplateUserDAO extends MultiDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(TemplateUserDAO.class);
	private final String NAMESPACE = "com.template.repository.template.ser.TemplateUserDAO.";



	public TemplateUser selectLoginUserDataByEmail(String email) {
		Map<String,	Object> map = new HashMap<>();
		map.put("email",	email);
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectOne(NAMESPACE + "selectLoginUserDataByEmail",	map);
	}

	public TemplateUser selectUserDataByEmail(String email) {
		Map<String,	Object> map = new HashMap<>();
		map.put("email",	email);
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectOne(NAMESPACE + "selectUserDataByEmail",	map);
	}

}
