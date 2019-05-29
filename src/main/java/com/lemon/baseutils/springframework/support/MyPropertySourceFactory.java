package com.lemon.baseutils.springframework.support;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * description: 重写配置文件工厂类，同时支持properties和yml
 *
 * @author lemon
 * @date 2019-05-29 14:08:06 创建
 */
public class MyPropertySourceFactory extends DefaultPropertySourceFactory {

    private final static String YML = ".yml";

    private final static String YAML = ".yaml";

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
        if (resource == null){
            return super.createPropertySource(name, resource);
        }
        if (name != null && (name.endsWith(YML) || name.endsWith(YAML))) {
            List<PropertySource<?>> sources = new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
            return sources.get(0);
        } else {
            return super.createPropertySource(name, resource);
        }
    }
}
