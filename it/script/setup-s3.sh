#!/bin/bash
set -x
aws s3api --endpoint-url=http://"${AWS_S3_ENDPOINT}" delete-bucket --bucket "${BUCKET}" --region us-east-1
aws s3api --endpoint-url=http://"${AWS_S3_ENDPOINT}" create-bucket --bucket "${BUCKET}" --region us-east-1

