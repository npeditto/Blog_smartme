FROM alpine

RUN apk add openjdk17 tzdata

RUN cp /usr/share/zoneinfo/Europe/Rome /etc/localtime && echo "Europe/Rome" >  /etc/timezone 

RUN mkdir /usr/app

WORKDIR /usr/app

COPY blog.jar .

CMD ["java", "-jar", "/usr/app/blog.jar"]