package com.template.common.model.board;

import com.template.common.enumType.BoardCategoryEnum;
import com.template.common.util.board.utiletc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class BoardVO {

	private String board_idx;
	private String category;
	private String title;
	private String writer;
	private String content;
	private String reg_ymdt;
	private String mod_ymdt;
	private String read_cnt;
	private String delete_flag;
	private String filecnt;
	private String replycnt;

	private String album_name;
	private String distribute;
	private String sale_date;
	private int album_idx;

	/* 첨부파일 */
	private List<MultipartFile> uploadfile;

	private BoardCategoryEnum boardCategoryEnum;

	/**
	 * 게시물 제목을 글자수에 맞추어 자르기
	 */
	public String getShortTitle(Integer len){
		return utiletc.getShortString(title, len);
	}

	public String getBoard_idx() {
		return board_idx;
	}

	public void setBoard_idx(String board_idx) {
		this.board_idx = board_idx;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReg_ymdt() {
		return reg_ymdt;
	}

	public void setReg_ymdt(String reg_ymdt) {
		this.reg_ymdt = reg_ymdt;
	}

	public String getRead_cnt() {
		return read_cnt;
	}

	public void setRead_cnt(String read_cnt) {
		this.read_cnt = read_cnt;
	}

	public String getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}

	public List<MultipartFile> getUploadfile() {
		return uploadfile;
	}

	public void setUploadfile(List<MultipartFile> uploadfile) {
		this.uploadfile = uploadfile;
	}

	public String getFilecnt() {
		return filecnt;
	}

	public void setFilecnt(String filecnt) {
		this.filecnt = filecnt;
	}

	public String getMod_ymdt() {
		return mod_ymdt;
	}

	public void setMod_ymdt(String mod_ymdt) {
		this.mod_ymdt = mod_ymdt;
	}

	public String getReplycnt() {
		return replycnt;
	}

	public void setReplycnt(String replycnt) {
		this.replycnt = replycnt;
	}

	public String getAlbum_name() {
		return album_name;
	}

	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}

	public String getDistribute() {
		return distribute;
	}

	public void setDistribute(String distribute) {
		this.distribute = distribute;
	}

	public String getSale_date() {
		return sale_date;
	}

	public void setSale_date(String sale_date) {
		this.sale_date = sale_date;
	}

	public int getAlbum_idx() {
		return album_idx;
	}

	public void setAlbum_idx(int album_idx) {
		this.album_idx = album_idx;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BoardCategoryEnum getBoardCategoryEnum() {
		return boardCategoryEnum;
	}

	public void setBoardCategoryEnum(BoardCategoryEnum boardCategoryEnum) {
		this.boardCategoryEnum = boardCategoryEnum;
	}
}
