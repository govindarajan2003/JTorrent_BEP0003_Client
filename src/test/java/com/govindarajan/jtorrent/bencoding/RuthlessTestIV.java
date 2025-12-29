package com.govindarajan.jtorrent.bencoding;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ruthless Validation Tests IV")
public class RuthlessTestIV {

    @Test
    void testDictionaryConsumption() {
        // Input: List containing a Dictionary and a String.
        // l d 3:key 5:value e 3:end e
        // If your dictionary doesn't consume its 'e', the outer list
        // will think that 'e' is ITS end marker and terminate early.
        String input = "ld3:key5:valuee3:ende";
        Bencoder b = new Bencoder(input);

        List<Object> result = (List<Object>) b.decode();

        // Expected: [ Map{"key"="value"}, "end" ]
        assertEquals(2, result.size(), "Outer list should have 2 elements. Did you consume the dictionary's 'e'?");

        Object second = result.get(1);
        assertEquals("end", second, "Second element should be 'end'.");
    }
}
