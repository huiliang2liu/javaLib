package com.xiaohei.java.lib;

import com.sun.source.tree.LineMap;
import com.xiaohei.java.lib.httpService.DefaultResponse;
import com.xiaohei.java.lib.httpService.Service;
import com.xiaohei.java.lib.io.StreamUtil;
import com.xiaohei.java.lib.reflect.FieldManager;
import com.xiaohei.java.lib.util.Monkey;
import jdk.nashorn.internal.codegen.CompilerConstants;
import sun.misc.LRUCache;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    public static void main(String[] args) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        System.out.println(simpleDateFormat.format(date));
    }

    private final StringBuffer TEST = new StringBuffer("======");
    public static int test = 1;

    public static void test() {
        try {
            test++;
            System.out.println(String.format("try:%d", test));
            return;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            test++;
            System.out.println(String.format("finally:%d", test));
        }
    }

    public static boolean isIp(String ip) {
        if (ip == null || ip.isEmpty())
            return false;
        String splits[] = ip.split("\\.");
        if (splits.length != 4)
            return false;
        for (String st : splits) {
            try {
                int a = Integer.valueOf(st);
                if (a < 0 || a > 255)
                    return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public static MaxString maxString(String st) {
        MaxString maxString = new MaxString();
        if (st != null && st.length() > 0) {
            char chars[] = st.toCharArray();
            StringBuffer sb = new StringBuffer();
            for (char ch : chars) {
                if (ch > 47 && ch < 58) {
                    sb.append(ch);
                } else {
                    int len = sb.length();
                    if (len > 0) {
                        if (len > maxString.len) {
                            maxString.len = len;
                            maxString.st = sb.toString();
                        }
                        sb.setLength(0);
                    }
                }
            }
            int len = sb.length();
            if (len > 0 && len > maxString.len) {
                maxString.len = len;
                maxString.st = sb.toString();
            }
        }
        return maxString;
    }

    public static class MaxString {
        int len = 0;
        String st = "";
    }

    public static String geshihua(double rmb) {
        StringBuffer sb = new StringBuffer("人民币");
        long yuan = (long) rmb;
        int yi = (int) (yuan / 10000000000l);
        if (yi > 0) {
            sb.append(num2string(yi)).append("亿");
            yuan %= 10000000000l;
        }
        int wan = (int) (yuan / 1000000);
        if (wan > 0) {
            sb.append(num2string(wan)).append("万");
            yuan %= 1000000;
        }
        int yu = (int) (yuan / 100);
        yuan %= 100;
        sb.append(num2string((int) yu)).append("元");
        if (yuan == 0) {
            sb.append("整");
        } else {
            int jiao = (int) (yuan / 10);
            yuan %= 10;
            if (jiao > 0)
                sb.append(num2string(jiao)).append("角");
            int fen = (int) (yuan);
            if (fen > 0)
                sb.append(num2string(fen)).append("分");
        }
        return sb.toString();
    }

    public static String num2string(int num) {
        StringBuffer sb = new StringBuffer();
        int qian = num / 1000;
        num %= 1000;
        int bai = num / 100;
        num %= 100;
        int shi = num / 10;
        int ge = num % 10;
        if (qian > 0) {
            sb.append(num2han(qian)).append("千");
            if (bai > 0) {
                sb.append(num2han(bai)).append("百");
                if (shi > 0) {
                    sb.append(num2han(shi)).append("拾");
                    if (ge > 0)
                        sb.append(num2han(ge));
                } else {
                    if (ge > 0)
                        sb.append(num2han(0)).append(num2han(ge));
                }
            } else {
                if (shi > 0) {
                    sb.append(num2han(0)).append(shi > 1 ? num2han(shi) : "").append("拾");
                    if (ge > 0)
                        sb.append(ge);
                } else {
                    if (ge > 0)
                        sb.append(num2han(0)).append(num2han(ge));
                }
            }
        } else {
            if (bai > 0) {
                sb.append(num2han(bai)).append("百");
                if (shi > 0) {
                    sb.append(num2han(shi)).append("拾");
                    if (ge > 0)
                        sb.append(num2han(ge));
                }
            } else {
                if (shi > 0) {
                    sb.append(shi > 1 ? num2han(shi) : "").append("拾");
                    if (ge > 0)
                        sb.append(num2han(ge));
                } else {
                    if (ge > 0)
                        sb.append(num2han(ge));
                }
            }
        }
        return sb.toString();
    }

    static String[] strings = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾"};

    public static String num2han(int num) {
        return strings[num];
    }

    public static String numAdd(String st) {
        if (st == null || st.length() <= 0)
            return null;
        StringBuffer sb = new StringBuffer();
        char chars[] = st.toCharArray();
        boolean add = false;
        for (char ca : chars) {
            if (ca > 47 && ca < 58) {
                if (add) {
                    sb.append(ca);
                } else {
                    add = true;
                    sb.append("*").append(ca);
                }
            } else {
                if (add) {
                    sb.append("*");
                    add = false;
                }
                sb.append(ca);
            }

        }
        return sb.toString();
    }

    public static int dengcha(int start, int cha, int num) {
        int end = start + cha * (num - 1);
        return (start + end) * num / 2;
    }

    public static int zishoushu(int n) {
        int size = 0;
        for (int i = 0; i < n; i++) {
            long cheng = i * i;
            String t = String.valueOf(i);
            String ch = String.valueOf(cheng);
            if (t.equals(ch.substring(ch.length() - t.length(), ch.length())))
                size++;
        }
        return size;
    }

    public static Integer[] sort(Integer[] nums, int sortTag) {
        Arrays.sort(nums, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int cha = o1 - o2;
                return sortTag == 0 ? cha : -cha;
            }
        });
        return nums;
    }

    public static List<Test1> test(String st) {
        List<Test1> list = new ArrayList<>();
        if (st != null && st.length() > 0) {
            char chars[] = st.toCharArray();
            for (char ca : chars) {
                Test1 test1 = new Test1(ca);
                int index = list.indexOf(test1);
                if (index >= 0)
                    list.get(index).num++;
                else
                    list.add(test1);
            }
        }
        list.sort(new Comparator<Test1>() {
            @Override
            public int compare(Test1 o1, Test1 o2) {
                return o2.num - o1.num;
            }
        });
        return list;
    }

    public static class Test1 {
        Test1(char ca) {
            this.ca = ca;
            num = 1;
        }

        char ca;
        int num;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Test1 test1 = (Test1) o;
            return ca == test1.ca;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ca);
        }
    }

    public static List<String> format(String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings == null || strings.length <= 0)
            return list;
        for (String st : strings) {
            int len = st.length();
            int index = 0;
            while (len > 0) {
                if (len > 8) {
                    list.add(format(st.substring(index, index + 8)));
                    index += 8;
                    len -= 8;
                } else {
                    list.add(format(st.substring(index, index + len)));
                    break;
                }
            }
        }
        return list;
    }

    public static String format(String st) {
        if (st.length() == 8)
            return st;
        int len = st.length();
        StringBuffer sb = new StringBuffer(st);
        for (int i = 0; i < 8 - len; i++)
            sb.append("0");
        return sb.toString();
    }


    public static Test statistical(float[] nums) {
        Test test = new Test();
        if (nums != null && nums.length > 0) {
            for (float num : nums) {
                if (num > 0)
                    test.average += num;
                else
                    test.negativeNumber += 1;
            }
            if (test.average > 0)
                test.average /= (nums.length - test.negativeNumber);
        }
        return test;
    }

    public static class Test {
        int negativeNumber = 0;
        float average = 0;
    }

    static String horse(String st) {
        if (st == null || st.isEmpty())
            return st;
        char chars[] = st.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = chars.length - 1; i > -1; i--) {
            sb.append(chars[i]);
        }
        return sb.toString();
    }

    public static int i16to120(String text) {
        if (text.startsWith("0x"))
            text = text.substring(2);
        text = text.toLowerCase();
        char cs[] = text.toCharArray();
        int j = 0;
        int sum = 0;
        for (int i = cs.length - 1; i > -1; i--) {
            char c = cs[i];
            sum += char2int(c) * Math.pow(16, j);
            j++;
        }
        return sum;
    }

    public static int char2int(char c) {
        if (c == '0')
            return 0;
        if (c == '1')
            return 1;
        if (c == '2')
            return 2;
        if (c == '3')
            return 3;
        if (c == '4')
            return 4;
        if (c == '5')
            return 5;
        if (c == '6')
            return 6;
        if (c == '7')
            return 7;
        if (c == '8')
            return 8;
        if (c == '9')
            return 9;
        if (c == 'a')
            return 10;
        if (c == 'b')
            return 11;
        if (c == 'c')
            return 12;
        if (c == 'd')
            return 13;
        if (c == 'e')
            return 14;
        if (c == 'f')
            return 15;
        return 0;
    }


    public static int p(int p) {
        if (p < 2)
            return 0;
        if (p == 2)
            return 1;
        int shang = p / 3;
        int modle = p % 3;
        return shang + p(shang + modle);
    }

    static double abs(double x) {
        return (x > 0 ? x : -x);
    }

    static double cubert(double y) {
        double x;
        for (x = 1.0; abs(x * x * x - y) > 1e-7; x = (2 * x + y / x / x) / 3) ;
        return x;
    }

    public static double pow(double num, double p) {
        if (p == 0)
            return 1;
        if (p < 0) {
            double po = pow(num, -p);
            return po == 0 ? po : 1.0 / po;
        }
        if (num < 0) {
            if (p < 1)
                return 0;
            int intP = (int) p;
            double po = num;
            for (int i = 1; i < intP; i++) {
                po *= num;
            }
            return po;
        }
        if (num < 1) {
            double po = pow(1.0 / num, p);
            return po == 0 ? po : 1.0 / po;
        }
        if (p < 1) {
            p = 1.0 / p;
            return 1;
        }
        int intP = (int) p;
        if (intP - p == 0) {
            double po = num;
            for (int i = 1; i < intP; i++) {
                po *= num;
            }
            return po;
        } else {
            double douP = p - intP;
            return pow(num, intP) * pow(num, douP);
        }
    }

    public static int LeastCommon(int a, int b) {
        if (a == 0 || b == 0)
            return 0;
        if (a > b && a % b == 0)
            return a;
        if (a < b && b % a == 0)
            return b;
        return a * b / common(a, b);
    }

    public static int common(int a, int b) {
        if (a == 0)
            return b;
        if (b == 0)
            return a;
        if (a == b)
            return a;
        if (a > b)
            return common(b, a % b);
        return common(a, b % a);
    }


    public static void readHead(InputStream inputStream) {
        byte[] buff = new byte[3];
        System.out.print("readHead:\t");
        try {
            inputStream.read(buff);
            System.out.print(String.format("%s\t", new String(buff)));
            System.out.print(String.format("%d\t", inputStream.read()));
            int i = inputStream.read();
            System.out.print(String.format("%s\t", (i & 1) == 1 ? "有视频" : "没有视频"));
            System.out.print(String.format("%s\t", (i & 4) == 4 ? "有音频" : "没有音频"));
            System.out.println(readLen(inputStream));
//            readBody(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
