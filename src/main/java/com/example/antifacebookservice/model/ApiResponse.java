package com.example.antifacebookservice.model;

import com.example.antifacebookservice.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * Response content
     */
    T result;

    /**
     * Error code. Null if responseCode indicates success.
     */
    Integer errorCode;

    /**
     * "OK" if response is successful. Otherwise contains error details.
     */
    Object message;

    /**
     * 200 on success, 400 (or other codes) on error.
     */
    Integer code;

    /**
     * Create a successful response
     *
     * @param result Response result
     * @param <T>    Type of result
     * @return Successful response object
     */
    public static <T> ApiResponse<T> successWithResult(T result) {
        return new ApiResponse<>(result, null, ResponseCode.OK.getMessage(), ResponseCode.OK.getCode());
    }

    public static <T> ApiResponse<T> successWithResult(T result, String message) {
        return new ApiResponse<>(result, null, message, ResponseCode.OK.getCode());
    }

    /**
     * Create a failed response
     *
     * @param errorCode Error code
     * @param message   Error message
     * @param <T>       Type of result
     * @return Failed response object
     */

    public static <T> ApiResponse<T> failureWithCode(Integer errorCode, String message) {
        return new ApiResponse<>(null, errorCode, message, null);
    }

    public static <T> ApiResponse<T> failureWithCode(Integer errorCode, String message, T result) {
        return new ApiResponse<>(result, errorCode, message, HttpStatus.BAD_REQUEST.value());
    }

    public static <T> ApiResponse<T> failureWithCode(Integer errorCode, Object message, T result, HttpStatus httpStatus) {
        return new ApiResponse<>(result, errorCode, message, httpStatus.value());
    }
}
