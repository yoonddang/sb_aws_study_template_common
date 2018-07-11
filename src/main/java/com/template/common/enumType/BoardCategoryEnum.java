package com.template.common.enumType;

/**
 * 게시판 카테고리 Enum Class.
 */
public enum BoardCategoryEnum {
	NOTICE("1", "카테고리1"),
	CATEGORY2("2", "카테고리2");

	private String category;
	private String categoryName;

	BoardCategoryEnum(String category, String categoryName) {
		this.category = category;
		this.categoryName = categoryName;
	}

	public String getCategory() {
		return category;
	}

	public String getCategoryName() {
		return categoryName;
	}
}