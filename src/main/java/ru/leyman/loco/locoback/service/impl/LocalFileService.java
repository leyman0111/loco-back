package ru.leyman.loco.locoback.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.leyman.loco.locoback.service.FileService;
import ru.leyman.loco.locoback.utils.FilenameUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
public class LocalFileService implements FileService {
    private static final String STORE_PATH = "../store/%s.%s";

    @Override
    public String upload(MultipartFile file) {
        var ext = FilenameUtil.getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Path.of(getFilename(ext));
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error on saving file: " + e.getMessage());
        }
    }

    @Override
    public Resource download(String filename) throws FileNotFoundException {
        Path filePath = Path.of(filename);
        try {
            return new ByteArrayResource(Files.readAllBytes(filePath));
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            log.error("Can't download file {}, caused of {}", filename, e.getMessage());
            return null;
        }
    }

    private String getFilename(String extension) {
        return String.format(STORE_PATH, UUID.randomUUID(),
                Objects.requireNonNullElse(extension, "txt"));
    }

}
