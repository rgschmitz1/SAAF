#!/bin/bash

json_output(){
	printf "\n\nJSON RESULT:\n $(echo $output | jq)\n\n"
}

# JSON object to pass to Lambda Function
json={"\"row\"":50,"\"col\"":10,"\"bucketname\"":"\"test.bucket.562f21.rgs\"","\"filename\"":"\"test.csv\""}

#echo "Invoking Lambda function using API Gateway"
#time output=`curl -s -H "Content-Type: application/json" -X POST -d $json "https://9bjpvtmxra.execute-api.us-east-2.amazonaws.com/hello_dev/"`
#json_output

echo "Invoking Lambda function using AWS CLI"
time output=`aws lambda invoke --invocation-type RequestResponse --function-name CreateCSV --region us-east-2 --payload $json /dev/stdout | head -n 1 | head -c -2 ; echo`
json_output
