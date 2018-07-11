
package com.template.service.board;

import com.template.common.model.board.BoardVO;
import com.template.common.model.board.FileVO;
import com.template.common.model.board.SearchVO;
import com.template.repository.board.BoardDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.template.common.util.board.utiletc.classifyCategory;

@Service
public class BoardBO {
	private static final Logger logger = LoggerFactory.getLogger(BoardBO.class);

	@Autowired
	private BoardDAO boardDAO;
	/*@Autowired
	private JtaTransactionManager txManager;*/
	//TODO 트랜잭션에 대한 처리는 새롭게 구성해야 할 듯???


		
    public Integer selectBoardCount(SearchVO searchVO) {
		return boardDAO.selectBoardCount(searchVO);
    }
    public List<BoardVO> selectBoardList(SearchVO searchVO) {

    	if((searchVO.getLength() - 20) < 0) {
    		searchVO.setFirst(0);
		} else {
    		searchVO.setFirst(searchVO.getLength()-20);
		}

    	List<BoardVO> boardList = boardDAO.selectBoardList(searchVO);
    	for(BoardVO boardVO : boardList) {
    		boardVO.setCategory(classifyCategory(boardVO.getCategory()));
		}

		return boardList;
    }

    public BoardVO selectBoardOne(String board_idx) {
		updateBoardRead(board_idx);
    	BoardVO boardVO = boardDAO.selectBoardOne(board_idx);
		boardVO.setCategory(classifyCategory(boardVO.getCategory()));

		/*
		boardVO.setContent(boardVO.getContent().replaceAll("′","'") // 치환된 작은따옴표 원래 작은따옴표로 환원처리
			.replaceAll("\r\n","<br>") // 줄바꿈처리
			.replaceAll("\u0020","&nbsp;")); // 스페이스바로 띄운 공백처리
		*/

			/* 시큐어코딩 (XSS / SQL Injection / CSRF ... */
		boardVO.setContent(
				boardVO.getContent()
						.replaceAll("\"","&quot;")
						.replaceAll("'","&#39;")
						.replaceAll("<br>","")
						/*.replaceAll("\n","<br>")
						.replaceAll("\r","<br>")
						.replaceAll("\r\n","<br>")*/
		);
		/*boardVO.setContent(
				boardVO.getContent()
						.replaceAll("<","&lt;")
						.replaceAll(">","&gt")
						.replaceAll("\"","&quot;")
						.replaceAll("'","&#39;")
						.replaceAll("\n","<br>")
						.replaceAll("\r","<br>")
						.replaceAll("\r\n","<br>")
		);*/

		return boardVO;
    }
    
    public boolean updateBoardRead(String boardIdx) {
		int result;
		try {
			result = boardDAO.updateBoardRead(boardIdx);
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

	public boolean insertBoard(BoardVO boardVO) {
		int result;
		try {
			result = boardDAO.insertBoard(secureCheck(boardVO));
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

	private BoardVO secureCheck(BoardVO boardVO) {
		/* 시큐어코딩 (XSS / SQL Injection / CSRF ... */
		boardVO.setContent(
				boardVO.getContent()
						.replaceAll("<br>","\n")
						.replaceAll("<","&lt;")
						.replaceAll(">","&gt")
						.replaceAll("\"","&quot;")
						.replaceAll("'","&#39;")
		);
		return boardVO;
	}

	public boolean updateBoard(BoardVO boardVO) {
		int result;
		try {
			result = boardDAO.updateBoard(secureCheck(boardVO));
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

    public boolean deleteBoardOne(String boardIdx) {
		int result;
		try {
			result = boardDAO.deleteBoardOne(boardIdx);
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


}
