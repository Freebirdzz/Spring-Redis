package com.shangxuefeng.cachetest.commons.exception;

/**
 * @author kevin
 */
public class MyException extends RuntimeException {

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}
