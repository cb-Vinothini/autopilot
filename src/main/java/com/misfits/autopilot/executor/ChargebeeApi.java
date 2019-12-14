package com.misfits.autopilot.executor;

import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONObject;
import com.chargebee.v2.Environment;
import com.chargebee.v2.Result;
import com.chargebee.v2.models.Invoice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.misfits.autopilot.models.entity.Action;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChargebeeApi {


    public void postApi(String entityName, Action action, JSONObject webhookEvent) throws Exception {

        JSONObject entityMeta = getEntityMeta(entityName);
        String className = entityMeta.getString("class_name");
        JSONArray actionsMeta = entityMeta.getJSONArray("actions");


        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> inputArgumentMap = getInputParamMap(action);
//        Map<String, List<MultiInput>> multiInputArgumentMap = getMultiInputParamMap(action);
        JSONObject actionMeta = getActionMeta(actionsMeta, action.getName());
        if(actionMeta == null){
            return ;
        }
        sendRequest(inputArgumentMap,null, actionMeta, className);


//        Invoice.charge();?

//com.chargebee.v2.models.i
//        Result result = Invoice.charge()
//                .subscriptionId("active")
//                .amount(1000)
//                .description("Support Charge")
//                .request();



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

    private void sendRequest(Map<String, String> inputArgumentMap, Map<String, List<MultiInput>> multiInputArgumentMap, JSONObject actionMeta, String className) throws Exception {

        System.setProperty("com.chargebee.api.domain.suffix", "localcb.in:8080");
        System.setProperty("com.chargebee.api.protocol", "http");

        Environment.configure("mannar-test","test___dev__vMVROChoJ0hkCFSX4zzlEQ3EOffCcdiTB");

        Class c = Class.forName(className);


        Object obj = invokeStaticMethod(c, actionMeta.getString("static_method_name"), getParamType(actionMeta.optString("type")), inputArgumentMap.get(actionMeta.optString("static_param_name")));

        c = obj.getClass();
        JSONArray args = actionMeta.getJSONArray("arguments");
        for (int i = 0; i < args.length(); i++) {
            JSONObject arg = args.getJSONObject(i);
            if (arg.optBoolean("is_multi")) {
                if(multiInputArgumentMap.get(arg.getString("name")) != null) {
//                    multiInputArgumentMap.get(arg.getString("name"));
                    invokeMultiInputMethod(obj,c,arg.getString("method_name"),getParamType(arg.getString("type")),multiInputArgumentMap.get(arg.getString("name")));
                }
            } else {
                if(inputArgumentMap.get(arg.getString("name")) != null) {
                    obj = invokeMethod(obj, c, arg.getString("method_name"), getParamType(arg.getString("type")), inputArgumentMap.get(arg.getString("name")));
                }
            }
        }

        Method m = c.getMethod("request", new Class[]{});
        m.invoke(obj);



    }


    private Object invokeStaticMethod(Class c, String methodName, Class<?> parameterType, Object val) throws Exception {
        Method m;
        if(parameterType == null) {
            m = c.getDeclaredMethod(methodName,  new Class[]{} );
        } else {
            m = c.getDeclaredMethod(methodName, parameterType);
        }
//        Method m = c.getDeclaredMethod(methodName, parameterType == null ? new Class[]{} : parameterType);
        return m.invoke(val);
    }


    private Object invokeMethod(Object obj, Class c, String paramName, Class<?> parameterType, Object val) throws Exception {
        Method m = c.getDeclaredMethod(paramName, parameterType);
        return m.invoke(obj, getParamValue(parameterType,val));
    }

    private Object invokeMultiInputMethod(Object obj, Class c, String paramName, Class<?> parameterType,List<MultiInput> multiInputs) throws Exception {

        Method m;
        for(MultiInput multiInput : multiInputs){
            Class<?> params[] = new Class[2];
            params[0] = Integer.TYPE;
            params[1] =parameterType;
             m = c.getDeclaredMethod(paramName, params);
             Object objs[] = new Object[2];
            objs[0]= multiInput.index;
            objs[1]=getParamValue(parameterType,multiInput.value);
            obj = m.invoke(obj, objs);
        }

//        Method m = c.getDeclaredMethod(paramName, parameterType);

        return obj;
    }

    private Map<String, String> getInputParamMap(Action action) throws Exception {

        JSONArray arr = new JSONArray(action.getApiParameters());

        Map<String, String> inputParamMap = new HashMap<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            inputParamMap.put(obj.getString("name"), obj.getString("value"));
        }
        return inputParamMap;
    }


    private Map<String, List<MultiInput>> getMultiInputParamMap(JSONObject action) throws Exception {
        Map<String, List<MultiInput>> multiList = new HashMap<>();
        if(action.has("multiAttribute")) {
            JSONArray arr = action.optJSONArray("multiAttribute");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.has("fields")) {
                    JSONArray fields = obj.getJSONArray("fields");
                    for (int j = 0; j < fields.length(); j++) {
                        JSONObject field = fields.getJSONObject(j);

                        if (multiList.containsKey(field.getString("name"))) {

                            MultiInput multiInput = new MultiInput(multiList.get(field.getString("name")).size(),field.getString("name"),field.getString("value"));
                            multiList.get(field.getString("name")).add(multiInput);
                        } else {
                            MultiInput multiInput = new MultiInput(0,field.getString("name"),field.getString("value"));
                            List<MultiInput> list = new ArrayList<>();
                            list.add(multiInput);
                            multiList.put(field.getString("name"),list);
                        }
                    }
                }
            }
        }
        return multiList;
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
        if(type == null){
            return null;
        }

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

    private Object getParamValue(Class<?> parameterType, Object val) {
        if(val == null){
            return null;
        }

        String value = (String) val;

        if(parameterType.equals(String.class)) {
            return value;
        } else if(parameterType.equals(Integer.class)) {
            return Integer.valueOf(value);
        } else if(parameterType.equals(Long.class)) {
            return Long.valueOf(value);
        } else if(parameterType.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        }
        return null;
    }

    public void test() throws Exception {
        System.setProperty("com.chargebee.api.domain.suffix", "localcb.in:8080");
        System.setProperty("com.chargebee.api.protocol", "http");

        Environment.configure("mannar-test","test___dev__vMVROChoJ0hkCFSX4zzlEQ3EOffCcdiTB");
        Class c = Class.forName("com.chargebee.v2.models.Invoice");

        Method charge = c.getDeclaredMethod("charge", new Class[]{});
//        Object obj = c.newInstance();
        Object obj = charge.invoke(null);
        c = obj.getClass();
        charge = c.getDeclaredMethod("subscriptionId", String.class);
        obj = charge.invoke(obj, "active");
        charge = c.getDeclaredMethod("amount", Integer.class);
        obj = charge.invoke(obj, 1000);
        charge = c.getDeclaredMethod("description", String.class);
        obj = charge.invoke(obj, "no desc");
        charge = c.getMethod("request", null);
        charge.invoke(obj);

    }

    public void multiTest() throws Exception {
        System.setProperty("com.chargebee.api.domain.suffix", "localcb.in:8080");
        System.setProperty("com.chargebee.api.protocol", "http");

        Environment.configure("mannar-test","test___dev__vMVROChoJ0hkCFSX4zzlEQ3EOffCcdiTB");
        Class c = Class.forName("com.chargebee.v2.models.Invoice");

        Method charge = c.getDeclaredMethod("charge", new Class[]{});
//        Object obj = c.newInstance();
        Object obj = charge.invoke(null);
        c = obj.getClass();
        charge = c.getDeclaredMethod("subscriptionId", String.class);
        obj = charge.invoke(obj, "active");
        charge = c.getDeclaredMethod("amount", Integer.class);
        obj = charge.invoke(obj, 1000);
        charge = c.getDeclaredMethod("description", String.class);
        obj = charge.invoke(obj, "no desc");
        charge = c.getMethod("request", null);
        charge.invoke(obj);

    }

    public static void main(String[] args) throws Exception{
        String obj ="{\n" +
                "    \"actionName\": \"Invoice.charge\",\n" +
                "    \"attributes\": [\n" +
                "      {\n" +
                "        \"name\": \"amount\",\n" +
                "        \"value\": \"100\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"description\",\n" +
                "        \"value\": \"test\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"subscriptionId\",\n" +
                "        \"value\": \"active\"\n" +
                "      }\n" +
                "    ]\n" +
                "   \n" +
                "  }";

        String multiObj = "{\n" +
                "    \"actionName\": \"Invoice.create\",\n" +
                "    \"attributes\": [\n" +
                "      {\n" +
                "        \"name\": \"customerId\",\n" +
                "        \"value\": \"active\"\n" +
                "      },\n" +
                "      \n" +
                "      {\n" +
                "        \"name\": \"shippingAddressFirstName\",\n" +
                "        \"value\": \"Karthick\"\n" +
                "      },\n" +
                "      \n" +
                "      {\n" +
                "        \"name\": \"shippingAddressLastName\",\n" +
                "        \"value\": \"active\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"currencyCode\",\n" +
                "        \"value\": \"USD\"\n" +
                "      }\n" +
                "\n" +
                "    ],\n" +
                "\n" +
                "\"multiAttribute\": [\n" +
                "      {\n" +
                "        \"fields\": [\n" +
                "          {\n" +
                "            \"name\": \"chargeAmount\",\n" +
                "            \"value\": \"100\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"chargeDescription\",\n" +
                "            \"value\": \"test2\"\n" +
                "          }\n" +
                "        ]\n" +
                "      },{\n" +
                "        \"fields\": [\n" +
                "          {\n" +
                "            \"name\": \"chargeAmount\",\n" +
                "            \"value\": \"100\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"name\": \"chargeDescription\",\n" +
                "            \"value\": \"test2\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "   \n" +
                "  }";


        ChargebeeApi sd = new ChargebeeApi();
//        sd.test();
//        sd.postApi("Invoice",new JSONObject(multiObj));
    }

    private class MultiInput {

        int index;
        String name;
        String value;

        public MultiInput(int index, String name, String value) {
            this.index = index;
            this.name = name;
            this.value = value;
        }
    }
}
