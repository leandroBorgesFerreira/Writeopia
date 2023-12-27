gcloud api-gateway api-configs create writeopia-config \
  --api=writeopia-api --openapi-spec=openapi2-run.yaml \
  --project=writeopia --backend-auth-service-account=writeopia-service-account@writeopia.iam.gserviceaccount.com