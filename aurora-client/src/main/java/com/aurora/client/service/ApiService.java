package com.aurora.client.service;

import com.baidubce.qianfan.core.StreamIterator;
import com.baidubce.qianfan.model.chat.ChatResponse;

public interface ApiService {

    StreamIterator<ChatResponse> doChatStream(String ask);

}
