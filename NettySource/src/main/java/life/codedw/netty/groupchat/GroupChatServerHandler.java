package life.codedw.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import jdk.jfr.events.ExceptionThrownEvent;

import java.nio.channels.ServerSocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author wangjiefang
 * @Date 23:50 2020/12/20
 * @Description
 **/
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //顶一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE是全局的时间执行器
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //handlerAdded连接建立，一旦链接，第一个被执行
    //将当前channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入到来哦天的信息推送到其他在线的客户端
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+" 加入聊天"+sdf.format(new Date())+"\n");
        channelGroup.add(channel);
    }

    //断开连接，将xx客户离开信息推送到当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+" 已离开!\n");
    }

    //表示channel处于活动状态，提示信息上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 上线了~");
    }

    //表示channel处于非活动状态，提示信息离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 离线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当前channel
        Channel channel = ctx.channel();
        //遍历，不同情况处理不同的事件
        channelGroup.forEach(ch->{
            if (channel !=ch){
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+" 发送消息"+msg+"\n");
            }else {
                ch.writeAndFlush("[自己]发送了消息"+msg+"\n");
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
