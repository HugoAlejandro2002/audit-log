package org.hugo.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hugo.common.errors.ApiError;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    public boolean success;
    public T data;
    public ApiError error;
    public Meta meta;

    public static <T> ApiResponse<T> ok(T data, String path) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = true;
        r.data = data;
        r.meta = Meta.of(path);
        return r;
    }

    public static ApiResponse<Void> ok(String path) {
        return ok(null, path);
    }

    public static ApiResponse<Void> error(ApiError error, String path) {
        ApiResponse<Void> r = new ApiResponse<>();
        r.success = false;
        r.error = error;
        r.meta = Meta.of(path);
        return r;
    }
}