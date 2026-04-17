--liquidbase formatted sql

--changeset SmykovRoman:create-identity-schema
--comment create new identity schema
create schema identity;
--rollback drop schema identity;

--changeset SmykovRoman:create-identity-user_accounts-table
--comment create table identity.user_accounts
create table identity.user_accounts
(
    id          serial primary key,
    password    varchar(128) not null ,
    first_name   varchar(50) not null ,
    last_name    varchar(80) not null ,
    phone_number varchar(12) not null
);
--rollback drop table identity.user_accounts;

--changeset SmykovRoman:insert-default-values-into-identity-user_accounts-table
--comment insert default values into identity.user_accounts

INSERT INTO identity.user_accounts (id, password, first_name, last_name, phone_number)
VALUES (1,  '{bcrypt}$2a$10$SkBejdz2WzUUBQei3ACS9.z8yhjyim0c6jUfRGj4tcWzbHjYxe/7q', 'Анастасія', 'Глибко', '1'),
       (2,  '{bcrypt}$2a$10$SkBejdz2WzUUBQei3ACS9.z8yhjyim0c6jUfRGj4tcWzbHjYxe/7q', 'Василій', 'Симонько', '2'),
       (3,  '{bcrypt}$2a$10$SkBejdz2WzUUBQei3ACS9.z8yhjyim0c6jUfRGj4tcWzbHjYxe/7q', 'UUU', 'uuu', '3');

--rollback DELETE FROM identity.user_accounts WHERE id IN (1, 2, 3);


--changeset SmykovRoman:create-identity-user_roles-table
--comment create table identity.user_roles
create table identity.user_roles(
    id          serial primary key,
    authority   varchar(32) unique not null
);
--rollback drop table identity.user_roles;

--changeset SmykovRoman:insert-default-values-into-identity-user_roles-table
--comment insert default values into identity.user_roles

INSERT INTO identity.user_roles (id, authority)
VALUES (1, 'ROLE_USER'), (2, 'ROLE_DOCTOR'), (3, 'ROLE_ADMIN');

--rollback DELETE FROM identity.user_roles WHERE id IN (1, 2, 3);

--changeset SmykovRoman:create-identity-user_accounts_roles-table
--comment create table identity.user_accounts_roles
create table identity.user_accounts_roles(
    user_account_id integer not null ,
    user_role_id integer not null
);
--rollback drop table identity.user_accounts_roles;

--changeset SmykovRoman:add-user_accounts_roles-constraints
--comment add constraints to user_accounts_roles
alter table identity.user_accounts_roles
    add constraint user_accounts_roles__user_roles__fk
        foreign key (user_role_id) references identity.user_roles (id);

alter table identity.user_accounts_roles
    add constraint user_accounts_roles__user_accounts__fk
        foreign key (user_account_id) references identity.user_accounts (id);

alter table identity.user_accounts_roles
    add constraint user_accounts_roles_unique
        unique (user_account_id, user_role_id);
--rollback alter table identity.user_accounts_roles drop constraint user_accounts_roles__user_roles__fk;
--rollback alter table identity.user_accounts_roles drop constraint user_accounts_roles__user_accounts__fk;
--rollback alter table identity.user_accounts_roles drop constraint user_accounts_roles_unique;

--changeset SmykovRoman:insert-default-values-into-identity.user_accounts_roles-table
--comment insert default values into identity.identity.user_accounts_roles

INSERT INTO identity.user_accounts_roles (user_account_id, user_role_id)
VALUES (1, 3), (2, 2), (3, 1);

--rollback DELETE FROM identity.user_accounts_roles WHERE user_account_id IN (1, 2, 3);