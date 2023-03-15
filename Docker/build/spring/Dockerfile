FROM alpine

RUN apk add openjdk17

RUN mkdir /usr/app

WORKDIR /usr/app

COPY ./target .

CMD ["java", "-jar", "/usr/app/blog.jar"]