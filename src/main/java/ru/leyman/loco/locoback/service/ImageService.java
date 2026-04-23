package ru.leyman.loco.locoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import ru.leyman.loco.locoback.domain.enums.ContentSize;
import ru.leyman.loco.locoback.utils.FilenameUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final FileService fileService;

    public String resize(String originFilename, Path originPath, ContentSize size) {
        var filename = FilenameUtils.replaceFilename(Objects.requireNonNull(originFilename),
                UUID.randomUUID().toString());
        var path = fileService.create(filename);

        try {
            var bufferedImage = ImageIO.read(originPath.toFile());
            Thumbnails.of(bufferedImage)
                    .size(size.getLength(), size.getWidth())
                    .toFile(path.toFile());
            return path.toString();
        } catch (IOException e) {
            log.error("Can't resize {} to {}", originFilename, size);
            return null;
        }
    }

    public byte[] resize(byte[] origin, ContentSize size) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(origin);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Thumbnails.of(in)
                    .size(size.getLength(), size.getWidth())
                    .toOutputStream(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Can't resize to {}", size);
            return null;
        }
    }

}
