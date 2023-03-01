package com.github.nkonev.aaa.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.nkonev.aaa.utils.ResourceUtils;
import com.github.nkonev.aaa.dto.UserAccountDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class AaaConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:/static/git.json")
    private Resource resource;

    private static final Logger LOGGER = LoggerFactory.getLogger(AaaConfig.class);

    @PostConstruct
    public void pc() throws Exception {
        SimpleModule rejectUserAccountDetailsDTOModule = new SimpleModule("Reject serialize UserAccountDetailsDTO");
        rejectUserAccountDetailsDTOModule.addSerializer(UserAccountDetailsDTO.class, new JsonSerializer<UserAccountDetailsDTO>() {
            @Override
            public void serialize(UserAccountDetailsDTO value, JsonGenerator jgen, SerializerProvider provider){
                throw new RuntimeException("You shouldn't to serialize UserAccountDetailsDTO");
            }
        });
        objectMapper.registerModule(rejectUserAccountDetailsDTOModule);
    }

    @PostConstruct
    public void printVersion() throws IOException {
        if(resource.exists()){
            String text = ResourceUtils.stringFromResource(resource);
            LOGGER.info("Version {}", text);
        }
    }

    @PreDestroy
    public void preDestroy() {
        LOGGER.info("Destroying aaa");
    }
}
