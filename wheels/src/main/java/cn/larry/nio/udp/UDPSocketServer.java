package cn.larry.nio.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by larry on 3/21/2017.
 */
public class UDPSocketServer {

    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(8080);

        while (true) {
            byte[] data = new byte[1024];
            // 接收数据报的包
            DatagramPacket packet = new DatagramPacket(data, data.length);
            datagramSocket.receive(packet);
            String pFromServer = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
            System.out.println("【NOTE】>>>>>> 收到客户端的消息：" + pFromServer);
            String toServer = "Hi，我是服务端，我的时间戳" + System.currentTimeMillis();
            byte[] soServerBytes = toServer.getBytes("UTF-8");
            DatagramPacket rsp = new DatagramPacket(soServerBytes, soServerBytes.length);
            rsp.setAddress(packet.getAddress());
            rsp.setPort(packet.getPort());
            datagramSocket.send(rsp);
        }
    }
}
