#!/bin/sh

set -e

if [ -z "$HOST_ADDRESS" ]; then
    echo "HOST_ADDRESS is required"
    exit 1
fi

if [ ! -f $NARAYANA_HOME/etc/jbossts-properties.xml ]; then
    cp $NARAYANA_HOME/etc/default-jts-jbossts-properties.xml $NARAYANA_HOME/etc/jbossts-properties.xml
fi

# Modify jbossts-properties.xml
sed -i "s/CONFIGURATION_FILE/NAME_SERVICE/g" $NARAYANA_HOME/etc/jbossts-properties.xml

# Modify jacorb.properties
sed -i "s/#OAPort=4711/OAPort=9998/g" $NARAYANA_HOME/jacorb/etc/jacorb.properties
sed -i "s/#jacorb.ior_proxy_host=1.2.3.4/jacorb.ior_proxy_host=$HOST_ADDRESS/g" $NARAYANA_HOME/jacorb/etc/jacorb.properties

exec "$@"
