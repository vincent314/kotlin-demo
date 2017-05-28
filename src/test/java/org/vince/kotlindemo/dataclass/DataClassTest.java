package org.vince.kotlindemo.dataclass;

import org.junit.Test;

public class DataClassTest {

    @Test
    public void testDataClass(){
        JavaUser javaUser = new JavaUser("John", "DOE");
        System.out.println("Hello, " + javaUser.getFirstname() + " "
                + javaUser.getLastname());
    }
}
