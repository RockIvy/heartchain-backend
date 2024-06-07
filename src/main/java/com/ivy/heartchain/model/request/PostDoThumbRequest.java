package com.ivy.heartchain.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 点赞/取消点赞请求
 */
@Data
public class PostDoThumbRequest implements Serializable {

    private static final long serialVersionUID = 40690582060178102L;
    /**
     * 帖子id
     */
    private long postId;

}
