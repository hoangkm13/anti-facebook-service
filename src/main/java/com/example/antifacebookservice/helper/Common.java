package com.example.antifacebookservice.helper;

import com.example.antifacebookservice.constant.ResponseCode;
import com.example.antifacebookservice.exception.CustomException;

import java.util.UUID;

public class Common {

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static void checkValidIndexCount(Integer count, Integer index, int size) throws CustomException {
        if(index != null && count != null) {
            if (count < index) {
                throw new CustomException(ResponseCode.INDEX_CANNOT_BIGGER_THAN_COUNT);
            }

            if (count > size) {
                throw new CustomException(ResponseCode.COUNT_CANNOT_BIGGER_THAN_RESULT_SIZE);
            }

            if (count + index > size) {
                throw new CustomException(ResponseCode.INDEX_CANNOT_BIGGER_THAN_COUNT);
            }
        }
    }
}
