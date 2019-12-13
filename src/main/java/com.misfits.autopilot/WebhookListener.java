package com.misfits.autopilot;

import com.chargebee.org.json.JSONException;
import com.chargebee.org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebhookListener {

    @RequestMapping(value = "/listener", method= RequestMethod.POST)
    public ResponseEntity listener(@RequestBody String event) throws JSONException {
        JSONObject ec = new JSONObject(event);
        System.out.println(ec.toString());
        return new ResponseEntity(HttpStatus.OK);
    }
}