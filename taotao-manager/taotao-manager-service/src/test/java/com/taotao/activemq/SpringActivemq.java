package com.taotao.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

public class SpringActivemq {
    //使用jmsTemplate 发送信息
    @Test
    public void testJmsTemplate() throws Exception{
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //从容器中获得JmsTemplate对象
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //从容器中获取Dastination对象
        Destination destination = (Destination) applicationContext.getBean("queue");
        //发送消息
        jmsTemplate.send(destination,new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage("987");
                return message;
            }
        });
    }
}
