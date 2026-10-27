#!/bin/bash
set -e

psql \
  --username "$POSTGRES_USER" \
  --dbname "$POSTGRES_DB" \
  --set=auth_app_password="$AUTH_APP_PASSWORD" \
  --set=ON_ERROR_STOP=1 \
  --file=/scripts/db_init.sql