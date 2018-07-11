package com.template.common.reststub.ping;

import com.template.common.model.PingModel;
import com.template.common.restfull.annotation.RestServer;
import com.template.common.restfull.annotation.RestStub;
import com.template.common.restfull.annotation.RestStubMethod;
import org.springframework.web.bind.annotation.RequestMethod;

@RestStub(restServer = RestServer.PROCESS_SERVER, value = "/ping")
public interface PingStubBO {

	@RestStubMethod(value = "/ping", requestMethod = RequestMethod.POST)
	public PingModel ping(PingModel map);
}
