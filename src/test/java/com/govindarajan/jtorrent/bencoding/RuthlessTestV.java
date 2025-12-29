package com.govindarajan.jtorrent.bencoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ruthless Validation Tests V")
public class RuthlessTestV {

    @Test
    void testNestedListConsumptionAgains() {
        // Input: List containing a List AND a String.
        // l l 4:spam e 3:end e
        // If the inner list doesn't consume its 'e', the outer list
        // will think that 'e' belongs to IT and terminate early.
        // You fixed Dictionary, but you ignored List. This test will prove it.
        String input = "ll4:spame3:ende";
        Bencoder b = new Bencoder(input);

        List<Object> result = (List<Object>) b.decode();

        // Expected: [ ["spam"], "end" ]
        assertEquals(2, result.size(),
                "Outer list should have 2 elements. You still haven't fixed the List termination logic!");
        assertEquals("end", result.get(1));
    }
}
