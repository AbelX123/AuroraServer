package com.aurora.client.service.websocket;

import com.alibaba.fastjson2.JSON;
import com.aurora.client.common.CommonResult;
import com.aurora.client.common.dto.ChatDTO;
import com.aurora.client.common.enumeration.ResultCode;
import com.aurora.client.exception.ServiceException;
import com.aurora.client.service.IContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.aurora.client.common.enumeration.ResultCode.OTHER_ERROR;
import static com.aurora.client.common.enumeration.ResultCode.SUCCESS;

@Slf4j
@Component
@ServerEndpoint("/ask")
public class ChatServerEndpointService {

    /**
     * 因为在websocket建立连接的时候还会创建ChatServerEndpointService,所以用直接用@Autowired注入会在使用的时候为null
     */
    private static IContentService contentService;

    @Autowired
    public void setContentService(IContentService contentService) {
        ChatServerEndpointService.contentService = contentService;
    }

    /**
     * 一个WebSocket连接关联一个Session.
     */
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();


    /**
     * 建立WebSocket连接触发
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionMap.put(session.getId(), session);
    }

    /**
     * 接收到消息触发
     */
    @OnMessage
    public void onMessage(String text, Session session) {
        ChatDTO dto = JSON.to(ChatDTO.class, text);
        String result;
        try {
            result = contentService.handleChat(dto);
            sendToClient(SUCCESS, result, session);
        } catch (ServiceException e) {
            sendToClient(e.getResultCode(), null, session);
        } catch (Exception e) {
            e.printStackTrace();
            sendToClient(OTHER_ERROR, null, session);
        }
    }

    /**
     * 连接关闭触发
     */
    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
    }

    /**
     * 连接异常触发
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        throw new ServiceException(ResultCode.MIDDLEWARE_ERROR);
    }

    /**
     *
     */
    public void sendToClient(ResultCode resultCode, String result, Session session) {
        try {
            if (resultCode.getStatus().equals(SUCCESS.getStatus())) {
                session.getBasicRemote().sendText(JSON.toJSONString(CommonResult.success(result)));
            } else {
                session.getBasicRemote().sendText(JSON.toJSONString(CommonResult.failure(resultCode)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
