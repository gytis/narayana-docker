# Build image

    git clone https://github.com/Gytis/narayana-docker.git
    cd narayana-docker/jts/name-server
    sudo docker build -t name-server .

# Run container

    sudo docker run -it --rm -p 3528:3528 --name name-server name-server