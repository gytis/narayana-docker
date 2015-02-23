sudo docker build -t transaction-service .
sudo docker rm transaction-service
sudo docker run -it --rm -p 9998:9998 --link name-server:name-server --name transaction-service transaction-service