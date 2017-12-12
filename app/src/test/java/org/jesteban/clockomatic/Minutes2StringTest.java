package org.jesteban.clockomatic;


import org.jesteban.clockomatic.helpers.Minutes2String;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Minutes2StringTest {
    @Test
    public void test_1entry_is_unfinished(){
        String str = Minutes2String.convert(1);
        assertEquals("00:01", str );
    }
}
