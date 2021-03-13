package com.lmj.rpc.registry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo implements Serializable {

    private String host;

    private int port;

    //todo 存续时间  region等
}