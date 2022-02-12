#!/bin/bash

./gradlew clean build

version=$(git rev-parse --short HEAD)

docker build --tag=reservation-api:"$version" .

docker tag reservation-api:"$version" aaronburk/reservation-api:"$version"

docker push aaronburk/reservation-api:"$version"

docker run -p8880:8080 reservation-api:"$version"