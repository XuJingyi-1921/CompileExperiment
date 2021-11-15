FROM openjdk:8
WORKDIR /app/
COPY ./* /app/
RUN javac ./*.java
