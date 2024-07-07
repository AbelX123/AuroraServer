package com.aurora.client.configuration;

import com.aurora.client.common.enumeration.ResultCode;
import com.aurora.client.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/my")
@EnableScheduling
public class WebServerEndpoint {

    /**
     * 一个WebSocket连接关联一个Session.
     */
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();


    /**
     * 建立WebSocket连接触发
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionMap.put(session.getId(), session);
        log.info("Websocket is open.");
    }

    /**
     * 接收到消息触发
     *
     * @param text
     */
    @OnMessage
    public void onMessage(String text) {
        log.info("Receive a new text is {}.", text);
    }

    /**
     * 连接关闭触发
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
        log.info("Websocket is closed.");
        sessionMap.clear();
    }

    /**
     * 连接异常触发
     *
     * @param session
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        throw new ServiceException(ResultCode.MIDDLEWARE_ERROR);
    }
}
