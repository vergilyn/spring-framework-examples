package com.vergilyn.examples.spel.properties;

import java.lang.reflect.Field;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.testng.collections.Maps;

/**
 * @author vergilyn
 * @date 2020-04-20
 */
public class DefaultToString {

    @Override
    public String toString(){
        String clazz = this.getClass().getSimpleName();

        Field[] fields = this.getClass().getDeclaredFields();
        Map<String, Map<String, Object>> rs = Maps.newHashMap();
        Map<String, Object> f;
        for (Field field : fields){
            try {
                field.setAccessible(true);

                f = Maps.newHashMap();
                f.put("class", field.getType().getSimpleName() + ".class");
                f.put("value", field.get(this));

                rs.put(field.getName(), f);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        return clazz + JSON.toJSONString(rs, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
    }
}
