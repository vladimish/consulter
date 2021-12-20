#!/bin/sh -v
kubectl apply -f mysql-auth-config.yaml
kubectl apply -f rabbitmq-config.yaml
kubectl apply -f mysql-auth-secret.yaml
kubectl apply -f rabbitmq-secret.yaml
kubectl apply -f gateway-config.yaml
kubectl apply -f mysql-timetable-secret.yaml
kubectl apply -f mysql-timetable-config.yaml

kubectl apply -f mysql-timetable.yaml
kubectl apply -f rabbitmq.yaml
kubectl apply -f mysql-auth.yaml
kubectl apply -f gateway.yaml
kubectl apply -f auth.yaml
kubectl apply -f frontend.yaml
kubectl apply -f timetable.yaml