package com.aurora.client.utils;

import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.core.StreamIterator;
import com.baidubce.qianfan.model.chat.ChatResponse;
import lombok.Builder;

@Builder
public class QFUtil {

    private String accessKey;

    private String secretKey;

    public StreamIterator<ChatResponse> ask(String ask) {
        Qianfan qianfan = new Qianfan(accessKey, secretKey);
        return qianfan.chatCompletion().model("ERNIE-4.0-8K").addMessage("user", ask).executeStream();
    }

}
