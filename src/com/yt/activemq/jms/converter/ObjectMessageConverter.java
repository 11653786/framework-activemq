package com.yt.activemq.jms.converter;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.*;

/**
 * 消息转换器用来搞定序列化问题的
 * Created by Administrator on 2016/1/13 0013.
 */
@Component
public class ObjectMessageConverter implements MessageConverter {
    //从消息中取出对象
    @Override
    public Object fromMessage(Message message) throws JMSException,MessageConversionException {
        Object object = null;
        if(message  instanceof ObjectMessage) {
            //两次强转，获得消息中的主体对象字节数组流
            byte[] obj = (byte[])((ObjectMessage)message).getObject();
            //读取字节数组中为字节数组流
            ByteArrayInputStream bis = new ByteArrayInputStream(obj);
            try {
                // 读字节数组流为对象输出流
                ObjectInputStream ois = new ObjectInputStream(bis);
                // 从对象输出流中取出对象 并强转
                object = ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (message  instanceof MapMessage){

        }
        return object;
    }


    //将对象转换成消息
    @Override
    public Message toMessage(Object object, Session session) throws JMSException,MessageConversionException {
        ObjectMessage objectMessage = session.createObjectMessage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//字节数组输出流
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);//对象输出流
            oos.writeObject(object);//写入对象
            byte[] objMessage = bos.toByteArray();//字节数组输出流转成字节数组
            objectMessage.setObject(objMessage);//将字节数组填充到消息中作为消息主体
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objectMessage;
    }

}
