package com.demo.taxi.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPUtils {

  public static String getIpAddress() throws SocketException {
    Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
    InetAddress ip;
    while (allNetInterfaces.hasMoreElements()) {
      NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
      //用于排除回送接口,非虚拟网卡,未在使用中的网络接口
      if (!netInterface.isLoopback() && !netInterface.isVirtual() && netInterface.isUp()) {
        //返回和网络接口绑定的所有IP地址
        Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
          ip = addresses.nextElement();
          if (ip instanceof Inet4Address) {
            return ip.getHostAddress();
          }
        }
      }

    }

    throw new RuntimeException("IP地址获取失败");
  }
}
