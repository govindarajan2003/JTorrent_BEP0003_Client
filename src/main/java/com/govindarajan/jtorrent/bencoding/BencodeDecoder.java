package com.govindarajan.jtorrent.bencoding;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BencodeDecoder {
    byte[] raw;
    ByteBuffer buffer;

    public BencodeDecoder(byte[] text) {
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
        // Getting start & end to figure out the size of String.
        int start_idx = buffer.position();
        while (buffer.get() != ':') {}
        int end_idx = buffer.position() - 1;
        String sizeStr = new String(raw, start_idx, end_idx - start_idx);
        int size = Integer.parseInt(sizeStr);

        byte[] data = new byte[size];
        buffer.get(data);

        return data;
    }

    public Long decodeBencodedInteger() {
        // Getting buffer to get rid of the first char.
        buffer.get();

        Long value = 0L;
        // Checker because -0 is not valid.
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

        // 0 without suceeding values are accepted
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
            // Checker to make sure there are not preceeding 0's.
            if(isZero)
                throw new RuntimeException("invalid bencoded integer: cannot start with zero");
            value *= 10;
            Long temp = Byte.toUnsignedLong(buffer.get()) - '0';
            value += temp;
        }
        // To push out 'e' at the end.
        buffer.get();
        if (isNegative)
            value = -value;
        return value;
    }

    public List<Object> decodeBencoderList(){
        // Getting rid of the first char
        buffer.get();

        List<Object> result = new ArrayList<>();
        while(buffer.get(buffer.position()) != 'e')
            result.add(decode());

        // Getting rid of the last char
        buffer.get();

        return result;
    }
    public Map<String, Object> decodeBencoderDictionary(){
       // Getting rid of the first char.
       buffer.get();

       Map<String, Object> result = new HashMap<>();
       // To convert to String from byte[] we need to use StandardCharsets
       // We have <String, Object> so need to cast,
       // first one to String and the next is left as byte[]
       while(buffer.get(buffer.position()) != 'e')
          result.put(
                  new String(
                          (byte[])decode(),
                          StandardCharsets.UTF_8
                  ),
                  decode());

       // Getting rid of the last char
       buffer.get();
       return result;
    }
}
