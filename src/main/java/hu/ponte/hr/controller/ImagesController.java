package hu.ponte.hr.controller;


import hu.ponte.hr.services.ImageStore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("api/images")
@RequiredArgsConstructor
public class ImagesController {

    private final ImageStore imageStore;

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
        return imageStore.listImages();
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            imageStore.getImagePreview(id, response);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Server error! Failed to load image preview.");
        }
    }

}
