package com.lmj.rpc.codec;

import com.lmj.rpc.compressor.Compressor;
import com.lmj.rpc.compressor.CompressorUtils;
import com.lmj.rpc.protocol.Header;
import com.lmj.rpc.protocol.Message;
import com.lmj.rpc.serialize.Serialization;
import com.lmj.rpc.serialize.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Objects;

/**
 * 消息的encoder
 */
public class RpcEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          Message message, ByteBuf byteBuf) throws Exception {
        Header header = message.getHeader();
        // 依次序列化消息头中的魔数、版本、附加信息以及消息ID
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getExtraInfo());
        byteBuf.writeLong(header.getMsgId());

        if (Objects.nonNull(message.getContent())) {
            // 按照extraInfo部分指定的序列化方式和压缩方式进行处理
            Serialization serialization = SerializationUtils.get(header.getExtraInfo());
            Compressor compressor = CompressorUtils.get(header.getExtraInfo());
            byte[] payload = compressor.compress(serialization.serialize(message.getContent()));

            // 写入消息体长度
            byteBuf.writeInt(payload.length);
            byteBuf.writeBytes(payload);
            return;
        }

        //心跳
        byteBuf.writeInt(0);
    }
}