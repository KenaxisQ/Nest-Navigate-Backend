package com.arista.nestnavigator.user.utils;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ErrorResponse extends ApiResponse<Object> {
    private String errorCode;
    private String debugMessage;
    private String supportReference;
}
