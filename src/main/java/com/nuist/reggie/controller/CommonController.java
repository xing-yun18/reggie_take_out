package com.nuist.reggie.controller;

import com.nuist.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.net.www.http.HttpCaptureOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author 浅梦
 * @Date 2023 05 05 15:54
 **/
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String BaseString;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        File dir = new File(BaseString);
        if (!dir.exists()) {
            //创建文件夹
            dir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        //截取后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //拼接新文件名
        String fileName = UUID.randomUUID() + substring;
        String newName = BaseString + fileName;
        log.info(fileName);
        //转存文件
        try {
            file.transferTo(new File(newName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            //输入流读取本地文件
            FileInputStream fis = new FileInputStream(BaseString + name);
            //输出流回写到页面
            ServletOutputStream ops = response.getOutputStream();

            response.setContentType("image/jpeg");
            //一边读一边写
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fis.read(bytes)) != -1) {
                ops.write(bytes, 0, len);
                ops.flush();
            }
            //关流
            fis.close();
            ops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
