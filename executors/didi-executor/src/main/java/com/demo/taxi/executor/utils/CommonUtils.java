package com.demo.taxi.executor.utils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import lombok.SneakyThrows;

public class CommonUtils {

  public static String randomBase64UUID() {
    UUID randomUUID = UUID.randomUUID();
    ByteBuffer wrap = ByteBuffer.wrap(new byte[16]);
    wrap.putLong(randomUUID.getMostSignificantBits());
    wrap.putLong(randomUUID.getLeastSignificantBits());
    return Base64.getEncoder().encodeToString(wrap.array()).replace('_', '-');
  }

  public static String buildSSUUid(String brand, String model, String fingerPrint) {
    SecureRandom secureRandom = new SecureRandom();
    return md5Hex(brand + model + fingerPrint + System.nanoTime() + secureRandom.nextLong());
  }

  @SneakyThrows
  public static String md5Hex(String str) {
    byte[] digest;
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(str.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte b : messageDigest.digest()) {
        sb.append(Integer.toHexString((b >> 4) & 15).toLowerCase());
        sb.append(Integer.toHexString(b & 15).toLowerCase());
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException unused) {
      return null;
    }
  }
}
