package com.misfits.autopilot.cb_api_meta_data;

import com.chargebee.models.enums.EntityType;
import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONObject;
import com.sun.deploy.util.StringUtils;
import com.chargebee.v2.internal.Request;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.*;

import static com.misfits.autopilot.Constants.ACTION_META_FILE_DIR;

public class ApiMetaGenerator {


    public static void generate() throws Exception {
        Map<String, Class> entityClasses = getEntityClasses();
        for (String className : entityClasses.keySet()) {
            Class entityClass = entityClasses.get(className);
            createEntityMetaFile(className, entityClass);
        }
    }

    private static void createEntityMetaFile(String entityName, Class c) throws Exception {

        File file = new File(ACTION_META_FILE_DIR + entityName + ".json");
        JSONObject jsonObject = new JSONObject();

        JSONArray actions = new JSONArray();
        Class[] entityInnerClasses = c.getEnclosingClass().getDeclaredClasses();
        for (Class entityInnerClass : entityInnerClasses) {
            if (Request.class.isAssignableFrom(entityInnerClass)) {
                actions.put(setActionParams(entityName, entityInnerClass));
            }
        }

        if (actions.length() > 0) {
            jsonObject.put("actions", actions);
            Class<?> enclosingClass = c.getEnclosingClass();
            if (enclosingClass != null) {
                jsonObject.put("class_name",enclosingClass.getName());
            } else {
                jsonObject.put("class_name",c.getName());
            }
            System.out.println("printing meta file");
            System.out.println(jsonObject);
        }

        try(FileWriter fw = new FileWriter(file)) {
            fw.write(jsonObject.toString(4));
            fw.flush();
        }
    }

    private static JSONObject setActionParams(String entityName, Class entityInnerClass) throws Exception {
        JSONObject action = new JSONObject();
        Method[] method = entityInnerClass.getEnclosingClass().getDeclaredMethods();
        String actionName = "";
        String staticParamName = null;
        Class staticParamType = null;
        ClassLoader.getSystemClassLoader().loadClass(entityInnerClass.getEnclosingClass().getName());
        for(Method me : method) {
            if(entityInnerClass.isAssignableFrom(me.getReturnType())) {
                actionName = me.getName();
                if(me.getParameterCount() ==1 ){
                    staticParamName = me.getParameters()[0].getName();
                    staticParamType = me.getParameterTypes()[0];
                }

            }
        }
        action.put("action_name", entityName + "." + actionName);
        action.put("static_method_name", actionName);
        if(staticParamName != null){
            action.put("static_param_name", actionName);
            if (staticParamType.isEnum()) {
                Method values = staticParamType.getDeclaredMethod("values");
                Object obj = values.invoke(null);
                action.put("type", "Enum");
                action.put("possible_values", new JSONArray((Object[]) obj));
            } else {
                action.put("type", staticParamType.getSimpleName());
            }
        }
        JSONArray args = new JSONArray();
        Method[] methods = entityInnerClass.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getParameterCount() > 0 && m.getParameterCount() < 3) {
                JSONObject arg = new JSONObject();
                arg.put("name", m.getName());
                arg.put("method_name", m.getName());
                arg.put("req", m.getName().toLowerCase().contains("opt"));
                arg.put("is_multi", m.getParameterCount() > 1);
                Class t = (m.getParameterCount() > 1) ? m.getParameterTypes()[1] : m.getParameterTypes()[0];
                if (t.isEnum()) {
                    Method values = t.getDeclaredMethod("values");
                    Object obj = values.invoke(null);
                    arg.put("type", "Enum");
                    arg.put("possible_values", new JSONArray((Object[]) obj));
                } else {
                    arg.put("type", t.getSimpleName());
                }
                args.put(arg);
            }
        }

        action.put("arguments", args);
        return action;
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
            String entityName = entity.name().toLowerCase().replaceAll("_", "");
            if (classMap.get(entityName) != null) {
                entityClasses.put(classMap.get(entityName).getEnclosingClass().getSimpleName(), classMap.get(entityName));
            }
        }

        return entityClasses;
    }

    public static void main(String[] args) throws Exception {
        generate();
    }

    public static String toCamelCase(String init) {
        if (init == null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(Character.toUpperCase(word.charAt(0)));
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == init.length()))
                ret.append(" ");
        }
        return ret.toString();
    }


}
