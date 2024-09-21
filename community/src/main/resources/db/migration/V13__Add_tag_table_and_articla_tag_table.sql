create table TAG
(
    id           bigint auto_increment,
    name         varchar(100) not null,
    count        int default 0,
    gmt_create   bigint,
    gmt_modified bigint,
    constraint TAG_PK
        primary key (id)
);

comment on column TAG.name is '标签名字';

comment on column TAG.count is '全部文章统计下标签的数量';

create unique index TAG_NAME_UINDEX
    on TAG (name);

create table article_tag
(
    id           bigint auto_increment,
    article_id   bigint not null,
    tag_id       bigint not null,
    gmt_create   bigint,
    gmt_modified bigint,
    constraint ARTICLE_TAG_PK
        primary key (id)
);



