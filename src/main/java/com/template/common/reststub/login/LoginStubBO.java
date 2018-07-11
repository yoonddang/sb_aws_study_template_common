package com.template.common.reststub.login;

import com.template.common.model.user.TemplateUser;
import com.template.common.restfull.annotation.RestServer;
import com.template.common.restfull.annotation.RestStub;
import com.template.common.restfull.annotation.RestStubMethod;
import org.springframework.web.bind.annotation.RequestMethod;

@RestStub(restServer = RestServer.PROCESS_SERVER, value = "/login")
public interface LoginStubBO {

	@RestStubMethod(value = "/userLogin/", requestMethod = RequestMethod.POST)
	public String userLogin(TemplateUser templateUser);


}
