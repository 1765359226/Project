package com.taotao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

public class testActiveMq {
    //queue
    //Producer
    /*
    * Activemq发送queue消息
    *
    * */
    @Test
    public void testQueueProducer() throws Exception{
        //1.创建一个链接工厂对象ConnectionFatory对象，需要指定mq服务的ip及端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.19.128:61616");
        //2.使用ConnectionFactory创建一个链接Connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接。调用Connection对象的start方法
        connection.start();
        //4.使用Connection对象创建一个Session对象
             //第一个参数为是否开启事务，一般不使用事务。保证数据的最终一致，可以使用信息队列实现
             //如果第一个参数为true，第二个参数自动忽略。如果不开启事务false,第二个参数为信息的应答模式。一般自动应答就可以。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象创建一个Destination对象，两种形式queue、topic。现在使用queue
          //参数就是信息队列的名称
        Queue queue = session.createQueue("test-queue");
        //6.使用Session对象创建一个Prodecer对象
        MessageProducer producer = session.createProducer(queue);
        //7.创建一个TextMessage对象
//        ActiveMQTextMessage textMessage = new ActiveMQTextMessage();
        TextMessage textMessage = session.createTextMessage("hello activemq");
        //8.发送信息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    /*
     * Activemq接收queue消息
     *
     * */
    @Test
    public void testQueueConsumer() throws Exception{
        //1.创建一个链接工厂对象ConnectionFatory对象，需要指定mq服务的ip及端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.19.128:61616");
        //2.使用一个链接工厂对象创建一个连接
        Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.使用连接对象创建一个Session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session创建一个Consumer对象
        Queue queue = session.createQueue("test-queue");
        //6.使用Sesion创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(queue);
        //7.向Consumer对象中设置一个MessageListener对象，用来接收对象
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //8.取消息内容
                if (message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        //9.打印消息内容
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //系统等待接收消息
        System.in.read();//从键盘输入，任意键
//        while(true){
//            Thread.sleep(100);
//        }
        //10.关闭资源(从小到大关闭)
        consumer.close();
        session.close();
        connection.close();
    }

    /*
     * Activemq发送topic消息
     * topic
     * Producer
     * */
    @Test
    public void testTopicProducer() throws Exception {
        //创建一个链接工厂对象
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.19.128:61616");
        //创建连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //创建Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建Destination目标，应该使用topic标题
        Topic topic = session.createTopic("test-topic");
        //创建一个Producer制造者对象
        MessageProducer producer = session.createProducer(topic);
        //创建一个TextMessage消息对象
        TextMessage textMessage = session.createTextMessage("hello activemq topic");
        //send发送消息
        producer.send(textMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    /*
     * Activemq接收topic消息
     * topic
     * Producer
     * */
        @Test
        public void testTopicConsumer() throws Exception{
            //创建一个连接工厂
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.19.128:61616");
            //创建连接
            Connection connection = connectionFactory.createConnection();
            //开启连接
            connection.start();
            //创建Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //创建Destination，应该使用topic
            Topic topic = session.createTopic("test-topic");
            // 创建一个Consumer消费者对象
            MessageConsumer consumer = session.createConsumer(topic);
            //向Consumer对象中设置一个MessageListener对象，用来接收对象
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    //取信息内容
                    if (message instanceof  TextMessage){
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            //打印信息内容
                            String text = textMessage.getText();
                            System.out.print(text);
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            //系统等待接收消息
            System.in.read();//从键盘输入，任意键
            //关闭资源
            consumer.close();
            session.close();
            connection.close();

        }
}
