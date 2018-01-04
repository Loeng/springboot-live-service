package com.sgc.comm;

import com.sgc.comm.util.GUIDHelper;
import com.sgc.app.controller.UpyunController;
import main.java.com.UpYun;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;


import java.io.*;
import java.util.Iterator;

public class FileUtil {

    public boolean uploadFile(String newName,InputStream inputStream,UpYun upyun){
        try {
            String filePath = UpyunController.DIR_ROOT + newName;
            File file = new File(System.getProperty("user.dir") + filePath);

            BufferedInputStream in = new BufferedInputStream(inputStream);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[1024*8];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
            upyun.setTimeout(120);
            upyun.setContentMD5(UpYun.md5(file));
            upyun.setFileSecret(UpyunController.IPCPWD);
            // 上传文件，并自动创建父级目录（最多10级）
            System.out.println("上传又拍云之前");
            boolean result = upyun.writeFile(filePath, file, true);
            System.out.println("上传又拍云之后");

            System.out.println("删除文件之前");
            FileUtils.deleteQuietly(file);
            System.out.println("删除文件之后");

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean filesUpload(MultipartRequest pictureFile, UpYun upyun){
        Iterator<String> fileNames = pictureFile.getFileNames();
        while (fileNames.hasNext()) {
            MultipartFile multipartFile = pictureFile.getFile(fileNames.next());
            String oriName = multipartFile.getOriginalFilename();
            System.out.println(oriName);
            String newName = GUIDHelper.genRandomGUID() + ".jpg";
            System.out.println(newName);

            FileUtil fileUtil = new FileUtil();
            InputStream inputStream = null;
            try {
                inputStream = multipartFile.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            boolean ret = fileUtil.uploadFile(newName,inputStream,upyun);

            return ret;
        }
        return true;
    }
}
