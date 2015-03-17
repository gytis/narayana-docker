#!/bin/sh

set -e

export HOST_IP=$(grep "${HOSTNAME}" /etc/hosts | awk '{print $1}')
[ -z "$NAME_SERVER_IP" ] && export NAME_SERVER_IP=$(grep 'name-server' /etc/hosts | awk '{print $1}')
[ -z "$NAME_SERVER_URL" ] && export NAME_SERVER_URL="corbaloc::${NAME_SERVER_IP}:${NAME_SERVER_ENV_NAME_SERVER_PORT}/StandardNS/NameServer-POA/_root"
[ -z "$NARAYANA_OPTS" ] && export NARAYANA_OPTS=""

echo "Starting Narayana Transaction Service container on ${HOST_IP}:${NARAYANA_PORT}"
echo "NARAYANA_OPTS=$NARAYANA_OPTS"
echo "NAME_SERVER_URL=$NAME_SERVER_URL"

exec "$@"