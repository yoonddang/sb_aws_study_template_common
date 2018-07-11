package com.template.common.reststub.reply;

import com.template.common.model.board.ReplyVO;
import com.template.common.restfull.annotation.RestServer;
import com.template.common.restfull.annotation.RestStub;
import com.template.common.restfull.annotation.RestStubMethod;
import com.template.common.restfull.annotation.RestStubParameter;
import org.springframework.web.bind.annotation.RequestMethod;

@RestStub(restServer = RestServer.BOARD_SERVER, value = "/reply")
public interface ReplyStubBO {


	/**
	 * 	public String getReplyList(@RestStubParameter(name = "idx") String idx, @RestStubParameter(name = "maxReplyIdx") String maxReplyIdx);
	 *  public String insertReply(@RestStubParameter(name = "idx") String idx, @RestStubParameter(name = "replyIdx") String replyIdx
	 ,  @RestStubParameter(name = "content") String content, @RestStubParameter(name = "email") String email);
	 *  public String updateReply(@RestStubParameter(name = "replyIdx") String replyIdx, @RestStubParameter(name = "content") String content
	 ,  @RestStubParameter(name = "email") String email);
	 *  public String deleteReply(@RestStubParameter(name = "replyIdx") String replyIdx, @RestStubParameter(name = "email") String email);
	 */

	@RestStubMethod(value = "/getReplyList/{idx}/{maxReorder}/{length}", requestMethod = RequestMethod.POST)
	public String getReplyList(@RestStubParameter(name = "idx") String idx, @RestStubParameter(name = "maxReorder") String maxReorder
			, @RestStubParameter(name = "length") int length);

	@RestStubMethod(value = "/getReplyListDepth/{parentReplyIdx}/{maxReorder}", requestMethod = RequestMethod.POST)
	public String getReplyListDepth(@RestStubParameter(name = "parentReplyIdx") String parentReplyIdx, @RestStubParameter(name = "maxReorder") String maxReorder);

	@RestStubMethod(value = "/insertReply/", requestMethod = RequestMethod.POST)
	public String insertReply(ReplyVO replyVO);

	@RestStubMethod(value = "/updateReply/", requestMethod = RequestMethod.POST)
	public String updateReply(ReplyVO replyVO);

	@RestStubMethod(value = "/deleteReply/{replyIdx}/{email}", requestMethod = RequestMethod.POST)
	public String deleteReply(@RestStubParameter(name = "replyIdx") String replyIdx, @RestStubParameter(name = "email") String email);


}
