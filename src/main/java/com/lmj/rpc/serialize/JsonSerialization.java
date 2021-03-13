package com.lmj.rpc.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * json序列化
 */
public class JsonSerialization implements Serialization {

    private static ObjectMapper mapper = new ObjectMapper();

    public static class Inner {
        static Serialization serialization = new JsonSerialization();
    }

    private JsonSerialization() {
    }

    @Override
    public byte[] serialize(Object data) throws IOException {
        return mapper.writeValueAsBytes(data);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        return mapper.readValue(data, clz);
    }
}