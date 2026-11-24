CREATE ROLE auth_app
    LOGIN
    PASSWORD :'auth_app_password';

CREATE DATABASE auth_db OWNER auth_app;

\connect auth_db

CREATE SCHEMA auth AUTHORIZATION auth_app;

GRANT CONNECT, CREATE, TEMPORARY
    ON DATABASE auth_db TO auth_app;

GRANT USAGE, CREATE
    ON SCHEMA auth TO auth_app;

ALTER ROLE auth_app IN DATABASE auth_db
    SET search_path TO auth, public;


CREATE ROLE expenses_app
    LOGIN
    PASSWORD :'expenses_app_password';

CREATE DATABASE expenses_db OWNER expenses_app;

\connect expenses_db

CREATE SCHEMA expenses AUTHORIZATION expenses_app;

GRANT CONNECT, CREATE, TEMPORARY
    ON DATABASE expenses_db TO expenses_app;

GRANT USAGE, CREATE
    ON SCHEMA expenses TO expenses_app;

ALTER ROLE expenses_app IN DATABASE expenses_db
    SET search_path TO expenses, public;
