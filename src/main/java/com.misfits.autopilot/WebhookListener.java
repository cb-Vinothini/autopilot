package com.misfits.autopilot;

import com.chargebee.org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebhookListener {

    @RequestMapping(value = "/listener", method= RequestMethod.POST, consumes = "application/json")
    public ResponseEntity listener(@RequestBody JSONObject event) {
        System.out.println(event);
        return new ResponseEntity(HttpStatus.OK);
    }
}