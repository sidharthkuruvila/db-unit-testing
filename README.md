Jooq and spring boot
====================

Experiments on adding spring boot to a jooq project

Building and running the tests
------------------------------

### Create a database in postgres called test_migration

    $ psql
    # create database test_migration

### Generate the jooq source files

    gradle generateSampleJooqSchemaSource
 
### Run the db migrations

    gradle update
  
### Run the unit tests

    gradle test
