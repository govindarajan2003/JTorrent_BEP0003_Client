package com.govindarajan.jtorrent.legacy.bencoding;

import com.govindarajan.jtorrent.bencoding.BencodeDecoder;

import java.nio.ByteBuffer;
/**
 * @deprecated This was a learning implementation. Use {@link BencodeDecoder} instead.
 */
@Deprecated
public class LegacyBytesBencoder {
    byte[] raw;
    ByteBuffer buffer;

    public LegacyBytesBencoder(byte[] text) {
        this.raw = text;
        buffer = ByteBuffer.wrap(raw);
    }

    public Object decode() {
        if (Character.isDigit(buffer.get(buffer.position())))
            return decodeBencodedString();
        if (buffer.get() == 'i')
            return decodeBencodedInteger();
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
        Long value = 0L;
        boolean isNegative = false;
        if (buffer.get(buffer.position()) == '-') {
            isNegative = true;
            if (buffer.get(buffer.position()) - '0' == 0)
                throw new NumberFormatException("" +
                        "invalid bencoded integer: cannot start with -0");
            buffer.get();
        }
        while (buffer.get(buffer.position()) != 'e') {
            value *= 10;
            Long temp = Byte.toUnsignedLong(buffer.get()) - '0';
            value += temp;
        }
        String decodedValue = String.valueOf(value);
        if (decodedValue.length() > 1 && decodedValue.startsWith("0"))
            throw new RuntimeException("invalid bencoded integer: cannot start with zero");
        buffer.get();
        if (isNegative)
            value = -value;
        return value;
    }
}
