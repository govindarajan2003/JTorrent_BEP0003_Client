package com.govindarajan.jtorrent.bencoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ruthless Validation Tests II")
public class RuthlessTestII {

    @Test
    void testSequentialParsing() {
        // This is the CRITICAL test for stateful parsing.
        // Input: "4:spami42e" -> "spam" then 42
        Bencoder b = new Bencoder("4:spami42e");

        Object first = b.decode();
        assertEquals("spam", first, "First element should be 'spam'");

        Object second = b.decode();
        assertEquals(42, second, "Second element should be 42. Your cursor management might be off.");
    }
}
