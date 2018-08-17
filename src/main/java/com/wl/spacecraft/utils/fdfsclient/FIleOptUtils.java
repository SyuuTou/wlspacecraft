package com.wl.spacecraft.utils.fdfsclient;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class FIleOptUtils {
    //第一种方式，静态变量注入
     private static FastDFSClient fastDFSClient;
    @Resource
    public void setFastDFSClient(FastDFSClient fastDFSClient) {
        FIleOptUtils.fastDFSClient = fastDFSClient;
    }
    //第二种方式，普通生成对象方式
//    private static FastDFSClient fastDFSClient = new FastDFSClient();

    /**
     * 将文件的url访问路径下载为字节数组并进行base64编码
     * @param fileUrl
     * @return
     * @throws FastDFSException
     */
    public static String downloadToBase64(String fileUrl) {

        //取得文件id(是在文件id存在group的情况下进行拆分)
        String fileId = fileUrl.substring(fileUrl.indexOf("group"));
        System.err.println(fileId);

        byte[] download = new byte[0];

        try {
            download = fastDFSClient.download(fileId);
        } catch (FastDFSException e) {

            e.printStackTrace();
        }

        String string = Base64.encodeBase64String(download);
        String safeString = Base64.encodeBase64URLSafeString(download);

        System.err.println(string);
        return string;
    }


    public static void main(String[] args) throws FastDFSException {
//        FIleOptUtils.downloadToBase64(null);
        System.err.println(fastDFSClient);
    }

}
