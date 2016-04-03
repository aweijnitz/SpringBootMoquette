package aw.server;

import aw.server.listeners.PublisherListener;
import io.moquette.interception.InterceptHandler;
import io.moquette.server.Server;
import io.moquette.server.config.FilesystemConfig;
import io.moquette.server.config.IConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

@Component
public class MqttServer {
    private static final Logger logger = LoggerFactory.getLogger(MqttServer.class);

    private String mqttConfigFile = "FILE_NAME_NOT_SET";
    private Server mqttBroker;

    @Autowired
    public MqttServer(@Value("${mqtt.ConfigFile}") String configFile) {
        this.mqttConfigFile = configFile;
    }


    public void start() throws IOException {

        File mqttConfFile = new File(mqttConfigFile);
        logger.info("Reading MQTT config file from: " + mqttConfigFile.toString());
        IConfig mqttConfig = new FilesystemConfig(mqttConfFile); //new ClasspathConfig();

        mqttBroker = new Server();
        List<? extends InterceptHandler> userHandlers = asList(new PublisherListener());
        mqttBroker.startServer(mqttConfig, userHandlers);
        logger.info("Starting MQTT Server. Listening on "
                + mqttConfig.getProperty("host") + ":" + mqttConfig.getProperty("port")
                + ". Websockets on port " + mqttConfig.getProperty("websocket_port"));
    }

    public void stop() {
        mqttBroker.stopServer();
    }
}
