package com.misfits.autopilot.controller;

import com.chargebee.org.json.JSONArray;
import com.misfits.autopilot.convertors.ApiModelBody;
import com.misfits.autopilot.convertors.CBMetaUiMetaConvertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value="/ui", produces ="application/json")
@RequestMapping("/ui")
public class UiController {

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation("Get CB action meta")
    public ResponseEntity<ApiModelBody> saveProduct() throws Exception {
        JSONArray uiMetaJson = CBMetaUiMetaConvertor.getCBMetaNconvert();
        return new ResponseEntity(uiMetaJson, HttpStatus.OK);
    }

}
