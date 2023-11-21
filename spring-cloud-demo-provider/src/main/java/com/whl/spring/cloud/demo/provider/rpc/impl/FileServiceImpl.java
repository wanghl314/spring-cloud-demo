package com.whl.spring.cloud.demo.provider.rpc.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.Hessian2Output;
import com.whl.spring.cloud.demo.FileService;
import com.whl.spring.cloud.demo.bean.FileInfo;
import com.whl.spring.cloud.demo.util.FileUtils;

@DubboService(protocol = "hessian")
public class FileServiceImpl implements FileService {
    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public FileInfo upload(String name, InputStream is) throws IOException {
        logger.info("upload");
        String suffix = "";

        if (StringUtils.isContains(name, '.')) {
            suffix = name.substring(name.lastIndexOf("."));
        }
        String storeFileName = UUID.randomUUID() + suffix;
        Path path = this.getFileStorePath();
        Path storePath = path.resolve(storeFileName);

        if (!Files.exists(storePath.getParent())) {
            Files.createDirectories(storePath.getParent());
        }
        Files.copy(is, storePath, StandardCopyOption.REPLACE_EXISTING);
        File file = storePath.toFile();
        return new FileInfo(storeFileName, file.length(), file.lastModified(), null, null);
    }

    @Override
    public InputStream download(String name) throws IOException {
        logger.info("download, {}", name);
        Path path = this.getFileStorePath();
        Path file = path.resolve(name);

        if (Files.exists(file)) {
//            File f = file.toFile();
//            long size = f.length();
//            long lastModified = f.lastModified();
//            String contentType = FileUtils.getContentType(f);
//            return new FileInfo(name, size, lastModified, contentType, Files.newInputStream(file));
            return Files.newInputStream(file);
        }
        return null;
    }

    private Path getFileStorePath() {
        return Paths.get(System.getProperty("user.dir"), "upload");
    }

    public static void main(String[] args) throws IOException {
        String name = "7587943c-c1bc-4475-b546-2579133bb592.JPG";
        Path path = Paths.get(System.getProperty("user.dir"), "upload");
        Path file = path.resolve(name);
        File f = file.toFile();
        long size = f.length();
        long lastModified = f.lastModified();
        String contentType = FileUtils.getContentType(f);
        System.out.println("name=" + name + ",size=" + size + ",lastModified=" + lastModified + ",contentType=" + contentType);
        FileInfo fileInfo = new FileInfo(name, size, lastModified, contentType, Files.newInputStream(file));
        FileOutputStream os = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\bbb");
        Hessian2Output ho = new Hessian2Output(os);
        ho.writeObject(fileInfo);
        ho.flush();

//        FileInputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\bbb");
//        Hessian2Input hi = new Hessian2Input(is);
//        FileInfo fileInfo = (FileInfo) hi.readObject(FileInfo.class);
//        System.out.println(fileInfo);
    }

}
