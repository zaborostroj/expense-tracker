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
