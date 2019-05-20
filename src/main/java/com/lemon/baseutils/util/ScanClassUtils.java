package com.lemon.baseutils.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 扫描指定包路径下的类
 *
 * @author lemon
 * @date 2019-04-26 10:47:06 创建
 */
public class ScanClassUtils {

    private static final String RESOURCE_PATTERN = "/**/*.class";

    /**
     * description: 扫描指定包下的spring注解类
     * @author lemon
     * @date 2019/4/26 10:37  创建
     * @param
     * @return
     */
    public static List<Object> scanPackageClass(String basePackage, List<?> annotationList){
        List<Object> result = new ArrayList<>();
        if (StringUtils.hasText(basePackage)) {
            try {
                ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage)
                        + RESOURCE_PATTERN;
                Resource[] resources = resourcePatternResolver.getResources(pattern);
                MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader reader = readerFactory.getMetadataReader(resource);
                        String className = reader.getClassMetadata().getClassName();
                        Class<?> clazz = Class.forName(className);

                        annotationList.stream().forEach(a -> {
                            Object annotation = clazz.getAnnotation((Class)a);
                            if(null != annotation){
                                result.add(clazz);
                            }
                        });
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
