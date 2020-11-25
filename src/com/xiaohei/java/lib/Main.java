package com.xiaohei.java.lib;

import com.xiaohei.java.lib.http.HttpManage;
import com.xiaohei.java.lib.http.request.FlowRequest;
import com.xiaohei.java.lib.http.request.Request;
import com.xiaohei.java.lib.http.response.Response;
import com.xiaohei.java.lib.http.util.Method;
import com.xiaohei.java.lib.json.JSONArray;
import com.xiaohei.java.lib.json.JSONException;
import com.xiaohei.java.lib.json.JSONObject;
import com.xiaohei.java.lib.paras.DOMParas;
import com.xiaohei.java.lib.paras.NodeTree;
import com.xiaohei.java.lib.rx.FilterFunction;
import com.xiaohei.java.lib.rx.MapFunction;
import com.xiaohei.java.lib.rx.Observable;
import com.xiaohei.java.lib.rx.SubscribeFunction;
import com.xiaohei.java.lib.thread.PoolManager;
import com.xiaohei.java.lib.time.Calendar;
import com.xiaohei.java.lib.util.MachinUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static class TestThreadLocal {
        private static final ThreadLocal<Integer> local = new ThreadLocal<>();

        public static int get() {
            Integer integer = local.get();
            if (integer != null)
                return integer;
            return -1;
        }

        public static void set(int index) {
//            BigInteger bigInteger=new BigInteger("正整数");
//            BigInteger bigInteger2=new BigInteger("shu");
//            bigInteger.add(bigInteger2);
        }
    }

    private static class TestThreadLocal2 {
        private static final ThreadLocal<String> local = new ThreadLocal<>();

        public static String get() {
            return local.get();
        }

        public static void set(String index) {
            local.set(index);
        }
    }

    public static class Singleton {
        public static Singleton singleton;

        //方法1
        static {
            singleton = new Singleton();
        }

        //方法2
        public synchronized static Singleton getSingleton() {
            if (singleton == null)
                singleton = new Singleton();
            return singleton;
        }

        //方法3
        public static Singleton getInstance() {
            if (singleton == null) {
                synchronized (Singleton.class) {
                    if (singleton == null)
                        singleton = new Singleton();
                }
            }
            return singleton;
        }

        //方法4
        public static class Sing {
            public static Singleton singleton = new Singleton();
        }
    }

    private static String[] gan_info = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛",
            "壬", "癸"};
    private static String[] zhi_info = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未",
            "申", "酉", "戌", "亥"};

    static ServerSocket serverSocket;
    static int port;

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        PoolManager.longTime(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(0);
                    port = serverSocket.getLocalPort();
                    countDownLatch.countDown();
                    while (true) {
                        Socket socket = serverSocket.accept();
                        System.out.println("0.0.0.0收到请求");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        CountDownLatch countDownLatch1 = new CountDownLatch(1);
        PoolManager.longTime(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
//                    ServerSocket serverSocket = new ServerSocket();
//                    serverSocket.bind(new InetSocketAddress("127.0.0.1", port));
                    countDownLatch1.countDown();
//                    while (true) {
//                        Socket socket = serverSocket.accept();
//                        System.out.println("127.0.0.1收到请求");
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            countDownLatch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始发送请求");
        for (int i = 0; i < 10; i++) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("10.230.106.26", port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void goTest() {
        test:
        for (int i = 0; i < 10; i++) {
            System.out.println("i=" + i);
            for (int j = 0; j < 10; j++) {
                if (j == i)
                    continue test;
                System.out.println("j=" + j);
            }
        }
    }

    private static void facebook() {
        HttpManage manage = new HttpManage();
        Map<String, String> head = new HashMap<>();
        head.put("Accept", "application/json");
        String appId = "378499546156658";
        String placementId = "378499546156658_408632569810022";
        int test = 0;
        String flow = "{\"id\":\"706ae768998041f1a24e\"," +
                "\"imp\":[{\"id\":\"FB Ad Impression\",\"tagid\":\"%s\"," +
                "\"instl\":0,\"video\":{\"h\":0,\"w\":0,\"linearity\":2,\"ext\":{\"videotype\":\"rewarded\"}}}]," +
                "\"app\":{\"publisher\":{\"id\":\"%s\"}}," +
                "\"device\":{\"lmt\":1},\"regs\":{\"coppa\":0},\"at\":1,\"tmax\":1000,\"test\":%d," +
                "\"ext\":{\"platformid\":\"%s\",\"bidding_kit_version\":\"0.5.1\"," +
                "\"bidding_kit_source\":\"auction\",\"limited_data_use\":0," +
                "\"id\":\"AzM4gjM1YDNxUDM2EzX4UjN2UTM2QTN5kDN4czMfFjV\"," +
                "\"timestamp\":1605146528830},\"user\":{\"buyeruid\":\"eJx1VNtyozgQ\\/RXK+zJTZWPEncmTuNimgoEFnExq2FIJkBMq2LgAJzuZmn\\/fFthJ9mF4QTqnW91qne5fMxzH9s4P3Nm3GZrNZ75LHOxsPJKlZJsCuOCoA1aeS3yXIz\\/+AeQOBzFO8GjyK5\\/VPWGHcz77ls\\/2tOlZPpvnM3p6Jn39xkZYVSzd0M2RGOoDe2uPjLT7fc+GkZdNU+LfxfNE+oF2A6tIx2jfHkebAO9CyC0hq2gXugTHvoxGewhfseL8+EiLZgo3dOcpiREnL7Q5T0SY5\\/kSj1RxrpuKDD9P\\/2d+w\\/V2oZ89wNXGy8AeO46Xpr7tB4ATL8R24Lmf+TgO8dYDxK6rqj4+3tZDSg+nhpMbnBLvexQH+MFLwITnxn1WMWw0VdeQrBVVpRVaqVlMlitZMvaGWkgm3VMwTHnoKCSZP0ZAuqQh7mWJkmwCv8W3HO\\/poT8fH8fnShJ\\/DMW93Vt40hhPufP3U+bqXJsbc4TmSJ8jWJj8SV0vTCcLWdRljbs6ieeFZOP5600GuGEYlwPvvISnxPMXERKlD+N73802gKvoIqc02iUOzy+MyBr0Mh4AW3qsurauYB+l\\/Dh+M2ncwQq\\/kzjEQbSeVFY+0e4Rijs+1\\/Ty9IXWDX91cmCHtvs5ac2yJKRKKtInubUDbT7zhqnIsoIsZeILOgzsQiFJEicN7jvGSH+iJbsQsmFqiqLJlj5pxMUZJnESjcoI1ySKMygJz\\/54bhouohSkitdeyGu3bd\\/qpqH5UhMl4UtQH8\\/\\/3giXawpIuhHS7WJtGYok2FyW+fLvGGERWZLB6yuDwevLVwGfQFL3rAB5wUmKISq68OV2k22DudDUz0xYs\\/K5\\/Srcsa6voWuWKkRznrr2wPKlqYuSqMqqJCJTE7ZtUTdMSEFjXf1+2I+VjUNog3NVs2PJQja8tt3zqu0uqd6s7JTzH7u7fAnpg5udL8v2IA7n9nR+pUfxCYJWbM+OPeM0txO5oc1X8L9LeTHQBAZOvnx7Ik5I\\/trQY8\\/1mERR5l3n0jZyvQDW1yoBZMMMCLiw\\/hSVqyeF5uMqtZiJNMOo9pWlVDIqJK2QFVbolWkphb5HjBqFVJk34BNEDh7P\\/ZQPwKGX3UfJLckeYu99WK4wLKVSrpBO1QUrEVuoVFYWZlnJCySVFNEK0YJxa5xliW\\/vuEhgkk7NCXPjqv2xif6gKeLAwMuShw9t+WGa4SC4Nvmf\\/MAo8z68bN91QZNZdAud6n3PEnyd36eLyBVRlnTQhyka5iTz6\\/QZU64KuVT0SlpQVpgLFQq5sIpCWVg61bS9qZcqLWa\\/\\/wOX2crS\"}}";
        flow = String.format(flow, placementId, appId, test, appId);
        FlowRequest request = new FlowRequest(flow);
//        request.setPath("http://fruit-console-sdk-1581845208.us-east-1.elb.amazonaws.com/sdk/settings/waterfall");
        request.setPath("https://an.facebook.com/placementbid.ortb");
        request.addHead(head);
        request.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
//        request
//        request.addParams(params);
        request.setMethod(Method.POST);
        Response response = manage.response(request);
        System.out.println(response.getString());
        System.out.println(response.getCode());
    }

    private static String getPayload() {
        JSONObject payload = new JSONObject();

        try {
            payload.put("id", "84274a89d4644482b3be");
            payload.put("imp", getImpFieldFromFormat());
            payload.put("app", (new JSONObject()).put("publisher", (new JSONObject()).put("id", "378499546156658")));
            payload.put("device", (new JSONObject()).put("lmt", 0));
            payload.put("regs", (new JSONObject()).put("coppa", 0));
            payload.put("at", 1);
            payload.put("tmax", 1000);
            payload.put("test", 1);
            payload.put("ext", (new JSONObject()).put("platformid", "378499546156658").put("bidding_kit_version", "0.5.0").put("bidding_kit_source", "auction").putOpt("id", "QzM5UDO2gTO5QDM2EzX4UjN2UTM2QTN5kDN4czMfFjV").putOpt("timestamp", 1604998685934l));
            payload.put("user", (new JSONObject()).put("buyeruid", "eJx1VFtvm0oQ\\/ivI56WVDN5d7s3T2sYxCsYcwImqcoQWWCcoGCwuSZsq\\/\\/3MgpMmD+WF3fnmPt\\/O7xkNguXB9dazbzM8m89uqRfQkO4iuP9OZmWX8tOQzL4lsyOrOp7M5smMnR\\/Trnzho1jVMbEwGoG+PPGXpuZpczx2vB9xYllIfBfLc9r1rO15kbacdU096nj04K+2Tphu9gd\\/ndLAJXjUh\\/AFz4b7e5ZVU7i+HaYkRnn6xKphAvwkSRZ0hLKhrIq0\\/3X+jLxCeQffjb9DaWMxcKerlRNF7tL1QJ46Pl16zvojHgQ+3TkgWZZFUdb3N2UfsdO5GsFNAIChosy0Mq2w7QxltmYeMTGRkTGeGUi3RVMjEWPvp7E7usIG0mzbMkxTUYkN+I7eCHnHTt1Q34NgRcPQdUKQCev1TbqiAZ2SBNkPda7N9bk5x3iOjTk2\\/wOtteNHE0wUg+jCbhU6jp9uHfd6G4PcNM2Lt1snFPmATFcMRWQYhHvRiGno57bJedelNTtNHcybk9IPzXl4ZrXy0Jx4wY+8vrABZsSKLr0Ygf7YOwDyquR1n3bD+dy0vUDE8F7\\/ZHbnruMthNSwSMFdp9H+EK5EJ5xdAKWMycKN1UXblAXc95FIXbQQjTc40XeQ+tTbX08l9E3PqvTET037ayzBtFRCVGyrxph0C3hfNnX6c0RlS0HYNLDhyPqI5w+svYdxj+jERfbEykrw8KNbTbeQiYmmWZ\\/dvkzcVxD4NCzLkbXP+GQtY0U1NU3TtTeFjPU9f\\/ONlenVHFvO0+7M8mkaGBHLVlXDJLZxYXUEb4deO74Y8655KauKJQtdQdIXr6yHn1fSpUsSRldStJOvbVNF0lK8k2Txb4Cpgm2oAwISUHh++irRM3D8jmfAd\\/CkAlMN6cvNNt55c6kqH7l0zfPH5qt0y9sOCkoWGkRbPbRAjmRhGQpSNKIhBVu6tGuysuJSxI6sLd+d\\/dgsqQ\\/vciiAJTn3ef\\/ctI+bpr2kerVZRgL\\/c7tNFpA+mC2Txd8YKWChpwjFpTjB\\/zYSzQCiw9lbJYuXh3Tlp\\/9sWd2JlxPu97Hztv92+7XjwfmtSSBawk7yBCv\\/FlRwL9pSwUbd5hbWTbM4FrZaEJwhPSMqbIICRpYZR8yZmaHCugIbb7+io98P+YDYd+K7fXiTxt8D55KUu95QOKKcFNhgmsxzzGWNEVW28oLIGOUMswKzjAttGsehuzzEYuO462mHwB57ezmKqMn1o5h63vuKuSyoUT1nGTOIqckYIwhzRLps21iTbaIeC2KSQtXQ7PV\\/sQ3LZw=="));
        } catch (JSONException var5) {
//            BkLog.e("FacebookBidderPayloadBuilder", "Creating Facebook Bidder Payload failed", var5);
        }

        String payloadString = payload.toString();
//        BkLog.d("FacebookBidderPayloadBuilder", "Bid request for Facebook: " + payloadString);
        return payloadString;
    }

    private static JSONArray getImpFieldFromFormat() throws JSONException {
        return (new JSONArray()).put((new JSONObject()).put("id", "FB Ad Impression").put("tagid", "378499546156658_408632569810022").put("instl", 0).put("video", new JSONObject().put("h", 0).put("w", 0).put("linearity", 2).put("ext", new JSONObject().put("videotype", "rewarded"))));
    }

    public static String hmacSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("utf-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("utf-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toLowerCase();

    }


//    script
//    脚本Tag一般只有一个，是flv的第一个Tag，用于存放flv的信息，比如duration、audiodatarate、creator、width等。
//    首先介绍下脚本的数据类型。所有数据都是以数据类型+（数据长度）+数据的格式出现的，数据类型占1byte，数据长度看数据类型是否存在，后面才是数据。
//    一般来说，该Tag Data结构包含两个AMF包。AMF（Action Message Format）是Adobe设计的一种通用数据封装格式，在Adobe的很多产品中应用，简单来说，AMF将不同类型的数据用统一的格式来描述。第一个AMF包封装字符串类型数据，用来装入一个“onMetaData”标志，这个标志与Adobe的一些API调用有，在此不细述。第二个AMF包封装一个数组类型，这个数组中包含了音视频信息项的名称和值。具体说明如下，大家可以参照图片上的数据进行理解。

    //    值	类型	说明
//0	Number type	8 Bypte Double
//1	Boolean type	1 Bypte bool
//2	String type	后面2个字节为长度
//3	Object type
//4	MovieClip type
//5	Null type
//6	Undefined type
//7	Reference type
//8	ECMA array type	数组,类似Map
//10	Strict array type
//11	Date type
//12	Long string type	后面4个字节为长度
    public static void readScript(InputStream inputStream) {
        System.out.print("readScript:\t");
        readLen(inputStream);//4
        try {
            int type = inputStream.read();//1
            int len = ((inputStream.read() & 255) << 16) | ((inputStream.read() & 255) << 8) | (inputStream.read() & 255);//3
            System.out.print(String.format("%d\t", len));//tag data size 3个字节。表示tag data的长度。从streamd id 后算起
            byte buff[] = new byte[7];
            inputStream.read(buff);//7
            while (len > 0) {
                buff = new byte[len];
                len -= inputStream.read(buff);
            }

            System.out.println(readLen(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int readVideo(InputStream inputStream) {
        System.out.print("readVideo:\t");
        try {
            int type = inputStream.read();
            int len = ((inputStream.read() & 255) << 16) | ((inputStream.read() & 255) << 8) | (inputStream.read() & 255);
            System.out.print(String.format("%d\t", len));//tag data size 3个字节。表示tag data的长度。从streamd id 后算起
            byte[] buff = new byte[7];
            inputStream.read(buff);
            buff = new byte[len];
            inputStream.read(buff);
            type = (buff[0] & 255) >> 4;
            if (type == 1)
                System.out.print("keyframe (for AVC, a seekable frame) 关键帧\t");
            else if (type == 2)
                System.out.print("inter frame (for AVC, a non-seekable frame)\t");
            else if (type == 3)
                System.out.print("disposable inter frame (H.263 only)\t");
            else if (type == 4)
                System.out.print("generated keyframe (reserved for server use only)\t");
            else if (type == 5)
                System.out.print("video info/command frame\t");
            else
                System.out.print("其他\t");
            type = (buff[0] & 255) & 15;
            if (type == 1)
                System.out.print("JPEG (currently unused)\t");
            else if (type == 2)
                System.out.print("Sorenson H.263\t");
            else if (type == 3)
                System.out.print("Screen video\t");
            else if (type == 4)
                System.out.print("On2 VP6\t");
            else if (type == 5)
                System.out.print("On2 VP6 with alpha channel\t");
            else if (type == 6)
                System.out.print("Screen video version 2\t");
            else if (type == 7)
                System.out.print("AVC(H.264)\t");
            else
                System.out.print("其他\t");
            if (type == 7) {
                type = buff[1] & 255;
                if (type == 0) {
                    System.out.print("AVCDecoderConfigurationRecord(AVC sequence header)\t");
                } else if (type == 1) {
                    System.out.print("AVC NALU\t");
                } else if (type == 2) {
                    System.out.print("AVC end of sequence (lower level NALU sequence ender is not required or supported)\t");
                } else {
                    System.out.print("其他\t");
                }
                System.out.print(String.format("%d\t", (buff[2] & 255 << 16) | (buff[2] & 255 << 8) | (buff[2] & 255)));
            }
//            System.out.println(buff[0]);
            len = readLen(inputStream);
            System.out.print(String.format("%d\n", len));// 固定4个字节，表示前一个tag的size
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void readAudio(InputStream inputStream) {
        System.out.print("readAudio:\t");
        try {
            int type = inputStream.read();
            int len = ((inputStream.read() & 255) << 16) | ((inputStream.read() & 255) << 8) | (inputStream.read() & 255);
            System.out.print(String.format("%d\t", len));//tag data size 3个字节。表示tag data的长度。从streamd id 后算起
            byte[] buff = new byte[7];
            inputStream.read(buff);
            buff = new byte[len];
            inputStream.read(buff);
            type = (buff[0] & 255) >> 4;
            if (type == 0) {
                System.out.print("Linear PCM, platform endian\t");
            } else if (type == 1) {
                System.out.print("ADPCM\t");
            } else if (type == 1) {
                System.out.print("Linear PCM, platform endian\t");
            } else if (type == 2) {
                System.out.print("MP3\t");
            } else if (type == 3) {
                System.out.print("Linear PCM, little endian\t");
            } else if (type == 4) {
                System.out.print("Nellymoser 16-kHz mono\t");
            } else if (type == 5) {
                System.out.print("Nellymoser 8-kHz mono\t");
            } else if (type == 6) {
                System.out.print("Nellymoser\t");
            } else if (type == 7) {
                System.out.print("G.711 A-law logarithmic PCM\t");
            } else if (type == 8) {
                System.out.print("G.711 mu-law logarithmic PCM\t");
            } else if (type == 9) {
                System.out.print("reserved\t");
            } else if (type == 10) {
                System.out.print("AAC\t");
            } else if (type == 11) {
                System.out.print("Speex\t");
            } else if (type == 14) {
                System.out.print("MP3 8-Khz\t");
            } else if (type == 15) {
                System.out.print("Device-specific sound");
            } else {
                System.out.print(String.format("其他%d\t", type));
            }
            type = ((buff[0] & 255) >> 2) & 3;
            if (type == 0) {
                System.out.print("5.5-kHz\t");
            } else if (type == 1) {
                System.out.print("11-kHz\t");
            } else if (type == 2) {
                System.out.print("22-kHz\t");
            } else if (type == 3) {
                System.out.print("44-kHz\t");
            }
            type = ((buff[0] & 255) >> 1) & 1;
            if (type == 0) {
                System.out.print("snd8Bit\t");
            } else {
                System.out.print("snd16Bit\t");
            }
            type = (buff[0] & 255) & 1;
            if (type == 0) {
                System.out.print("sndMono\t");
            } else {
                System.out.print("sndStereo\t");
            }
            System.out.print(String.format("%d\n", readLen(inputStream)));// 固定4个字节，表示前一个tag的size
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean readBody(InputStream inputStream) {
        System.out.print("readBody:\t");
        try {
            int type = inputStream.read();//type 1个字节。8为Audio,9为Video,18为scripts
            if (type == -1)
                return true;
            System.out.print(type);
            System.out.print(String.format("%s\t", type == 8 ? "音频" : type == 9 ? "视频" : "未发现"));
            int len = ((inputStream.read() & 255) << 16) | ((inputStream.read() & 255) << 8) | (inputStream.read() & 255);
            System.out.print(String.format("长度:%d\t", len));//tag data size 3个字节。表示tag data的长度。从streamd id 后算起
            System.out.print(String.format("时间:%d\t", ((inputStream.read() & 255) << 16) | ((inputStream.read() & 255) << 8) | (inputStream.read() & 255)));//Timestreamp 3个字节。当前帧时戳，单位是毫秒。相对于FLV文件的第一个TAG时戳。第一个tag的时戳总是0。——不是时戳增量，rtmp中是时戳增量
            System.out.print(String.format("补位时间:%d\t", inputStream.read()));//TimestampExtended 1个字节。时间戳扩展字段，如果时戳大于0xFFFFFF，将会使用这个字节。这个字节是时戳的高8位，上面的三个字节是低24位
            System.out.print(String.format("id:%d\t", ((inputStream.read() & 255) << 16) | ((inputStream.read() & 255) << 8) | (inputStream.read() & 255)));//stream id 3个字节。总是0
            if (type == 9) {
                int frame = inputStream.read() & 255;
                len--;
                type = frame >> 4;
                if (type == 1)
                    System.out.print("keyframe (for AVC, a seekable frame) 关键帧\t");
                else if (type == 2)
                    System.out.print("inter frame (for AVC, a non-seekable frame)\t");
                else if (type == 3)
                    System.out.print("disposable inter frame (H.263 only)\t");
                else if (type == 4)
                    System.out.print("generated keyframe (reserved for server use only)\t");
                else if (type == 5)
                    System.out.print("video info/command frame\t");
                else
                    System.out.print("其他\t");
                type = frame & 15;
                if (type == 1)
                    System.out.print("JPEG (currently unused)\t");
                else if (type == 2)
                    System.out.print("Sorenson H.263\t");
                else if (type == 3)
                    System.out.print("Screen video\t");
                else if (type == 4)
                    System.out.print("On2 VP6\t");
                else if (type == 5)
                    System.out.print("On2 VP6 with alpha channel\t");
                else if (type == 6)
                    System.out.print("Screen video version 2\t");
                else if (type == 7)
                    System.out.print("AVC(H.264)\t");
                else
                    System.out.print("其他\t");
                if (type == 7) {
                    type = inputStream.read();
                    len--;
                    if (type == 0) {
                        System.out.print("AVCDecoderConfigurationRecord(AVC sequence header)\t");
                    } else if (type == 1) {
                        System.out.print("AVC NALU\t");
                    } else if (type == 2) {
                        System.out.print("AVC end of sequence (lower level NALU sequence ender is not required or supported)\t");
                    } else {
                        System.out.print("其他\t");
                    }
                    System.out.print(String.format("%d\t", (inputStream.read() & 255 << 16) | (inputStream.read() & 255 << 8) | inputStream.read() & 255));
                    len -= 3;
                }
            } else if (type == 8) {
                int frame = inputStream.read() & 255;
                type = frame >> 4;
                if (type == 0) {
                    System.out.print("Linear PCM, platform endian\t");
                } else if (type == 1) {
                    System.out.print("ADPCM\t");
                } else if (type == 1) {
                    System.out.print("Linear PCM, platform endian\t");
                } else if (type == 2) {
                    System.out.print("MP3\t");
                } else if (type == 3) {
                    System.out.print("Linear PCM, little endian\t");
                } else if (type == 4) {
                    System.out.print("Nellymoser 16-kHz mono\t");
                } else if (type == 5) {
                    System.out.print("Nellymoser 8-kHz mono\t");
                } else if (type == 6) {
                    System.out.print("Nellymoser\t");
                } else if (type == 7) {
                    System.out.print("G.711 A-law logarithmic PCM\t");
                } else if (type == 8) {
                    System.out.print("G.711 mu-law logarithmic PCM\t");
                } else if (type == 9) {
                    System.out.print("reserved\t");
                } else if (type == 10) {
                    System.out.print("AAC\t");
                } else if (type == 11) {
                    System.out.print("Speex\t");
                } else if (type == 14) {
                    System.out.print("MP3 8-Khz\t");
                } else if (type == 15) {
                    System.out.print("Device-specific sound");
                } else {
                    System.out.print(String.format("其他%d\t", type));
                }
                type = (frame >> 2) & 3;
                if (type == 0) {
                    System.out.print("5.5-kHz\t");
                } else if (type == 1) {
                    System.out.print("11-kHz\t");
                } else if (type == 2) {
                    System.out.print("22-kHz\t");
                } else if (type == 3) {
                    System.out.print("44-kHz\t");
                }
                type = (frame >> 1) & 1;
                if (type == 0) {
                    System.out.print("snd8Bit\t");
                } else {
                    System.out.print("snd16Bit\t");
                }
                type = frame & 1;
                if (type == 0) {
                    System.out.print("sndMono\t");
                } else {
                    System.out.print("sndStereo\t");
                }
                len--;
            }
            byte[] buff;
            while (true) {
                buff = new byte[len];
                len -= inputStream.read(buff);
                if (len <= 0)
                    break;
            }
            System.out.print(String.format("%d\n\n", readLen(inputStream)));// 固定4个字节，表示前一个tag的size
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static int readLen(InputStream is) {
        try {
            int i = (is.read() & 255) << 24;
            i |= (is.read() & 255) << 16;
            i |= (is.read() & 255) << 8;
            i |= (is.read() & 255);
            return i;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
