package com.template.common.reststub.board;

import com.template.common.model.board.BoardVO;
import com.template.common.model.board.SearchVO;
import com.template.common.restfull.annotation.RestServer;
import com.template.common.restfull.annotation.RestStub;
import com.template.common.restfull.annotation.RestStubMethod;
import com.template.common.restfull.annotation.RestStubParameter;
import org.springframework.web.bind.annotation.RequestMethod;

@RestStub(restServer = RestServer.BOARD_SERVER, value = "/board")
public interface BoardStubBO {

	/**
	 * Integer selectBoardCount(RankParamVO searchVO)
	 * List<?> selectBoardList(RankParamVO searchVO)
	 * void insertBoard(BoardVO param, List<FileVO> filelist, String[] fileno)
	 *
	 * BoardVO selectBoardOne(String boardIdx)
	 * boolean updateBoardRead(String boardIdx)
	 * boolean deleteBoardOne(String boardIdx) throws
	 *
	 * List<?> selectBoardFileList(String boardIdx)
	 */
	/**
	 * private String category;                       // 게시판 그룹
	 private String searchKeyword = "";         // 검색 키워드
	 private String searchType = "";            // 검색 필드: 제목, 내용
	 private String[] searchTypeArr;            // 검색 필드를 배열로 변환
	 */

	@RestStubMethod(value = "/getBoardList/", requestMethod = RequestMethod.POST)
	public String getBoardList(SearchVO searchVO);

	@RestStubMethod(value = "/getBoardOne/{boardIdx}", requestMethod = RequestMethod.POST)
	public String getBoardOne(@RestStubParameter(name = "boardIdx") String boardIdx);

	@RestStubMethod(value = "/updateBoardRead/{boardIdx}", requestMethod = RequestMethod.POST)
	public String updateBoardRead(@RestStubParameter(name = "boardIdx") String boardIdx);

	@RestStubMethod(value = "/deleteBoardOne/{boardIdx}", requestMethod = RequestMethod.POST)
	public String deleteBoardOne(@RestStubParameter(name = "boardIdx") String boardIdx);

	@RestStubMethod(value = "/insertBoard/", requestMethod = RequestMethod.POST)
	public String insertBoard(BoardVO boardVO);

	@RestStubMethod(value = "/updateBoard/", requestMethod = RequestMethod.POST)
	public String updateBoard(BoardVO boardVO);

}
