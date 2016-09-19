FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/spoily.jar /spoily/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/spoily/app.jar"]
