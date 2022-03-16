#!/bin/bash
IMAGE_NAME=kirya522/small-image

pushd ../

mvn clean install -DskipTests=true

docker build -t $IMAGE_NAME .

# get image info
docker image ls | grep $IMAGE_NAME