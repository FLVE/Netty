package life.codedw.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author wangjiefang
 * @Date 18:48 2020/12/28
 * @Description
 **/
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //入栈进行解码
        pipeline.addLast(new MyByteToLongDecoder());

        //自定义handler
        pipeline.addLast(new MyServerHandler());
    }
}
