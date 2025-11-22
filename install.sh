# Stop and Remove existing container
docker ps -aq --filter "name=dynamic-prices" | grep -q . && docker stop dynamic-prices && docker rm -fv dynamic-prices

# Remove current image
docker images --filter "reference=dynamic-prices" | grep -q . && docker image rm dynamic-prices

# Build new image
docker build -t dynamic-prices:latest .

# Run compose file
docker-compose up -d
