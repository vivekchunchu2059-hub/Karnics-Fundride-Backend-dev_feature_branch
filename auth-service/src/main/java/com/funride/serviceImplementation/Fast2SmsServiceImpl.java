//package com.funride.serviceImplementation;
//
//import com.funride.service.SmsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class Fast2SmsServiceImpl implements SmsService {
//
//    @Value("${fast2sms.api-key}")
//    private String apiKey;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Override
//    public void sendOtp(String phone, String otp) {
//
//        String message =
//                "Your FunRide OTP is " + otp;
//
//        String url =
//                "https://www.fast2sms.com/dev/bulkV2"
//                        + "?authorization=" + apiKey
//                        + "&route=otp"
//                        + "&variables_values=" + otp
//                        + "&flash=0"
//                        + "&numbers=" + phone;
//
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.set("authorization", apiKey);
//
//        HttpEntity<String> entity =
//                new HttpEntity<>(headers);
//
//        ResponseEntity<String> response =
//                restTemplate.exchange(
//                        url,
//                        HttpMethod.GET,
//                        entity,
//                        String.class
//                );
//
//        System.out.println(response.getBody());
//    }
//}