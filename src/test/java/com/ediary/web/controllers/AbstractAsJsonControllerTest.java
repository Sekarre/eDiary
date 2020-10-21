package com.ediary.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractAsJsonControllerTest {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
