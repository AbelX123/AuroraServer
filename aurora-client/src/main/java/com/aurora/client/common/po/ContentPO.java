package com.aurora.client.common.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * mongo存储内容持久对象
 */
@Data
@Document(collection = "content")
public class ContentPO {
    @Field("content_id")
    private String contentId;
    @Field("content")
    private String content;
}
