./gradlew backend:intronotes:clean backend:intronotes:shadowJar

aws s3 cp backend/intronotes/build/libs/intronotes-0.0.19-SNAPSHOT-all.jar s3://cdk-hnb659fds-assets-352180533495-us-east-1 --profile $AWS_PROFILE