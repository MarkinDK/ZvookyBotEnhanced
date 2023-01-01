FROM gradle:latest AS BUILD
WORKDIR /usr/app/
COPY build.gradle settings.gradle /usr/app/
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

ENV PATH="/jre/bin:${PATH}"

# копируем JRE из базового образа
COPY --from=corretto-jdk /customjre /jre/

WORKDIR /usr/app/
COPY --from=BUILD /usr/app/build/libs/ZvookyBotEnhanced-1.0-SNAPSHOT.jar .

ENTRYPOINT ["java","-jar","ZvookyBotEnhanced-1.0-SNAPSHOT.jar"]