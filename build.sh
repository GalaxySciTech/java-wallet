#!/bin/bash
set -e
./gradlew bootJar --no-daemon -x test
rm -rf build/dist
mkdir -p build/dist
cp build/libs/*.jar build/dist/
