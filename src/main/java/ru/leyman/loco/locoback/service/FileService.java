package ru.leyman.loco.locoback.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileService {

    Path create(String filename);

    Path upload(MultipartFile file, String filename);

    Resource download(String filename);

    void delete(String filename);

}
