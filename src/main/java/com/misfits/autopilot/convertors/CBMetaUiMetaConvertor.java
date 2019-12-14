package com.misfits.autopilot.convertors;

import com.chargebee.models.enums.EntityType;
import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONException;
import com.chargebee.org.json.JSONObject;
import com.google.common.base.Strings;
import com.misfits.autopilot.Constants;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.misfits.autopilot.cb_api_meta_data.ApiMetaGenerator.toCamelCase;

public class CBMetaUiMetaConvertor {

    private static List<String> dataTypes = new ArrayList<>();

    public static JSONArray getCBMetaNconvert() throws Exception {
        JSONArray uiMetaJson = new JSONArray();

        for(EntityType entityType : EntityType.values()) {
            JSONObject cbActionObj = getCBActionMetaJson(entityType);
            JSONObject cbCriteriaObj = getCBCriteriaMetaJson(entityType);
            if(cbActionObj == null || cbActionObj.length() < 1 || cbCriteriaObj == null || cbCriteriaObj.length() < 1) {
                continue;
            }
            JSONObject cbEntityObj = new JSONObject();

            JSONObject uiEntityObj = convert(cbActionObj, cbEntityObj, cbCriteriaObj, entityType);
            if(uiEntityObj == null) {
                continue;
            }
            uiMetaJson.put(uiEntityObj);
        }

        System.out.println(dataTypes);
        File file = new File("./sample_get.json");
        try(FileWriter fw = new FileWriter(file)) {
            fw.write(uiMetaJson.toString(4));
            fw.flush();
        }
        return uiMetaJson;
    }

    private static JSONObject getCBActionMetaJson(EntityType entityType) throws Exception {
        return getFromFile(entityType, Constants.ACTION_META_FILE_DIR + convEntityName(entityType.name()) + ".json");
    }

    private static JSONObject getCBCriteriaMetaJson(EntityType entityType) throws Exception {
        return getFromFile(entityType, Constants.CRITERIA_META_FILE_DIR + "/" + "criteria.json");
    }

    private static JSONObject getFromFile(EntityType entityType, String filePath) throws Exception {
        JSONObject toRet;
        File f = new File(filePath);
        if(!f.exists()) {
            return null;
        }
        byte[] b = Files.readAllBytes(Paths.get(filePath));
        toRet = new JSONObject(new String(b));
        return toRet;
    }

    private static JSONObject convert(JSONObject cbActionObj, JSONObject cbEntityObj, JSONObject cbCriteriaObj, EntityType entityType) throws JSONException {
        JSONObject uiEntityObj = new JSONObject();
        JSONArray uiTriggers = getTriggers(entityType);
        if(uiTriggers == null) {
            return null;
        }
        JSONArray cbActions = cbActionObj.getJSONArray("actions");
        JSONObject cbCriteria = cbCriteriaObj.getJSONObject(entityType.name().toLowerCase());
        JSONArray uiActions = convActionArray(cbActions);
        JSONArray uiCriteria = convCriteriaArray(cbCriteria, entityType.name());

        uiEntityObj.put("entityName", entityType.name());
        uiEntityObj.put("criterias", uiCriteria);
        uiEntityObj.put("actions", uiActions);
        uiEntityObj.put("trigger", uiTriggers);
        return uiEntityObj;
    }

    private static JSONArray convActionArray(JSONArray cbActions) throws JSONException {
        JSONArray uiActions = new JSONArray();
        for(int i = 0; i < cbActions.length(); i++) {
            JSONObject uiAction = new JSONObject();
            JSONArray uiAttributes = new JSONArray();
            JSONArray uiMultiAttributes = new JSONArray();

            JSONObject cbAction = cbActions.getJSONObject(i);
            JSONArray arguments = cbAction.getJSONArray("arguments");

            uiAction.put("name", cbAction.getString("action_name"));
            for(int j = 0; j < arguments.length(); j++) {
                JSONObject cbArgument = arguments.getJSONObject(j);
                JSONObject uiAttribute = new JSONObject();
                uiAttribute.put("name", cbArgument.getString("name"));
//                uiAttribute.put("mandatory", cbArgument.getString("req"));
                if(cbArgument.getBoolean("is_multi")) {
                    System.out.println("\n\n\n" +cbArgument.getString("name") + " \n\n\n$$$$$$$$$$$$$");
                    // add multi attributes here
                } else {
                    uiAttribute.put("type", convType(cbArgument.getString("name"), cbArgument.getString("type")));
                    JSONArray possibleValues = cbArgument.optJSONArray("possible_values");
                    if(possibleValues != null) {
                        JSONArray possibleVals = new JSONArray();
                        for(int k =0; k<possibleValues.length() ; k++) {
                            if(!"_UNKNOWN".equals(possibleValues.get(k))) {
                                possibleVals.put(possibleValues.get(k));
                            }
                        }
                        uiAttribute.put("possibleValues", possibleVals);
                    }
                    uiAttributes.put(uiAttribute);
                }
            }
            uiAction.put("attributes", uiAttributes);
            uiAction.put("multiAttribute", uiMultiAttributes);
            uiActions.put(uiAction);
        }
        return uiActions;
    }

    private static JSONArray convCriteriaArray(JSONObject cbCriteria, String entityType) throws JSONException {
        JSONArray uiCriteria = new JSONArray();
        JSONArray cbCrtieriaAttributes = cbCriteria.getJSONArray("attributes");
        for(int i = 0; i< cbCrtieriaAttributes.length(); i++) {
            JSONObject cbCrtieriaAttribute = cbCrtieriaAttributes.getJSONObject(i);
            Iterator itr = cbCrtieriaAttribute.keys();
            do {
                JSONObject uiCriteriaObj = new JSONObject();
                String key = (String) itr.next();
                JSONObject attrObj = cbCrtieriaAttribute.getJSONObject(key);
                String attrName = entityType.toLowerCase() + "." + key;
                if(Strings.isNullOrEmpty(attrObj.optString("data_type"))) {
                    continue;
                }
                String dType = attrObj.getString("data_type").substring( attrObj.getString("data_type").lastIndexOf('.') + 1).trim();
                String dataType = convType(entityType.toLowerCase() + "." + key, dType);
                uiCriteriaObj.put("name",attrName);
                uiCriteriaObj.put("type", dataType);
                check(dType);
                if("Enum".equals(dType)) {
                    JSONArray possibleVals = new JSONArray();
                    JSONArray obj = attrObj.getJSONArray("possible_values");
                    for(int k =0; k<obj.length() ; k++) {
                        if(!"_UNKNOWN".equals(obj.get(k))) {
                            possibleVals.put(obj.get(k));
                        }
                    }
                    uiCriteriaObj.put("possibleValues", possibleVals);
                }
                uiCriteria.put(uiCriteriaObj);
            } while(itr.hasNext());
        }
        return uiCriteria;
    }

    private static String convType(String name, String dataType) {
        switch (dataType) {
            case "String":
            case "JSONObject" :
            case "JSONArray" :
            case "Timestamp" :
            case "String[]" :
                return "string";
            case "Integer":
            case "Long" :
            case "BigDecimal" :
            case "Double" :
                return "number";
            case "Enum":
            case "List":
                return "list";
            case "Boolean":
                return "boolean";
            default:
                System.out.println(name + " --- "  + dataType);
                return ";";
//                throw new RuntimeException("Invalid data type");
        }
    }

    private static void check(String dataType) {
        switch (dataType) {
            case "String" :
            case "Integer" :
            case "Enum" :
            case "Boolean" :
            case "Long" :
            case "Timestamp" :
            case "JSONObject" :
            case "JSONArray" :
            case "BigDecimal" :
            case "Double" :
            case "List" :
                break;
            default:
                dataTypes.add(dataType);
        }
    }

    private static String convEntityName(String entityName) {
        return toCamelCase(entityName.replace("_",""));
    }

    public static void main(String[] args) throws Exception {
        getCBMetaNconvert();
    }

    private static JSONArray getTriggers(EntityType type) {
        JSONArray triggers = new JSONArray();
        switch (type) {
            case CUSTOMER:
                triggers.put("CUSTOMER_CREATED");
                triggers.put("CUSTOMER_CHANGED");
                triggers.put("CUSTOMER_DELETED");
                return triggers;
            case SUBSCRIPTION:
                triggers.put("SUBSCRIPTION_CREATED");
                triggers.put("SUBSCRIPTION_STARTED");
                triggers.put("SUBSCRIPTION_TRIAL_ENDING");
                triggers.put("SUBSCRIPTION_ACTIVATED");
                triggers.put("SUBSCRIPTION_CHANGED");
                triggers.put("SUBSCRIPTION_CANCELLATION_SCHEDULED");
                triggers.put("SUBSCRIPTION_CANCELLING");
                triggers.put("SUBSCRIPTION_CANCELLED");
                triggers.put("SUBSCRIPTION_REACTIVATED");
                triggers.put("SUBSCRIPTION_RENEWED");
                triggers.put("SUBSCRIPTION_SCHEDULED_CANCELLATION_REMOVED");
                triggers.put("SUBSCRIPTION_SHIPPING_ADDRESS_UPDATED");
                triggers.put("SUBSCRIPTION_DELETED");
                return triggers;
            case INVOICE:
                triggers.put("INVOICE_CREATED");
                triggers.put("INVOICE_GENERATED");
                triggers.put("INVOICE_UPDATED");
                triggers.put("INVOICE_DELETED");
                return triggers;
            default:
                return null;
        }
    }

}
