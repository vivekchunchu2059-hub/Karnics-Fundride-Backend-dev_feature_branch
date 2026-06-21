
package com.funride;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping("/create-order")
    public String createOrder() {
        return "Order Created";
    }

    @PostMapping("/verify")
    public String verify() {
        return "Payment Verified";
    }
}
