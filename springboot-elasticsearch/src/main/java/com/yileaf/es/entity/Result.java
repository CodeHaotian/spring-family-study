package com.yileaf.es.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回实体
 *
 * @author Haotian
 * @version 1.0.0
 * @date 2020/7/19 22:21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result<T> {
    private Integer code;
    private T data;
    private String message;
}