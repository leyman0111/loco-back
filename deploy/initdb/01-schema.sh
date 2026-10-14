#!/bin/sh
# Выполняется postgres только при первой инициализации тома pgdata.
# Liquibase не создаёт схему сам, поэтому создаём её здесь.
set -e
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<SQL
CREATE SCHEMA IF NOT EXISTS "${DB_SCHEMA:-loco}" AUTHORIZATION "$POSTGRES_USER";
SQL
