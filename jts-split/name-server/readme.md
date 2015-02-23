sudo docker build -t name-server .
sudo docker rm name-server
sudo docker run -it --rm -p 9999:9999 --name name-server name-server