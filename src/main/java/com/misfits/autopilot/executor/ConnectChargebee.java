package com.misfits.autopilot.executor;

import com.chargebee.org.json.JSONObject;
import com.chargebee.v2.Environment;
import com.chargebee.v2.Result;
import com.chargebee.v2.models.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Base64;

@Service
@Component
public class ConnectChargebee {


//    RestTemplateC

//    private final RestTemplate restTemplate;


    public ConnectChargebee() {

        System.setProperty("com.chargebee.api.domain.suffix", "localcb.in:8080");
        System.setProperty("com.chargebee.api.protocol", "http");

        Environment.configure("mannar-test","test___dev__vMVROChoJ0hkCFSX4zzlEQ3EOffCcdiTB");
//        this.restTemplate = restTemplateBuilder
//                .basicAuthentication("test___dev__vMVROChoJ0hkCFSX4zzlEQ3EOffCcdiTB", "")
//                .build();

    }


    public void hitChargebee() throws Exception {
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
        obj  = charge.invoke(obj,"active");
         charge = c.getDeclaredMethod("amount", new Class[]{Integer.class});
        obj  = charge.invoke(obj,1000);
         charge = c.getDeclaredMethod("description", new Class[]{String.class});
        obj  = charge.invoke(obj,"no desc");
         charge = c.getMethod("request", new Class[]{});
        charge.invoke(obj);





//        Class c = Class.forName("com.misfits.autopilot.executor.ConnectChargebee");
//
//        Method charge = c.getDeclaredMethod("disp", new Class[]{});
//        Object obj = c.newInstance();
//        obj  = charge.invoke(obj);


//        Object obj = c.newInstance();
//        obj  = charge.invoke(obj);


//        Customer

//        if(obj != null) {
//            return;
//
//        }
//
//        String action_name = obj.getString("action_name");
//
//        Class.forName(action_name+"()");
//
//
//
//        final String uri = "http://mannar-test.localcb.in:8080/api/v2/invoices/__demo_inv__1";


//        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET,null, String.class);

//        System.out.println(response.toString());
    }

    public void disp() throws Exception {
        System.out.println("display");
    }

    public void postInv() throws Exception{



//        final String uri = "http://mannar-test.localcb.in:8080/api/v2/invoices/charge";
//        String qr = "customer_id=\"active\" \\\n" +
//                "currency_code=\"USD\""+
//                "addons[id][0]=\"day-pass\" \\\n" +
//                "addons[unit_price][0]=2000 \\\n" +
//                "addons[quantity][0]=2 \\\n" +
//                "shipping_address[first_name]=\"John\" \\\n" +
//                "shipping_address[last_name]=\"Mathew\" \\\n" +
//                "shipping_address[city]=\"Walnut\" \\\n" +
//                "shipping_address[state]=\"California\" \\\n" +
//                "shipping_address[zip]=\"91789\" \\\n" +
//                "shipping_address[country]";
//
//
//        JSONObject obj = new JSONObject();
//        obj.put("customer_id","active");
//        obj.put("currency_code","USD");
//        obj.put("amount","100");
//        obj.put("description","non sdsd");
//
//        HttpEntity<String> request =
//                new HttpEntity<String>(obj.toString());
//
//
//        restTemplate.postForObject(uri,request,String.class);
//
//
//
//
//
//        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST,null, String.class);


    }

    public static void main(String[] args) throws Exception {
        ConnectChargebee  cc = new ConnectChargebee();
        cc.hitChargebee();

    }


}
