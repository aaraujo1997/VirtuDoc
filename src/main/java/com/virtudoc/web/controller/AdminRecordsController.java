package com.virtudoc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminRecordsController {
    @GetMapping("/admin_records")
    public String getMessagePage(){
        return "admin_records";
    }
}
