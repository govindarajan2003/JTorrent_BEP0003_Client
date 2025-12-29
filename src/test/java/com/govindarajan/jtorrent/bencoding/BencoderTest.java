package com.govindarajan.jtorrent.bencoding;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Bencoder Testing")
public class BencoderTest {
    private Bencoder bencoder;
    @BeforeAll
    public static void setupAll(){
        System.out.println("Bencoder test started");
    }
    @Test
    void testStringBencoder(){
        bencoder = new Bencoder("4:demo");
        String actual = (String) bencoder.decode();
        String expected = "demo";
        assertEquals(expected, actual);
    }
    @Test
    void testStringBencoder2(){
        bencoder = new Bencoder("10:helloworld");
        String actual = (String) bencoder.decode();
        String expected = "helloworld";
        assertEquals(expected, actual);
    }
    @Test
    void testIntegerBencoder(){
        bencoder = new Bencoder("i42e");
        Integer actual = (Integer) bencoder.decode();
        Integer expected = 42;
        assertEquals(expected, actual);
    }
    @Test
    void testIntegerBencoder2(){
        bencoder = new Bencoder("i-5e");
        Integer actual = (Integer) bencoder.decode();
        Integer expected = -5;
        assertEquals(expected, actual);
    }
    @Test
    void testListBencoder(){
        bencoder = new Bencoder("l4:spami42ee");
        List<Object> actual = (List<Object>) bencoder.decode();
        List<Object> expected = Arrays.asList("spam", 42);
        assertEquals(expected, actual);
    }
    @Test
    void testListBencoder2(){
        bencoder = new Bencoder("ll4:spamee");
        List<Object> actual = (List<Object>) bencoder.decode();
        List<Object> expected = Arrays.asList(List.of("spam"));
        assertEquals(expected, actual);
    }
    @Test
    void testDictBencoder(){
        bencoder = new Bencoder("d3:cow3:moo4:spam4:eggse");
        Map<Object,Object> actual = (Map<Object,Object>) bencoder.decode();
        Map<String,String> expected = Map.of("cow", "moo", "spam", "eggs");
        assertEquals(expected, actual);
    }
}
