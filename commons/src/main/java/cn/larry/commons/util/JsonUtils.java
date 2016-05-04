package cn.larry.commons.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by larryfu on 2016/1/25.
 * 对jackson的简单封装，使用全局的ObjectMapper
 *
 * @author larryfu
 */
public class JsonUtils {

    private JsonUtils() {
    }

    private final static ObjectMapper om = new ObjectMapper();

    public static <E> E readValue(InputStream is, Class<E> clazz) throws IOException {
        return om.readValue(is, clazz);
    }

    public static <E> E readValue(File fl, Class<E> clazz) throws IOException {
        return om.readValue(fl, clazz);
    }

    public static <E> E readValue(String str, Class<E> clazz) throws IOException {
        return om.readValue(str, clazz);
    }

    public static void writeValue(File fl, Object obj) throws IOException {
        om.writeValue(fl, obj);
    }

    public static String writeValueAsString(Object obj) throws JsonProcessingException {
        return om.writeValueAsString(obj);
    }

    public static void writeValue(OutputStream os, Object obj) throws IOException {
        om.writeValue(os, obj);
    }


}
