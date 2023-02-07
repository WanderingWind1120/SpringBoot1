package com.example.practice.services;

import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.Patch;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {

    IStorageService();

    public String storeFile(MultipartFile file);

    public Stream<Path> loadAll();
    public byte[] readFileContent(String fileName);
    public void deleteAllFiles();
}
