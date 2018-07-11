package com.template.common.typehandler;

import com.template.common.enumType.BoardCategoryEnum;
import org.apache.ibatis.type.EnumTypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Type Handler for Enum Class.
 */
public class BoardCategoryType {

    public static class BoardCategoryTypeHandler extends EnumTypeHandler<BoardCategoryEnum> {

        public BoardCategoryTypeHandler(Class<BoardCategoryEnum> type) {
            super(type);
        }

        @Override
        public BoardCategoryEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
            return valueOf(rs.getString(columnName));
        }
    }

    public static BoardCategoryEnum valueOf(String matcher) {
        for (BoardCategoryEnum code : BoardCategoryEnum.values()) {
            if (code.getCategory().equals(matcher)) {
                return code;
            }
        }
        return null;
    }

}
