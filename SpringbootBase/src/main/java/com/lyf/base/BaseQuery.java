package com.lyf.base;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class BaseQuery implements Serializable {
    private static final long serialVersionUID = 8824817665030866472L;
    private Integer pageNum=1;
    private Integer pageSize=10;
}
