#!/bin/sh

set -e

export TRANSACTION_SERVICE_IP=$(grep "${HOSTNAME}" /etc/hosts | awk '{print $1}')

echo "Starting Narayana Transaction Service container on ${TRANSACTION_SERVICE_IP}:${NARAYANA_PORT}"

# TODO use env variable to get containers name
export NAME_SERVER_IP=$(grep 'name-server' /etc/hosts | awk '{print $1}')
export NAME_SERVER_URL="corbaloc::${NAME_SERVER_IP}:${NAME_SERVER_ENV_NAME_SERVER_PORT}/StandardNS/NameServer-POA/_root"

[ -z "$NARAYANA_OPTS" ] && NARAYANA_OPTS=""

exec "$@"
