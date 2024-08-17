create table USER
(
    ID           INT auto_increment,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    LOGIN        VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT,
    constraint USER_PK
        primary key (ID)
);

comment on column USER.ACCOUNT_ID is 'github or gitee oauthId';

comment on column USER.TOKEN is 'identify user';

comment on column USER.GMT_CREATE is 'Record creation timestamp';

comment on column USER.GMT_MODIFIED is 'Record last modification timestamp';

