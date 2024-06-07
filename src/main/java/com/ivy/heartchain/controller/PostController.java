package com.ivy.heartchain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ivy.heartchain.common.BaseResponse;
import com.ivy.heartchain.common.ErrorCode;
import com.ivy.heartchain.common.ResultUtils;
import com.ivy.heartchain.exception.BusinessException;
import com.ivy.heartchain.model.domain.Post;
import com.ivy.heartchain.model.domain.User;
import com.ivy.heartchain.model.request.PostDoThumbRequest;
import com.ivy.heartchain.service.PostService;
import com.ivy.heartchain.service.PostThumbService;
import com.ivy.heartchain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.ivy.heartchain.contant.UserConstant.ADMIN_ROLE;
import static com.ivy.heartchain.contant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {
    @Resource
    PostService postService;
    @Resource
    UserService userService;
    @Resource
    PostThumbService postThumbService;

    @GetMapping("/search/{username}")
    private BaseResponse<List<User>> searchPost(@PathVariable String username,HttpServletRequest request){
        return null;
    }

    @GetMapping("/list")
    private BaseResponse<List<Post>> listPost(HttpServletRequest request){

        List<Post> list = postService.list();
        return ResultUtils.success(list);
    }

    @GetMapping("/listwithuser")
    private BaseResponse<List<Post>> listPostWithUser(HttpServletRequest request){
        //通过审核显示
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reviewStatus",1);
        queryWrapper.orderByDesc("createTime");
        List<Post> postList = postService.list(queryWrapper);
        postList.stream().map(post -> {
            User user = userService.getById(post.getUserId());
            post.setUser(user);
            return null;
        }).collect(Collectors.toList());
        return ResultUtils.success(postList);
    }

    @DeleteMapping("/delete/{id}")
    private BaseResponse<Boolean> deletePost(@PathVariable long id, HttpServletRequest request){
        //仅管理员可删除
       if(!isAdmin(request)){
           throw  new BusinessException(ErrorCode.NO_AUTH_ERROR);
       }
       if(id <= 0){
           throw  new BusinessException(ErrorCode.PARAMS_ERROR);
       }
        boolean b = postService.removeById(id);
        return ResultUtils.success(b);
    }

    @PutMapping("/update")
    private BaseResponse<Boolean> updatePost(@RequestBody Post post,HttpServletRequest request){
        //仅管理员可修改
        if(!isAdmin(request)){
            throw  new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if(post == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(post.getId() <= 0){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = postService.updateById(post);
        return ResultUtils.success(true);
    }

    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody Post post,HttpServletRequest request){
        if(post == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = postService.addPost(post,request);
        return ResultUtils.success(result);
    }

    @PostMapping("/thumb")
    public BaseResponse<Long> postDoThumb(@RequestBody PostDoThumbRequest postDoThumbRequest, HttpServletRequest request){
        if(postDoThumbRequest == null || postDoThumbRequest.getPostId() <= 0){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser =(User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if(loginUser == null){
            throw  new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = loginUser.getId();
        Long postId = postDoThumbRequest.getPostId();
        long result = postThumbService.doThumb(userId, postId);
        return ResultUtils.success(result);
    }


    /**
     * 是否为管理员
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        //仅管理员可查询
        User user =(User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
