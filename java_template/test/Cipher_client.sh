#!/bin/bash

json_output(){
	printf "\nJSON RESULT:\n $(echo "$1" | jq)\n\n"
}

# JSON object to pass to Lambda Function
json_object() {
	cat<<-OBJ
	{
		\"msg\": \"${1}\",
		\"shift\": ${2}
	}
	OBJ
}

echo "Invoking Lambda function for Encoding using API Gateway"
offset=22
json=$(json_object ServerlessComputingWithFaaS $offset | xargs)
output=`curl -s -H "Content-Type: application/json" -X POST -d "$json" "https://vadstqcyo9.execute-api.us-east-2.amazonaws.com/encode_dev/"`
json_output "$output"

echo "Invoking Lambda function for Decoding using API Gateway"
encoded_msg=$(echo $output | jq -r '.msg')
json=$(json_object $encoded_msg $offset | xargs)
output=`curl -s -H "Content-Type: application/json" -X POST -d "$json" "https://ivjgcc3oug.execute-api.us-east-2.amazonaws.com/decode_dev/"`
json_output "$output"
