package com.secbro.drools.component;

import com.secbro.drools.utils.KieUtils;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;



@Component
public class ReloadDroolsRules {

    public void reload() throws IOException {
        KieServices kieServices = getKieServices();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write("src/main/resources/rules/temp.drl", loadRules());


        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

        KieUtils.setKieContainer(kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId()));
        System.out.println("reload success");
    }

    private String loadRules() throws IOException {
        // Rules loaded from the database


        String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\vmuralidharan\\Downloads\\drools-master (1)\\drools-master\\springboot-drools-reload-rules\\src\\main\\resources\\rules\\address.drl")));

        return content;

    }

    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }

    public void reloadByHelper() throws IOException {

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(loadRules(), ResourceType.DRL);

        Results results = kieHelper.verify();
        if (results.hasMessages(Message.Level.ERROR)) {
            System.out.println(results.getMessages());
            throw new IllegalStateException("### errors ###");
        }

//        KieBase kieBase = kieHelper.build();
        KieContainer kieContainer = kieHelper.getKieContainer();


        KieUtils.setKieContainer(kieContainer);
        System.out.println("New rule overloaded successfully");
    }

}
