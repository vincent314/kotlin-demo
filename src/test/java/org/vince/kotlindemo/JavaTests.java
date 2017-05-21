package org.vince.kotlindemo;

import org.junit.Test;

public class JavaTests {

    @Test
    public void valVar(){
        final int value = 1;
        int variable = 1;

        // forbidden : value = 2;
        variable = 2;
    }
}
