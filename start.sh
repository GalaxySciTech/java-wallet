#!/bin/bash
service docker start
docker start mysql5.7
cd /home/java-wallet/xxl-job-admin
nohup java -Xmx1G -jar xxl-job-admin-2.2.0.jar &
cd /home/java-wallet/hsm
nohup java -Xmx1G -jar wallet-hsm-3.0.0.jar &
cd /home/java-wallet/webapi
nohup java -Xmx1G -jar wallet-webapi-3.0.0.jar &
cd /home/java-wallet/task
nohup java -Xmx1G -jar wallet-task-3.0.0.jar &