package com.misfits.autopilot.cb_api_meta_data;

import com.chargebee.models.enums.EntityType;
import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONObject;
import com.chargebee.v2.internal.Request;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import springfox.documentation.spring.web.json.Json;
import sun.reflect.Reflection;
import sun.tools.java.ClassPath;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.*;

public class ApiMetaGenerator {


    public static void generate() throws Exception {

        Map<String, Class> entityClasses = getEntityClasses();

        for (String className : entityClasses.keySet()) {
            Class entityClass = entityClasses.get(className);
            createEntityMetaFile(className, entityClass);
        }


    }

    private static void createEntityMetaFile(String entityName, Class c) throws Exception {

        File file = new File(entityName);

        JSONObject jsonObject = new JSONObject();

        JSONArray actions = new JSONArray();
        Class[] declaredClasses = c.getClasses();
        for (Class clas : declaredClasses) {
            if (c.isAssignableFrom(Request.class)) {
                JSONObject action = new JSONObject();
                action.put("action_name", entityName + "." + (c.getEnclosingClass() != null ? c.getEnclosingClass().getName() : c.getName()));
                JSONArray args = new JSONArray();
                Method[] methods = c.getMethods();
                for (Method m : methods) {
                    if (m.getParameterCount() > 0 && m.getParameterCount() < 3) {
                        JSONObject arg = new JSONObject();
                        arg.put("method_name", m.getName());
                        arg.put("reg", m.getName().toLowerCase().contains("opt"));
                        arg.put("is_multi", m.getParameterCount() > 1);
                        TypeVariable t = m.getParameterCount() > 1 ? m.getTypeParameters()[1] : m.getTypeParameters()[0];
                        arg.put("type", t.getName());
                        if (t.getClass() != null && t.getClass().isEnum()) {
                            Method values = t.getClass().getDeclaredMethod("values");
                            Object obj = values.invoke(null);
                            arg.put("possible_values", new JSONArray((Object[]) obj));
                        }
                        args.put(arg);
                    }

                }

                action.put("arguments", args);
                actions.put(action);
            }

        }

        if (actions.length() > 0) {
            jsonObject.put("actions", actions);
            System.out.println("printing meta file");
            System.out.println(jsonObject);
        }


    }


    private static Map<String, Class> getEntityClasses() {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.chargebee.v2.models"))));
        Set<Class<? extends Object>> allClasses =
                reflections.getSubTypesOf(Object.class);

        Map<String, Class> classMap = new HashMap<>();

        for (Class c : allClasses) {
            Class<?> enclosingClass = c.getEnclosingClass();
            if (enclosingClass != null) {
                classMap.put(enclosingClass.getSimpleName().toLowerCase(), c);
            } else {
                classMap.put(c.getSimpleName().toLowerCase(), c);
            }

        }

        Map<String, Class> entityClasses = new HashMap<>();
        List<EntityType> entities = Arrays.asList(EntityType.values());
        for (EntityType entity : entities) {
            if (classMap.get(entity.name().toLowerCase()) != null) {
                entityClasses.put(entity.name(), classMap.get(entity.name().toLowerCase()));
            }
        }

        return entityClasses;
    }

    public static void main(String[] args) throws Exception {
        generate();
    }


}
