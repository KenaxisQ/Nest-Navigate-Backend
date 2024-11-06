package com.arista.nestnavigator.user.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private List<String> errors;
    private String requestId;  // For tracking requests
    private MetaData metadata;

    @Builder.Default
    private boolean success = true;

}