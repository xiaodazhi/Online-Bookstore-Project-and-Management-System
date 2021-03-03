package com.panda.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUploadUtil {

    public static Map<String, String> uploadFile(HttpServletRequest request, String[] suffixes, long limitSize, int fileNum) throws FileUploadException, IOException, SuffixNotMatchException, OutOfLimitSizeException, OutOfLimitFileCountException {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        servletFileUpload.setHeaderEncoding("UTF-8");

        if(!ServletFileUpload.isMultipartContent(request)) {//fileUpload工具包只能解析Multipart结构的，所以要先进行判断。
            return null;
        }

        Map<String, String> params = new HashMap<>();
        //FileItem
        List<FileItem> fileItemList = servletFileUpload.parseRequest(request);

        for (FileItem fileItem : fileItemList) {
            if(fileItem.isFormField()) {//表示是表单字段
                params.put(fileItem.getFieldName(), new String(fileItem.getString().getBytes("ISO-8859-1") , "UTF-8"));
            } else {//表示二进制文件
                String fieldName = fileItem.getFieldName();
                String jsonStr = params.get(fieldName);
                JSONArray jsonArray = null;
                if (jsonStr == null) {//当前上传的文件，是这个Field的第一个文件
                    jsonArray = new JSONArray();
                } else {//当前上传的文件不是当前这个Field的第一个文件
                    jsonArray = JSON.parseArray(jsonStr);
                    if (jsonArray.size() >= fileNum) {
                        throw new OutOfLimitFileCountException();
                    }
                }
                String originFileName = fileItem.getName();//获取上传的文件名
                if (!suffixIsOK(originFileName, suffixes)) throw new SuffixNotMatchException();
                if (fileItem.getSize() > limitSize) throw new OutOfLimitSizeException();
                String filePath = "D:/upload_files/";//定义一个文件存储的路径
                String realFileName = filePath + UUID.randomUUID().toString();//创建新的文件名
                File file = new File(realFileName);//创建文件对象
                file.createNewFile();//在磁盘上创建文件，此时文件是一个空文件
                //文件在网络中，我们获取到的都是二进制流
                InputStream inputStream = fileItem.getInputStream();//获取上传文件的流
                FileOutputStream fileOutputStream = new FileOutputStream(file);//获取写入文件的流
                IOUtils.copy(inputStream, fileOutputStream);//使用工具将inputStream导入outputStream
                fileOutputStream.close();
                inputStream.close();
                //构建文件信息的JSON，我们需要保存文件的原名和当前的路径全名
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("originFileName", originFileName);
                jsonObject.put("realFileName", realFileName);
                jsonArray.add(jsonObject);//将这个json对象添加到json数组
                params.put(fieldName, jsonArray.toJSONString());//将这个json数组写回到map中。
            }
        }
        return params;
    }

    private static boolean suffixIsOK(String fileName, String[] suffixes) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        for (String temp : suffixes) {
            if (temp.equals(suffix.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static class SuffixNotMatchException extends Exception {
        public SuffixNotMatchException() {
            super();
        }
        public SuffixNotMatchException(String str) {
            super(str);
        }
    }

    public static class OutOfLimitSizeException extends Exception {
        public OutOfLimitSizeException() {
            super();
        }
        public OutOfLimitSizeException(String str) {
            super(str);
        }
    }

    public static class OutOfLimitFileCountException extends Exception {
        public OutOfLimitFileCountException() {
            super();
        }
        public OutOfLimitFileCountException(String str) {
            super(str);
        }
    }
}
