package com.template.repository.sample;

import com.template.repository.common.DBConstants;
import com.template.repository.common.MultiDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class SampleDAO extends MultiDaoSupport {

    private final String NAMESPACE = "com.template.repository.sample.SampleDAO.";

    public int selectOne() {
        return sqlSession(DBConstants.INTEGRATED_DBID1).selectOne(NAMESPACE + "select");
    }
}
