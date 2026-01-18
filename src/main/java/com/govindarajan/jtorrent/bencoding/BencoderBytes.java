package com.govindarajan.jtorrent.bencoding;

import com.sun.jdi.Value;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BencoderBytes {
    byte[] raw;
    ByteBuffer buffer;

    public BencoderBytes(byte[] text) {
        this.raw = text;
        buffer = ByteBuffer.wrap(raw);
    }

    public Object decode() {
        if (Character.isDigit(buffer.get(buffer.position())))
            return decodeBencodedString();
        if (buffer.get(buffer.position()) == 'i')
            return decodeBencodedInteger();
        if (buffer.get(buffer.position()) == 'l')
            return decodeBencoderList();
        if (buffer.get(buffer.position()) == 'd')
            return decodeBencoderDictionary();
        return null;
    }

    public byte[] decodeBencodedString() {
        int start_idx = buffer.position();
        while (buffer.get() != ':') {
        }
        int end_idx = buffer.position() - 1;
        String sizeStr = new String(raw, start_idx, end_idx - start_idx);
        int size = Integer.parseInt(sizeStr);
        byte[] data = new byte[size];
        buffer.get(data);
        return data;
    }

    public Long decodeBencodedInteger() {
        buffer.get();
        Long value = 0L;
        boolean isNegative = false;
        if (buffer.get(buffer.position()) == '-') {
            isNegative = true;
            if(buffer.get(buffer.position()+1) - '0' == 0)
                throw new
                        NumberFormatException("" +
                        "invalid bencoded integer: cannot start with -0"
                );
            buffer.get();
        }
        boolean isZero = false;
        if(buffer.get(buffer.position()) - '0' == 0) {
            if (buffer.get(buffer.position()+1) == 'e') {
                buffer.get();
                buffer.get();
                return 0L;
            }
            isZero = true;
            buffer.get();
        }
        while (buffer.get(buffer.position()) != 'e') {
            if(isZero)
                throw new RuntimeException("invalid bencoded integer: cannot start with zero");
            value *= 10;
            Long temp = Byte.toUnsignedLong(buffer.get()) - '0';
            value += temp;
        }
        String decodedValue = String.valueOf(value);
        buffer.get();
        if (isNegative)
            value = -value;
        return value;
    }
    public List<Object> decodeBencoderList(){
        buffer.get();
        List<Object> result = new ArrayList<>();
        while(buffer.get(buffer.position()) != 'e'){
            result.add(decode());
        }
        buffer.get();
        return result;
    }
    public Map<String, Object> decodeBencoderDictionary(){
       Map<String, Object> result = new HashMap<>();
       buffer.get();
       while(buffer.get(buffer.position()) != 'e'){
           result.put(new String((byte[])decode(), StandardCharsets.UTF_8) , decode());
       }
       buffer.get();
       return result;
    }
}
