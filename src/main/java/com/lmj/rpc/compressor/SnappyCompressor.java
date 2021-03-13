package com.lmj.rpc.compressor;


import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * snappy压缩方案
 */
public class SnappyCompressor implements Compressor {

    public static class Inner {
       static Compressor compressor = new SnappyCompressor();
    }

    private SnappyCompressor() {
    }

    @Override
    public byte[] compress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }
        return Snappy.compress(array);
    }

    @Override
    public byte[] unCompress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }
        return Snappy.uncompress(array);
    }
}
