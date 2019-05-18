-- auto-generated definition
create table persons
(
    id              serial                not null
        constraint persons_pk
            primary key,
    personal_number varchar(16)           not null,
    first_name      text                  not null,
    second_name     text                  not null,
    middle_name     text,
    birth_date      date,
    is_deceased     boolean default false not null,
    birth_place     text                  not null
);

alter table persons
    owner to postgres;

create unique index persons_personal_number_uindex
    on persons (personal_number);

