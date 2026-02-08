package com.govindarajan.jtorrent.legacy.bencoding;

import com.govindarajan.jtorrent.bencoding.BencodeDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @deprecated This was a learning implementation. Use {@link BencodeDecoder} instead.
 */
@Deprecated
public class LegacyStringBencoder {
    // The raw bencode
    private String raw;
    // Current index
    private int cursor;

    public LegacyStringBencoder(String raw) {
        this.raw = raw;
    }

    // Entry point to decode
    public Object decode() {
        if (Character.isDigit(raw.charAt(cursor)))
            return decodeBencodedString();
        if (raw.charAt(cursor) == 'i')
            return decodeBencodedInteger();
        if (raw.charAt(cursor) == 'l')
            return decodeBencoderList();
        if (raw.charAt(cursor) == 'd')
            return decodeBencoderDictionary();
        return null;
    }

    // Helper to decode string 4:qwer
    public String decodeBencodedString() {
        int start_idx = cursor;
        while (true) {
            if (raw.charAt(cursor) == ':')
                break;
            cursor++;
        }
        int end_idx = cursor;
        int size = Integer.parseInt(raw.substring(start_idx, end_idx));
        cursor += size + 1;
        return raw.substring(end_idx + 1, cursor);
    }

    // Helper to decode integer
    public Integer decodeBencodedInteger() {
        int start_idx = cursor + 1;
        int end_idx;
        while (true) {
            if (raw.charAt(cursor) == 'e')
                break;
            cursor++;
        }
        end_idx = cursor;
        cursor += 1;
        String decodedValue = raw.substring(start_idx, end_idx);
        if (decodedValue.startsWith("-0"))
            throw new NumberFormatException("invalid bencoded integer: cannot start with -0");
        if (decodedValue.length() > 1 && decodedValue.startsWith("0"))
            throw new RuntimeException("invalid bencoded integer: cannot start with zero");
        return Integer.valueOf(decodedValue);
    }

    // l l 4:spam e i42e e
    public List<Object> decodeBencoderList() {
        List<Object> result = new ArrayList<>();
        cursor++;
        while (cursor < raw.length() - 1) {
            if (raw.charAt(cursor) == 'e') {
                cursor++;
                return result;
            }
            result.add(decode());
        }
        return result;
    }

    // d3:cow3:moo4:spam4:eggse
    public Map<Object, Object> decodeBencoderDictionary() {
        Map<Object, Object> result = new HashMap<>();
        cursor++;
        while (raw.charAt(cursor) != 'e') {
            result.put(decode(), decode());
        }
        cursor++;
        return result;
    }
}
