aws dynamodb create-table \
    --table-name IntroNotesLocal \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
        AttributeName=title,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
        AttributeName=title,KeyType=RANGE \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --table-class STANDARD \
    --profile $AWS_PROFILE \
    --endpoint-url http://localhost:8000