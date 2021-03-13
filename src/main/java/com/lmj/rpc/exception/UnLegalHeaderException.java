package com.lmj.rpc.exception;

/**
 * @Author: lmj
 * @Description:
 * @Date: Create in 4:44 下午 2021/3/12
 **/
public class UnLegalHeaderException extends RuntimeException {
    public UnLegalHeaderException(String msg) {
        super(msg);
    }
}