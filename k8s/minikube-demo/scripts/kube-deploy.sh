#!/bin/bash
# workdir /
set -o errexit

source ./scripts/set-env.sh

mkdir -p ./target/k8s/configs
rm -f ./target/k8s/configs/* || true

for file in ./k8s/*.yml; do
  eval "echo \"$(cat ${file})\"" > ./target/k8s/configs/$(basename "$file")
done


kubectl apply -f ./target/k8s/configs/minikube-demo-namespace.yml
kubectl apply -f ./target/k8s/configs --recursive