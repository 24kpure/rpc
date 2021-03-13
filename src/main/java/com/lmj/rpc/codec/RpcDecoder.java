package com.lmj.rpc.codec;


import com.lmj.rpc.Constants;
import com.lmj.rpc.compressor.Compressor;
import com.lmj.rpc.compressor.CompressorUtils;
import com.lmj.rpc.exception.UnLegalHeaderException;
import com.lmj.rpc.protocol.Header;
import com.lmj.rpc.protocol.Message;
import com.lmj.rpc.protocol.Request;
import com.lmj.rpc.protocol.Response;
import com.lmj.rpc.serialize.Serialization;
import com.lmj.rpc.serialize.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.lmj.rpc.Constants.Type.MODE;
import static com.lmj.rpc.Constants.Type.REQUEST;
import static com.lmj.rpc.Constants.Type.RESPONSE;

public class RpcDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() < Constants.HEADER_SIZE) {
            //消息头小于最小 headerSize
            throw new UnLegalHeaderException("size is less than ：" + Constants.HEADER_SIZE);
        }
        // 记录当前readIndex指针的位置，方便重置
        byteBuf.markReaderIndex();
        // 尝试读取消息头的魔数部分
        short magic = byteBuf.readShort();
        if (magic != Constants.MAGIC) {
            // 魔数不匹配会抛出异常
            byteBuf.resetReaderIndex(); // 重置readIndex指针
            throw new UnLegalHeaderException("magic number error:" + magic);
        }
        // 依次读取消息版本、附加信息、消息ID以及消息体长度四部分
        byte version = byteBuf.readByte();
        byte extraInfo = byteBuf.readByte();
        long messageId = byteBuf.readLong();
        int size = byteBuf.readInt();

        Object body = null;
        if (size > 0) {
            // 对于非心跳消息，没有积累到足够的数据是无法进行反序列化的
            if (byteBuf.readableBytes() < size) {
                byteBuf.resetReaderIndex();
                return;
            }
            // 读取消息体并进行反序列化
            byte[] payload = new byte[size];
            byteBuf.readBytes(payload);
            // 这里根据消息头中的extraInfo部分选择相应的序列化和压缩方式
            //todo method factory
            Serialization serialization = SerializationUtils.get(extraInfo);
            Compressor compressor = CompressorUtils.get(extraInfo);

            //todo factory
            switch (extraInfo & MODE) {
                case REQUEST:
                    // 得到消息体
                    body = serialization.deserialize(compressor.unCompress(payload),
                            Request.class);
                    break;
                case RESPONSE:
                    // 得到消息体
                    body = serialization.deserialize(compressor.unCompress(payload),
                            Response.class);
                    break;
                default:
                    throw new UnLegalHeaderException("msgType is unlegal:" + extraInfo);
            }
        }
        // 将上面读取到的消息头和消息体拼装成完整的Message并向后传递
        Header header = new Header(magic, version, extraInfo, messageId, size);
        out.add(new Message(header, body));
    }
}