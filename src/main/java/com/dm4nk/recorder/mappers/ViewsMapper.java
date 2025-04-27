package com.dm4nk.recorder.mappers;

import com.dm4nk.recorder.configuration.MapStructConfig;
import com.dm4nk.recorder.model.ViewDTO;
import com.dm4nk.recorder.model.ViewResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface ViewsMapper {

    ViewResponse toViewResponse(ViewDTO from);

    List<ViewResponse> toViewResponses(List<ViewDTO> from);
}
