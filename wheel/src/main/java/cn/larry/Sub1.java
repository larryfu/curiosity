package cn.larry;

import cn.larry.test.Sub2;
import cn.larry.test.Super;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * Created by larry on 16-7-10.
 */
public class Sub1 extends Super {
    public Sub1(String name) {
        super(name);
    }

    public static void mains(String[] args) {
        int a = 128;
        for (int i = 31; i >= 0; i--) {
            System.out.print((a >> i) % 2);
        }
        System.out.println();
        int b = -128;
        for (int i = 31; i >= 0; i--) {
            System.out.print(Math.abs((b >> i) % 2));
        }
    }

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(4);
        byteBuf.writeInt(0x167c);
        byte[] bytes = byteBuf.array();
        byteBuf.resetReaderIndex();
        byteBuf.resetWriterIndex();
        byteBuf.clear();
        byteBuf.writeShort(60132);
        byte[] bytes1 = byteBuf.array();
        System.out.println();

    }
}
