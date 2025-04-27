package com.dm4nk.recorder.mappers;

import com.dm4nk.recorder.configuration.MapStructConfig;
import com.dm4nk.recorder.model.ActionDTO;
import com.dm4nk.recorder.model.ActionResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface ActionsMapper {
    ActionResponse toActionResponse(ActionDTO from);

    List<ActionResponse> toActionResponses(List<ActionDTO> from);
}
