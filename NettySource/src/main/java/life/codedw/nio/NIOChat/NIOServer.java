package life.codedw.nio.NIOChat;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author wangjiefang
 * @Date 22:29 2020/11/30
 * @Description
 **/
public class NIOServer {
    public static void main(String[] args) throws Exception {
        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector
        Selector selector = Selector.open();

        //绑定一个端口，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("注册后的selectionkey 数量=" + selector.keys().size()); // 1

        //循环等待客户端连接
        while (true) {

            //等待1秒，如果没有事件发生，返回
            if (selector.select(1000) == 0) {//没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            //如果返回非0,就获取到相关的selectionKey集合
            //表示已经获取到关注的事件
            //selector.selectedKeys() 返回关注事件的集合
            //通过selectionKeys可以反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历 Set<SelectionKey>
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            System.out.println("selectionKeys 数量 = " + selectionKeys.size());

            while (keyIterator.hasNext()) {
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生事件做相应的处理
                if (key.isAcceptable()) {//如果是OP_ACTIVE
                    //该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel "+socketChannel.hashCode());
                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将当前socketChannel注册到selector
                    //关注事件为OP_READ,同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后，注册的selectionkey数量="+selector.keys().size());
                }
                if (key.isReadable()) {//如果是OP_READ
                    //通过key反向获取到对应的Channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端 "+new String(buffer.array()));
                }
                //手动从集合中移动当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
