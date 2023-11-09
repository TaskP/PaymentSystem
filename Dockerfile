FROM eclipse-temurin:17-alpine
MAINTAINER TaskP
RUN mkdir -p /opt/taskp/data
COPY build/libs/PaymentSystem.jar /opt/taskp/PaymentSystem.jar
COPY data/merchants.csv /opt/taskp/data/merchants.csv
COPY data/users.csv /opt/taskp/data/users.csv
RUN echo '#!/bin/bash' > /opt/taskp/import.sh
RUN echo 'java -cp /opt/taskp/PaymentSystem.jar \' >> /opt/taskp/import.sh
RUN echo '-Dspring.profiles.active=cli \' >> /opt/taskp/import.sh
RUN echo '-Dloader.main=com.example.payment.app.main.AppCliUserImport \' >> /opt/taskp/import.sh
RUN echo 'org.springframework.boot.loader.PropertiesLauncher /opt/taskp/data/users.csv && \' >> /opt/taskp/import.sh
RUN echo 'echo " " && echo " " && \' >> /opt/taskp/import.sh
RUN echo 'java -cp /opt/taskp/PaymentSystem.jar \' >> /opt/taskp/import.sh
RUN echo '-Dspring.profiles.active=cli \' >> /opt/taskp/import.sh
RUN echo '-Dloader.main=com.example.payment.app.main.AppCliMerchantImport \' >> /opt/taskp/import.sh
RUN echo 'org.springframework.boot.loader.PropertiesLauncher /opt/taskp/data/merchants.csv && \' >> /opt/taskp/import.sh
RUN echo 'echo " " && echo " " && echo "Done" && exit' >> /opt/taskp/import.sh
RUN chmod +x /opt/taskp/import.sh
ENTRYPOINT ["java","-jar","/opt/taskp/PaymentSystem.jar"]