#!/bin/bash

VERSION=4.5.0.Final
BIN_FOLDER=keycloak-${VERSION}/bin
HOST=localhost
PORT=8180

ADMIN_USER=admin
ADMIN_PASSWORD=nimda
REALM=osorealm
USERNAME=john

wget https://downloads.jboss.org/keycloak/${VERSION}/keycloak-${VERSION}.zip
if [ $? -ne 0 ]; then
    exit 1
fi

echo "Unpacking Keycloak..."
unzip -q keycloak-${VERSION}.zip

echo "Creating initial admin account"
${BIN_FOLDER}/add-user-keycloak.sh -r master -u ${ADMIN_USER} -p ${ADMIN_PASSWORD}

# 8080 is the default port of Keycloak
let OFFSET=${PORT}-8080
echo "Starting Keycloak instance"
${BIN_FOLDER}/standalone.sh -Djboss.socket.binding.port-offset=${OFFSET} > /dev/null &

# we have to ensure that the server is up before sending requests to it
while ! nc -z ${HOST} ${PORT}
do
    echo "Pinging Keycloak instance to see if it's up"
    sleep 8
done

echo "Keycloak instance is up"

echo "Logging into admin account"
${BIN_FOLDER}/kcadm.sh config credentials --server http://${HOST}:${PORT}/auth --realm master --user ${ADMIN_USER} --password ${ADMIN_PASSWORD}

echo "Creating realm named ${REALM}"
${BIN_FOLDER}/kcadm.sh create realms -s realm=${REALM} -s enabled=true

echo "Creating client"
${BIN_FOLDER}/kcadm.sh create clients -r ${REALM} -s clientId=login-app -s 'redirectUris=["http://localhost:8080/*"]'

echo "Creating user role"
${BIN_FOLDER}/kcadm.sh create roles -r ${REALM} -s name=user

echo "Creating user called ${USERNAME}"
${BIN_FOLDER}/kcadm.sh create users -r ${REALM} -s username=${USERNAME} -s enabled=true

echo "Adding user role to ${USERNAME}"
${BIN_FOLDER}/kcadm.sh add-roles -r osorealm --uusername ${USERNAME} --rolename user

rm keycloak-${VERSION}.zip

echo "Shutting down Keycloak instance"

PID=$(netstat -tulpn 2>/dev/null | grep ${PORT} | tr -s ' ' | cut -d ' ' -f7 | tr -d /java)
kill ${PID}