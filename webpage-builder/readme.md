sudo docker build --tag narayana.io-builder .
sudo docker run --rm -it -v /tmp/narayana.io:/home/narayana.io narayana.io-builder
