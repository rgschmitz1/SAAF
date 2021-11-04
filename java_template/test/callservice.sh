#!/bin/bash

json_output(){
	printf "\n\nJSON RESULT:\n $(echo $output | jq)\n\n"
}

# JSON object to pass to Lambda Function
json={"\"name\"":"\"Susan\u0020Smith\",\"param1\"":1,\"param2\"":2,\"param3\"":3}

echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H "Content-Type: application/json" -X POST -d $json "https://9bjpvtmxra.execute-api.us-east-2.amazonaws.com/hello_dev/"`
json_output

echo "Invoking Lambda function using AWS CLI"
time output=`aws lambda invoke --invocation-type RequestResponse --function-name hello --region us-east-2 --payload $json /dev/stdout | head -n 1 | head -c -2 ; echo`
json_output
