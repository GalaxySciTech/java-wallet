#!/bin/bash
./gradlew bootRepackage
rm -rf build/*
mv wallet-webapi/build/libs/*.jar build/
mv wallet-task/build/libs/*.jar build/
mv wallet-hsm/build/libs/*.jar build/
