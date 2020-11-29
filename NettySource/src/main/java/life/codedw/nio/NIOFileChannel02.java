package life.codedw.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author wangjiefang
 * @Date 22:10 2020/11/29
 * @Description
 **/
public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {
        //创建文件的输入流
        File file = new File("./file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过 fileInputStream 获取对应的 FileChannel -> 实际类型 FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据读到缓冲区
        int read = fileChannel.read(byteBuffer);

        //将byteBuffer的字节数据转换成string
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
