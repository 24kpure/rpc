package com.lmj.rpc.exception;

/**
 * @Author: lmj
 * @Description:
 * @Date: Create in 4:44 下午 2021/3/12
 **/
public class CompressNotExistException extends RuntimeException {
    public CompressNotExistException(short extraInfo) {
        super("compress is not exist ：" + extraInfo);
    }
}