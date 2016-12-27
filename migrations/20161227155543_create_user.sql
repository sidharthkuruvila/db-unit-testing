--liquibase formatted sql
--changeset db_migration:0

CREATE TABLE users (
  id uuid NOT NULL,
  username character varying(255) NOT NULL,
  password character varying(255),
  created_at timestamp with time zone,
  updated_at timestamp with time zone,
  first_name character varying(255),
  last_name character varying(255)
);

--rollback drop table users