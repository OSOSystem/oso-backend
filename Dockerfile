FROM openjdk:8-jre-alpine3.8
COPY ./build/libs/*.jar oso-backend.jar

ENV DB_NAME oso
ENV DB_URL jdbc:postgresql://postgres:5432/${DB_NAME}?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false
ENV DB_USER developer
ENV DB_PASSWORD ososystem
ENV MAIL_USER ""
ENV MAIL_PASSWORD ""
ENV SMTP_HOST ""
ENV SMTP_PORT ""

EXPOSE 8000 8081 9999

ENTRYPOINT ["java", \
"-Dspring.datasource.url=${DB_URL}", \
"-Dspring.datasource.username=${DB_USER}", \
"-Dspring.datasource.password=${DB_PASSWORD}", \
"-Dspring.mail.host=${SMTP_HOST}", \
"-Dspring.mail.port=${SMTP_PORT}", \
"-Dspring.mail.username=${MAIL_USER}", \
"-Dspring.mail.password=${MAIL_PASSWORD}", \
"-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000", \
"-jar", \
"oso-backend.jar"]