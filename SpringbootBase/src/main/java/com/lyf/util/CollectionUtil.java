package com.lyf.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {

    public static <T> List<T> copyIterator(Iterable<T> iter) {
        List<T> copy = new ArrayList<T>();
        iter.forEach(item->copy.add(item));
        return copy;
    }
}
