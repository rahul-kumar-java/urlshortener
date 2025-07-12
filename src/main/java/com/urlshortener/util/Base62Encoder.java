package com.urlshortener.util;

public class Base62Encoder {

	 // Base62 character set: 0-9, a-z, A-Z (total 62 characters)
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();

        // Convert the number to base62
        while (num > 0) {
            int remainder = (int)(num % 62);
            sb.append(BASE62.charAt(remainder));
            num = num / 62;
        }

        return sb.reverse().toString(); // Most significant digit first
}
}
