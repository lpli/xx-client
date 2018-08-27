package com.xx.core.convertor;

import java.util.List;

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
    List<Message> toByteMessage(T t);

}
