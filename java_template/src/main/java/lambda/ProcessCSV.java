package lambda;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;
import saaf.Inspector;
import saaf.Response;

/**
 * uwt.lambda_test::handleRequest
 *
 * @author Wes Lloyd
 * @author Robert Cordingly
 */
public class ProcessCSV implements RequestHandler<Request, HashMap<String, Object>> {

    /**
     * Lambda Function Handler
     * 
     * @param request Request POJO with defined variables from Request.java
     * @param context 
     * @return HashMap that Lambda will automatically convert into JSON.
     */
    public HashMap<String, Object> handleRequest(Request request, Context context) {
        String bucketname = request.getBucketname();
        String filename = request.getFilename();

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();         
        //get object file using source bucket and srcKey name
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketname, filename));
        //get content of the file
        InputStream objectData = s3Object.getObjectContent();
        //scanning data line by line
        Scanner scanner = new Scanner(objectData);
        long total=0, elements=0;
        while (scanner.hasNext()) {
            String[] nums = scanner.nextLine().split(",");
            for (String num : nums) {
                total += Long.parseLong(num);
                elements++;
            }
        }
        double avg = total/elements;
        scanner.close();

        LambdaLogger logger = context.getLogger();
        logger.log("ProcessCSV bucketname:" + bucketname + " filename:" + filename + " avg-element:" + avg + " total:" + total);

/*
        int val = 0;
        StringWriter sw = new StringWriter();
        Random rand = new Random();

        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                val = rand.nextInt(1000);
                sw.append(Integer.toString(val));
                if ((j+1)!=col)
                    sw.append(",");
                else
                    sw.append("\n");
            }
        }

        byte[] bytes = sw.toString().getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(bytes);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(bytes.length);
        meta.setContentType("text/plain");
        // Create new file on S3
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
        s3Client.putObject(bucketname, filename, is, meta);
*/
        //Collect inital data.
        Inspector inspector = new Inspector();
        inspector.inspectAll();
        
        //****************START FUNCTION IMPLEMENTATION*************************
        //Add custom key/value attribute to SAAF's output. (OPTIONAL)
//        inspector.addAttribute("message", "Hello " + request.getName() 
//                + "! This is an attributed added to the Inspector!");
        
        
        //Create and populate a separate response object for function output. (OPTIONAL)
        Response response = new Response();
        response.setValue("Bucket:" + bucketname + " filename:" + filename + " processed.");
        
        inspector.consumeResponse(response);
        
        //****************END FUNCTION IMPLEMENTATION***************************
        
        //Collect final information such as total runtime and cpu deltas.
        inspector.inspectAllDeltas();
        return inspector.finish();
    }
}
