alter table QUESTION
alter column CREATOR BIGINT;

alter table COMMENT
alter column COMMENTATOR BIGINT not null;