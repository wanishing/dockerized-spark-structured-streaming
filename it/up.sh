set -x
docker-compose -f it/docker-compose.infra.yml up -d
docker wait $(docker-compose -f it/docker-compose.infra.yml ps -q init-kafka)
docker wait $(docker-compose -f it/docker-compose.infra.yml ps -q dependencies)
docker-compose -f it/docker-compose.yml up -d
