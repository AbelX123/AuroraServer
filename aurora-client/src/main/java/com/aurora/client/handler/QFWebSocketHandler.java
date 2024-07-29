package com.aurora.client.handler;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aurora.client.common.CommonResult;
import com.aurora.client.common.dto.ChatDTO;
import com.aurora.client.common.enumeration.ResultCode;
import com.aurora.client.service.ApiService;
import com.aurora.client.service.IContentDetailService;
import com.aurora.client.service.IContentService;
import com.baidubce.qianfan.core.StreamIterator;
import com.baidubce.qianfan.model.chat.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.UUID;

import static com.aurora.client.common.enumeration.ResultCode._401;

/**
 * websocket连接处理
 */
@Slf4j
@Component
public class QFWebSocketHandler extends TextWebSocketHandler {

    private static ApiService apiService;

    @Autowired
    public void setApiService(ApiService apiService) {
        QFWebSocketHandler.apiService = apiService;
    }

    private static IContentService contentService;

    @Autowired
    public void setContentService(IContentService contentService) {
        QFWebSocketHandler.contentService = contentService;
    }

    private static IContentDetailService contentDetailService;

    @Autowired
    public void setContentDetailService(IContentDetailService contentDetailService) {
        QFWebSocketHandler.contentDetailService = contentDetailService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatDTO chatDTO = JSON.to(ChatDTO.class, payload);

        // check
        if (StringUtils.isBlank(chatDTO.getUserId())) {
            session.sendMessage(new TextMessage(JSON.toJSONString(new CommonResult<>(_401))));
        }
        String contentId = chatDTO.getContentId();
        String pdId = chatDTO.getPreviousDetailId();
        if (
                (StringUtils.isBlank(contentId) && StringUtils.isNotBlank(pdId)) || (StringUtils.isNotBlank(contentId) && StringUtils.isBlank(pdId))
        ) {
            session.sendMessage(new TextMessage(JSON.toJSONString(new CommonResult<>(_401))));
        }

        // 是否新的会话
        if (StringUtils.isBlank(contentId)) {
            contentId = StringUtils.replace(UUID.randomUUID().toString(), "-", "");
        }
        String detailId = StringUtils.replace(UUID.randomUUID().toString(), "-", "");

        String ask = chatDTO.getAsk();
        // 发送第三方请求，接收流式对象
        StreamIterator<ChatResponse> streamIterator = apiService.doChatStream(ask);
        // 将答案通过session应答给客户端
        JSONObject data = new JSONObject();
        data.put("contentId", contentId);
        data.put("detailId", detailId);

        String detailStatus = "0000"; // 应答状态
        String detailMessage = "success"; // 应答描述
        StringBuilder allAnswer = new StringBuilder(); // 回答
        try {
            streamIterator.forEachRemaining(response -> {
                String answer = response.getResult();
                allAnswer.append(answer);
                Integer sentenceId = response.getSentenceId();
                data.put("answer", answer);
                data.put("sentenceId", sentenceId);
                CommonResult<String> success = CommonResult.success(data.toString());
                try {
                    session.sendMessage(new TextMessage(JSON.toJSONString(success)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            detailStatus = ResultCode.MIDDLEWARE_ERROR.getStatus();
            detailMessage = ResultCode.MIDDLEWARE_ERROR.getMessage();
        }

        // 内容存储
        if (StringUtils.isBlank(chatDTO.getContentId())) {
            contentService.saveContent(contentId, chatDTO);
        }
        contentDetailService.saveContentDetail(detailId, contentId, detailStatus, detailMessage, allAnswer.toString(), chatDTO);
    }
}
