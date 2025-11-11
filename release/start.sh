#!/bin/bash
SERVICE=landlord
MEMORY=512m

Download() {
    APP_FILE=./${SERVICE}.tar.gz
    if [ -e ${APP_FILE} ];then rm -rf ${APP_FILE};fi
    result=$(curl -G 'https://a.ebus.vip/apps/landlord.jar')
    if [ $result = 'newly'  ];then
      echo 'Landlord version is up to date'
      exit 5
    else
      curl -L -o ${APP_FILE} ${result}
      tar -xvf ${APP_FILE}
    fi
}

Start() {
    proc=$(ps -ef | grep ${SERVICE}.jar | grep -v grep | wc -l)
    if [ $proc != 0  ];then
      sleep 3
      echo 'Landlord Is Runing! http://127.0.0.1:7037'
      exit 5
    else
      java -server -Xms${MEMORY} -Xmx${MEMORY}  -jar ./${SERVICE}.jar >> ./${SERVICE}.log &
      echo 'Starting Success! http://127.0.0.1:7037'
    fi
}

Stop() {
    ps aux | grep ${SERVICE}.jar | grep -v grep | awk '{print $2}'| xargs kill -9
    sleep 3
    proc=$(ps -ef | grep ${SERVICE}.jar | grep -v grep | wc -l)
    if [ $proc != 0 ];then
        ps -ef | grep ${SERVICE}.jar | grep -v grep | awk '{print $2}'| xargs kill -9
    fi
    echo 'Landlord Stoped'
}
Install() {
    Stop
    Download
    Start
}
Restart() {
    Stop
    Start
}

case $1 in
    start|run)
        Start
        ;;
    stop)
        Stop
        ;;
    download)
        Download
        ;;
    upgrade)
        Install
        ;;
    restart)
        Restart
        ;;
    *)
        Start
        ;;
esac