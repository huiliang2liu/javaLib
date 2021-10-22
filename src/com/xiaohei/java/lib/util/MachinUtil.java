package com.xiaohei.java.lib.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

public class MachinUtil {
    public static final int HTTP = 1;
    public static final int HTTPS = HTTP << 1;
    public static final int FTP = HTTPS << 1;

    public static String getIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getDisplayName().equals("eth0") || intf.getDisplayName().equals("wlan0")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getWlan() {
        Map<String, String> map = macMap();
        if (map.containsKey("wlan0"))
            return map.get("wlan0");
        return "";
    }

    public static String getEth() {
        Map<String, String> map = macMap();
        if (map.containsKey("eth0"))
            return map.get("eth0");
        return "";
    }

    public static Map<String, String> macMap() {
        Map<String, String> map = new HashMap<>();
        try {
            List<NetworkInterface> all =
                    Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                byte macBytes[] = nif.getHardwareAddress();
                if (macBytes == null)
                    continue;
                StringBuilder res1 = new StringBuilder();
                for (Byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1 != null) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                map.put(nif.getName(), res1.toString());
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return map;
    }


    public static String uuid(String context) {
        return UUID.nameUUIDFromBytes(context.getBytes()).toString();
    }

    public static String uuidMac() {
        Map<String, String> map = macMap();
        StringBuilder sb = new StringBuilder();
        for (String value : map.values())
            sb.append(value);
        return uuid(sb.toString());
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String getUserAgent() {
        return System.getProperty("http.agent", "okhttp/3.0");
    }

    public static boolean isProxy() {
        String proxyAddress;
        int proxyPort;
        proxyAddress = System.getProperty("http.proxyHost");
        String portStr = System.getProperty("http.proxyPort");
        proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        return proxyAddress != null && !proxyAddress.isEmpty() && proxyPort != -1;
    }

    public static void noHttpProxy() {
        setHttpProxy("", "-1");
    }

    public static void setHttpProxy(String host, String port) {
        setHttpProxy(host, port, null);
    }

    public static void setHttpProxy(String host, String port, String extHosts) {
        setProxy(host, port, extHosts, HTTP);
    }

    public static void noHttpsProxy() {
        setHttpsProxy("", "-1");
    }

    public static void setHttpsProxy(String host, String port) {
        setHttpsProxy(host, port, null);
    }

    public static void setHttpsProxy(String host, String port, String extHosts) {
        setProxy(host, port, extHosts, HTTPS);
    }

    public static void noFtpProxy() {
        setFtpProxy("", "-1");
    }

    public static void setFtpProxy(String host, String port) {
        setFtpProxy(host, port, null);
    }

    public static void setFtpProxy(String host, String port, String extHosts) {
        setProxy(host, port, extHosts, FTP);
    }


    public static void noProxy() {
        setProxy("", "-1");
    }

    public static void setProxy(String host, String port) {
        setProxy(host, port, null);
    }

    public static void setProxy(String host, String port, String extHosts) {
        setProxy(host, port, extHosts, HTTP | HTTPS | FTP);
    }

    public static void setProxy(String host, String port, String extHosts, int type) {
        int hport = -1;
        try {
            hport = Integer.valueOf(port);
        } catch (Exception e) {
        }
        if (host == null || host.isEmpty() || hport == -1) {
            if ((type & HTTP) == HTTP) {
                System.getProperties().put("http.proxyHost", "");
                System.getProperties().put("http.proxyPort", "-1");
                System.getProperties().put("http.nonProxyHosts", "");
            }
            if ((type & HTTPS) == HTTPS) {
                System.getProperties().put("https.proxyHost", "");
                System.getProperties().put("https.proxyPort", "-1");
            }
            if ((type & FTP) == FTP) {
                System.getProperties().put("ftp.proxyHost", "");
                System.getProperties().put("ftp.proxyPort", "-1");
                System.getProperties().put("ftp.nonProxyHosts", "");
            }
        } else {
            System.getProperties().put("proxySet", "true");
            if ((type & HTTP) == HTTP) {
                System.getProperties().put("http.proxyHost", host);
                System.getProperties().put("http.proxyPort", port);
                if (extHosts != null && !extHosts.isEmpty()) {
                    System.getProperties().put("http.nonProxyHosts", extHosts);
                }
            }
            if ((type & HTTPS) == HTTPS) {
                System.getProperties().put("https.proxyHost", host);
                System.getProperties().put("https.proxyPort", port);
            }
            if ((type & FTP) == FTP) {
                System.getProperties().put("ftp.proxyHost", host);
                System.getProperties().put("ftp.proxyPort", port);
                if (extHosts != null && !extHosts.isEmpty()) {
                    System.getProperties().put("ftp.nonProxyHosts", extHosts);
                }
            }
        }
    }
}
