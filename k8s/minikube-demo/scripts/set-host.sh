#!/bin/bash
# workdir /
set -o errexit

source ./scripts/set-env.sh

HOST_IP=`kubectl get ingress --namespace=$NAMESPACE | grep 'minikube-demo-http' |  cut -d ' ' -f 10`

sudo echo "$HOST_IP $APP_DEV_HOST" >> /etc/hosts