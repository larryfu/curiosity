package cn.larry.decompile.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by larryfu on 16-4-6.
 */
public class ClassByte {
    private final byte[] content;
    private int index = 0;

    public ClassByte(Path path) throws IOException {
        content = Files.readAllBytes(path);
    }

    public byte getByte() {
        if (index < content.length) {
            return content[index++];
        } else
            throw new IllegalArgumentException("input end");
    }

    public short getShort() {
        if (index < content.length - 1) {
            short c = (short) (content[index] << 4 + content[index + 1]);
            index += 2;
            return c;
        } else
            throw new IllegalArgumentException("input end");
    }

    public char getChar() {
        if (index < content.length - 1) {
            char c = (char) (content[index] << 4 + content[index + 1]);
            index += 2;
            return c;
        } else
            throw new IllegalArgumentException("input end");
    }

    public int getInt() {
        if (index < content.length - 3) {
            int i = (int) (content[index] << 16 + content[index + 1] << 8 + content[index + 2] << 4 + content[index + 3]);
            index += 4;
            return i;
        } else
            throw new IllegalArgumentException("input end");
    }

    public boolean end() {
        return index >= content.length;
    }

    public int index() {
        return index;
    }

    public int left() {
        return content.length - index;
    }

}
