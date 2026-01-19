package org.hugo.common.errors;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    public String code;
    public String message;
    public List<ApiErrorDetail> details;

    public static ApiError of(String code, String message) {
        ApiError e = new ApiError();
        e.code = code;
        e.message = message;
        return e;
    }

    public static ApiError of(String code, String message, List<ApiErrorDetail> details) {
        ApiError e = of(code, message);
        e.details = details;
        return e;
    }
}
