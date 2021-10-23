FROM openjdk:12
WORKDIR /app/
COPY ./* /app/
RUN javac ./Lexer/src/*.java
