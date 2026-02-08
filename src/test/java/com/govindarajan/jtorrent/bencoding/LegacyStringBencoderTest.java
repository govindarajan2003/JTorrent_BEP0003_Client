package com.govindarajan.jtorrent.bencoding;

import com.govindarajan.jtorrent.legacy.bencoding.LegacyStringBencoder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Bencoder Testing")
public class LegacyStringBencoderTest {
    private LegacyStringBencoder legacyStringBencoder;
    @BeforeAll
    public static void setupAll(){
        System.out.println("Bencoder test started");
    }
    @Test
    void testStringBencoder(){
        legacyStringBencoder = new LegacyStringBencoder("4:demo");
        String actual = (String) legacyStringBencoder.decode();
        String expected = "demo";
        assertEquals(expected, actual);
    }
    @Test
    void testStringBencoder2(){
        legacyStringBencoder = new LegacyStringBencoder("10:helloworld");
        String actual = (String) legacyStringBencoder.decode();
        String expected = "helloworld";
        assertEquals(expected, actual);
    }
    @Test
    void testIntegerBencoder(){
        legacyStringBencoder = new LegacyStringBencoder("i42e");
        Integer actual = (Integer) legacyStringBencoder.decode();
        Integer expected = 42;
        assertEquals(expected, actual);
    }
    @Test
    void testIntegerBencoder2(){
        legacyStringBencoder = new LegacyStringBencoder("i-5e");
        Integer actual = (Integer) legacyStringBencoder.decode();
        Integer expected = -5;
        assertEquals(expected, actual);
    }
    @Test
    void testListBencoder(){
        legacyStringBencoder = new LegacyStringBencoder("l4:spami42ee");
        List<Object> actual = (List<Object>) legacyStringBencoder.decode();
        List<Object> expected = Arrays.asList("spam", 42);
        assertEquals(expected, actual);
    }
    @Test
    void testListBencoder2(){
        legacyStringBencoder = new LegacyStringBencoder("ll4:spamee");
        List<Object> actual = (List<Object>) legacyStringBencoder.decode();
        List<Object> expected = Arrays.asList(List.of("spam"));
        assertEquals(expected, actual);
    }
    @Test
    void testDictBencoder(){
        legacyStringBencoder = new LegacyStringBencoder("d3:cow3:moo4:spam4:eggse");
        Map<Object,Object> actual = (Map<Object,Object>) legacyStringBencoder.decode();
        Map<String,String> expected = Map.of("cow", "moo", "spam", "eggs");
        assertEquals(expected, actual);
    }
}
