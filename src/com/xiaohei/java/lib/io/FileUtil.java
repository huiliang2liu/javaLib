package com.xiaohei.java.lib.io;


import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.zip.*;

public class FileUtil {
    /**
     * instruction:判断文件为空 2018-6-7 下午3:26:37
     *
     * @param file
     * @return
     */
    public static boolean isEmpty(File file) {
        if (file == null) {
            System.out.println("file is null");
            return true;
        }
        if (!file.exists()) {
            System.out.println("file is not exists");
            return true;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length <= 0) {
                System.out.println("file is directory,but listFiles is null");
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * instruction:将文件转化为输出流 2018-6-7 下午12:06:06
     *
     * @param file
     * @return
     */
    public static FileInputStream file2inputStream(File file) {
        if (isEmpty(file))
            return null;
        if (file.isDirectory())
            return null;
        try {
            return new FileInputStream(file);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    /**
     * description：文件转化为string
     */
    public static String file2string(File file) {
        FileInputStream fileInputStream = file2inputStream(file);
        if (fileInputStream == null)
            return "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line).append("\n");
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * instruction:将文件转化为输出流 2018-6-7 下午12:35:09
     *
     * @param file
     * @return
     */
    public static FileInputStream file2inputStream(String file) {
        if (file == null || file.isEmpty())
            return null;
        return file2inputStream(new File(file));
    }

    /**
     * instruction:将文件转化为输入流 2018-6-7 下午12:12:57
     *
     * @param file
     * @param add  是否是添加数据
     * @return
     */
    public static FileOutputStream file2outputStream(File file, boolean add) {
        if (file == null)
            return null;
        File parent = file.getParentFile();
        if (!parent.exists())
            parent.mkdirs();
        try {
            return new FileOutputStream(file, add);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            try {
                return new FileOutputStream(file, add);
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    /**
     * instruction:将文件转化为输入流 2018-6-7 下午12:36:34
     *
     * @param file
     * @param add  是否是添加数据
     * @return
     */
    public static FileOutputStream file2outputStream(String file, boolean add) {
        if (file == null || file.isEmpty())
            return null;
        return file2outputStream(new File(file), add);
    }

    /**
     * instruction:将文件转化为输入流 2018-6-7 下午12:14:16
     *
     * @param file
     * @return
     */
    public static FileOutputStream file2outputStream(File file) {
        return file2outputStream(file, false);
    }

    /**
     * instruction:将文件转化为输入流 2018-6-7 下午12:37:26
     *
     * @param file
     * @return
     */
    public static FileOutputStream file2outputStream(String file) {
        return file2outputStream(file, false);
    }

    /**
     * instruction:复制文件夹 2018-6-7 下午5:10:38
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean copyDirectory(File source, File target) {
        if (isEmpty(source))
            return false;
        if (source.isDirectory()) {
            boolean success = true;
            List<File> files = listAll(source);
            if (files == null || files.size() <= 0)
                return false;
            int len = files.size();
            for (int i = 0; i < len; i++) {
                File file = files.get(i);
                String fileName = target.getAbsolutePath()
                        + file.getAbsolutePath().substring(
                        source.getAbsolutePath().length());
                success &= copyFile(file, new File(fileName));
            }
            return success;
        }
        return false;

    }

    public static boolean copyDirectory(File source, File target,
                                        String[] contains) {
        if (isEmpty(source))
            return false;
        if (source.isDirectory()) {
            boolean success = true;
            List<File> files = listAll(source, contains);
            if (files == null || files.size() <= 0)
                return false;
            int len = files.size();
            for (int i = 0; i < len; i++) {
                File file = files.get(i);
                String fileName = target.getAbsolutePath()
                        + file.getAbsolutePath().substring(
                        source.getAbsolutePath().length());
                success &= copyFile(file, new File(fileName));
            }
            return success;
        }
        return false;

    }

    public static boolean copyDirectoryStart(File source, File target,
                                             String[] starts) {
        if (isEmpty(source))
            return false;
        if (source.isDirectory()) {
            boolean success = true;
            List<File> files = listAllStart(source, starts);
            if (files == null || files.size() <= 0)
                return false;
            int len = files.size();
            for (int i = 0; i < len; i++) {
                File file = files.get(i);
                String fileName = target.getAbsolutePath()
                        + file.getAbsolutePath().substring(
                        source.getAbsolutePath().length());
                success &= copyFile(file, new File(fileName));
            }
            return success;
        }
        return false;

    }

    public static boolean copyDirectoryEdn(File source, File target,
                                           String[] ends) {
        if (isEmpty(source))
            return false;
        if (source.isDirectory()) {
            boolean success = true;
            List<File> files = listAllEnd(source, ends);
            if (files == null || files.size() <= 0)
                return false;
            int len = files.size();
            for (int i = 0; i < len; i++) {
                File file = files.get(i);
                String fileName = target.getAbsolutePath()
                        + file.getAbsolutePath().substring(
                        source.getAbsolutePath().length());
                success &= copyFile(file, new File(fileName));
            }
            return success;
        }
        return false;

    }

    /**
     * instruction:复制文件 2018-6-7 下午12:44:16
     *
     * @param source 被复制的文件
     * @param target 存储文件
     * @return
     */
    public static boolean copyFile(File source, File target) {
        if (isEmpty(source))
            return false;
        FileInputStream fis = file2inputStream(source);
        if (fis == null)
            return false;
        FileOutputStream fos = file2outputStream(target);
        if (fos == null) {
            StreamUtil.close(fis);
            return false;
        }
        boolean success = false;
        FileChannel input = fis.getChannel();
        FileChannel output = fos.getChannel();
        try {
            output.transferFrom(input, 0, source.length());
            success = true;
            fos.flush();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            StreamUtil.close(output);
            StreamUtil.close(fos);
            StreamUtil.close(input);
            StreamUtil.close(fis);
        }
        return success;
    }

    /**
     * instruction: 复制 2018-6-7 下午5:11:59
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean copy(File source, File target) {
        if (isEmpty(source))
            return false;
        if (source.isDirectory())
            return copyDirectory(source, target);
        return copyFile(source, target);
    }

    /**
     * instruction:往文件添加内容 2018-6-7 下午2:34:59
     *
     * @param is     输入流
     * @param target 目标文件
     * @return
     */
    public static boolean add(InputStream is, File target) {
        if (is == null)
            return false;
        FileOutputStream fos = file2outputStream(target, true);
        if (fos == null) {
            StreamUtil.close(is);
            return false;
        }
        boolean success = false;
        try {
            byte[] buff = new byte[1024 * 1024];
            int len = is.read(buff);
            while (len > 0) {
                fos.write(buff, 0, len);
                len = is.read(buff);
            }
            success = true;
            fos.flush();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            StreamUtil.close(fos);
            StreamUtil.close(is);
        }
        return success;
    }

    /**
     * instruction:往文件添加内容 2018-6-7 下午2:40:26
     *
     * @param buff   添加的字节数组
     * @param target 目标文件
     * @return
     */
    public static boolean add(byte[] buff, File target) {
        if (buff == null || buff.length <= 0)
            return false;
        FileOutputStream fos = file2outputStream(target, true);
        if (fos == null)
            return false;
        boolean success = false;
        try {
            fos.write(buff);
            success = true;
            fos.flush();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            StreamUtil.close(fos);
        }
        return success;
    }

    /**
     * instruction:往文件添加内容 2018-6-7 下午2:56:32
     *
     * @param string
     * @param target 目标文件
     * @return
     */
    public static boolean add(String string, File target) {
        if (string == null || string.isEmpty())
            return false;
        try {
            return add(string.getBytes("utf-8"), target);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }

    /**
     * instruction:往文件添加内容 2018-6-7 下午2:56:21
     *
     * @param source 源文件
     * @param target 目标文件
     * @return
     */
    public static boolean add(File source, File target) {
        FileInputStream fis = file2inputStream(source);
        if (fis == null)
            return false;
        FileOutputStream fos = file2outputStream(target, true);
        if (fos == null) {
            StreamUtil.close(fis);
            return false;
        }
        boolean success = false;
        FileChannel input = fis.getChannel();
        FileChannel output = fos.getChannel();
        try {
            output.transferFrom(input, target.length(), source.length());
            success = true;
            fos.flush();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            StreamUtil.close(output);
            StreamUtil.close(fos);
            StreamUtil.close(input);
            StreamUtil.close(fis);

        }
        return success;
    }

    /**
     * instruction:查找目录下的所有文件,包括子目录的 2018-6-7 下午3:35:46
     *
     * @param target 目标目录
     * @return
     */
    public static List<File> listAll(File target) {
        if (isEmpty(target))
            return null;
        if (target.isDirectory()) {
            List<File> list = new ArrayList<File>();
            File[] files = target.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (isEmpty(file))
                    continue;
                if (file.isDirectory()) {
                    List<File> list2 = listAll(file);
                    if (list2 != null && list2.size() > 0)
                        list.addAll(list2);

                } else
                    list.add(file);
            }
            return list;
        }
        return null;
    }

    /**
     * instruction:包含某写字段的文件 2018-6-7 下午5:00:17
     *
     * @param target
     * @param contains 包含的字段几个
     * @return
     */
    public static List<File> listAll(File target, String[] contains) {
        if (contains == null || contains.length <= 0)
            listAll(target);
        if (isEmpty(target))
            return null;
        if (target.isDirectory()) {
            List<File> list = new ArrayList<File>();
            File[] files = target.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (isEmpty(file))
                    continue;
                if (file.isDirectory()) {
                    List<File> list2 = listAll(file, contains);
                    if (list2 != null && list2.size() > 0)
                        list.addAll(list2);

                } else {
                    for (int j = 0; j < contains.length; j++) {
                        if (file.getName().contains(contains[j]))
                            list.add(file);
                    }
                }
            }
            return list;
        }
        return null;
    }

    public static List<File> listAllStart(File target, String[] starts) {
        if (starts == null || starts.length <= 0)
            listAll(target);
        if (isEmpty(target))
            return null;
        if (target.isDirectory()) {
            List<File> list = new ArrayList<File>();
            File[] files = target.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (isEmpty(file))
                    continue;
                if (file.isDirectory()) {
                    List<File> list2 = listAllStart(file, starts);
                    if (list2 != null && list2.size() > 0)
                        list.addAll(list2);

                } else {
                    for (int j = 0; j < starts.length; j++) {
                        if (file.getName().startsWith(starts[j]))
                            list.add(file);
                    }
                }
            }
            return list;
        }
        return null;
    }

    public static List<File> listAllEnd(File target, String[] ends) {
        if (ends == null || ends.length <= 0)
            listAll(target);
        if (isEmpty(target))
            return null;
        if (target.isDirectory()) {
            List<File> list = new ArrayList<File>();
            File[] files = target.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (isEmpty(file))
                    continue;
                if (file.isDirectory()) {
                    List<File> list2 = listAllEnd(file, ends);
                    if (list2 != null && list2.size() > 0)
                        list.addAll(list2);

                } else {
                    for (int j = 0; j < ends.length; j++) {
                        if (file.getName().endsWith(ends[j]))
                            list.add(file);
                    }
                }
            }
            return list;
        }
        return null;
    }

    /**
     * instruction:查找该目录下的文件 2018-6-7 下午3:39:16
     *
     * @param target
     * @return
     */
    public static List<File> list(File target) {
        if (isEmpty(target))
            return null;
        if (target.isDirectory()) {
            File[] files = target.listFiles();
            int len = files.length;
            List<File> list = new ArrayList<File>(len);
            for (int i = 0; i < len; i++) {
                File file = files[i];
                if (file.isFile())
                    list.add(file);
            }
            return list;
        } else
            return null;
    }

    /**
     * instruction:查找该目录下以什么结尾的文件 2018-6-7 下午4:53:13
     *
     * @param target
     * @param ends   结尾符集合
     * @return
     */
    public static List<File> listEnd(File target, String[] ends) {
        if (ends == null || ends.length <= 0)
            return list(target);
        if (isEmpty(target))
            return null;
        if (target.isDirectory()) {
            File[] files = target.listFiles();
            int len = files.length;
            List<File> list = new ArrayList<File>(len);
            for (int i = 0; i < len; i++) {
                File file = files[i];
                if (file.isFile()) {
                    for (int j = 0; j < ends.length; j++) {
                        if (file.getName().endsWith(ends[j]))
                            list.add(file);
                    }
                }
            }
            return list;
        } else
            return null;

    }

    /**
     * instruction:查找该目录下包含什么的文件 2018-6-7 下午4:53:13
     *
     * @param target 包含符集合
     * @return
     */
    public static List<File> list(File target, String[] contains) {
        if (contains == null || contains.length <= 0)
            return list(target);
        if (isEmpty(target))
            return null;
        if (target.isDirectory()) {
            File[] files = target.listFiles();
            int len = files.length;
            List<File> list = new ArrayList<File>(len);
            for (int i = 0; i < len; i++) {
                File file = files[i];
                if (file.isFile()) {
                    for (int j = 0; j < contains.length; j++) {
                        if (file.getName().contains(contains[j]))
                            list.add(file);
                    }
                }
            }
            return list;
        } else
            return null;

    }

    /**
     * instruction:查找该目录下以什么开头的文件 2018-6-7 下午4:53:13
     *
     * @param target 开头符集合
     * @return
     */
    public static List<File> listStart(File target, String[] starts) {
        if (starts == null || starts.length <= 0)
            return list(target);
        if (isEmpty(target))
            return null;
        if (target.isDirectory()) {
            File[] files = target.listFiles();
            int len = files.length;
            List<File> list = new ArrayList<File>(len);
            for (int i = 0; i < len; i++) {
                File file = files[i];
                if (file.isFile()) {
                    for (int j = 0; j < starts.length; j++) {
                        if (file.getName().startsWith(starts[j]))
                            list.add(file);
                    }
                }
            }
            return list;
        } else
            return null;

    }

    /**
     * 获取文件的编码格式
     *
     * @return
     */
    public static String getCharset(File file) {
        String code = "UTF-8";
        try {
            BufferedInputStream bin = new BufferedInputStream(
                    new FileInputStream(file));
            int p = (bin.read() << 8) + bin.read();
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return code;
    }

    public static boolean insert(InputStream is, File target, long startWith) {
        return insert(StreamUtil.stream2bytes(is), target, startWith);
    }

    public static boolean insert(String string, String charset, File target,
                                 long startWith) {
        try {
            return insert(string.getBytes(charset), target, startWith);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }

    public static boolean insert(String string, File target, long startWith) {
        return insert(string, getCharset(target), target, startWith);
    }

    public static boolean insert(File soure, File target, long startWith) {
        return insert(file2inputStream(soure), target, startWith);
    }

    public static boolean insert(byte[] buff, File target, long startWith) {
        if (buff == null || buff.length <= 0)
            return false;
        if (isEmpty(target))
            return false;
        if (target.isDirectory())
            return false;
        if (startWith < 0)
            startWith = 0;
        RandomAccessFile accessFile = null;
        byte[] buf = new byte[(int) (target.length() - startWith)];
        boolean success = false;
        try {
            accessFile = new RandomAccessFile(target, "rw");
            accessFile.seek(startWith);
            accessFile.read(buf);
            accessFile.seek(startWith);
            accessFile.write(buff);
            accessFile.write(buf);
            success = true;
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (accessFile != null)
                StreamUtil.close(accessFile);
        }
        return success;
    }

    public static boolean update(File file,
                                 byte[] content, int start, int len) {
        if (!file.exists()) {
            return StreamUtil.stream2file(StreamUtil.byte2stream(Arrays.copyOfRange(content, start, start + len)), file);
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            int content_len = content.length;
            if (len > content_len)
                len = content_len;
            byte b[] = new byte[len];
            System.arraycopy(content, 0, b, 0, len);
            raf.seek(start);
            raf.write(b);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (raf != null)
                StreamUtil.close(raf);
        }
        return false;
    }

    public static boolean update(File file,
                                 InputStream inputStream, int start, int length) {
        if (!file.exists()) {
            return StreamUtil.stream2file(inputStream, file);
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            int len = -1;
            byte[] buff = new byte[1024 * 1024];
            raf.seek(start);
            while ((len = inputStream.read(buff)) > 0) {
                if (len < length) {
                    raf.write(buff, 0, len);
                    length -= len;
                } else {
                    raf.write(buff, 0, length);
                    break;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (raf != null)
                StreamUtil.close(raf);
            StreamUtil.close(inputStream);
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    public static boolean delete(File file) {
        if (file == null || !file.exists())
            return false;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (file != null && file.length() > 0) {
                for (File file2 : files) {
                    delete(file2);
                }
            }
            file.delete();
        } else
            file.delete();
        return true;
    }

    /**
     * 是否为文件
     *
     * @param file
     * @return
     */
    public static boolean isFile(File file) {
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return true;
    }

    /**
     * instruction:获取文件大小 2018-6-29 上午9:51:30
     *
     * @param file
     * @return
     */
    public static long size(File file) {
        if (file != null)
            return 0;
        if (!file.exists())
            return 0;
        long size = 0;
        if (file.isFile())
            size = file.length();
        else {
            File[] files = file.listFiles();
            for (File file2 : files) {
                size += size(file2);
            }
        }
        return size;
    }

    public static String toString(File file) {
        if (isEmpty(file))
            return null;
        if (file.isDirectory())
            return null;
        return StreamUtil.byte2string(
                StreamUtil.stream2bytes(file2inputStream(file)),
                getCharset(file));
    }

    public static long lastModified(File file) {
        return file == null ? 0 : file.lastModified();
    }

    public static File[] ascendTime(File[] files) {
        return sort(files, new Comparator<File>() {

            @Override
            public int compare(File arg0, File arg1) {
                // TODO Auto-generated method stub
                long poor =lastModified(arg1) - lastModified(arg0);
                return poor > 0 ? 1 : poor < 0 ? -1 : 0;
            }
        });
    }

    public static File[] sortTime(File[] files) {
        return sort(files, new Comparator<File>() {

            @Override
            public int compare(File arg0, File arg1) {
                // TODO Auto-generated method stub
                long poor = lastModified(arg1) - lastModified(arg0);
                return poor < 0 ? 1 : poor > 0 ? -1 : 0;
            }
        });
    }

    public static File[] ascendSize(File[] files) {
        return sort(files, new Comparator<File>() {

            @Override
            public int compare(File arg0, File arg1) {
                // TODO Auto-generated method stub
                long poor = size(arg1) - size(arg0);
                return poor > 0 ? 1 : poor < 0 ? -1 : 0;
            }
        });
    }

    public static File[] sortSize(File[] files) {
        return sort(files, new Comparator<File>() {

            @Override
            public int compare(File arg0, File arg1) {
                // TODO Auto-generated method stub
                long poor = size(arg0) - size(arg1);
                return poor > 0 ? 1 : poor < 0 ? -1 : 0;
            }
        });
    }

    public static File[] sort(File[] files, Comparator<File> comparator) {
        if (files == null || files.length <= 0)
            return files;
        File[] files2 = Arrays.copyOf(files, files.length);
        Arrays.sort(files2, comparator);
        return files2;
    }

    public static boolean saveFile(InputStream is, RandomEntity random) {
        if (random == null || is == null)
            return false;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(random.file, "rwd");
            raf.seek(random.start + random.len);
            int len = -1;
            byte[] buff = new byte[1024 * 1024];
            while ((len = is.read(buff)) > 0) {
                raf.write(buff, 0, len);
                random.len += len;
                if (random.pause)
                    return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (raf != null)
                StreamUtil.close(raf);
            StreamUtil.close(is);
        }
        return false;
    }

    public static void unJar(String zip_file, String file_path)
            throws Exception {
        if (file_path == null)
            throw new RuntimeException("zip file path is null");
        if (file_path.isEmpty())
            throw new RuntimeException("zip file path is empty");
        if (zip_file == null)
            throw new RuntimeException("zip file name is null");
        if (zip_file.isEmpty())
            throw new RuntimeException("zip file name is empty");
        File file_z = new File(zip_file);
        if (!file_z.exists())
            throw new RuntimeException("zip file is not exists");
        File file_p = new File(file_path);
        if (!file_p.exists())
            file_p.mkdirs();
        JarInputStream jis = new JarInputStream(new FileInputStream(zip_file));
        ZipEntry ze = null;
        while ((ze = jis.getNextEntry()) != null) {
            if (!ze.isDirectory()) {
                File file = new File(file_path + "/" + ze.getName());
                File file_parent = new File(file.getParent());
                if (!file_parent.exists()) {
                    file_parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte buf[] = new byte[1024];
                while ((len = jis.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                jis.closeEntry();
            }
        }
        jis.close();
    }

    public static void unZip(String zip_file, String file_path)
            throws Exception {
        if (zip_file == null)
            throw new RuntimeException("zip file name is null");
        if (zip_file.isEmpty())
            throw new RuntimeException("zip file name is empty");
        if (file_path == null || file_path.isEmpty()) {
            File f = new File(zip_file);
            String name = f.getName();
            file_path = String.format("%s/%s", f.getParent(), name.substring(0, name.lastIndexOf(".")));
        }
        File file_z = new File(zip_file);
        if (!file_z.exists())
            throw new RuntimeException("zip file is not exists");
        File file_p = new File(file_path);
        if (!file_p.exists())
            file_p.mkdirs();
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zip_file));
        ZipEntry ze = null;
        while ((ze = zis.getNextEntry()) != null) {
            if (!ze.isDirectory()) {
                File file = new File(file_path + "/" + ze.getName());
                File file_parent = new File(file.getParent());
                if (!file_parent.exists()) {
                    file_parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte buf[] = new byte[1024];
                while ((len = zis.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                zis.closeEntry();
            }
        }
        zis.close();
    }

    private static void compressByType(File file, ZipOutputStream out,
                                       String basedir) {
        if (file.isDirectory())
            compressDirectory(file, out, basedir);
        else
            compressFile(file, out, basedir);
    }

    private static void compressDirectory(File dir, ZipOutputStream out,
                                          String basedir) {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compressByType(files[i], out, basedir + dir.getName() + "/");
        }
    }

    private static void compressFile(File file, ZipOutputStream out,
                                     String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[1024];
            while ((count = bis.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static List<String> view(String zip_file) throws Exception {
        if (zip_file == null)
            throw new RuntimeException("zip file name is null");
        if (zip_file.isEmpty())
            throw new RuntimeException("zip file name is empty");
        File file_z = new File(zip_file);
        if (!file_z.exists())
            throw new RuntimeException("zip file is not exists");
        List<String> list = new ArrayList<String>();
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zip_file));
        ZipEntry ze = null;
        while ((ze = zis.getNextEntry()) != null) {
            if (!ze.isDirectory()) {
                list.add(ze.getName());
            }
        }
        return list;
    }


    public static void addZipEntry(String zip_file, String file_path,
                                   String file_name, String entry_name, byte[] text) throws Exception {
        if (zip_file == null)
            throw new RuntimeException("zip file name is null");
        if (zip_file.isEmpty())
            throw new RuntimeException("zip file name is empty");
        if (file_path == null)
            throw new RuntimeException("zip file name is null");
        if (file_path.isEmpty())
            throw new RuntimeException("zip file name is empty");
        if (file_name == null)
            throw new RuntimeException("zip file name is null");
        if (file_name.isEmpty())
            throw new RuntimeException("zip file name is empty");
        if (entry_name == null)
            throw new RuntimeException("zip file name is null");
        if (entry_name.isEmpty())
            throw new RuntimeException("zip file name is empty");
        File file_z = new File(zip_file);
        if (!file_z.exists())
            throw new RuntimeException("zip file is not exists");
        ZipFile zipFile = new ZipFile(zip_file);
        ZipEntry ze = new ZipEntry(entry_name);
        if (text != null) {
            ze.setSize(text.length);
            ze.setCompressedSize(text.length);
            ze.setExtra(text);
        } else {
            ze.setSize(0);
            ze.setCompressedSize(0);
            ze.setExtra(null);
        }
        File file = new File(file_path);
        if (!file.exists())
            file.mkdirs();
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
                file_path + "/" + file_name));
        zos.putNextEntry(ze);
        if (text != null && text.length > 0) {
            System.out.println("dadadaddd");
            zos.write(text);
        }

        zos.closeEntry();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = entries.nextElement();
            ze = new ZipEntry(e.getName());
            ze.setSize(e.getSize());
            ze.setCompressedSize(e.getCompressedSize());
            zos.putNextEntry(ze);
            if (!e.isDirectory()) {
                InputStream is = zipFile.getInputStream(e);
                int len = 0;
                byte[] buff = new byte[1024];
                while ((len = is.read(buff)) > 0) {
                    zos.write(buff, 0, len);
                }
                is.close();
            }
            zos.closeEntry();
        }
//        ZipInputStream zis = new ZipInputStream(new FileInputStream(zip_file));
//        while ((ze = zis.getNextEntry()) != null) {
//            if (!ze.isDirectory()) {
//                zos.putNextEntry(ze);
//                int len = 0;
//                int sum = 0;
//                byte[] b = new byte[1024];
//                while ((len = zis.read(b)) > 0) {
//                    sum += len;
//                    zos.write(b, 0, len);
//                }
//                System.out.println(sum);
//                zos.closeEntry();
//                zis.closeEntry();
//            }
//        }
        zos.flush();
        zos.close();
//        zis.close();
    }

    public static class RandomEntity {
        public String file;
        public long start;
        public long len;
        public boolean pause = false;
    }


    private static final int BUF_SIZE = 8192;

    public static void unzip(String zipFileName) throws Exception {
        unzip(zipFileName, null);
    }

    public static void unzip(String zipFileName, String dest) throws Exception {
        File f = new File(zipFileName);
        if (!f.exists()) {
            System.out.println("Zip File : " + zipFileName + " Not Exists!");
            return;
        }

        if (dest == null || dest.isEmpty()) {
            String name = f.getName();
            dest = String.format("%s/%s", f.getParent(), name.substring(0, name.lastIndexOf(".")));
        }

        byte[] buffer = new byte[BUF_SIZE];
        ZipFile zf = new ZipFile(zipFileName);
        ZipEntry theEntry = null;
        long totalLength = 0;
        int maxFileNameLength = 0;
        Enumeration enumer = zf.entries();
        while (enumer.hasMoreElements()) {
            theEntry = (ZipEntry) enumer.nextElement();
            totalLength += theEntry.getSize();
            if (theEntry.getName().length() > maxFileNameLength) {
                maxFileNameLength = theEntry.getName().length();
            }
        }

        System.out.println("Zip File " + lengthString(f.length()) + " and Total " + lengthString(totalLength) + " Data to unzip");
        long bytesHandled = 0;
        enumer = zf.entries();
        String zipBase;
        while (enumer.hasMoreElements()) {
            theEntry = (ZipEntry) enumer.nextElement();
            String entryName = theEntry.getName();
            String entryName2 = "";
            for (int i = 0; i < entryName.length(); i++) {
                char c = entryName.charAt(i);
                if (c == '\\') {
                    c = '/';
                }
                entryName2 += c;
            }
            entryName = entryName2;
            zipBase = ".";
            if (dest != null) {
                if (dest.endsWith("/")) {
                    dest = dest.substring(0, dest.length() - 1);
                }
                File dF = new File(dest);
                if (!dF.exists())
                    dF.mkdirs();
                zipBase = dest;
            }
            String tmpFolder = zipBase;

            int begin = 0;
            int end = 0;
            while (true) {
                end = entryName.indexOf("/", begin);
                if (end == -1) {
                    break;
                }
                tmpFolder += "/" + entryName.substring(begin, end);
                begin = end + 1;
                f = new File(tmpFolder);
                if (!f.exists()) {
                    f.mkdir();
                }
            }
            if(theEntry.isDirectory())
                continue;
//	            F:/work/text
            System.out.println(zipBase + "/" + entryName);
            OutputStream os = new FileOutputStream(zipBase + "/" + entryName);
            InputStream is = zf.getInputStream(theEntry);
            int bytesRead = is.read(buffer, 0, BUF_SIZE);
            bytesHandled += bytesRead;
            while (bytesRead > 0) {
                os.write(buffer, 0, bytesRead);
                bytesRead = is.read(buffer, 0, BUF_SIZE);
                bytesHandled += bytesRead;
                System.out.print("\r");
                int percent = (int) (((double) bytesHandled / totalLength) * 100);
                System.out.print("\r" + percent + "%  ");
                if (totalLength > 1024 * 1024 * 80) {
                    int j;
                    for (j = 0; j < percent % 4 + 1; j++) {
                        System.out.print(">");

                    }
                    for (int x = 0; x < 4 - j; x++) {
                        System.out.print(" ");
                    }
                }

                System.out.print("  Unzip File " + entryName + getSpace(maxFileNameLength - entryName.length()));
            }
            is.close();
            os.close();
        }

        System.out.println("\r100%  Zip File : " + zipFileName + " has been unCompressed to " + dest + "/ !" + getSpace(40));
    }

    private static String lengthString(long fileLength) {
        long newValue = 0;
        String size = null;
        if (fileLength < 1024) {
            size = fileLength + " B";
        } else if (fileLength < (1024 * 1024)) {
            newValue = fileLength / 1024;
            size = newValue + " KB";
        } else if (fileLength < (1024 * 1024 * 1024)) {
            newValue = fileLength / (1024 * 1024);
            size = newValue + " MB";
        } else {
            newValue = fileLength / (1024 * 1024 * 1024);
            size = newValue + " GB";
        }
        return size;
    }

    private static String getSpace(int num) {
        String space = "";
        for (int i = 0; i < num; i++) {
            space += " ";
        }
        return space;
    }


    public static void zip(String path) throws Exception {
        zip(path, null);
    }

    public static void zip(String path, String dest) throws Exception {
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("File : " + path + " Not Exists!");
            return;
        }
        long[] l = getLength(f);
        long totalLength = l[0];
        long maxFileNameLength = l[1];
        System.out.println("total " + lengthString(totalLength) + " to zip");
        String path2 = "";
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '\\') {
                c = '/';
            }
            path2 += c;
        }
        path = path2;

        String zipBase = path.substring(path.lastIndexOf("/") == -1 ? 0 : path.lastIndexOf("/") + 1);
        if (dest == null || dest.isEmpty()) {
            dest = String.format("%s/%s.zip", f.getParent(), f.getName());
        }
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(dest));
        zipFile(f, zipOut, zipBase, 0, totalLength, maxFileNameLength);
        zipOut.close();
        System.out.print("\r100%\t");
        System.out.println(path + " has been compressed to zip file : " + dest + getSpace(40));
    }

    public static void zipFile(File f, ZipOutputStream zipOut, String zipBase, long bytesHandled, long totalLength, long maxFileNameLength) throws Exception {
        if (f.isDirectory()) {
            File[] fs = f.listFiles();
            for (int i = 0; i < fs.length; i++) {
                zipFile(fs[i], zipOut, zipBase, bytesHandled, totalLength, maxFileNameLength);
            }
        } else {
            String path = f.getPath();
            FileInputStream fileIn = new FileInputStream(path);
            String entryName = path;
            int basePos = path.indexOf(zipBase);
            if (basePos > 0) {
                entryName = path.substring(basePos);
            }
            ZipEntry theEntry = new ZipEntry(entryName);
            theEntry.setSize(f.length());

            zipOut.putNextEntry(theEntry);

            byte[] buffer = new byte[BUF_SIZE];
            int bytesRead = fileIn.read(buffer, 0, BUF_SIZE);
            bytesHandled += bytesRead;

            while (bytesRead > 0) {
                zipOut.write(buffer, 0, bytesRead);
                bytesRead = fileIn.read(buffer, 0, BUF_SIZE);
                bytesHandled += bytesRead;
                int percent = (int) (((double) bytesHandled / totalLength) * 100);
                System.out.print("\r" + percent + "%  ");
                if (totalLength > 1024 * 1024 * 80) {
                    int j;
                    for (j = 0; j < percent % 4 + 1; j++) {
                        System.out.print(">");

                    }
                    for (int x = 0; x < 4 - j; x++) {
                        System.out.print(" ");
                    }
                }
                System.out.print("  Compress File " + path + getSpace((int) (maxFileNameLength - path.length())));
            }
            fileIn.close();
            zipOut.closeEntry();
        }
    }

    private static long[] getLength(File path) {
        long totalLength = 0;
        long maxFileNameLength = 0;
        if (path.isFile()) {
            totalLength += path.length();
            if (path.getPath().length() > maxFileNameLength) {
                maxFileNameLength = path.getPath().length();
            }
        } else {
            File[] fs = path.listFiles();
            for (int i = 0; i < fs.length; i++) {
                long[] l = getLength(fs[i]);
                totalLength += l[0];
                totalLength += l[1];
            }
        }
        return new long[]{totalLength, maxFileNameLength};
    }

    /**
     * 数据结构体的签名标记
     */
    private static final String SIG = "MCPT";

    /**
     * 用于Android添加渠道，360版，利用zip文件添加comment（摘要）
     */
    public static boolean addZipSmallEndSequence(File path, byte[] content) {
        try {
            byte[] bytesContent = content;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(bytesContent); // 写入内容；
            baos.write(short2Stream((short) bytesContent.length)); // 写入内容长度；
            byte[] data = baos.toByteArray();
            baos.close();
            if (data.length > Short.MAX_VALUE) {
                throw new IllegalStateException("Zip comment length > 32767.");
            }
            // Zip文件末尾数据结构：{@see java.util.zip.ZipOutputStream.writeEND}
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            raf.seek(path.length() - 2); // comment长度是short类型
            raf.write(short2Stream((short) data.length)); // 重新写入comment长度，注意Android apk文件使用的是ByteOrder.LITTLE_ENDIAN（小端序）；
            raf.write(data);
            raf.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] getZipSmallEndSequence(File path) {
        byte[] bytesContent = null;
        try {
            byte[] bytes = new byte[2];
            RandomAccessFile raf = new RandomAccessFile(path, "r");
            long index = raf.length();
            index -= bytes.length;
            readFully(raf, index, bytes); // 读取内容长度；
            int lengthContent = stream2Short(bytes, 0);
            bytesContent = new byte[lengthContent];
            index -= lengthContent;
            readFully(raf, index, bytesContent); // 读取内容；
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesContent;
    }


    /**
     * RandomAccessFile seek and readFully
     *
     * @param raf
     * @param index
     * @param buffer
     * @throws IOException
     */
    private static void readFully(RandomAccessFile raf, long index, byte[] buffer) throws IOException {
        raf.seek(index);
        raf.readFully(buffer);
    }

    /**
     * short转换成字节数组（小端序）
     *
     * @param stream
     * @param offset
     * @return
     */
    private static short stream2Short(byte[] stream, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(stream[offset]);
        buffer.put(stream[offset + 1]);
        return buffer.getShort(0);
    }

    /**
     * 字节数组转换成short（小端序）
     *
     * @param data
     * @return
     */
    private static byte[] short2Stream(short data) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(data);
        buffer.flip();
        return buffer.array();
    }

    public static void gzipCompress(File source, File save) {
        GZIPOutputStream gzipOutputStream = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(source);
            gzipOutputStream = new GZIPOutputStream(new FileOutputStream(save));
            byte[] buff = new byte[1024 * 1024];
            int len = 0;
            while ((len = fis.read(buff)) > 0)
                gzipOutputStream.write(buff, 0, len);
            gzipOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StreamUtil.close(gzipOutputStream);
        StreamUtil.close(fis);
    }

    public static void unGzipCompress(File source, File save) {
        GZIPInputStream gzipInputStream = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(save);
            gzipInputStream = new GZIPInputStream(new FileInputStream(source));
            byte[] buff = new byte[1024 * 1024];
            int len = 0;
            while ((len = gzipInputStream.read(buff)) > 0)
                fos.write(buff, 0, len);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StreamUtil.close(fos);
        StreamUtil.close(gzipInputStream);
    }
}
