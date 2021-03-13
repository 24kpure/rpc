package com.lmj.rpc;


public interface Constants {
    /**
     * 开头魔数
     */
    short MAGIC = 0xE3;

    /**
     * header大小
     */
    int HEADER_SIZE = 17;

    /**
     * 0-1 bit
     */
    interface Type {
        short MODE = 0x03;
        short REQUEST = 0x01;
        short RESPONSE = 0x00;
        //0x02 0x03 预留
    }

    /**
     * 压缩相关 第3-4个bit
     * 0000 1100
     */
    interface Compressor {
        short MODE = 0x0c;

        short SNAPPY = 0x00;
        short ZIP = 0x04;
        //预留 0x08 0x0c
    }

    /**
     * 压缩相关 第5-6个bit
     * 0011 0000
     */
    interface Serialization {
        short MODE = 0x30;

        short HESSIAN = 0x00;
        short JSON = 0x10;
    }

    /**
     * 压缩相关 第7个bit
     * 0100 0000
     */
    interface Heartbeat {
        short MODE = 0x40;

        short FIT = 0x40;
    }

    int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    int HEARTBEAT_CODE = -1;

    byte HEART_EXTRA_INFO = 1;

    int DEFAULT_TIMEOUT = 500000;

    byte VERSION = 1;

    static boolean isHeartBeat(short extraInfo) {
        return (extraInfo & Heartbeat.MODE) == Heartbeat.FIT;
    }
}
