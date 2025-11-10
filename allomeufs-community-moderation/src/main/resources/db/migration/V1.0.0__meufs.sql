create type user_type AS ENUM ('MEUF','ADMIN','MODO');

create table users (
                       type varchar(31) not null check ((type in ('user_type','MEUF','ADMIN','MODO'))),
                       id bigint not null,
                       account_verified boolean DEFAULT false not null,
                       first_name varchar(30) not null,
                       last_name varchar(30) not null,
                       mail varchar(90) not null,
                       password bpchar(60) not null,
                       password_verification_code varchar(100),
                       password_verification_timestamp TIMESTAMP,
                       phone_number bpchar(10) not null,
                       profile_pic_url varchar(200),
                       reference bpchar(32) not null,
                       verification_code varchar(100),
                       verification_code_timestamp TIMESTAMP,
                       primary key (id)
);

alter table if exists users
    drop constraint if exists unique_user_mail;

alter table if exists users
    add constraint unique_user_mail unique (mail);

alter table if exists users
    drop constraint if exists unique_user_phone;

alter table if exists users
    add constraint unique_user_phone unique (phone_number);

alter table if exists users
    drop constraint if exists unique_user_reference;

alter table if exists users
    add constraint unique_user_reference unique (reference);

create sequence users_seq start with 1 increment by 1;

CREATE TABLE request_log (
    id bigint not null,
    logged_at TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    payload json not null,
    primary key (id)
)