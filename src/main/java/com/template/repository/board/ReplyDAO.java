
package com.template.repository.board;

import com.template.common.model.board.BoardVO;
import com.template.common.model.board.FileVO;
import com.template.common.model.board.ReplyVO;
import com.template.common.model.board.SearchVO;
import com.template.repository.common.DBConstants;
import com.template.repository.common.MultiDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReplyDAO extends MultiDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(ReplyDAO.class);
	private final String NAMESPACE = "com.template.repository.board.ReplyDAO.";

	public ReplyVO selectMaxReorderByIdx(String idx) {
		Map<String,	Object> map = new HashMap<>();
		map.put("idx",	idx);
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectOne(NAMESPACE + "selectMaxReorderByIdx", map);
	}

	public List<ReplyVO> selectReplyList(String idx, int maxReorder, int length) {
		Map<String,	Object> map = new HashMap<>();
		map.put("idx",	idx);
		map.put("maxReorder",	maxReorder);
		map.put("length",	length);
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectList(NAMESPACE + "selectReplyList", map);
	}

	public ReplyVO selectReplyListCount(String idx) {
		Map<String,	Object> map = new HashMap<>();
		map.put("idx",	idx);
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectOne(NAMESPACE + "selectReplyListCount", map);
	}

	public List<ReplyVO> selectReplyListDepth(String parent, int maxReorder, int length) {
		Map<String,	Object> map = new HashMap<>();
		map.put("parent",	parent);
		map.put("maxReorder",	maxReorder);
		map.put("length",	length);
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectList(NAMESPACE + "selectReplyListDepth", map);
	}

	public ReplyVO selectReplyListDepthCount(String parent) {
		Map<String,	Object> map = new HashMap<>();
		map.put("parent",	parent);
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectOne(NAMESPACE + "selectReplyListDepthCount", map);
	}

	public int insertReply(ReplyVO replyVO) {
		return sqlSession(DBConstants.INTEGRATED_DBID1).insert(NAMESPACE + "insertReply", replyVO);
	}

	public int updateReply(ReplyVO replyVO) {
		return sqlSession(DBConstants.INTEGRATED_DBID1).update(NAMESPACE + "updateReply", replyVO);
	}

	public int deleteReply(String replyIdx, String email) {
		Map<String,	Object> map = new HashMap<>();
		map.put("reply_idx",	replyIdx);
		map.put("email",	email);
		return sqlSession(DBConstants.INTEGRATED_DBID1).delete(NAMESPACE + "deleteReply", map);
	}

	public int updateDeleteFlag(String replyIdx, String deleteFlag) {
		Map<String,	Object> map = new HashMap<>();
		map.put("reply_idx",	replyIdx);
		map.put("delete_flag",	deleteFlag);
		return sqlSession(DBConstants.INTEGRATED_DBID1).update(NAMESPACE + "updateDeleteFlag", map);
	}









}
