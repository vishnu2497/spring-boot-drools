package com.vishnu.drools.controller;

import com.vishnu.drools.component.ReloadDroolsRules;
import com.vishnu.drools.model.fact.AddressCheckResult;
import com.vishnu.drools.model.Address;
import com.vishnu.drools.utils.KieUtils;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private ReloadDroolsRules rules;


    @GetMapping("/address")
    public void test() throws IOException {
        KieSession kieSession = KieUtils.getKieContainer().newKieSession();

        Address address = new Address();
        address.setPostcode("994251");

        AddressCheckResult result = new AddressCheckResult();
        kieSession.insert(address);
        kieSession.insert(result);
        int ruleFiredCount = kieSession.fireAllRules();
        System.out.println("Total Rules Fired" + ruleFiredCount);

        if(result.isPostCodeResult()){
            System.out.println("Rule verification");
        }

        kieSession.dispose();

    }


    @GetMapping("/reload")
    public String reload() throws IOException {
        rules.reload();
        return "ok";
    }
}
