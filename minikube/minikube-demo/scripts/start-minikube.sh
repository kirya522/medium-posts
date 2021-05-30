#!/bin/bash

minikube stop
minikube delete
minikube start && \
minikube addons enable registry && \
minikube addons enable ingress
kubectl delete -A ValidatingWebhookConfiguration ingress-nginx-admission