# Build image

    git clone https://github.com/Gytis/narayana-docker.git
    cd narayana-docker/jts-split/transaction-service
    sudo docker build -t transaction-service .

# Run container

    sudo docker run -it --rm -p 4710:4710 --link name-server:name-server --name transaction-service transaction-service

# Optional configuration

## Exporting object store

In order to keep object store save, it is recommended to store it outside of the docker container. There are two ways of
doing that: providing jbossts-properties.xml with specific options as explained in the next section, or using mounted
directory for object store. Object store is located in container's /home/tx-object-store directory, therefore you can use
-v attribute to mount host's directory as follows:

    sudo docker run -it --rm -p 4710:4710 -v /tmp/tx-object-store:/home/tx-object-store --link name-server:name-server --name transaction-service transaction-service

## Providing Narayana configuration

Narayana configuration is located in /home/narayana/etc/jbossts-properties.xml and it's logging configuration in
/home/narayana/etc/log4j.properties. Therefore, they could be replaced by mounting host's directory which contains
jbossts-properties.xml and log4j.properties files.

NOTE: OrbPortabilityEnvironmentBean.bindMechanism and ObjectStoreEnvironmentBean.objectStoreDir properties are always overwritten with "NAME_SERVICE" and "/home/tx-object-store" respectively.

## Providing Narayana configuration via environment variable

Separate configuration options could be passed via NARAYANA_OPTS environment variable, which is later forwarded to
transaction service. Use docker's -e option for that.