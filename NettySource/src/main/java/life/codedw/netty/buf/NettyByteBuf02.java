package life.codedw.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @Author wangjiefang
 * @Date 23:30 2020/12/20
 * @Description
 **/
public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建按ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world", CharsetUtil.UTF_8);
        //使用相关的方法
        if (byteBuf.hasArray()){
            byte[] content = byteBuf.array();
            //将content转换成字符串
            System.out.println(new String(content, Charset.forName("utf-8")));

            System.out.println(byteBuf);
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

            int len = byteBuf.readableBytes();
            System.out.println(len);
        }
    }
}
