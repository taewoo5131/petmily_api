package com.petmily.api.common;

import java.util.Map;
import java.util.Optional;

public class PetmilyUtil {
    /**
     * service layer parameter null check
     * paramMap : 넘어온 parameter Map
     * keyArr : 필수로 존재해야하는 key 값
     */
    public static boolean parameterNullCheck(Map<String, Object> paramMap , String[] keyArr) {
        try {
            for (String key : keyArr) {
                Optional.of(paramMap.get(key)).toString();
            }
            return true;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
