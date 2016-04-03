package aw.server.listeners;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublisherListener extends AbstractInterceptHandler {


    protected static final Logger logger = LoggerFactory.getLogger(PublisherListener.class);

    @Override
    public void onPublish(InterceptPublishMessage msg) {
        logger.debug("Received on topic: " + msg.getTopicName() + " content: " + new String(msg.getPayload().array()));
    }
}