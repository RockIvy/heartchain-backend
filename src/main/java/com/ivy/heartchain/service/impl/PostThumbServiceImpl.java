package com.ivy.heartchain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ivy.heartchain.common.ErrorCode;
import com.ivy.heartchain.exception.BusinessException;
import com.ivy.heartchain.mapper.PostThumbMapper;
import com.ivy.heartchain.model.domain.Post;
import com.ivy.heartchain.model.domain.PostThumb;
import com.ivy.heartchain.service.PostService;
import com.ivy.heartchain.service.PostThumbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author ivy
 * @description 针对表【post_thumb(帖子点赞记录)】的数据库操作Service实现
 * @createDate 2024-04-23 20:51:48
 */
@Service
public class PostThumbServiceImpl extends ServiceImpl<PostThumbMapper, PostThumb>
        implements PostThumbService {

    @Resource
    PostService postService;

    @Override
    public long doThumb(long userId, long postId) {
        //判断帖子是否存在
        Post post = postService.getById(postId);
        if(post == null){
            throw  new BusinessException(ErrorCode.NULL_ERROR);
        }
        //判断是否已点赞
        synchronized (String.valueOf(userId).intern()){
            return doThumbInner(userId,postId);
        }
    }

    /**
     * 封装了事务方法
     * @param userId
     * @param postId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int doThumbInner(long userId,long postId){
        PostThumb postThumb = new PostThumb();
        postThumb.setUserid(userId);
        postThumb.setPostid(postId);
        QueryWrapper<PostThumb> wrapper = new QueryWrapper(postThumb);
        long count = this.count(wrapper);
        //已点赞
        if(count > 1){
            boolean result = this.remove(wrapper);
            if(result){
                //帖子点赞数量-1
                postService.update()
                        .eq("id",postId)
//                        .gt("thumb_num",0)
                        .setSql("thumbNum = thumbNum - 1")
                        .update();
                return result ? -1 : 0;
            } else{
                throw  new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }else{
            //未点赞
            boolean result = this.save(postThumb);
            if(result){
                //帖子点赞数量+1
                postService.update()
                        .eq("id",postId)
//                        .gt("thumb_num",0)
                        .setSql("thumbNum = thumbNum + 1")
                        .update();
                return result ? 1 : 0;
            } else{
                throw  new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}