package life.codedw.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @Author wangjiefang
 * @Date 23:21 2020/12/20
 * @Description
 **/
public class NettyByteBuf01 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        //1、创建对象，该对象包含了一个数组arr，是一个byte[10]
        //2、在netty的ByteBuf，不用切换，底层维护了一个readerIndex和writerIndex
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        System.out.println(buffer.capacity());
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readerIndex());
        }
    }
}
