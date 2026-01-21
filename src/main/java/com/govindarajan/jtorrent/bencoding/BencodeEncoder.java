package com.govindarajan.jtorrent.bencoding;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.util.stream.Collectors;

public class BencodeEncoder {
    public static byte[] encode(Object obj) throws IOException {
        if(obj instanceof Long)
            return encodeLong(obj);
        if(obj instanceof byte[])
            return encodeString(obj);
        if(obj instanceof String)
            return encodeString(((String) obj).getBytes());
        if(obj instanceof List<?>)
            return encodeList(obj);
        if(obj instanceof Map<?,?>)
            return encodeDictionary(obj);
        return null;
    }
    public static byte[] encodeLong(Object obj){
        String data = "i"+obj.toString()+"e";
        return data.getBytes(StandardCharsets.UTF_8);
    }
    public static byte[] encodeString(Object obj) throws IOException {
        byte[] data = (byte[]) obj;
        String prefix = data.length +":";
        byte[] prefixByte = prefix.getBytes(StandardCharsets.UTF_8);
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            baos.write(prefixByte);
            baos.write(data);
            return baos.toByteArray();
        }
    }
    public static byte[] encodeList(Object obj) throws IOException {
        ArrayList<?> list = (ArrayList<?>) obj;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write('l');
        for(Object item: list){
            baos.write(encode(item));
        }
        baos.write('e');
        return baos.toByteArray();
    }
    public static byte[] encodeDictionary(Object obj) throws IOException {
        Map<String,?> map = (HashMap<String,?>) obj;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write('d');
        List<String> sortedKeys = map.keySet().stream()
                .sorted()
                .collect(Collectors.toList());
        for(String key: sortedKeys){
            baos.write(encode(key));
            baos.write(encode(map.get(key)));
        }
        baos.write('e');
        return baos.toByteArray();
    }
}
