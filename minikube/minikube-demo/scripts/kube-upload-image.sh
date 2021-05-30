#!/bin/bash
set -o errexit

source ./scripts/set-env.sh

minikube image load ${DOCKER_IMAGE_NAME}
