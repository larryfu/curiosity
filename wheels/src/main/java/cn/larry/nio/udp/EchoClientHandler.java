package cn.larry.nio.udp;



import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import java.io.UnsupportedEncodingException;


public class EchoClientHandler extends ChannelInboundHandlerAdapter {

 private final ByteBuf firstMessage;
 
    /**
      * Creates a client-side handler.
       */
      public EchoClientHandler() {
          String str = "echoclient";
          byte[] bytes = str.getBytes();
         firstMessage = Unpooled.buffer(bytes.length);
          firstMessage.writeBytes(bytes);
//         for (int i = 0; i < firstMessage.capacity(); i ++) {
//              firstMessage.writeByte((byte) i);
//         }
      }
  
      @Override
      public void channelActive(ChannelHandlerContext ctx) {
          ctx.writeAndFlush(firstMessage);
      }
  
      @Override
      public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
          DatagramPacket packet = (DatagramPacket)msg;
          byte[] bytes = new byte[packet.content().readableBytes()];
         packet.content().readBytes(bytes);
          String pFromServer =new String(bytes); //new String(packet., 0, packet.getLength(), "UTF-8");
          System.out.println("【NOTE】>>>>>> 收到服务端的消息：" + pFromServer);

          String str = "echoclient";
          byte[] bytes1 = str.getBytes();
          ByteBuf message = Unpooled.buffer(bytes1.length);
          message.writeBytes(bytes1);
          ctx.write(message);
      }
  
     @Override
      public void channelReadComplete(ChannelHandlerContext ctx) {
         ctx.flush();
      }
  
     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {         // Close the connection when an exception is raised.
          cause.printStackTrace();
         ctx.close();
      }
  }