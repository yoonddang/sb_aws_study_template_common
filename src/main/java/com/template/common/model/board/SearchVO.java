package com.template.common.model.board;


public class SearchVO extends PageVO {

    private String category;                       // 게시판 그룹
    private String searchKeyword = "";         // 검색 키워드
    private String searchType = "";            // 검색 필드: 제목, 내용  
    private String[] searchTypeArr;            // 검색 필드를 배열로 변환
    private int length;
    private int first;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }
    
    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
    
    public String getSearchType() {
        return searchType;
    }
    
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
    
    public String[] getSearchTypeArr() {
        return searchType.split(",");
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }
}
 