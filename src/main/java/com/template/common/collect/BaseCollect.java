package com.template.common.collect;


import java.io.IOException;
import java.util.List;

public interface BaseCollect<T> {

    void setJobName(String JobName);

    /**
     * DB에서 작업에 필요한 데이터 를 조회하여 List로 리턴
     * @return
     * @throws IOException
     */
    List<T> read() throws IOException;

    /**
     * 전달받은 데이터 를 디비에 입력하는 로직을 작성할 것, sqlSession.insert의 성공시 리턴값인 1로 BaseCollect에서 카운트함
     * @param data
     * @return
     * @throws IOException
     */
    int write(T data) throws IOException;

    /**
     * 입력 전처리 로직 작성할 것
     * @param list
     * @return
     * @throws IOException
     */
    List<T> process(List<T> list) throws IOException;

    /**
     * 벌크 인서트 실행 로직 작성할 것, 인서트 카운트 후 리턴
     * @param data
     * @return
     * @throws IOException
     */
    int bulkWrite(List<T> data) throws IOException;

    /**
     * 후처리 작업, 작업 성공여부를 리턴, 실패(false)시 BatchException 처리됨
     * @return
     * @throws IOException
     */
    boolean setAfter() throws IOException;

}
