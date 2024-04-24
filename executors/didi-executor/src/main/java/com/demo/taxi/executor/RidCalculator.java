package com.demo.taxi.executor;

import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class RidCalculator {

  private static final AtomicInteger ATOMIC_INT = new AtomicInteger(new SecureRandom().nextInt());
  private static final RandomGeneratorFactory<RandomGenerator> random = RandomGeneratorFactory.of(
      "L128X128MixRandom");


  public static String calc(InetAddress inetAddress) {
    byte[] bArr;
    byte[] bArr2 = new byte[16];
    if (inetAddress != null) {
      bArr = inetAddress.getAddress();
    } else {
      bArr = new byte[4];
      new SecureRandom().nextBytes(bArr);
    }
    System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
    int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
    bArr2[4] = (byte) ((currentTimeMillis >> 24) & 255);
    bArr2[5] = (byte) ((currentTimeMillis >> 16) & 255);
    bArr2[6] = (byte) ((currentTimeMillis >> 8) & 255);
    bArr2[7] = (byte) (currentTimeMillis & 255);

    // 根据Linux pid 分配区间随机生成pid
    int myPid = random.create().nextInt(300, 32768);
    bArr2[8] = (byte) ((myPid >> 24) & 255);
    bArr2[9] = (byte) ((myPid >> 16) & 255);
    bArr2[10] = (byte) ((myPid >> 8) & 255);
    bArr2[11] = (byte) (myPid & 255);
    int andIncrement = ATOMIC_INT.getAndIncrement();
    bArr2[12] = (byte) ((andIncrement >> 24) & 255);
    bArr2[13] = (byte) ((andIncrement >> 16) & 255);
    bArr2[14] = (byte) ((andIncrement >> 8) & 255);
    bArr2[15] = (byte) (andIncrement & 255);
    return hexToStr(bArr2);
  }

  private static String hexToStr(byte[] bArr) {
    StringBuilder sb = new StringBuilder();
    int length = bArr.length;
    for (byte b : bArr) {
      sb.append(String.format("%02x", b & 255));
    }
    return sb.toString();
  }

}
