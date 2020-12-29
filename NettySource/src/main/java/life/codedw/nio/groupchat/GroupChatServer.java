package life.codedw.nio.groupchat;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Author wangjiefang
 * @Date 23:06 2020/12/1
 * @Description
 **/
public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //初始化
    public GroupChatServer() {
        try {
            //得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将listenChannel注册到Selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try {
            //循环处理
            while (true){
                int count = selector.select(2000);
                if (count>0){//有事件处理
                    //遍历得到的selectkey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出selectkey
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if (key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将该sc注册到selector
                            sc.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress()+" 上线 ");
                        }
                        if (key.isReadable()){//通道发生read事件，即通道是可读的状态
                            //处理读的状态
                            readData(key);
                        }
                        //当前的key删除
                        iterator.remove();
                    }
                }else {
                    //System.out.println("等待...");
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }finally {

        }
    }

    //读取客户端消息
    public void readData(SelectionKey key){
        //定义一个SocketChannel
        SocketChannel channel = null;
        try {
            //取到关联的channel
            channel = (SocketChannel) key.channel();
            //创建Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            //根据count的值做处理
            if (count>0){
                //把缓冲区的数据转换成字符串
                String msg = new String(buffer.array());
                //输出此消息
                System.out.println("from 客户端："+msg);

                //向其他的客户端转发消息(去掉自己)
                sendInfoToClient(msg,channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress()+" 离线了...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    //转发消息给其他的客户端(通道)
    private void sendInfoToClient(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        //遍历所有注册到selector上的socketChannel，并排除自己
        for (SelectionKey key:selector.keys()) {
            //通过key取出对应的SocketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self){
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入到通道
                dest.write(buffer);
            }
        }

    }


    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
