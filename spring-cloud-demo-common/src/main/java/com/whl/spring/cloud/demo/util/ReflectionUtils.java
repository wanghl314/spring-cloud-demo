package com.whl.spring.cloud.demo.util;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtils {
    private static final Map<String, Set<Class<?>>> CACHE = new ConcurrentHashMap<String, Set<Class<?>>>();

    @SuppressWarnings("unchecked")
    public static Set<Class<?>>  getSubTypesOf(String className) throws ClassNotFoundException {
        Set<Class<?>> classSet;

        if (CACHE.containsKey(className)) {
            classSet = CACHE.get(className);
        } else {
            Class<?> service = Class.forName(className);
            Reflections reflections = new Reflections(service.getPackage().getName());
            classSet = service.isInterface() ? (Set<Class<?>>) reflections.getSubTypesOf(service) : null;
            CACHE.put(className, classSet);
        }
        return classSet;
    }

    public static Object invoke(Method method, Object[] args, Class<?> target) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        if (!method.isAccessible()) {
            makeAccessible(method);
        }
        if (isStatic(method)) {
            return method.invoke(null, args);
        }
        return method.invoke(target.getDeclaredConstructor().newInstance(), args);
    }

    public static void makeAccessible(Method method) {
        boolean isNotPublic = !Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers());

        if (isNotPublic && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

}
