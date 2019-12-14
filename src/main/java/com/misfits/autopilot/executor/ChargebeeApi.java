package com.misfits.autopilot.executor;

import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONObject;
import com.chargebee.v2.Environment;
import com.chargebee.v2.Result;
import com.chargebee.v2.models.Invoice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ChargebeeApi {


    public void postApi(String entityName, JSONObject action) throws Exception {

        if (!action.has("action_name")) {
            return;
        }

        JSONObject entityMeta = getEntityMeta(entityName);
        String className = entityMeta.getString("class_name");
        JSONArray actionsMeta = entityMeta.getJSONArray("actions");


        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> inputArgumentMap = getInputParamMap(action);
        Map<Integer,Map<String, String>> multiInputArgumentMap = getMultiInputParamMap(action);
        JSONObject actionMeta = getActionMeta(actionsMeta, action.getString("action_name"));
        if(actionMeta == null){
            return ;
        }
        sendRequest(inputArgumentMap,multiInputArgumentMap, actionMeta, className);


//        Invoice.charge();?

//com.chargebee.v2.models.i
        Result result = Invoice.charge()
                .subscriptionId("active")
                .amount(1000)
                .description("Support Charge")
                .request();

        Class c = Class.forName("com.chargebee.v2.models.Invoice");

        Method charge = c.getDeclaredMethod("charge", new Class[]{});
//        Object obj = c.newInstance();
        Object obj = charge.invoke(null);
        c = obj.getClass();
        charge = c.getDeclaredMethod("subscriptionId", new Class[]{String.class});
        obj = charge.invoke(obj, "active");
        charge = c.getDeclaredMethod("amount", new Class[]{Integer.class});
        obj = charge.invoke(obj, 1000);
        charge = c.getDeclaredMethod("description", new Class[]{String.class});
        obj = charge.invoke(obj, "no desc");
        charge = c.getMethod("request", new Class[]{});
        charge.invoke(obj);


    }

    private JSONObject getActionMeta(JSONArray actions, String actionName) throws Exception {
        for (int i = 0; i < actions.length(); i++) {
            JSONObject actionMeta = actions.getJSONObject(i);
            if (actionMeta.has("action_name") && actionMeta.getString("action_name").toLowerCase().equals(actionName.toLowerCase())) {
                return actionMeta;
            }
        }
        return null;
    }

    private void sendRequest(Map<String, String> inputArgumentMap,Map<Integer,Map<String, String>> multiInputArgumentMap, JSONObject actionMeta, String className) throws Exception {

        System.setProperty("com.chargebee.api.domain.suffix", "localcb.in:8080");
        System.setProperty("com.chargebee.api.protocol", "http");

        Environment.configure("mannar-test","test___dev__vMVROChoJ0hkCFSX4zzlEQ3EOffCcdiTB");

        Class c = Class.forName(className);
        Object obj = invokeStaticMethod(c, actionMeta.getString("static_method_name"), getParamType(actionMeta.getString("type")), inputArgumentMap.get(actionMeta.getString("static_param_name")));

        JSONArray args = actionMeta.getJSONArray("arguments");
        for (int i = 0; i < args.length(); i++) {
            JSONObject arg = args.getJSONObject(i);
            if (arg.optBoolean("is_multi")) {

            } else {
                obj = invokeMethod(obj, c, arg.getString("method_name"), getParamType(arg.getString("type")), inputArgumentMap.get(arg.getString("name")));
            }
        }

        Method m = c.getMethod("request", new Class[]{});
        m.invoke(obj);



    }


    private Object invokeStaticMethod(Class c, String methodName, Class<?> parameterType, Object val) throws Exception {
        Method m = c.getDeclaredMethod(methodName, parameterType);
        return m.invoke(val);
    }


    private Object invokeMethod(Object obj, Class c, String paramName, Class<?> parameterType, Object val) throws Exception {
        Method m = c.getDeclaredMethod(paramName, parameterType);
        return m.invoke(obj, val);
    }

    private Object invokeMultiInputMethod(Object obj, Class c, String paramName, Class<?> parameterType, Object val) throws Exception {
        Method m = c.getDeclaredMethod(paramName, parameterType);
        return m.invoke(obj, val);
    }

    private Map<String, String> getInputParamMap(JSONObject action) throws Exception {

        JSONArray arr = action.getJSONArray("attributes");

        Map<String, String> inputParamMap = new HashMap<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            inputParamMap.put(obj.getString("name"), obj.getString("value"));
        }
        return inputParamMap;
    }


    private Map<Integer, Map<String, String>> getMultiInputParamMap(JSONObject action) throws Exception {
        Map<Integer, Map<String, String>> inputParamMap = new HashMap<>();
        if(action.has("multiAttribute")) {
            JSONArray arr = action.optJSONArray("multiAttribute");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.has("fields")) {
                    JSONArray fields = obj.getJSONArray("fields");
                    for (int j = 0; j < fields.length(); j++) {
                        JSONObject field = fields.getJSONObject(j);
                        if (inputParamMap.containsKey(i)) {
                            inputParamMap.get(i).put(field.getString("name"), field.getString("value"));
                        } else {
                            Map<String, String> map = new HashMap<>();
                            map.put(field.getString("name"), field.getString("value"));
                            inputParamMap.put(i, map);
                        }
                    }
                }
            }
        }
        return inputParamMap;
    }


    private JSONObject getEntityMeta(String entityName) throws Exception {
        File file = new File("./meta/" + entityName + ".json");
        FileReader fileReader = new FileReader(file);
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(fileReader);
        JSONObject jsonObject = new JSONObject(jsonElement.getAsJsonObject().toString());
        return jsonObject;
    }


    private Class<?> getParamType(String type) {
        switch (type.toLowerCase()) {
            case "string":
            case "enum":
                return String.class;
            case "integer":
                return Integer.class;
            case "long":
                return Long.class;
            case "boolean":
                return Boolean.class;

        }
        return null;
    }

    public static void main(String[] args) throws Exception{
        String obj ="{\n" +
                "\t\"action_name\": \"Invoice.charge\",\n" +
                "\t\"attributes\": [{\n" +
                "\t\t\t\"name\": \"amount\",\n" +
                "\t\t\t\"value\": 100\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"description\",\n" +
                "\t\t\t\"value\": \"string\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";


        ChargebeeApi sd = new ChargebeeApi();
        sd.postApi("Invoice",new JSONObject(obj));
    }
}
