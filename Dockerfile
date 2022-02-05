FROM openjdk:17-alpine
EXPOSE 8787
ADD target/cloud-storage-1.0.0.jar cloud-storage.jar
ENTRYPOINT ["java","-jar","cloud-storage.jar"]