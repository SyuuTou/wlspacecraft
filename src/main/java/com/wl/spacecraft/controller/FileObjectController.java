package com.wl.spacecraft.controller;

import com.wl.spacecraft.controller.common.GenericController;
import com.wl.spacecraft.utils.FileCopyUtil;
import com.wl.spacecraft.utils.fdfsclient.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 文件接口
 * <p>
 *
 * @author jiangzhou.bo@hand-china.com
 * @version 1.0
 * @name FileObjectController
 * @date 2017-10-15 14:09
 */
//@Controller
//@RequestMapping("/fastdfs")
@RestController
public class FileObjectController extends GenericController {

    @Resource
    private FastDFSClient fastDFSClient;
//    private FastDFSClient fastDFSClient = new FastDFSClient();

//    @Resource
//    private FIleOptUtils fIleOptUtils;

    /**
     * 文件服务器地址
     */
    @Value("${file_server_addr}")
    private String fileServerAddr;
    /**
     * org.slf4j.Logger
     */
//    private static Logger logger = LoggerFactory.getLogger(FileObjectController.class);

    /**
     * FastDFS秘钥
     */
    @Value("${fastdfs.http_secret_key}")
    private String fastDFSHttpSecretKey;

    @RequestMapping("/test")
//    @ResponseBody
    public Object test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,Object> map =new HashMap<>();

        String a="qw";
        String b="as";

        this.LOGGER.debug("There are now {} user accounts: {}", a, b);
        this.LOGGER.info("There are now {} user accounts: {}", a, b);
        this.LOGGER.trace("There are now {} user accounts: {}", a, b);

        FileResponseData fileResponseData = new FileResponseData(true);

        System.err.println("contextPath: {"+request.getContextPath()+"}");
        String realPath = request.getServletContext().getRealPath("/upload/img/");
        System.err.println(realPath);

        System.err.println(fileResponseData);
//        response.getOutputStream().print("response");
        response.getOutputStream().write(1);
//        response.getWriter().print(1);

        map.put("contextPath",request.getContextPath());
        map.put("application",realPath);
        map.put("fileResponseData",fileResponseData);

        return map;
    }

    /**
     * 设置response响应头实现文件下载
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("file/down/test")
    public void testFileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String realPath = request.getSession().getServletContext().getRealPath("/upload/image/");
        System.err.println(realPath);
        // 1.获取要下载的文件的绝对路径
//        String path = request.getServletContext().getRealPath("/download/1.jpg");
        String path = "/Users/syuutousan/Downloads/icon/aixin.png";
        // 2.获取要下载的文件名
        String filename = path.substring(path.lastIndexOf("/") + 1);
        System.err.println(filename);
        // 3.设置content-disposition响应头控制浏览器以下载的形式打开文件
        response.setHeader("content-disposition", "attachment;filename=" + filename);

        InputStream in = null;
        OutputStream out = null;
        try {
            // 4.获取要下载的文件输入流
            in = new FileInputStream(path);
            int len = 0;
            // 5.创建数据缓冲区
            byte[] buffer = new byte[1024];
            // 6.通过response对象获取OutputStream流
            out = response.getOutputStream();
            // 7.将FileInputStream流写入到buffer缓冲区
            while ((len = in.read(buffer)) != -1) {
                // 8.使用OutputStream将缓冲区的数据输出到客户端浏览器
                out.write(buffer, 0, len);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 上传文件通用，只上传文件到服务器，不会保存记录到数据库
     *
     * @param file
     * @param request
     * @return 返回文件路径等信息
     */
    @RequestMapping(value = "/upload/file/sample")
    @ResponseBody
    public FileResponseData uploadFileSample(MultipartFile file, HttpServletRequest request) {
        System.err.println(file.getOriginalFilename());
        System.err.println(file.getContentType());
        System.err.println(file.getName());
        return uploadSample(file, request);
    }

    /**
     * 只能上传图片，只上传文件到服务器，不会保存记录到数据库. <br>
     * 会检查文件格式是否正确，默认只能上传 ['png', 'gif', 'jpeg', 'jpg'] 几种类型.
     *
     * @param file
     * @param request
     * @return 返回文件路径等信息
     */
    @RequestMapping("/upload/image/sample")
    @ResponseBody
    public FileResponseData uploadImageSample(@RequestParam MultipartFile file, HttpServletRequest request) {
        System.err.println("********");
        // 检查文件类型
        if (!FileCheck.checkImage(file.getOriginalFilename())) {
            FileResponseData responseData = new FileResponseData(false);
            responseData.setCode(ErrorCode.FILE_TYPE_ERROR_IMAGE.CODE);
            responseData.setMessage(ErrorCode.FILE_TYPE_ERROR_IMAGE.MESSAGE);
            return responseData;
        }

        return uploadSample(file, request);
    }

    /**
     * 只能上传文档，只上传文件到服务器，不会保存记录到数据库. <br>
     * 会检查文件格式是否正确，默认只能上传 ['pdf', 'ppt', 'xls', 'xlsx', 'pptx', 'doc', 'docx'] 几种类型.
     *
     * @param file
     * @param request
     * @return 返回文件路径等信息
     */
    @RequestMapping("/upload/doc/sample")
    @ResponseBody
    public FileResponseData uploadDocSample(@RequestParam MultipartFile file, HttpServletRequest request) {
        // 检查文件类型
        if (!FileCheck.checkDoc(file.getOriginalFilename())) {
            FileResponseData responseData = new FileResponseData(false);
            responseData.setCode(ErrorCode.FILE_TYPE_ERROR_DOC.CODE);
            responseData.setMessage(ErrorCode.FILE_TYPE_ERROR_DOC.MESSAGE);
            return responseData;
        }

        return uploadSample(file, request);
    }

    /**
     * 以附件形式下载文件
     *
     * @param fileId   文件id
     * @param response
     */
    @RequestMapping("/download/file")
    public void downloadFile(String fileId, HttpServletResponse response) throws FastDFSException {
        try {

            fastDFSClient.downloadFile(fileId, response);
        } catch (FastDFSException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取图片 使用输出流输出字节码，可以使用< img>标签显示图片<br>
     *
     * @param fileId 图片地址
     * @param response
     */
    @RequestMapping("/download/image")
    public void downloadImage(String fileId, HttpServletResponse response) throws FastDFSException {
        try {
            fastDFSClient.downloadFile(fileId, response.getOutputStream());
        } catch (FastDFSException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据指定的路径删除服务器文件，适用于没有保存数据库记录的文件
     *
     * @param filePath
     */
    @RequestMapping("/delete/file")
    public FileResponseData deleteFile(String filePath, Locale locale) {
        FileResponseData responseData = new FileResponseData();
        try {
            fastDFSClient.deleteFile(filePath);
        } catch (FastDFSException e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setCode(e.getCode());
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * 获取访问文件的token
     *
     * @param filePath 文件路径
     * @return
     */
    @RequestMapping("/get/token")
    @ResponseBody
    public FileResponseData getToken(String filePath) {
        FileResponseData responseData = new FileResponseData();
        // 设置访文件的Http地址. 有时效性.
        String token = FastDFSClient.getToken(filePath, fastDFSHttpSecretKey);
        responseData.setToken(token);
        responseData.setHttpUrl(fileServerAddr + "/" + filePath + "?" + token);

        return responseData;
    }

    /**
     * 上传通用方法，只上传到服务器，不保存记录到数据库
     *
     * @param file
     * @param request
     * @return
     */
    public FileResponseData uploadSample(MultipartFile file, HttpServletRequest request) {
        FileResponseData responseData = new FileResponseData();
        try {
            // 上传到服务器
            String fileId = fastDFSClient.uploadFileWithMultipart(file);

            responseData.setFileName(file.getOriginalFilename());
            responseData.setFilePath(fileId);
            responseData.setFileType(FastDFSClient.getFilenameSuffix(file.getOriginalFilename()));
            // 设置访文件的Http地址. 有时效性.
            String token = FastDFSClient.getToken(fileId, fastDFSHttpSecretKey);
            responseData.setToken(token);
            responseData.setHttpUrl(fileServerAddr + "/" + fileId + "?" + token);
        } catch (FastDFSException e) {
            responseData.setSuccess(false);
            responseData.setCode(e.getCode());
            responseData.setMessage(e.getMessage());
        }

        return responseData;
    }

    /**
     * 模拟文件下载
     *
     * @param fileUrl 文件的url访问路径
     * @return
     * @throws FastDFSException
     */
    @RequestMapping("/img/download")
    public String download(HttpServletRequest request, String fileUrl) throws FastDFSException, IOException {



        String url = "onlygame.us:8080/group1/M00/00/00/rB9feFt1XnaAaAT_AABWwJMwWIc766.png";
        String fileId = "group1/M00/00/00/rB9feFt1XnaAaAT_AABWwJMwWIc766.png";

        String realPath = request.getSession().getServletContext().getRealPath("/upload/image/");
        System.err.println("realPath" + realPath);


        //客户端下载并进行base64编码
        String base64 = FIleOptUtils.downloadToBase64(url);

//        转存方式一,通过客户端提供的api下载并实现转存
//        fastDFSClient.downloadFile(fileId,new FileOutputStream(new File("/usr/local/image/a.png")));

//        转存方式二,通过客户端下载并自行转存实现
        byte[] download = fastDFSClient.download(fileId);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(download);
        //拷贝方式1
        long copyByArray = new FileCopyUtil(byteArrayInputStream, realPath+"a.png").copyByArray();
        System.err.println(copyByArray);
        //拷贝方式2
        long copyOneByOne = new FileCopyUtil(byteArrayInputStream, "/usr/local/image/b/a.png").copyByOne();
        System.err.println(copyOneByOne);

        return null;
    }

    /**
     * 根据文件id删除文件
     *
     * @param fileUrl
     * @return 成功后返回0
     * @throws FastDFSException
     * @throws FileNotFoundException
     */
    @RequestMapping("/img/delete")
    public Object delete(String fileUrl) throws FastDFSException, FileNotFoundException {
        String url = "onlygame.us:8080/group1/M00/00/00/rB9feFt01KSAWzNYAADRCnNkqcQ075.png";
        int i = fastDFSClient.deleteFile("group1/M00/00/00/rB9feFt01KSAWzNYAADRCnNkqcQ075.png");
        System.err.println(i);
        return i;
    }

    /**
     * 本地文件上传，根据文件路径
     *
     * @return 成功后返回文件id
     * @throws FastDFSException {
     *                          "fileUrl": "onlygame.us:8080/group1/M00/00/00/rB9feFt654GAHdaLAHOkVrdH6po452.mp3",
     *                          "fileId": "group1/M00/00/00/rB9feFt654GAHdaLAHOkVrdH6po452.mp3"
     *                          }
     */
    @RequestMapping("/img/upload")
    public Object upload(HttpServletRequest request) throws FastDFSException {
        System.err.println(fastDFSClient);

        String filePath = "/Users/syuutousan/Music/网易云音乐/canon.mp3";
        //文件id
        String fileId = fastDFSClient.uploadFileWithFilepath(filePath);
        //文件的可访问url
        String fileUrl = fileServerAddr + "/" + fileId;

        Map<String, String> map = new HashMap<>();

        map.put("fileId", fileId);
        map.put("fileUrl", fileUrl);

        System.err.println(map);

        return map;
    }

    /**
     * 获取文件信息
     *
     * @return
     * @throws FastDFSException
     */
    @RequestMapping("file/info")
    public Object fileInfo() throws FastDFSException {

        String fileId = "group1/M00/00/00/rB9feFt1R6uAR17SAABMkKx3uIg125.png";
        Map<String, Object> fileInfo = fastDFSClient.getFileInfo(fileId);
        Map<String, Object> fileDescriptions = fastDFSClient.getFileDescriptions(fileId);
        String originalFilename = fastDFSClient.getOriginalFilename(fileId);
        String filename = FastDFSClient.getFilename(fileId);


        return filename;
    }

}
