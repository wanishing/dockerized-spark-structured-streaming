
* Run `./create-local-docker.sh` to build the application image on your local machine (this is the image being used in `it/docker-compose.yml`)
* Run `/it/up.sh` to spin up the application along with its dependencies (defined in `it/docker-compose.infra.yml`)
* Run `sbt "project it; IntegrationTest / test"`%    