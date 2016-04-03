package aw.server;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.server.Server;
import io.moquette.server.config.FilesystemConfig;
import io.moquette.server.config.IConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import static java.util.Arrays.asList;

@SpringBootApplication
public class Application {

    protected static final Logger logger = LoggerFactory.getLogger(Application.class);

    @PostConstruct
    public void confirmPrepareStart() {
        logger.debug("Instance created. Preparing server start.");
    }

    public static void main(String[] args) {

        // Start it up
        logger.debug("SpringBoot Application starting.");
        ApplicationContext app = SpringApplication.run(Application.class, args);

        MqttServer mqttBroker = app.getBean(MqttServer.class);  //new MqttServer();
        try {
            mqttBroker.start();
        } catch (IOException e) {
            logger.warn("Fatal! Could not start MQTT server. Reason: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        logger.info("Broker started press [CTRL+C] to stop");
        //Bind  a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.warn("Stopping broker");
                mqttBroker.stop();
                logger.warn("Broker stopped");
            }
        });


    }
}
