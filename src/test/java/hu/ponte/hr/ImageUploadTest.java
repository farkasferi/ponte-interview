package hu.ponte.hr;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.controller.ImagesController;
import hu.ponte.hr.controller.upload.UploadController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ImageUploadTest {

    @Autowired
    private UploadController uploadController;
    @Autowired
    private ImagesController imagesController;

    @Test
    public void testImageCanBeListedAfterUpload() throws IOException {
        Path path = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "images/cat.jpg").toPath();
        String name = "cat.jpg";
        String originalFileName = "cat.jpg";
        String contentType = "image/jpeg";
        byte[] content = Files.readAllBytes(path);

        MultipartFile multipartFile = new MockMultipartFile(name,
                originalFileName, contentType, content);

        uploadController.handleFormUpload(multipartFile);

        List<ImageMeta> images = imagesController.listImages();

        assertThat(images.size(), is(equalTo(1)));

        ImageMeta image = images.get(0);
        assertThat(image.getName(), is(equalTo(name)));
        assertThat(image.getMimeType(), is(equalTo(contentType)));
        assertThat(image.getDigitalSign(), is(equalTo("XYZ+wXKNd3Hpnjxy4vIbBQVD7q7i0t0r9tzpmf1KmyZAEUvpfV8AKQlL7us66rvd6eBzFlSaq5HGVZX2DYTxX1C5fJlh3T3QkVn2zKOfPHDWWItdXkrccCHVR5HFrpGuLGk7j7XKORIIM+DwZKqymHYzehRvDpqCGgZ2L1Q6C6wjuV4drdOTHps63XW6RHNsU18wHydqetJT6ovh0a8Zul9yvAyZeE4HW7cPOkFCgll5EZYZz2iH5Sw1NBNhDNwN2KOxrM4BXNUkz9TMeekjqdOyyWvCqVmr5EgssJe7FAwcYEzznZV96LDkiYQdnBTO8jjN25wlnINvPrgx9dN/Xg==")));
    }
}
