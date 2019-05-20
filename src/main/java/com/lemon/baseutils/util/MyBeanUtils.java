package com.lemon.baseutils.util;

import org.springframework.beans.BeanUtils;

/**
 * description: 类相关操作
 *
 * @author lemon
 * @date 2019-04-25 17:41:06 创建
 */
public class MyBeanUtils {

    /**
     * description: 拷贝属性
     * @author lemon
     * @date 2019/4/25 17:43  创建
     * @param
     * @return
     */
    public static <S, T> T to(S source, Class<T> targetClass) {
        T target = BeanUtils.instantiateClass(targetClass);
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
