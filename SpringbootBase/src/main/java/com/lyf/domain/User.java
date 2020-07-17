package com.lyf.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 438708631972799142L;
    private Integer id;
    private String name;
    private Integer age;
}