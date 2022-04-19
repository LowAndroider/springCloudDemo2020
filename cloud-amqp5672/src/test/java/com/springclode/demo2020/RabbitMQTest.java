package com.springclode.demo2020;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitMQApplication.class)
public class RabbitMQTest {

    private static Logger logger = LoggerFactory.getLogger(RabbitMQTest.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test1() {
        logger.info("开始向队列中发送一条消息");

        JSONObject data = new JSONObject();
        data.put("title", "测试");
        data.put("param", new JSONObject().fluentPut("name", "张33三").fluentPut("age", 8));
        rabbitTemplate.convertAndSend("queueName1", data);

        logger.info("消息发送完毕");
    }
}
