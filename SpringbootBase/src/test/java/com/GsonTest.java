package com;

import com.google.gson.Gson;
import org.junit.Test;

public class GsonTest {

    @Test
    public void test01() {
        String s = new Gson().toJson(null);
        System.out.println(s);
    }
}
