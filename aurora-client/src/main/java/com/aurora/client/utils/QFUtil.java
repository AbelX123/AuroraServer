package com.aurora.client.utils;

import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.model.chat.ChatResponse;
import com.baidubce.qianfan.model.exception.ApiException;

public class QFUtil {

    public static String ask(String ask) {
        String accessKey = "8ef0bd9b8c42423eaf2fae33739d2ca3";
        String secretKey = "9cc1e409d7ba48bca8e4fe5dc822a39f";

        Qianfan qianfan = new Qianfan(accessKey, secretKey);
        ChatResponse resp = null;
        try {
            resp = qianfan.chatCompletion().model("ERNIE-4.0-8K")
                    .addMessage("user", "你好")
                    .execute();
        } catch (ApiException e) {
            throw e;
        }
        return resp.getResult();
    }
}
