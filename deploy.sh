#!/bin/bash
yum install -y docker git java-1.8.0-openjdk
service docker start
docker run --name mysql5.7 -p 3306:3306 -v /etc/localtime:/etc/localtime -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7
sleep 10
docker exec -i mysql5.7 mysql -uroot -p123456 < xxl_job.sql
docker exec -i mysql5.7 mysql -uroot -p123456 < wallet_db.sql
# shellcheck disable=SC2164
mkdir /home && cd /home
# shellcheck disable=SC2164
mkdir java-wallet
git clone https://github.com/lailaibtc/xxl-job-admin
# shellcheck disable=SC2164
cd java-wallet
mkdir hsm task webapi
