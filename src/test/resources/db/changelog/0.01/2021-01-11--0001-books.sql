--liquibase formatted sql

--changeset sergey.lobin:2021-01-11-001-books
create table books (
 id bigserial IDENTITY NOT NULL PRIMARY KEY,
 title varchar(255),
 description VARCHAR(255)
)