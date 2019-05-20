package com.lemon.baseutils.util;

import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * description: 获取资源
 *
 * @author lemon
 * @date 2019-04-10 18:17:06 创建
 */
public class ResourceUtils {

    private final static String YML = "yml";

    private final static String PROPERTIES = "properties";

    /**
     * description: 获取资源内容
     * @author lemon
     * @date 2019/4/10 18:29  创建
     * @param path 路径
     * @param fileName 文件名
     * @return Map<String, OriginTrackedValue>
     */
    public static Map<String, OriginTrackedValue> getResource4Map(String path, String fileName) throws Exception {
        String filenameExtension = StringUtils.getFilenameExtension(fileName);
        if(null == filenameExtension){
            throw new IllegalArgumentException("文件名格式错误");
        }
        PropertySourceLoader loader = null;
        if(filenameExtension.equals(YML)){
            loader = new YamlPropertySourceLoader();
        }
        if(filenameExtension.equals(PROPERTIES)){
            loader = new PropertiesPropertySourceLoader();
        }
        if(null == loader){
            throw new IllegalArgumentException("文件拓展名格式错误");
        }
        Resource resource = new DefaultResourceLoader().getResource("classpath:" + path + File.separator + fileName);
        List<PropertySource<?>> loaded = loader.load(fileName, resource);
        if(CollectionUtils.isEmpty(loaded)){
            throw new IllegalArgumentException(String.format("找不到对应%s文件", fileName));
        }
        return (Map<String, OriginTrackedValue>) loaded.get(0).getSource();
    }
}
