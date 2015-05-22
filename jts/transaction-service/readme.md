# Build image

    git clone https://github.com/Gytis/narayana-docker.git
    cd narayana-docker/jts/transaction-service
    sudo docker build -t transaction-service .

# Run container

    sudo docker run -it --rm -p 4710:4710 --link name-server:name-server --name transaction-service transaction-service

## Prerequisite

Transaction service requires [JacORB name server](../name-server) to be started.

# Optional configuration

## Using an external object store

In order to keep the contents of the object store safe, it is recommended to store it outside of the docker container. There are two ways of doing that: providing a jbossts-properties.xml with any standard Narayana options (such as connecting to a JBDC object store) as explained in the next section, or mounting a directory to the containers host for object store. With the way we have chosen to configure the docker container, the object store is by default located in container's /home/tx-object-store directory, therefore you can use
-v attribute to mount host's directory as follows:

    sudo docker run -it --rm -p 4710:4710 -v /<REQUIRED_FOLDER_ON_HOST>/:/home/tx-object-store --link name-server:name-server --name transaction-service transaction-service

## Providing Narayana configuration

Narayana configuration is located in /home/narayana/etc/jbossts-properties.xml and it's logging configuration in
/home/narayana/etc/log4j.properties. These can be replaced by mounting host's directory which contains
jbossts-properties.xml and log4j.properties files.

NOTE: In the docker configuration we have provided, the OrbPortabilityEnvironmentBean.bindMechanism and ObjectStoreEnvironmentBean.objectStoreDir properties will always be overwritten with "NAME_SERVICE" and "/home/tx-object-store" respectively. You would need to modify the dockerfile to change that.

## Providing Narayana configuration via environment variable

Individual configuration options can be passed via the NARAYANA_OPTS environment variable. Use docker's -e option for that.

-e "NARAYANA_OPTS=\"-DObjectStoreEnvironmentBean.objectSreDir=/home/object-store\""

## Providing external libraries

For the particular use cases, such as JDBC object store usage, external libraries have to be provided for Narayana. This can be achieved by mounting directory containing required jars to /home/lib directory. All jars from this directory will be added to the class path. NOTE: directory is not scanned recursively.

# Run tests

1. Make sure that docker daemon is started with the following command:

    sudo docker -H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock -d

2. Executed tests with maven as usual:

    mvn clean test
