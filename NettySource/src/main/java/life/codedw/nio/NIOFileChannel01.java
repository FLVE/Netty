package life.codedw.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author wangjiefang
 * @Date 21:55 2020/11/29
 * @Description
 **/
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {
        String str = "hello,尚硅谷";
        //创建一个输出流——>channel
        FileOutputStream fileOutputStream = new FileOutputStream("./file01.txt");
        //通过fileOutputStream获取对应的FileChannel
        //fileChannel真是类型为FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        //创建一个缓冲区byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入到byteBuffer
        byteBuffer.put(str.getBytes());
        //对byteBuffer翻转
        byteBuffer.flip();
        //将byteBuffer数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
