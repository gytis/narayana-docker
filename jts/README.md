# Build image

    git clone https://github.com/Gytis/narayana-docker.git
    cd narayana-docker/jts
    sudo docker build -t jts .

# Run container

JacORB name server needs to know host address which is accessible from outside the container (and from internet if clients will be remote) when creating IORs. Therefore, when starting the container, HOST_ADDRESS variable is required. You can find out local Docker daemon IP with "ip addr" command.
Also, 9998 and 9999 IP addresses need to be exposed to access transaction service (port 9998) and name server (port 9999).

    sudo docker run -it -e HOST_ADDRESS=<replace with host address> -p 9998:9998 -p 9999:9999 jts

This will start the container in interactive mode. Logs of transaction service, recovery manager, and name server will be merged into one, and tagged respectively: tm, rm, ns.

# Optional configuration

## Exporting logs

TM, RM, and NS logs are stored separately in /home/logs directory. Therefore, they could be exported by mounting host's directory:

    sudo docker run -it -e HOST_ADDRESS=<replace with host address> -p 9998:9998 -p 9999:9999 -v /path/to/logs/dir:/home/logs jts

## Providing Narayana configuration

Narayana configuration is located in /home/narayana/etc/jbossts-properties.xml. Therefore, it could be replaced by mounting host's directory which contains the jbossts-properties.xml file. However, since we use sed to change OrbPortabilityEnvironmentBean.bindMechanism, you have to make sure that it is set to either CONFIGURATION_FILE or NAME_SERVICE.

Narayana logging is configured by /home/narayana/etc/log4j.properties which you can also provide in the same mounted host's directory.