package life.codedw.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author wangjiefang
 * @Date 18:50 2020/12/28
 * @Description
 **/
public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     *
     * @param ctx 上下文对象
     * @param in 入栈的ByteBuf
     * @param out List集合，将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("Server 解码 执行");
        if (in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}
