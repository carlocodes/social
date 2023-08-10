package com.carlocodes.scoial.utilities;

import com.carlocodes.scoial.dtos.Otp;

import java.util.HashMap;
import java.util.Map;

public class InMemoryOtpCache {
    private static final Map<String, Otp> otpCache = new HashMap<>();

    public static void put(String email, Otp otp) {
        otpCache.put(email, otp);
    }

    public static Otp get(String email) {
        return otpCache.get(email);
    }

    public static void remove(String email) {
        otpCache.remove(email);
    }
}
