package com.commonUtils.controller;

import com.commonUtils.utils.NetworkTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ServerIpStatus {

    @Autowired
    private NetworkTester networkTester;

    @GetMapping("/getIp")
    public String getIp() throws Exception {
        return networkTester.getPrimaryIp();
    }

}
