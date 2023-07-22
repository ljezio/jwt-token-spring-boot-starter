package io.github.ljezio.jwttoken.utils;

import com.auth0.jwt.interfaces.Claim;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ObjectMapUtil {

    public static Map<String, Object> objectToMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException ignored) {
            }
        }
        return map;
    }

    public static <T> T claimMapToObject(Map<String, Claim> claimMap, Class<T> clazz) {
        T object;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())
                    || !claimMap.containsKey(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(object, claimMap.get(field.getName()).as(field.getType()));
            } catch (IllegalAccessException ignored) {
            }
        }
        return object;
    }
}
