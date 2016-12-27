cat <<FILE_DELIM > "migrations/`date +"%Y%m%d%H%M%S"`_$1.sql"
--liquibase formatted sql
--changeset db_migration:0


--rollback 


FILE_DELIM

