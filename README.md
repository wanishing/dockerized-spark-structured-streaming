
# Background 
This repository demonstrates how to dockerize Spark Structured Streaming with Kafka and LocalStack. For a full description of this project, you are welcome to read the accompanying [medium blogpost](https://medium.com/riskified-technology/dockerizing-spark-structured-streaming-with-kafka-and-localstack-5409a34e1dfc).
# Integration Test
* Run `./create-local-docker.sh` to build the application image on your local machine (this is the image being used in `it/docker-compose.yml`)
* Run `/it/up.sh` to spin up the application along with its dependencies (defined in `it/docker-compose.infra.yml`)
* Run `sbt "project it; IntegrationTest / test"`%    
