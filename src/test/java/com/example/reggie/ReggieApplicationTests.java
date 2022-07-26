package com.example.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class ReggieApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(new Random().nextInt(10));
        String s = Integer.toHexString(new Random().nextInt());
        System.out.println(s);
    }

}
