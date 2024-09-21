alter table ARTICLE_TAG
alter column ARTICLE_ID rename to QUESTION_ID;

alter table ARTICLE_TAG
    rename to QUESTION_TAG;

alter table QUESTION
    add score BIGINT default 0;

comment on column QUESTION.score is '根据评论数和创建时间算出来的分数。';