package com.lyf.util;

import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class CheckNullUtil {
    public static String valid(Object target, String... props) {
        for (int i = 0; i < props.length; i++) {
            String prop = props[i];
            try {
                PropertyDescriptor proDescriptor = new PropertyDescriptor(prop, target.getClass());
                Method method = proDescriptor.getReadMethod();
                Object invoke = method.invoke(target);
                if (null == invoke || StringUtils.isEmpty(invoke.toString())) {
                    return prop;
                }
            } catch (Exception e) {
                return prop;
            }
        }
        return null;
    }
}
