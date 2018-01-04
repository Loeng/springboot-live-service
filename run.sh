#!/bin/sh

step=2 #间隔的秒数，不能大于60

for (( i = 0; i < 60; i=(i+step) )); do
  ps -fe|grep 'sgc-service.jar' |grep -v grep
  if [ $? -ne 0 ]
  then
    echo  "`date` [$step] start sgc-service process...." >> /var/cron.log  &&
    nohup /usr/bin/java  /var/www/sgc-java/target/sgc-service.jar >> /var/www/sgc-java/sgc-service.log 2>&1 &
  else
    echo "`date` [$step] sgc-service is running....." >> /var/log/cron.log
  fi
  sleep $step
done

exit 0