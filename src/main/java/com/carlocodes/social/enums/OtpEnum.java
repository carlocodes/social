package com.carlocodes.social.enums;

public enum OtpEnum {
    OTP_LENGTH(6),
    OTP_EXPIRATION_MINUTES(10);

    private final int value;

    OtpEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
