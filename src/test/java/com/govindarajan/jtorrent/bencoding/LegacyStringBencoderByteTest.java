package com.govindarajan.jtorrent.bencoding;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Bencoder Testing")
public class LegacyStringBencoderByteTest {
    private BencodeDecoder bencoder;
    @BeforeAll
    public static void setupAll(){
        System.out.println("Bencoder test started");
    }
    @Test
    void testStringBencoder(){
        String val = "4:demo";
        bencoder = new BencodeDecoder(val.getBytes(StandardCharsets.UTF_8));
        String actual = new String((byte[]) bencoder.decode(), StandardCharsets.UTF_8);
        String expected = "demo";
        assertEquals(expected, actual);
    }
//    @Test
//    void testStringBencoder2(){
//        bencoder = new Bencoder("10:helloworld");
//        String actual = (String) bencoder.decode();
//        String expected = "helloworld";
//        assertEquals(expected, actual);
//    }
//    @Test
//    void testIntegerBencoder(){
//        bencoder = new Bencoder("i42e");
//        Integer actual = (Integer) bencoder.decode();
//        Integer expected = 42;
//        assertEquals(expected, actual);
//    }
//    @Test
//    void testIntegerBencoder2(){
//        bencoder = new Bencoder("i-5e");
//        Integer actual = (Integer) bencoder.decode();
//        Integer expected = -5;
//        assertEquals(expected, actual);
//    }
    @Test
    void testListBencoder(){
        String val = "l4:spami42ee";
        bencoder = new BencodeDecoder(val.getBytes(StandardCharsets.UTF_8));
        List<Object> actual = (List<Object>) bencoder.decode();
        assertEquals(2, actual.size());
        byte[] firstElement = (byte[]) actual.get(0);
        assertEquals("spam", new String(firstElement, StandardCharsets.UTF_8));
        assertEquals(42L, actual.get(1));
    }
//    @Test
//    void testListBencoder2(){
//        bencoder = new Bencoder("ll4:spamee");
//        List<Object> actual = (List<Object>) bencoder.decode();
//        List<Object> expected = Arrays.asList(List.of("spam"));
//        assertEquals(expected, actual);
//    }
    @Test
    void testDictBencoder(){
        bencoder = new BencodeDecoder(
                "d3:cow3:moo4:spam4:eggse"
                        .getBytes(StandardCharsets.UTF_8)
        );
        Map<String ,Object> actual = (Map<String ,Object>) bencoder.decode();
        assertEquals(2, actual.size());
        byte[] cowVal = (byte[]) actual.get("cow");
        assertEquals("moo", new String(cowVal, StandardCharsets.UTF_8));
        byte[] spamVal = (byte[]) actual.get("spam");
        assertEquals("eggs", new String(spamVal,StandardCharsets.UTF_8));
    }
}
