package com.template.service.board;

import com.template.common.model.board.ReplyVO;
import com.template.repository.board.ReplyDAO;
import com.template.repository.common.DBConstants;
import com.template.repository.common.MultiDaoSupport;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReplyBO extends MultiDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(ReplyBO.class);

	@Autowired
	private ReplyDAO replyDAO;

	//TODO 트랜잭션에 대한 처리는 새롭게 구성해야 할 듯???

	public List<ReplyVO> getReplyList(String idx) {
		List<ReplyVO> replyList = replyDAO.selectReplyList(idx, 0, 5);
		if(replyList == null || replyList.isEmpty()) {
			return null;
		}
		return replyList;
	}

	public int getReplyListCount(String idx) {
		return replyDAO.selectReplyListCount(idx).getReplyListCount();
	}

	public List<ReplyVO> getReplyListMore(String idx, String minReorder, int length) {
		// 댓글 더보기
		int intMinReorder = Integer.parseInt(minReorder);
		//int intLength = Integer.parseInt(length);
		List<ReplyVO> replyList = replyDAO.selectReplyList(idx, intMinReorder, length + 5);
		if(replyList == null || replyList.isEmpty()) {
			return null;
		}
		return replyList;
	}

	public List<ReplyVO> getReplyListDepth(String parent, String minReorder, String length) {
		int intMinReorder = Integer.parseInt(minReorder);
		int intLength = Integer.parseInt(length);
		List<ReplyVO> replyList = replyDAO.selectReplyListDepth(parent, intMinReorder, intLength);
		if(replyList == null || replyList.isEmpty()) {
			return null;
		}
		return replyList;
	}

	public int getReplyListDepthCount(String parent) {
		return replyDAO.selectReplyListDepthCount(parent).getReplyListDepthCount();
	}

	// 댓글과 대댓글의 차이는 replyIdx가 넘어 왔느냐 아니냐 이다.
	// 즉 replyIdx는 parentReplyIdx 이다.
	// 또한 reorder에도 차이가 생긴다. 대댓글은 부모의 reorder +1 을 해준다. 그래서 입력후 reorder +1 보다 큰 댓글 모두 reorder +1 해준다.
	// 댓글은 그냥 max(reorder) where board_idx = idx 보다 +1 해주면 된다.
	public boolean insertReply(ReplyVO replyVO) {
		try {
			if(StringUtils.isBlank(replyVO.getEmail()) || StringUtils.isBlank(replyVO.getContent()) || StringUtils.isBlank(replyVO.getIdx())) {
				return false;
			}
			if (StringUtils.isBlank(replyVO.getReply_idx())) {
				// 댓글 삽입
				// 마지막 reorder값 가져옴
				int maxReorder = getMaxReorderByIdx(replyVO.getIdx());
				replyVO.setReorder(maxReorder+1);
				replyVO.setDepth("0");
				replyVO.setParent("0");
				replyVO.setContent(replyVO.getContent().replaceAll("&lt;","<")
						.replaceAll("&gt",">")
						.replaceAll("\"","&quot;")
						.replaceAll("'","&#39;")
						.replaceAll("\r\n","<br>"));

				replyDAO.insertReply(replyVO);
				return true;
			} else {
				// 대댓글 삽입
				//TODO 또한 reorder에도 차이가 생긴다. 대댓글은 부모의 reorder +1 을 해준다. 그래서 입력후 reorder +1 보다 큰 댓글 모두 reorder +1 해준다.

				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public boolean updateReply(ReplyVO replyVO) {
		int result;
		try {
			result = replyDAO.updateReply(replyVO);
			if(result == 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public boolean deleteReply(String replyIdx, String email) {
		int result;
		try {
			replyDAO.updateDeleteFlag(replyIdx,"Y");
			result = replyDAO.deleteReply(replyIdx, email);
			replyDAO.updateDeleteFlag(replyIdx,"N");
			if(result == 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			replyDAO.updateDeleteFlag(replyIdx,"N");
			return false;
		}
	}

	public int getMaxReorderByIdx(String idx) {
		ReplyVO replyVO = replyDAO.selectMaxReorderByIdx(idx);
		if(replyVO == null) {
			return 0;
		}
		return replyVO.getReorder();
	}

}
