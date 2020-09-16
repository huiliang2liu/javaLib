package com.xiaohei.java.lib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() <= 0;
    }

    public static boolean isBlank(CharSequence cs) {
        if (isEmpty(cs))
            return true;
        char c = ' ';
        int len = cs.length();
        for (int i = 0; i < len; i++) {
            if (c != cs.charAt(i))
                return false;
        }
        return true;
    }

    public static String leftPad(String target, int len, String fill) {
        if (isEmpty(target)) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len; i++)
                sb.append(fill);
            return sb.toString();
        }
        StringBuffer sb = new StringBuffer();
        for (int i = target.length(); i < len; i++) {
            sb.append(fill);
        }
        sb.append(target);
        return sb.toString();
    }

    public static String rightPad(String target, int len, String fill) {
        StringBuffer sb;
        int sLen;
        if (isEmpty(target)) {
            sb = new StringBuffer();
            sLen = 0;
        } else {
            sb = new StringBuffer(target);
            sLen = target.length();
        }
        for (int i = sLen; i < len; i++)
            sb.append(fill);
        return sb.toString();

    }

    public static String join(String[] target, String separator) {
        if (target == null || target.length <= 0)
            return "";
        int len = target.length - 1;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++)
            sb.append(target[i]).append(separator);
        sb.append(target[len]);
        return sb.toString();
    }

    public static String join(Collection<String> target, String separator) {
        if (target == null || target.size() <= 0)
            return "";
        int len = target.size() - 1;
        StringBuffer sb = new StringBuffer();
        for (String s : target)
            sb.append(s).append(separator);
        return sb.substring(0, sb.length() - 1);
    }

    public static String[] split(String target, String separator) {
        if (isEmpty(target))
            return null;
        if (isEmpty(separator))
            return new String[]{target};
        String[] sps = target.split(separator);
        return sps;
    }

    public static String[] splitNoEmpty(String target, String separator) {
        if (isEmpty(target))
            return null;
        if (isEmpty(separator))
            return new String[]{target};
        String[] sps = target.split(separator);
        List<String> list = new ArrayList<>();
        for (String s : sps) {
            if (isEmpty(s))
                continue;
            list.add(s);
        }
        return list.toArray(new String[list.size()]);
    }

    public static boolean equals(String string1, String string2, boolean caseSensitive) {
        if (string1 == null && string2 == null)
            return true;
        if (string1 == null || string2 == null)
            return false;
        if (string1.isEmpty() && string2.isEmpty())
            return true;
        if (string1.isEmpty() || string2.isEmpty())
            return false;
        return caseSensitive ? string1.equals(string2) : string1.equalsIgnoreCase(string2);
    }

    public static int comparator(String s1, String s2) {
        int len1 = s1 == null ? -1 : s1.length();
        int len2 = s2 == null ? -1 : s2.length();
        if (len1 == -1 || len2 == -1 || len1 == 0 || len2 == 0) {
            if (len1 > len2)
                return 1;
            if (len1 == len2)
                return 0;
            return -1;
        }
        for (int i = 0; i < len1; i++) {
            if (i >= len2)
                return 1;
            char a1 = s1.charAt(i);
            char a2 = s2.charAt(i);
            if (a1 > a2)
                return 1;
            if (a1 < a2)
                return -1;
        }
        return 0;
    }

    public static int indexTraverse(String[] st, String s) {
        for (int i = 0; i < st.length; i++) {
            String s1 = st[i];
            if (comparator(s1, s) == 1)
                return i;
        }
        return st.length;
    }

    public static int indexTraverse(List<String> st, String s) {
        for (int i = 0; i < st.size() - 1; i++) {
            String s1 = st.get(i);
            if (comparator(s1, s) == 1)
                return i;
        }
        return st.size();
    }

    public static int indexDichotomy(String[] st, String s) {
        int star = 0;
        int end = st.length;
        while (end - star > 1) {
            int index = star + (end - star) >> 1;
            if (comparator(st[index], s) == 1)
                end = index;
            else
                star = index;
        }
        return star + 1;
    }

    public static int indexDichotomy(List<String> st, String s) {
        int star = 0;
        int end = st.size();
        while (end - star > 1) {
            int index = star + (end - star) >> 1;
            if (comparator(st.get(index), s) == 1)
                end = index;
            else
                star = index;
        }
        return star + 1;
    }

    public static int index618(String[] st, String s) {
        int star = 0;
        int end = st.length;
        while (end - star > 1) {
            int len = (int) ((end - star) * 0.618);
            int index0618 = star + len;
            if (comparator(st[index0618], s) == 1)
                end = index0618;
            else
                star = index0618;
        }
        return star + 1;
    }

    public static int index618(List<String> st, String s) {
        int star = 0;
        int end = st.size();
        while (end - star > 1) {
            int len = (int) ((end - star) * 0.618);
            int index0618 = star + len;
            if (comparator(st.get(index0618), s) == 1)
                end = index0618;
            else
                star = index0618;
        }
        return star + 1;
    }


    public static boolean isNumbers(char c) {
        if (c > 47 && c < 58)
            return true;
        return false;
    }

    public static boolean isNumbers(String s) {
        if (isEmpty(s))
            return false;
        char[] cs = s.toCharArray();
        for (char c : cs)
            if (!isNumbers(c))
                return false;
        return true;
    }

    public static boolean isLowercase(char c) {
        if (c > 96 && c < 123)
            return true;
        return false;
    }


    public static boolean isCapital(char c) {
        if (c > 64 && c < 91)
            return true;
        return false;
    }

    public static boolean isLetters(char c) {
        return isLowercase(c) || isCapital(c);
    }

    public static boolean isLetters(String string) {
        if (isEmpty(string))
            return false;
        char[] cs = string.toCharArray();
        for (char c : cs)
            if (!isLetters(c))
                return false;
        return true;
    }

    public static boolean isNumbersOrLetters(char c) {
        return isLetters(c) || isNumbers(c);
    }

    public static boolean isNumbersOrLetters(String string) {
        if (isEmpty(string))
            return false;
        char[] cs = string.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (!isNumbers(cs[i]) || isLetters(cs[i]))
                return false;
        }
        return true;
    }

    public static boolean isChinese(char c) {
        return (c > 19967 && c < 40865) ? true : false;
    }

    public static boolean isChinese(String string) {
        if (isEmpty(string))
            return false;
        char cs[] = string.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (!isChinese(cs[i]))
                return false;
        }
        return true;
    }

    public static String[] bubbleSort(String[] st) {
        if (st == null)
            return st;
        for (int i = 1; i < st.length; i++) {
            for (int j = 0; j < st.length - i; j++) {
                if (comparator(st[j], st[j + 1]) == 1) {
                    String sj = st[j];
                    st[j] = st[j + 1];
                    st[j + 1] = sj;
                }
            }
        }
        return st;
    }

    public static List<String> bubbleSort(List<String> st) {
        if (st == null)
            return st;
        for (int i = 1; i < st.size(); i++) {
            for (int j = 0; j < st.size() - i; j++) {
                if (comparator(st.get(j), st.get(j + 1)) == 1) {
                    String sj = st.get(j);
                    st.set(j, st.get(j + i));
                    st.set(j + 1, sj);
                }
            }
        }
        return st;
    }

    public static String[] directlySort(String[] st) {
        if (st == null)
            return null;
        int index;
        for (int i = 0; i < st.length; i++) {
            index = 0;
            for (int j = 0; j < st.length - i; j++) {
                if (comparator(st[j], st[index]) == 1)
                    index = j;
            }
            String s = st[st.length - i - 1];
            st[st.length - i - 1] = st[index];
            st[index] = s;
        }
        return st;
    }

    public static List<String> directlySort(List<String> st) {
        if (st == null)
            return null;
        int index;
        for (int i = 0; i < st.size(); i++) {
            index = 0;
            for (int j = 0; j < st.size() - i; j++) {
                if (comparator(st.get(j), st.get(index)) == 1)
                    index = j;
            }
            String s = st.get(st.size() - i - 1);
            st.set(st.size() - i - 1, st.get(index));
            st.set(index, s);
        }
        return st;
    }

    public static String[] insertSort(String[] st) {
        if (st == null || st.length < 2)
            return st;
        String st1[] = new String[st.length];
        for (int i = 0; i < st1.length; i++) {
            String s = st[i];
            int index = index618(st1, s);
            for (int j = i; j >= index; j--) {
                st1[j + 1] = st[j];
            }
            st1[index] = s;
        }
        return st1;
    }

    public static List<String> insertSort(List<String> st) {
        if (st == null || st.size() < 2)
            return st;
        List<String> st1 = new ArrayList<>(st.size());
        for (int i = 0; i < st1.size(); i++) {
            String s = st.get(i);
            if (i == 0)
                st1.add(s);
            else {
                if (comparator(s, st1.get(0)) == 1) {
                    st1.add(s);
                } else {
                    st1.add(0, s);
                }
            }
        }
        return st1;
    }

    public static Integer string2int(String string) {
        if (isEmpty(string))
            return null;
        if (string.startsWith("#"))
            return Integer.valueOf(string.substring(1), 16);
        if (string.startsWith("0x"))
            return Integer.valueOf(string.substring(2), 16);
        if (string.startsWith("0"))
            return Integer.valueOf(string.substring(1), 8);
        return Integer.valueOf(string, 10);
    }

    public static final int F = Integer.valueOf("f", 16);
    public static final int FF = Integer.valueOf("ff", 16);

    public static int string2int(String string, boolean is16) {
        return Integer.valueOf(string, is16 ? 16 : 10);
    }


    private static int rgb(int red, int green, int blue) {
        return 0xff000000 | (red << 16) | (green << 8) | blue;
    }

    private static int argb(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static Integer string2color(String string) {
        if (isEmpty(string))
            return null;
        boolean is16 = false;
        if (string.startsWith("#")) {
            is16 = true;
            string = string.substring(1);
        } else if (string.startsWith("0x")) {
            is16 = true;
            string = string.substring(2);
        }
        int len = string.length();
        if (len != 3 && len != 4 && len != 6 && len != 8)
            return null;
        switch (len) {
            case 3: {
                int r = string2int(string.substring(0, 1), is16);
                r = r * FF / F;
                int g = string2int(string.substring(1, 2), is16);
                g = g * FF / F;
                int b = string2int(string.substring(2, 3), is16);
                b = b * FF / F;
                return rgb(r, g, b);
            }
            case 4: {
                int a = string2int(string.substring(0, 1), is16);
                a = a * FF / F;
                int r = string2int(string.substring(1, 2), is16);
                r = r * FF / F;
                int g = string2int(string.substring(2, 3), is16);
                g = g * FF / F;
                int b = string2int(string.substring(3, 4), is16);
                b = b * FF / F;
                return argb(a, r, g, b);
            }
            case 6: {
                int r = string2int(string.substring(0, 2), is16);
                int g = string2int(string.substring(2, 4), is16);
                int b = string2int(string.substring(4, 6), is16);
                return rgb(r, g, b);
            }
            default: {
                int a = string2int(string.substring(0, 2), is16);
                int r = string2int(string.substring(2, 4), is16);
                int g = string2int(string.substring(4, 6), is16);
                int b = string2int(string.substring(6, 8), is16);
                return argb(a, r, g, b);
            }
        }
    }

}
