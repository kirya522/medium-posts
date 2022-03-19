#!/bin/bash
IMAGE_NAME=kirya522/default-image

pushd ../../

./mvnw spring-boot:build-image -DskipTests=true -Dspring-boot.build-image.imageName=$IMAGE_NAME

# get image info
docker image ls | grep $IMAGE_NAME