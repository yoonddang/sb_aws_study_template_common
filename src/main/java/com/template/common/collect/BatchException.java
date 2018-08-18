package com.template.common.collect;

public class BatchException extends Exception {

    public BatchException(){  super(BatchErrorEnum.FAIL.getReason());  }

    public BatchException(BatchErrorEnum reason) {
        super(reason.getReason());
    }

    public BatchException(String reason) {
        super(reason);
    }


}
