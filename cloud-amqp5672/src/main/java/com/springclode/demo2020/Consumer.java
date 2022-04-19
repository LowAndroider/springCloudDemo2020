package com.springclode.demo2020;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * @author DJH
 */
@Component
@RabbitListener(queues = "queueName1")
public class Consumer {

    private static Logger logger = LoggerFactory.getLogger(Consumer.class);

    @RabbitHandler
    public void showMsg(JSONObject msg) {
        logger.info("接受到的消息内容为{}", msg);
    }
}
