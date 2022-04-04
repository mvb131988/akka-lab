(0) minikube docker-env 
	then copy all env variables from the command output 
(1) host.bat
(2) docker build -t host-0 -f Dockerfile-app .
(3) kubectl apply -f chart\host1-deployment.yaml

=============================================================================================================================

# Build docker image using name host-0
docker build -t host-0 -f Dockerfile-app .

docker run -d host-0
docker exec -it host-0 /bin/bash

docker-compose up -d
docker-compose stop

docker exec -it host-0 /bin/bash

nohup java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:4000 host1.jar 1 > log.txt 2>&1 &

=============================================================================================================================

(1) Add maven-assembly-plugin and maven-shade-plugin. Otherwise no .jar file is producing 
during maven build

Use: 
mvn assembly:assembly -DskipTests

(2) Change application.conf to include only local1.conf

(3) Change postgres version to 42.2.14, otherwise there would be an exception on startup 
(on hibernate session factory creation)

(4) Change database name in config files to postgres-db

(5) When starting .jar from the container set up address 0.0.0.0 together with debug port
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:4000

#nohup java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:4000 app1-shade.jar > log.txt 2>&1 &

Otherwise debugger is not connecting to the app, even if port forwarding is configured

=============================================================================================================================

To build local image and run it in minikube:

(1) Use the following command that outputs environment variables needed to point the local Docker 
daemon to the minikube internal Docker registry(when local image is build with Docker by default it
goes to Docker local registry that is unaccessible by minikube). Use comments on how to apply
this variables(comments are present in the command output):
 
minikube docker-env

(2) Edit autogeneretaed deployment:
 
kubectl edit deployment kube-test-depl
Change property to:
imagePullPolicy: Never

====================================================================================================
To access db in kubernetes cluster create LoadBalancer service with nodePort

In a separate window run (this way external ip would be assigned to the service):
minikube tunnel

Acees db using port(with a tunnel not a nodePort)

=============================================================================================================================

To copy logs:

kubectl cp <pod1_name>:/tmp/log.txt .\log1.txt

