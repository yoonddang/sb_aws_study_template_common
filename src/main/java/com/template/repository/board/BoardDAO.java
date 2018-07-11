
package com.template.repository.board;

import com.template.common.model.board.BoardVO;
import com.template.common.model.board.FileVO;
import com.template.common.model.board.SearchVO;
import com.template.repository.common.DBConstants;
import com.template.repository.common.MultiDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardDAO extends MultiDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(BoardDAO.class);
	private final String NAMESPACE = "com.template.repository.board.BoardDAO.";


		
    public int selectBoardCount(SearchVO searchVO) {
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectOne(NAMESPACE + "selectBoardCount", searchVO);
    }
    public List<BoardVO> selectBoardList(SearchVO searchVO) {
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectList(NAMESPACE + "selectBoardList", searchVO);
    }

    public int insertBoard(BoardVO boardVO) {
    	return sqlSession(DBConstants.INTEGRATED_DBID1).insert(NAMESPACE + "insertBoard", boardVO);
	}

	public int updateBoard(BoardVO boardVO) {
		return sqlSession(DBConstants.INTEGRATED_DBID1).insert(NAMESPACE + "updateBoard", boardVO);
	}

 
    public BoardVO selectBoardOne(String boardIdx) {
		Map<String,	Object> map = new HashMap<>();
		map.put("boardIdx",	boardIdx);
		return sqlSession(DBConstants.INTEGRATED_DBID1).selectOne(NAMESPACE + "selectBoardOne", map);
    }
    
    public int updateBoardRead(String boardIdx) {
		Map<String,	Object> map = new HashMap<>();
		map.put("boardIdx",	boardIdx);
		return sqlSession(DBConstants.INTEGRATED_DBID1).insert(NAMESPACE + "updateBoardRead", map);
    }
    
    public int deleteBoardOne(String boardIdx) {
		Map<String,	Object> map = new HashMap<>();
		map.put("boardIdx",	boardIdx);
		return sqlSession(DBConstants.INTEGRATED_DBID1).delete(NAMESPACE + "deleteBoardOne", map);
    }

}
