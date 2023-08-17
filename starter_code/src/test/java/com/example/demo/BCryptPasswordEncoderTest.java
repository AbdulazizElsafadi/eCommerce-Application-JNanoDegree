package com.example.demo;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest(classes = SareetaApplication.class)
public class BCryptPasswordEncoderTest {

    private SareetaApplication sareetaApplication;

    @Before
    public void setUP() {
        sareetaApplication = new SareetaApplication();
    }

    @Test
    public void BCryptPasswordEncoder() {
        BCryptPasswordEncoder response = sareetaApplication.bCryptPasswordEncoder();
        Assert.assertEquals(BCryptPasswordEncoder.class, response.getClass());

    }

}
