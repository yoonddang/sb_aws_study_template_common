package com.template.service.template.ser;

import com.template.common.model.user.*;
import com.template.common.model.common.Code;
import com.template.common.model.common.ResultInfo;
import com.template.repository.templateUser.TemplateUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateUserBO {
	private static final Logger logger = LoggerFactory.getLogger(TemplateUserBO.class);

	@Autowired
	private TemplateUserDAO templateUserDAO;

	public ResultInfo checkUserByUsername(String loginId, String password) {
		ResultInfo resultInfo = new ResultInfo();
		Map<String,	Object> resultData = new HashMap<>();
		logger.info("loadUserByUsername(id) : " + loginId);
		TemplateUser user = templateUserDAO.selectLoginUserDataByEmail(loginId);

		if(user != null) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			if (passwordEncoder.matches(password, user.getPass_word()))	{

				user.setRole_name("로그인~");

				resultData.put("isLogin",	true);
				resultData.put("templateUser",	user);
				resultInfo.setCode(Code.SUCCESS);
				resultInfo.setMessage("로그인 성공");
			}else {
				resultData.put("isLogin",	false);
				resultInfo.setCode(Code.FAIL);
				resultInfo.setMessage("이메일 이나 비밀번호가 맞지 않습니다.");
			}
		} else {
			resultData.put("isLogin",	false);
			resultInfo.setCode(Code.FAIL);
			resultInfo.setMessage("존재하지 않는 계정입니다.");
		}

		resultInfo.setResultData(resultData);
		return resultInfo;
	}

}