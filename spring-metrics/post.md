# How to collect metrics from Spring Boot application with micrometer prometheus and grafana full guide

## Preconditions:
1. __Maven__, but you can adapt it for gradle too.
2. __Docker__ and __docker-compose__, prometheus and grafana will be used as containers.
3. __Spring boot 2__ and higher by default, [but it was backported for 1.3-1.5](https://spring.io/blog/2018/03/16/micrometer-spring-boot-2-s-new-application-metrics-collector#what-is-it).

[Since Spring boot 2 __Micrometer__ is default metric collector](https://spring.io/blog/2018/03/16/micrometer-spring-boot-2-s-new-application-metrics-collector). 
And it is simple to expose metrics of application through new actuator approach. It is neccesary to just add metric provider and read them. Here we will use [__Prometheus__](https://prometheus.io/) as metric collector. 

I will use sample application as an example. All the source code will be available on github.

## Setup actuator

## Metrics collection enable

## Prometheus logic of work

## Grafana
### Dashboards

## Results


## Configure custom metric

