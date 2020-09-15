package com.smswithsmpp.nipuna.samplesms.controller;

import com.smswithsmpp.nipuna.samplesms.service.MultipleSubmitExample;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sms")
@CrossOrigin
public class SMSController {

    @PostMapping(value = "/send")
    public void send(){
        List numbers = new ArrayList();
        numbers.add("94768958496");
        MultipleSubmitExample multipleSubmitExample = new MultipleSubmitExample();
        multipleSubmitExample.broadcastMessage("Hi All", numbers);
    }
}
