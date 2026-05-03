#!/usr/bin/env bash
# Run the single Spring Boot jar built with: ./gradlew bootJar
set -euo pipefail
JAR=$(ls build/libs/*.jar 2>/dev/null | head -1)
if [[ -z "${JAR}" ]]; then
  echo "No jar in build/libs — run ./gradlew bootJar first" >&2
  exit 1
fi
exec java -XX:+UseContainerSupport -Xmx1G -jar "${JAR}" "$@"
