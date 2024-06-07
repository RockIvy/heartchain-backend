create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '状态 0-正常',
    phone        varchar(128)                       null comment '电话',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0-普通用户 1-管理员',
    planetCode   varchar(512)                       null comment '星球编号'
)
    comment '用户表';

create table post(
                     id            bigint auto_increment comment 'id' primary key,
                     content       text                               null comment '内容',
                     photo         varchar(1024)                      null comment '照片地址',
                     reviewStatus  int      default 0                 not null comment '状态（0-待审核, 1-通过, 2-拒绝）',
                     reviewMessage varchar(512)                       null comment '审核信息',
                     viewNum       int                                not null default 0 comment '浏览数',
                     thumbNum      int                                not null default 0 comment '点赞数',
                     userId        bigint                             not null comment '创建用户 id',
                     createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
                     updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
                     isDelete      tinyint  default 0                 not null comment '是否删除'
);

alter table post add FOREIGN KEY (userId) REFERENCES user(id);

create table post_thumb
(
    id         bigint auto_increment comment 'id'
        primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '帖子点赞记录';