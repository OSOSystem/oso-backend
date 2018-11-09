#!/bin/bash
set -e

PG_USER=""
PG_PASSWORD=""
PG_DB="oso"

if [ -z ${DB_USER+x} ] && [ -z ${DB_PASSWORD+x} ]; then
    PG_USER="developer"
    PG_PASSWORD="ososystem"
else
    PG_USER=${DB_USER}
    PG_PASSWORD=${DB_PASSWORD}
fi

echo "USER: ${PG_USER}"
echo "PASSWORD: ${PG_PASSWORD}"

psql -v ON_ERROR_STOP=1 --username postgres <<-EOSQL
    CREATE USER ${PG_USER} WITH PASSWORD '${PG_PASSWORD}';
    CREATE DATABASE ${PG_DB};
    CREATE DATABASE keycloak;
    ALTER DATABASE ${PG_DB} OWNER TO ${PG_USER};
    ALTER DATABASE keycloak OWNER TO ${PG_USER};
    GRANT ALL PRIVILEGES ON DATABASE "${PG_DB}" TO ${PG_USER};
    GRANT ALL PRIVILEGES ON DATABASE "keycloak" TO ${PG_USER};
EOSQL