package com.cjh.common;

import java.lang.reflect.Method;

/**
 * @author Created by cjh
 * @date 2017/10/18 下午4:07
 * @description
 */
public class Params {
    public Method method;

    public Params(Method method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Params params = (Params) o;

        return method.equals(params.method);
    }

    @Override
    public int hashCode() {
//            return method.hashCode();
        int result = 17;
        final int prime = 31;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        return result;
    }
}
