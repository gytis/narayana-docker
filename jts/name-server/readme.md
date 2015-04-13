# Build image

    git clone https://github.com/Gytis/narayana-docker.git
    cd narayana-docker/jts/name-server
    sudo docker build -t name-server .

# Run container

    sudo docker run -it --rm -p 3528:3528 --name name-server name-server
    
# Run tests

1. Make sure that docker daemon is started with the following command:

    sudo docker -H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock -d

2. Executed tests with maven as usual:

    mvn clean test
