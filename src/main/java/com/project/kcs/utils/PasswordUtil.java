package com.project.kcs.utils;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

public class PasswordUtil {

    public static String convertEncoding(String password) {

        Encoder encoder = Base64.getEncoder();

        return new String(encoder.encode(password.getBytes()));
    }

    public static String convertDecoding(String password) {

        Decoder decoder = Base64.getDecoder();

        return new String(decoder.decode(password.getBytes()));
    }

}
