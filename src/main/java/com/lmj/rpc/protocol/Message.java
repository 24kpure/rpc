package com.lmj.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lmj
 * @Description: 消息
 * @Date: Create in 5:15 下午 2021/3/12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> {
    private Header header;

    private T content;
}