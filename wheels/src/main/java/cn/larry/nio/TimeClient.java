package cn.larry.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.StandardSocketOptions;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimeClient {


    static final ExecutorService executorService = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        int num = 1;
        CountDownLatch latch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            executorService.submit(() -> {
                execute(latch);
            });
        }
        latch.await();

        long end = System.currentTimeMillis();
        System.out.println("cost :" + (end - start));


    }

    private static void execute(CountDownLatch latch) {
        String host = "127.0.0.1";// args[0];
        int port = 8080;//Integer.parseInt(args[1]);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        System.out.println("get time :" + Thread.currentThread().getName());
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });
            // 启动客户端
            for (int i = 0; i < 1; i++) {
                try {
                    ChannelFuture f = b.connect(host, port).sync(); // (5)
                    // 等待连接关闭
                    System.out.println("end thread "+Thread.currentThread().getName());

                    f.channel().closeFuture().sync();
                   // System.out.println("end thread "+Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            long end = System.currentTimeMillis();

        } finally {
            workerGroup.shutdownGracefully();
        }
        latch.countDown();
    }
}
