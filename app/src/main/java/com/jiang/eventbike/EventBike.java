package com.jiang.eventbike;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;

/**
 * Created by knowing on 2017/11/13.
 */

public class EventBike {

    private static volatile EventBike defaultInstance;

    private Map<Object, ArrayList<Method>> registTable;

    public static EventBike getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBike.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBike();
                }
            }
        }

        return defaultInstance;
    }

    private EventBike() {
        registTable = new HashMap<>();
    }

    /**
     * @param subscriber
     */

    public void register(Object subscriber) {
        Class clazz = subscriber.getClass();
        Method[] methods = clazz.getMethods();

        ArrayList<Method> subjects;

        if ((subjects = registTable.get(clazz)) == null) {
            subjects = new ArrayList<>();
            registTable.put(subscriber, subjects);
        }

        for (Method method : methods) {
            if (method.getAnnotation(Subscribe.class) != null) {
                subjects.add(method);
            }
        }

    }

    public boolean isRegistered(Object subscriber) {
        if (registTable.containsKey(subscriber)) {
            return true;
        }

        return false;

    }

    public void unRegister(Object subscriber) {
        Class clazz = subscriber.getClass();
        Method[] methods = clazz.getMethods();

        ArrayList<Method> subjects;

        if ((subjects = registTable.get(clazz)) != null) {
            registTable.remove(subscriber);
        }
    }

    public void post(Object event) {

        for (Map.Entry<Object, ArrayList<Method>> entry : registTable.entrySet()) {
            Object subscriber = entry.getKey();

            for (Method method : entry.getValue()) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    if (parameterTypes[0] == event.getClass()) {
                        try {
                            method.invoke(subscriber, event);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }
}
