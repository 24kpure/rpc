package com.lmj.rpc.compressor;

import java.io.IOException;

/**
 * @Author: lmj
 * @Description: 压缩
 * @Date: Create in 2:10 下午 2021/3/12
 **/
public interface Compressor {

    byte[] compress(byte[] array) throws IOException;

    byte[] unCompress(byte[] array) throws IOException;
}
