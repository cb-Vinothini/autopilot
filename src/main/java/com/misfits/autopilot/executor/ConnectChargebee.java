package com.misfits.autopilot.executor;

import com.chargebee.org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@Component
public class ConnectChargebee {


//    RestTemplateC

    private final RestTemplate restTemplate;


    public ConnectChargebee(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication("test___dev__vMVROChoJ0hkCFSX4zzlEQ3EOffCcdiTB", "")
                .build();

    }


    public void hitChargebee() {


        final String uri = "http://mannar-test.localcb.in:8080/api/v2/invoices/__demo_inv__1";


        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET,null, String.class);

        System.out.println(response.toString());
    }

    public void postInv() throws Exception{



        final String uri = "http://mannar-test.localcb.in:8080/api/v2/invoices/charge";
        String qr = "customer_id=\"active\" \\\n" +
                "currency_code=\"USD\""+
                "addons[id][0]=\"day-pass\" \\\n" +
                "addons[unit_price][0]=2000 \\\n" +
                "addons[quantity][0]=2 \\\n" +
                "shipping_address[first_name]=\"John\" \\\n" +
                "shipping_address[last_name]=\"Mathew\" \\\n" +
                "shipping_address[city]=\"Walnut\" \\\n" +
                "shipping_address[state]=\"California\" \\\n" +
                "shipping_address[zip]=\"91789\" \\\n" +
                "shipping_address[country]";


        JSONObject obj = new JSONObject();
        obj.put("customer_id","active");
        obj.put("currency_code","USD");
        obj.put("amount","100");
        obj.put("description","non sdsd");

        HttpEntity<String> request =
                new HttpEntity<String>(obj.toString());


        restTemplate.postForObject(uri,request,String.class);





        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST,null, String.class);


    }


}
