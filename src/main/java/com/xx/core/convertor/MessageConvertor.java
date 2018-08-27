package com.xx.core.convertor;

import com.xx.core.dto.Message;

/**
 * 消息转换器接口
 * @Author liliping
 * @Date 2018/8/27
 **/
public interface MessageConvertor<T> {

    /**
     * 转换为 字节消息
     * @param
     * @return
     */
    Message toByteMessage(T t);

    /**
     * 转换为对象消息
     * @param message
     * @return
     */
    T toObjectMessage(Message message);
}
