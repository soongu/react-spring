create table todotodo.tbl_user
(
    id       varchar(255) null,
    username varchar(255) not null,
    email    varchar(255) null,
    password varchar(255) not null,
    constraint tbl_user_pk
        primary key (id),
    constraint tbl_user_pk2
        unique (email)
);


create table todotodo.tbl_todo
(
    id     varchar(255) null,
    title  varchar(255) null,
    done   varchar(255) null,
    userId varchar(255) null,
    constraint tbl_todo_pk
        primary key (id),
    constraint tbl_todo___fk1
        foreign key (userId) references todotodo.tbl_user (id)
            on delete cascade
);

alter table tbl_user
    add profileImg varchar(255) null;
