#!/bin/sh

set -e

export TRANSACTION_SERVICE_IP=$(grep "${HOSTNAME}" /etc/hosts | awk '{print $1}')

echo "Starting Narayana Transaction Service container on ${TRANSACTION_SERVICE_IP}:${NARAYANA_PORT}"

# TODO use env variable to get containers name
export NAME_SERVER_IP=$(grep 'name-server' /etc/hosts | awk '{print $1}')
export NAME_SERVER_URL="corbaloc::${NAME_SERVER_IP}:${NAME_SERVER_PORT_9999_TCP_PORT}/StandardNS/NameServer-POA/_root"

if [ ! -f $NARAYANA_HOME/etc/jbossts-properties.xml ]; then
    cp $NARAYANA_HOME/etc/default-jts-jbossts-properties.xml $NARAYANA_HOME/etc/jbossts-properties.xml
fi

sed -i "s/CONFIGURATION_FILE/NAME_SERVICE/g" $NARAYANA_HOME/etc/jbossts-properties.xml
sed -i "s/#OAPort=4711/OAPort=${NARAYANA_PORT}/g" $NARAYANA_HOME/jacorb/etc/jacorb.properties
#sed -i "s/#jacorb.ior_proxy_host=1.2.3.4/jacorb.ior_proxy_host=$TRANSACTION_SERVICE_IP/g" $NARAYANA_HOME/jacorb/etc/jacorb.properties

exec "$@"
