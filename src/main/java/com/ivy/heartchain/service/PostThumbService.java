package com.ivy.heartchain.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ivy.heartchain.model.domain.PostThumb;

/**
* @author ivy
* @description 针对表【post_thumb(帖子点赞记录)】的数据库操作Service
* @createDate 2024-04-23 20:51:48
*/
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞/取消点赞
     * @param userId
     * @param postId
     * @return
     */
    long doThumb(long userId, long postId);

}
