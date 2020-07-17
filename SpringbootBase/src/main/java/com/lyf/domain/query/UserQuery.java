package com.lyf.domain.query;

import com.lyf.base.BaseQuery;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class UserQuery extends BaseQuery {
    private String name;
    private Integer age;
}
