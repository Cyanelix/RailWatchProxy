#!/bin/bash
set -ev

cat "Branch: $TRAVIS_BRANCH"

if [ "$TRAVIS_BRANCH" == "master" ]; then
    docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD";
    docker push USER/REPO;
fi
