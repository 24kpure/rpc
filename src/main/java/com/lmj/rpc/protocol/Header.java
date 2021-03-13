package com.lmj.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lmj
 * @Description:
 * @Date: Create in 2:23 下午 2021/3/12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Header {

    private short magic;

    private byte version;

    private short extraInfo;

    private long msgId;

    private int size;

    public Header(short magic, byte version) {
        this.magic = magic;
        this.version = version;
        this.extraInfo = 0;
    }
}