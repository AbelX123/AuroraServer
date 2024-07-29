package com.aurora.client.service.impl;

import com.aurora.client.service.ApiService;
import com.aurora.client.utils.QFUtil;
import com.baidubce.qianfan.core.StreamIterator;
import com.baidubce.qianfan.model.chat.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QFApiServiceImpl implements ApiService {

    @Value("${ai.baidu.access_key}")
    private String accessKey;

    @Value("${ai.baidu.secret_key}")
    private String secretKey;

    public StreamIterator<ChatResponse> doChatStream(String text) {
        return QFUtil.builder().accessKey(accessKey)
                .secretKey(secretKey)
                .build().ask(text);
    }
}
