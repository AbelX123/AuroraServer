package com.aurora.client.utils;

import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.model.chat.ChatResponse;
import com.baidubce.qianfan.model.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QFUtil {

    @Value("${ai.baidu.access_key}")
    private static String accessKey;

    @Value("${ai.baidu.secret_key}")
    private static String secretKey;

    public String ask(String ask) {

        Qianfan qianfan = new Qianfan(accessKey, secretKey);
        ChatResponse resp;
        try {
            resp = qianfan.chatCompletion().model("ERNIE-4.0-8K")
                    .addMessage("user", ask)
                    .execute();
        } catch (ApiException e) {
            log.error("调用baidu失败: {}", e.getMessage());
            throw e;
        }
        return resp.getResult();
    }
}
