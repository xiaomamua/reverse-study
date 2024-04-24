package com.demo.taxi.executor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class SignUtils {

  public static String doSign(String preSign) {
    var bArr = preSign.getBytes(StandardCharsets.UTF_8);
    if (bArr == null) {
      return null;
    }
//    return SecurityLib.sign(context, bArr);
    return null;
  }

  public static String prepareSign(String url, byte[] bodyBytes) {
    try {
      String query = getQuery(url);
      byte[] body = getBody(bodyBytes);
      Map<String, String> queryStringToMap = queryStringToMap(query);
      String bytesToHex = bytesToHex(body);
      if (StringUtils.isNoneBlank(bytesToHex)) {
        queryStringToMap.put(bytesToHex, "");
      }
      return signMapToString(queryStringToMap);
    } catch (Exception unused) {
      return null;
    }
  }

  public static String getQuery(String str) {
    int i;
    try {
      int indexOf = str.indexOf("?");
      if (indexOf > 0 && (i = indexOf + 1) < str.length()) {
        return str.substring(i);
      }
      return null;
    } catch (Exception _) {
      return null;
    }
  }


  public static Map<String, String> queryStringToMap(String str) {
    String[] split;
    HashMap<String, String> hashMap = new HashMap<>();
    try {
      for (String str2 : str.split("&")) {
        if (StringUtils.isEmpty(str2)) {
          String[] split2 = str2.split("=", 2);
          if (split2.length == 2) {
            hashMap.put(URLDecoder.decode(split2[0], StandardCharsets.UTF_8)
                + URLDecoder.decode(split2[1], StandardCharsets.UTF_8), "");
          } else {
            hashMap.put(URLDecoder.decode(split2[0], StandardCharsets.UTF_8), "");
          }
        }
      }
    } catch (Exception _) {
    }
    return hashMap;
  }

  public static byte[] getBody(byte[] bArr) {
    if (bArr == null) {
      return null;
    }
    if (bArr.length <= 4096) {
      return (byte[]) bArr.clone();
    }
    byte[] bArr2 = new byte[4096];
    System.arraycopy(bArr, 0, bArr2, 0, 4096);
    return bArr2;
  }

  public static String bytesToHex(byte[] bArr) {
    if (bArr == null || bArr.length == 0) {
      return null;
    }
    char[] charArray = "0123456789abcdef".toCharArray();
    char[] cArr = new char[bArr.length * 2];
    for (int i = 0; i < bArr.length; i++) {
      int i2 = bArr[i] & 255;
      int i3 = i * 2;
      cArr[i3] = charArray[i2 >>> 4];
      cArr[i3 + 1] = charArray[i2 & 15];
    }
    return new String(cArr);
  }

  public static String signMapToString(Map<String, String> map) {
    StringBuilder sb = new StringBuilder();
    try {
      ArrayList<String> arrayList = new ArrayList<>(map.keySet());
      arrayList.sort(Collections.reverseOrder());
      for (String s : arrayList) {
        if (!s.startsWith("__x_") && !"wsgsig".equalsIgnoreCase(s)) {
          sb.append(s);
          sb.append(map.get(s));
        }
      }
    } catch (Exception _) {
    }
    return sb.toString();
  }
}
