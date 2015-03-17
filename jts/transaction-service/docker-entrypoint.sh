#!/bin/sh

set -e

export TRANSACTION_SERVICE_IP=$(grep "${HOSTNAME}" /etc/hosts | awk '{print $1}')

echo "Starting Narayana Transaction Service container on ${TRANSACTION_SERVICE_IP}:${NARAYANA_PORT}"

# TODO use env variable to get containers name
export NAME_SERVER_IP=$(grep 'name-server' /etc/hosts | awk '{print $1}')
export NAME_SERVER_URL="corbaloc::${NAME_SERVER_IP}:${NAME_SERVER_ENV_NAME_SERVER_PORT}/StandardNS/NameServer-POA/_root"

# Initialize Narayana configuration file, if user didn't provide one
if [ ! -f $NARAYANA_HOME/etc/jbossts-properties.xml ]; then
    cp $NARAYANA_HOME/etc/default-jts-jbossts-properties.xml $NARAYANA_HOME/etc/jbossts-properties.xml
fi

# Modify Narayana configuration to set object store directory
sed -ir "s/<entry key=\"ObjectStoreEnvironmentBean.objectStoreDir\">.*<\/entry>/<entry key=\"ObjectStoreEnvironmentBean.objectStoreDir\">\/home\/tx-object-store<\/entry>/g" $NARAYANA_HOME/etc/jbossts-properties.xml

# Modify Narayana configuration to use name server
sed -ir "s/<entry key=\"OrbPortabilityEnvironmentBean.bindMechanism\">.*<\/entry>/<entry key=\"OrbPortabilityEnvironmentBean.bindMechanism\">NAME_SERVICE<\/entry>/g" $NARAYANA_HOME/etc/jbossts-properties.xml

# Modify JacORB configuration to use a specific port
sed -i "s/#OAPort=4711/OAPort=${NARAYANA_PORT}/g" $NARAYANA_HOME/jacorb/etc/jacorb.properties

# Modify JacORB log level
sed -i "s/jacorb.log.default.verbosity=4/jacorb.log.default.verbosity=3/g" $NARAYANA_HOME/jacorb/etc/jacorb.properties

[ -z "$NARAYANA_OPTS" ] && NARAYANA_OPTS=""

exec "$@"
