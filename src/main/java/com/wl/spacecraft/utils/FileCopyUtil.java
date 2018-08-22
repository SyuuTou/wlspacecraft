package com.wl.spacecraft.utils;

import java.io.*;
import java.time.Instant;

/**
 * 文件拷贝
 */
public class FileCopyUtil implements Closeable {
    private InputStream in;
    private OutputStream out;

    public FileCopyUtil(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    /**
     * @param in          输入流
     * @param fileOutPath 文件输出路径
     */
    public FileCopyUtil(InputStream in, String fileOutPath) throws FileNotFoundException {
        this.in = in;

        File outFile = new File(fileOutPath);
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        if (!outFile.exists()) {
            try {
                outFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);
        this.out = fileOutputStream;
    }

    /**
     * @param in      输入流
     * @param outFile 输出文件File对象
     * @throws FileNotFoundException
     */
    public FileCopyUtil(InputStream in, File outFile) throws FileNotFoundException {
        this.in = in;

        FileOutputStream fileOutputStream = new FileOutputStream(outFile);
        this.out = fileOutputStream;
    }

    /**
     * 文件拷贝，每次读取一个字节，然后写入
     *
     * @return 输入流
     * @throws IOException
     */
    public long copyByOne() throws IOException {
        long start = System.currentTimeMillis();
        int temp = 0;//保存每一个读取的字节数据
        while ((temp = this.in.read()) != -1) {
            this.out.write(temp);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    /**
     * 文件拷贝，读取数据到字节数组，然后一次写入
     *
     * @return
     * @throws IOException
     */
    public long copyByArray() throws IOException {
        long start = System.currentTimeMillis();
        byte[] data = new byte[1024];

        int len = 0;//保存读取到的字节个数
        //将每一次读取的数据保存到data数组里面，返回读取到的字节个数
        while ((len = this.in.read(data)) != -1) {
            this.out.write(data, 0, len);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }


    @Override
    public void close() throws IOException {
        this.in.close();
        this.out.close();
    }

    public static void main(String[] args) {
        Instant now = Instant.now();
        Instant later = now.plusSeconds(3);
        Instant earlier = now.minusSeconds(3);
        System.err.println(now.toString());
        System.err.println(later);
        System.err.println(earlier);
    }
}
