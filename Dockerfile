FROM openjdk:12
WORKDIR /app/
COPY ./* /app/
RUN javac ./*.java
