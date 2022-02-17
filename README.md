# Coding Challenge
This project is to meet all the requirements given in the coding challenge. 
It also includes the following parts of the exercise.

Part 1 - The Web Service

Part 3 - Describe how to run your application

Part 4 - Dockerize It (Please note this is a bonus section)

Part 5 - Kubeify it (Please note this is a bonus section)

Part 6 - Productionize It (Please note this is a bonus section)
For this part, The application can be scaled in production environment using auto-scale configuration in kubernetes to spin new pods to support higher loads.
Also, There can be other components like load balancers, gateway components to support scale in production.

### Pre-requisites

- Java11
- Maven
- Docker Desktop
- Kubernetes
- Helm Chart

### Steps to run the application

#### Install Zookeeper

Install the zookeeper with the following commands as it is dependency for kafka cluster management.

```shell
helm repo add bitnami https://charts.bitnami.com/bitnami
```

```shell
helm install zookeeper bitnami/zookeeper \
--set replicaCount=1 \
--set auth.enabled=false \
--set allowAnonymousLogin=true
```

Wait until the pod is up and running

#### Install Kafka

Kafka is used as the broker for message queue. Use the following commands to install Kafka.

```shell
helm install kafka bitnami/kafka \
--set zookeeper.enabled=false \
--set replicaCount=1 \
--set externalZookeeper.servers=zookeeper.default.svc.cluster.local
```

#### Install Postgres

For this application PostgreSQL is used as the database. Use the following command to install PostgreSQL.

```shell
helm install postgresql bitnami/postgresql --set global.postgresql.auth.username=admin \
--set global.postgresql.auth.password=admin --set=global.postgresql.auth.database=task
```

```shell
kubectl run postgresql-client --rm --tty -i --restart='Never' --namespace default \
--image docker.io/bitnami/postgresql:14.1.0-debian-10-r80 --env="PGPASSWORD=$POSTGRES_PASSWORD" \
--command -- psql --host postgresql -U admin -d task -p 5432
```

#### Build and Deploy Task App

Use the following command to build the jar file and create docker image and deploy in kubernetes cluster.

```shell
mvn clean package

docker build -t task-app:latest .

kubectl apply -f app-deployment.yaml
```

#### Testing App

To call an API request with `POST` method use the following steps.
```shell

# To list pods
kubectl get pods

# To enter into task pod
kubectl  exec -it <tas-pod-name> -- bash

# To execute POST request
curl -X POST http://localhost:8080/data \
-H "Content-Type: application/json" \
-d '{ "ts":"012345670", "sender":"demo", "message": { "foo":"foo", "bar": "bar" }, "sent-from-ip":"0.0.0.0", "priority": 2 }'

```

To monitor the messages being sent by the application, use the following Kafka Client with consumer to view the messages.

```shell
kubectl run kafka-client --restart='Never' --image docker.io/bitnami/kafka:3.1.0-debian-10-r20 --namespace default --command -- sleep infinity
kubectl exec --tty -i kafka-client --namespace default -- bash

kafka-console-consumer.sh \

            --bootstrap-server kafka.default.svc.cluster.local:9092 \
            --topic message \
            --from-beginning
```

#### Note: The process can be simplified by using ingress module.
