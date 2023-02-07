package com.example.practice.controllers.controllers;

import com.example.practice.models.ResponseObject;
import com.example.practice.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/v1/FileUpload")
public class FileUploadController {
    @Autowired
    private IStorageService storageService;
    // Từ động tìm 1 bean/ 1object đã được IOC quét và tạo ra dựa trên annotation service đã được gắn trên class
    // implement interface này

    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile (@RequestParam("file")MultipartFile file){
        try{
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok",
                    "up load file successfully", generatedFileName));
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("ok",
                    exception.getMessage(),""));
        }
    }
    @GetMapping("files/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String filename){
        try{
            byte[] bytes = storageService.readFileContent(filename);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        }
        catch (Exception exception){
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping("")
    public ResponseEntity<ResponseObject> getUploadedFiles(){
        try{
            List<String> urls = storageService.loadAll().map(path ->{
                String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "readDetailFile", path.getFileName().toString().build().toUri().toString();
                return urlPath;
            }).
                collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseObject("ok", "List file successfully", urls));
        } catch (Exception exception){
            return ResponseEntity.ok(new ResponseObject("Failed", "List files Failed", new String[] {}))
        }
    }


}
