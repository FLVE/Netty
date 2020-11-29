package life.codedw.nio;

import java.nio.IntBuffer;

/**
 * @Author wangjiefang
 * @Date 16:28 2020/11/29
 * @Description
 **/
public class BasicBuffer {
    public static void main(String[] args) {
        //创建一个buffer
        //创建一个buffer，大小为5
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //向buffer存放数据
        //intBuffer.put(10);
        //intBuffer.put(11);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }

        //从buffer中取数据
        //将buffer转换，读写切换(!!!)
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
