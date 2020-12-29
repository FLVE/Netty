package life.codedw.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @Author wangjiefang
 * @Date 15:51 2020/12/19
 * @Description
 **/

/**
 * SimpleChannelInboundHandler是ChannelInboundHandlerAdapter的子类
 * HttpObject客户端和服务端相互通信的数据被封装成httpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是httprequest
        if (msg instanceof HttpRequest){
            System.out.println("msg类型="+msg.getClass());
            System.out.println("客户端地址:"+ctx.channel().remoteAddress());

            //获取到
            HttpRequest httpRequest=(HttpRequest) msg;
            //获取uri,过滤指定资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了favicon.ico,不作响应");
                return;
            }
            //回复信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("hello,I am Server我是服务器", CharsetUtil.UTF_8);
            //构造一个http的响应,即httpresponse
            FullHttpMessage response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            //将构建好response返回
            ctx.writeAndFlush(response);
        }
    }
}
