package com.lmj.rpc.serialize;

import com.lmj.rpc.exception.SerializationNotExistException;

import static com.lmj.rpc.Constants.Serialization.HESSIAN;
import static com.lmj.rpc.Constants.Serialization.JSON;
import static com.lmj.rpc.Constants.Serialization.MODE;

public class SerializationUtils {
    public static Serialization get(short extraInfo) {
        switch (extraInfo & MODE) {
            case HESSIAN:
                return HessianSerialization.Inner.serialization;
            case JSON:
                return JsonSerialization.Inner.serialization;
            default:
                throw new SerializationNotExistException(extraInfo);
        }
    }
}
