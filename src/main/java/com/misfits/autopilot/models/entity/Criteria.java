package com.misfits.autopilot.models.entity;


import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONException;
import com.chargebee.org.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="criterias")
public class Criteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long id;

    @Column(name="parameter", nullable=false)
    @ApiModelProperty(example = "subscription.shipping_address.country")
    private String name;

    @Column(name="operator", nullable=false)
    @Enumerated(EnumType.ORDINAL)
    @ApiModelProperty(example = "equals")
    private Operator operator;

    @Column(name="value", nullable=false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(example = "US")
    private String value;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="modified_at", nullable=false)
    private LocalDateTime modifiedAt;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(example = "US")
    public String from;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(example = "US")
    public String to;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(dataType = "list", example = "[\"US\", \"EU\"]")
    public List<String> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void convertValues() throws JSONException {
        JSONObject val = new JSONObject();
        if(from != null && to != null){
            val.put("from", from);
            val.put("to", to);
        } else if(list != null && !list.isEmpty()){
            val.put("list", new JSONArray(list));
        }else {
            val.put("value", value);
        }
        value = val.toString();
    }

    public void reConvert() throws JSONException {
         JSONObject val = new JSONObject(value);
         if(!Strings.isNullOrEmpty(val.optString("from")) && !Strings.isNullOrEmpty(val.optString("to"))) {
            from = val.optString("from");
            to = val.optString("to");
         } else if(val.optJSONArray("list") != null) {
             JSONArray array = val.optJSONArray("list");
             list = new ArrayList<>();
             for(int i =0; i<array.length();i++) {
                list.add(array.getString(i));
             }
         } else {
            value = val.optString("value");
         }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public enum Operator {
        between("between", "between"),
        after("after", "is after"),
        before("before", "is before"),
        not_empty("not_empty", "is present"),
        empty("empty", "is not present"),
        equals("equals", "equals"),
        not_equals("not_equals", "not equal to"),
        less_than("less_than", "less than"),
        greater_than("greater_than", "greater than"),
        less_than_equal("less_than_equal", "less than or equal"),
        greater_than_equal("greater_than_equal", "greater than or equal"),
        is("is", "is"),
        is_not("is_not", "is not"),
        includes("includes", "includes"),
        does_not_include("does_not_include", "does not include"),
        contains_only("contains_only", "contains only"),
        contains("contains", "contains"),
        not_contains("not_contains", "does not contain"),
        does_not_contains("does_not_contains", "does not contain"),
        starts_with("starts_with", "starts with");

        String name ;
        String displayName ;

        Operator(String name,String displayName) {
            this.name = name;
            this.displayName = displayName;
        }

        public static boolean perform(Operator operator, JSONObject value, Object obj, Object prevObj) throws JSONException {
            if(operator.equals(equals)){
                return value.getString("value").equals(obj.toString());
            } else if(operator.equals(between)) {
                int from = value.getInt("from");
                int to = value.getInt("to");
                return ((int)obj) > from && ((int)obj) > to;
            } else if(operator.equals(greater_than)){
                return value.getInt("value") < Integer.valueOf(obj.toString());
            } else if(operator.equals(less_than)){
                return value.getInt("value") > Integer.valueOf(obj.toString());
            }
            return false;
        }
    }

}
