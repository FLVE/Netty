package life.codedw.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author wangjiefang
 * @Date 21:42 2020/12/21
 * @Description
 **/
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //将evt向下转型Idlestateevent
            IdleStateEvent event = (IdleStateEvent) evt;
            String evenType = null;
            switch (event.state()) {
                case READER_IDLE:
                    evenType = "读空闲";
                    break;
                case WRITER_IDLE:
                    evenType = "写空闲";
                    break;
                case ALL_IDLE:
                    evenType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress()+"---超时时间---"+evenType);
            System.out.println("服务器进行相应处理...");
            //如果空闲，关闭通道
            ctx.channel().close();

        }
    }
}
