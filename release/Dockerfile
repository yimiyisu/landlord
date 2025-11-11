FROM eclipse-temurin:17-jre

ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8

WORKDIR /app

COPY landlord.jar /app/

EXPOSE 7037

CMD ["java","-Dfile.encoding=UTF-8", "-jar", "/app/landlord.jar"]