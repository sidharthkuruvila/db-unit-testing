--liquibase formatted sql
--changeset db_migration:0
CREATE TABLE jobs (
  id uuid NOT NULL,
  name character varying(255) NOT NULL
);

CREATE TABLE tasks (
  id uuid NOT NULL,
  job_id uuid NOT NULL,
  task character varying(255) NOT NULL
);
--rollback drop table jobs;
--rollback drop table tasks;


