package hu.ponte.hr.mapper;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.model.Image;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageMeta toDto(Image image);

    List<ImageMeta> toDtos(List<Image> images);
}
