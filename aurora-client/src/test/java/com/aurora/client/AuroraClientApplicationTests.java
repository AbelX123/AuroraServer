package com.aurora.client;

import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.model.chat.ChatResponse;
import com.baidubce.qianfan.model.chat.ChatUsage;
import com.baidubce.qianfan.model.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AuroraClientApplicationTests.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AuroraClientApplicationTests {

    @Test
    public void testSelect() {
        String accessKey = "8ef0bd9b8c42423eaf2fae33739d2ca4";
        String secretKey = "9cc1e409d7ba48bca8e4fe5dc822a39f";

        Qianfan qianfan = new Qianfan(accessKey, secretKey);
        ChatResponse resp = null;
        try {
            resp = qianfan.chatCompletion().model("ERNIE-4.0-8K")
                    .addMessage("user", "你好")
                    .execute();
        } catch (ApiException e) {
            System.out.println(e.getErrorResponse());
        }
        System.out.println(resp.getId());
        System.out.println(resp.getObject());
        System.out.println(resp.getCreated());
        System.out.println(resp.getSentenceId());
        System.out.println(resp.getEnd());
        System.out.println(resp.getTruncated());
        System.out.println(resp.getFinishReason());
        System.out.println(resp.getSearchInfo());
        System.out.println(resp.getResult());
        System.out.println(resp.getNeedClearHistory());
        System.out.println(resp.getFlag());
        System.out.println(resp.getBanRound());
        ChatUsage usage = resp.getUsage();
        System.out.println(usage.getPromptTokens());
        System.out.println(usage.getCompletionTokens());
        System.out.println(usage.getTotalTokens());
    }

}
