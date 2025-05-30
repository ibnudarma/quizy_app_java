package com.idarma;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ObjectMapperTest {
    @Test
    void testObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        Assertions.assertNotNull(objectMapper);
    }
}
