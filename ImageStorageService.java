package com.example.practice.services;

import ch.qos.logback.core.rolling.helper.FileNamePattern;
import org.apache.catalina.util.ResourceSet;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;
@Service
public class ImageStorageService implements IStorageService {
    private final Path storageFolder = Paths.get("upload");

    public ImageStorageService() {
        try{
            Files.createDirectories(storageFolder)
        }
        catch (IOException exception){
            throw new RuntimeException("Cannot initialize storage", exception);
        }
    }

    private boolean isImageFile(MultipartFile file){

        String fileExtentsion = FilenameUtils.getExtentsion(File.getOriginalFileName());
        return Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp"})
                .contains((fileExtentsion.trim().toLowerCase()));
    }

    @Override
    public String storeFile(MultipartFile file) {
        try{
            if(file.isEmpty()){
                throw new RuntimeException("Failed to store empty file.");
            }
            if (isImageFile(file)) {
                throw new RuntimeException(" You can only uploead image file");
            }
            float fileSizeInMegabytes = file.getSize()/ 1_000_000f;
            if (fileSizeInMegabytes > 5.0f){
                throw new RuntimeException("File must be <= 5mbs");
            }
            String fileExtension = FileNameUtils.getExtension(File.getOriginalFileName());
            String generateFileName = UUID.randomUUID().toString().replace("-", "");
            generateFileName = generateFileName + "." + fileExtension;
            Path destinatioFilePath = this.storageFolder.resolve(Path.get(generateFileName)).normalize()
                    .toAbsolutePath();
                // Tạo một file rỗng có tên là generatedFileNam vào đường dẫn mong muốn
            if (!destinatioFilePath.getParent().equals(this.storageFolder.toAbsolutePath())){
                throw new RuntimeException("Cannot store outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinatioFilePath, StandardCopyOption.REPLACE_EXISTING);
                // copy dũ liệu từ tham số pass in file vào đường dẫn đã tạo
            }
            return generateFileName;
        }
        catch (IOException exception){
            throw new RuntimeException("Failed to store file", exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try{
            return Files.walk(this.storageFolder, 1).filter(path -> !path.equals(this.storageFolder)
            && !path.toString().contains("._")).map(this.storageFolder::relativize);
        }
        catch (IOException exception){
            throw new RuntimeException("Failed to load stored file", exception);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try{
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()){
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else{
                throw new RuntimeException("Coutd not read files: " + fileName)
            }
        }
        catch (IOException exception){
            throw new RuntimeException("Could not read files: " + fileName, exception);

        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
