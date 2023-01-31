package com.cnh.rvcalculatorprocessor.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class RequestQueueDTO {

    private String json;
    private String sessionId;
}
