package com.futura.commerce.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class FuturaSecurityApplicationTests {

    @Test
    void testBcrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("123456");
        System.out.println("BCRYPT_HASH_FOR_123456: " + hash);
        org.junit.jupiter.api.Assertions.assertTrue(encoder.matches("123456", hash));
    }

}

