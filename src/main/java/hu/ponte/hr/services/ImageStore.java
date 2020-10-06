package hu.ponte.hr.services;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.mapper.ImageMapper;
import hu.ponte.hr.model.Image;
import hu.ponte.hr.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageStore {

    private final SignService signService;
    private final ImageRepository repository;
    private final ImageMapper mapper;

    public void store(MultipartFile file) throws IOException {
        validateFile(file);

        String signResult = signService.sign(file.getBytes());

        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .size(file.getSize())
                .data(file.getBytes())
                .digitalSign(signResult)
                .build();

        repository.save(image);
    }

    public List<ImageMeta> listImages() {
        return mapper.toDtos(repository.findAll());
    }

    public void getImagePreview(String id, HttpServletResponse response) throws IOException {
        Image image = repository.getOne(id);

        response.setContentType(image.getMimeType());
        response.setContentLength((int) image.getSize());

        ByteArrayInputStream in = new ByteArrayInputStream(image.getData());
        OutputStream out = response.getOutputStream();

        byte[] buf = new byte[1024];
        int count;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.close();
        in.close();
    }

    private void validateFile(MultipartFile file) {
        if (file.getContentType() == null || !file.getContentType().startsWith("image")) {
            throw new RuntimeException("Wrong image format.");
        }
        if (file.getSize() > 2*1024*1024) {
            throw new RuntimeException("File size too large! Maximum allowed size is 2MB.");
        }
    }

}
