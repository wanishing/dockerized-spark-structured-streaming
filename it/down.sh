set -x
docker-compose -f it/docker-compose.infra.yml down --remove-orphans
docker-compose -f it/docker-compose.yml down --remove-orphans
