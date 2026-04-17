--liquibase formatted sql

--changeset SmykovRoman:create-reservations-schema
--comment create new reservation schema
create schema if not exists reservations;
--rollback drop schema if exists reservations cascade;

-- =============================================
-- 1. Створення таблиці Location
-- =============================================
--changeset SmykovRoman:create-reservations-location-table
--comment create table reservations.locations
create table reservations.locations (
                                        id bigserial primary key,
                                        city varchar(50),
                                        district varchar(50),
                                        location_address varchar(150),
                                        phone_number varchar(30),
                                        work_time_start time,
                                        work_time_end time
);
--rollback drop table if exists reservations.locations;

-- =============================================
-- 2. Doctor (One Location)
-- =============================================
--changeset SmykovRoman:create-reservations-doctor-table
--comment create table reservations.doctor
create table reservations.doctor (
                                     id bigserial primary key,
                                     user_account_id bigint,
                                     speciality varchar(120) not null,
                                     experience integer not null,
                                     location_id bigint not null                    -- ← OneToMany з Location
);
--rollback drop table if exists reservations.doctor;

--changeset SmykovRoman:add-doctor-constraints
--comment add constraints to reservations.doctor
alter table reservations.doctor
    add constraint doctor__user_account__fk
        foreign key (user_account_id) references identity.user_accounts (id);

alter table reservations.doctor
    add constraint doctor__user_account_id__unique
        unique (user_account_id);

alter table reservations.doctor
    add constraint doctor__location__fk
        foreign key (location_id) references reservations.locations (id);
--rollback alter table reservations.doctor drop constraint if exists doctor__user_account__fk;
--rollback alter table reservations.doctor drop constraint if exists doctor__user_account_id__unique;
--rollback alter table reservations.doctor drop constraint if exists doctor__location__fk;

-- =============================================
-- 3. Administrator (One Location)
-- =============================================
--changeset SmykovRoman:create-reservations-administrator-table
--comment create table reservations.administrator
create table reservations.administrator (
                                            id bigserial primary key,
                                            user_account_id bigint not null,
                                            location_id bigint not null                    -- ← OneToMany з Location
);
--rollback drop table if exists reservations.administrator;

--changeset SmykovRoman:add-administrator-constraints
--comment add constraints to reservations.administrator
alter table reservations.administrator
    add constraint administrator__user_account__fk
        foreign key (user_account_id) references identity.user_accounts (id);

alter table reservations.administrator
    add constraint administrator__user_account_id__unique
        unique (user_account_id);

alter table reservations.administrator
    add constraint administrator__location__fk
        foreign key (location_id) references reservations.locations (id);
--rollback alter table reservations.administrator drop constraint if exists administrator__user_account__fk;
--rollback alter table reservations.administrator drop constraint if exists administrator__user_account_id__unique;
--rollback alter table reservations.administrator drop constraint if exists administrator__location__fk;

-- =============================================
-- 4. Видалення старих ManyToMany join-таблиць
-- =============================================
--changeset SmykovRoman:drop-doctors-locations-join-table
--comment remove obsolete many-to-many doctors_locations
drop table if exists reservations.doctors_locations;
--rollback (немає сенсу відновлювати)

--changeset SmykovRoman:drop-administrators-locations-join-table
--comment remove obsolete many-to-many administrators_locations
drop table if exists reservations.administrators_locations;
--rollback (немає сенсу відновлювати)

-- =============================================
-- 5. Reservation
-- =============================================
--changeset SmykovRoman:create-reservations-reservation-table
--comment create table reservations.reservation
create table reservations.reservation (
                                          id bigserial primary key,
                                          reservation_description varchar(120) not null,
                                          reservation_date date not null,
                                          start_time time not null,
                                          end_time time not null,
                                          location_id bigint not null,
                                          doctor_id bigint not null,
                                          user_account_id bigint not null,
                                          is_accepted boolean not null,
                                          is_canceled boolean not null default false
);
--rollback drop table if exists reservations.reservation;

--changeset SmykovRoman:add-reservations-constraints
--comment add constraints to reservations.reservation
alter table reservations.reservation
    add constraint reservation__location__fk
        foreign key (location_id) references reservations.locations (id);

alter table reservations.reservation
    add constraint reservation__user_account__fk
        foreign key (user_account_id) references identity.user_accounts (id);

alter table reservations.reservation
    add constraint reservation__doctor__fk
        foreign key (doctor_id) references reservations.doctor (id);
--rollback alter table reservations.reservation drop constraint if exists reservation__location__fk;
--rollback alter table reservations.reservation drop constraint if exists reservation__doctor__fk;
--rollback alter table reservations.reservation drop constraint if exists reservation__user_account__fk;

-- =============================================
-- 6. Default data
-- =============================================
--changeset SmykovRoman:insert-default-values-locations
--comment insert default location
INSERT INTO reservations.locations (id, location_address, work_time_start, work_time_end, phone_number, city, district)
SELECT 1,
       'Вул. Володимира Мономаха 15',
       '08:00',
       '18:00',
       '+38 093 23 23',
       'Дніпро',
       'ж\м Тополь 1'
    WHERE NOT EXISTS (SELECT 1 FROM reservations.locations WHERE id = 1);
--rollback DELETE FROM reservations.locations WHERE id = 1;

--changeset SmykovRoman:insert-default-values-doctor
--comment insert default doctor
INSERT INTO reservations.doctor (id, user_account_id, speciality, experience, location_id)
VALUES (1, 2, 'Дантист', 5, 1)
    ON CONFLICT (id) DO NOTHING;
--rollback DELETE FROM reservations.doctor WHERE id = 1;

--changeset SmykovRoman:insert-default-values-administrator
--comment insert default administrator
INSERT INTO reservations.administrator (id, user_account_id, location_id)
VALUES (1, 1, 1)
    ON CONFLICT (id) DO NOTHING;
--rollback DELETE FROM reservations.administrator WHERE id = 1;