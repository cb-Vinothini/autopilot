package com.misfits.autopilot.convertors;

import com.chargebee.models.enums.EntityType;
import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONException;
import com.chargebee.org.json.JSONObject;
import com.misfits.autopilot.Constants;

import org.json.simple.parser.JSONParser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.misfits.autopilot.cb_api_meta_data.ApiMetaGenerator.toCamelCase;

public class CBMetaUiMetaConvertor {

    public static JSONArray getCBMetaNconvert() throws Exception {
        JSONArray uiMetaJson = new JSONArray();

        for(EntityType entityType : EntityType.values()) {
            JSONObject cbActionObj = getCBActionMetaJson(entityType);
            JSONObject cbEntityObj = new JSONObject();
            JSONObject uiEntityObj = convert(cbActionObj, cbEntityObj, entityType);
            uiMetaJson.put(uiEntityObj);
        }

        return uiMetaJson;
    }

    private static JSONObject getCBActionMetaJson(EntityType entityType) throws Exception {
        JSONObject cbActionMetaJson;
        JSONParser jsonParser = new JSONParser();
        String filePath = Constants.ACTION_META_FILE_DIR + convEntityName(entityType.name()) + ".json";
        File f = new File(filePath);
        if(!f.exists()) {
            return null;
        }
        byte[] b = Files.readAllBytes(Paths.get(filePath));
        cbActionMetaJson = new JSONObject(new String(b));
        return cbActionMetaJson;
    }

    private static JSONObject convert(JSONObject cbActionObj, JSONObject cbEntityObj, EntityType entityType) throws JSONException {
        JSONObject uiEntityObj = new JSONObject();
        JSONArray cbActions = cbActionObj.getJSONArray("actions");
        JSONArray uiActions = convActionArray(cbActions);

        uiEntityObj.put("entityName", entityType.name());
        uiEntityObj.put("trigger", new JSONArray());
        uiEntityObj.put("criterias", new JSONArray());
        uiEntityObj.put("actions", uiActions);
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
                uiAttribute.put("mandatory", cbArgument.getString("req"));

                if(cbArgument.getBoolean("is_multi")) {
                    // add multi attributes here
                } else {
                    uiAttribute.put("type", cbArgument.getString("type"));
                    JSONArray possibleValues = cbArgument.optJSONArray("possible_values");
                    if(possibleValues != null) {
                        uiAttribute.put("possible_values", possibleValues);
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

    private static String convEntityName(String entityName) {
        return toCamelCase(entityName.replace("_",""));
    }

    public static void main(String[] args) throws Exception {
        getCBMetaNconvert();
    }

}
