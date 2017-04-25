package cn.larry.nio.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class EchoClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        DatagramSocket datagramSocket = new DatagramSocket(9999);
        // 调用connect之后，每次send时DatagramPacket就不需要设计目标主机的ip和port了
        // * 注意：connect方法一定要在DatagramSocket.receive()方法之前调用，
        // * 不然整send数据将会被错误地阻塞。这或许是官方API的bug，也或许是调
        // * 用规范就应该这样，但没有找到官方明确的说明
//        datagramSocket.connect(
//                InetAddress.getByName("127.0.0.1"), 8080);
//        datagramSocket.setReuseAddress(true);
        System.out.println("new DatagramSocket()已成功完成.");

        while (true) {
            String toServer = "Hi，我是客户端，我的时间戳" + System.currentTimeMillis();
            byte[] soServerBytes = toServer.getBytes("UTF-8");
            DatagramPacket req = new DatagramPacket(soServerBytes, soServerBytes.length);
            req.setAddress(InetAddress.getByName("127.0.0.1"));
            req.setPort(8081);
            datagramSocket.send(req);

            byte[] data = new byte[1024];
            // 接收数据报的包
            DatagramPacket packet = new DatagramPacket(data, data.length);
            if ((datagramSocket.isClosed()))
                continue;
            // 阻塞直到收到数据
            datagramSocket.receive(packet);

            // 解析服务端发过来的数据
            String pFromServer = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
            System.out.println("【NOTE】>>>>>> 收到服务端的消息：" + pFromServer);
            Thread.sleep(1000);
        }
    }

}


