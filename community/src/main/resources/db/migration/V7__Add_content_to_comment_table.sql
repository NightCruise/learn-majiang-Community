alter table COMMENT
    add content varchar(1024) not null;

comment on column COMMENT.content is '评论内容';