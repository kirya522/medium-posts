# Minikube demo application, manifests, scripts
All the required scripts are located in __scripts__ folder

##__Requirements:__
1. To run application install: docker, minikube
2. Maven && Java 11
3. Build docker image with maven
4. Run minikube
5. Start deploying and research k8s with scripts

## Example script execution:
Your directory should be root of this mvn project:
```shell
$ echo $PWD 
.../medium-posts/minikube/minikube-demo

$ bash ./scripts/kube-deploy.sh
```

## Feel free to modify and trying different aspects.