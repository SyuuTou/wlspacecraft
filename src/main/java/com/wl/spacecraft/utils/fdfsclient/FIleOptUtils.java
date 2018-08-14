package com.wl.spacecraft.utils.fdfsclient;

import org.apache.commons.codec.binary.Base64;

public class FIleOptUtils {

    private static FastDFSClient fastDFSClient = new FastDFSClient();

    /**
     * 将文件的url访问路径下载为字节数组并进行base64编码
     * @param fileUrl
     * @return
     * @throws FastDFSException
     */
    public static String downloadToBase64(String fileUrl) {

        //取得文件id
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
        System.err.println(FIleOptUtils.downloadToBase64("http://onlygame.us:8080/group1/M00/00/00/rB9feFtv-iGAQBRBAAG2tITFUoc347.png"));
    }

}
