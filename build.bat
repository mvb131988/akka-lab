@echo off

CALL host

docker build -t host-0 -f Dockerfile-app .

kubectl delete deployment host1-deployment
kubectl delete deployment host2-deployment
kubectl delete deployment host3-deployment

kubectl apply -f chart\host1-deployment.yaml
kubectl apply -f chart\host2-deployment.yaml
kubectl apply -f chart\host3-deployment.yaml