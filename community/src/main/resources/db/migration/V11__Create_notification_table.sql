create table notification
(
    id         BIGINT auto_increment,
    notifier   BIGINT        not null,
    receiver   BIGINT        not null,
    type       int           not null,
    outer_id   BIGINT        not null,
    gmt_create BIGINT        not null,
    status     int default 0 not null,
    constraint NOTIFICATION_PK
        primary key (id)
);

comment on column notification.notifier is '消息的发送者';

comment on column notification.receiver is '消息的接受者';

comment on column notification.type is '指明outerId的类型';

comment on column notification.outer_id is '回复的问题、回复或者点赞的ID';

comment on column notification.status is '表明已读和未读的状态';

