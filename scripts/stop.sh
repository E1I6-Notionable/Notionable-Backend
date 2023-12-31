#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/notionable.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 현재 구동 중인 애플리케이션 pid 확인
CURRENT_PID=$(pgrep -f "$JAR_FILE")

echo "current pid: $CURRENT_PID" >> "$DEPLOY_LOG"

# 프로세스가 켜져 있으면 종료
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다" >> "$DEPLOY_LOG"
else
  echo "$TIME_NOW > 실행중인 $CURRENT_PID 애플리케이션 종료 " >> "$DEPLOY_LOG"
  kill -15 "$CURRENT_PID"

  sleep 5
  echo "$TIME_NOW > 프로세스 종료 대기 완료" >> "$DEPLOY_LOG"
fi