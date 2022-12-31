FROM gradle:latest AS BUILD
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME
COPY . .
RUN gradle clean build

# базовый образ для сборки JRE
FROM amazoncorretto:17.0.3-alpine as corretto-jdk

# требуется, чтобы работал strip-debug 
RUN apk add --no-cache binutils

# собираем маленький JRE-образ
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /customjre

# главный образ
FROM python:3.7-alpine as progs

RUN python3.7 -m pip install -U yt-dlp
RUN apk add ffmpeg

ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# копируем JRE из базового образа
COPY --from=corretto-jdk /customjre $JAVA_HOME

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/build/libs/ZvookyBotEnhanced-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java","-jar","ZvookyBotEnhanced-1.0-SNAPSHOT.jar"]
#EXPOSE 8081