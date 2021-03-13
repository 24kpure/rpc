package com.lmj.rpc.compressor;


import com.lmj.rpc.exception.CompressNotExistException;

import static com.lmj.rpc.Constants.Compressor.MODE;
import static com.lmj.rpc.Constants.Compressor.SNAPPY;
import static com.lmj.rpc.Constants.Compressor.ZIP;

public class CompressorUtils {
    public static Compressor get(short extraInfo) {
        switch (extraInfo & MODE) {
            case SNAPPY:
                return SnappyCompressor.Inner.compressor;
            case ZIP:
                //todo zip compress
            default:
                throw new CompressNotExistException(extraInfo);
        }
    }
}
