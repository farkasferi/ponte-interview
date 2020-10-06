package hu.ponte.hr.controller.upload;

import hu.ponte.hr.services.ImageStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequestMapping("api/file")
@RequiredArgsConstructor
public class UploadController
{

    private final ImageStore imageStore;

    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam("file") MultipartFile file) {
        try {
            imageStore.store(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Server error! Failed to store image.");
        }

        return "ok";
    }
}
