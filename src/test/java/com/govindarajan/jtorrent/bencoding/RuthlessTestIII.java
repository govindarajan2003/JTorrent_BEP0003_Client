package com.govindarajan.jtorrent.bencoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ruthless Validation Tests III")
public class RuthlessTestIII {

    @Test
    void testNestedListConsumption() {
        // Input: List containing a List and an Integer.
        // l l 4:spam e i42e e
        // If your inner list doesn't consume its 'e', the outer list
        // will think that 'e' belongs to IS and terminate early.
        String input = "ll4:spameei42ee";
        Bencoder b = new Bencoder(input);

        List<Object> result = (List<Object>) b.decode();

        // Expected: [ ["spam"], 42 ]
        assertEquals(2, result.size(),
                "Outer list should have 2 elements. Did you consume the inner list's 'e' properly?");
        assertEquals(42, result.get(1), "Second element should be 42.");
    }
}
