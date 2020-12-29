package life.codedw.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author wangjiefang
 * @Date 18:58 2020/12/28
 * @Description
 **/
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();


        //加入自定义的handler 处理业务
        pipeline.addLast(new MyClientHandler());

        //出栈的handler，对数据进行编码
        pipeline.addLast(new MyLongToByteEncoder());
    }
}
