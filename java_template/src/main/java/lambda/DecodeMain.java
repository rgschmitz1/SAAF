package lambda;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import saaf.Inspector;
import saaf.Response;
import java.util.HashMap;

/**
 * uwt.lambda_test::handleRequest
 *
 * @author Wes Lloyd
 * @author Robert Cordingly
 */
public class DecodeMain implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {

	/**
	 * Lambda Function Handler
	 *
	 * @param request Hashmap containing request JSON attributes.
	 * @param context
	 * @return HashMap that Lambda will automatically convert into JSON.
	 */
	public HashMap<String, Object> handleRequest(HashMap<String, Object> request, Context context) {

		//Collect initial data.
		Inspector inspector = new Inspector();
		inspector.inspectAll();

		//****************START FUNCTION IMPLEMENTATION*************************
		String message=(String) request.get("msg");
		StringBuilder decryptedMessage = new StringBuilder();
		int key=(Integer)request.get("shift") % 26;

		for (char ch : message.toCharArray()) {
			if (ch >= 'a' && ch <= 'z') {
				ch = (char)(ch - key);
				if (ch < 'a')
					ch = (char)(ch + 26);
			} else if(ch >= 'A' && ch <= 'Z') {
				ch = (char)(ch - key);
				if (ch < 'A')
					ch = (char)(ch + 26);
			}
			decryptedMessage.append(ch);
		}

		//Add custom key/value attribute to SAAF's output. (OPTIONAL)
		inspector.addAttribute("msg", decryptedMessage.toString());

		//Create and populate a separate response object for function output. (OPTIONAL)
		//Response response = new Response();
		//response.setValue(decryptedMessage.toString());

		//inspector.consumeResponse(response);
		//****************END FUNCTION IMPLEMENTATION***************************

		//Collect final information such as total runtime and cpu deltas.
		inspector.inspectAllDeltas();
		return inspector.finish();
	}

	public static void main (String[] args) {
		Context c = new Context() {
			@Override
			public String getAwsRequestId() {
				return "";
			}

			@Override
			public String getLogGroupName() {
				return "";
			}

			@Override
			public String getLogStreamName() {
				return "";
			}

			@Override
			public String getFunctionName() {
				return "";
			}

			@Override
			public String getFunctionVersion() {
				return "";
			}

			@Override
			public String getInvokedFunctionArn() {
				return "";
			}

			@Override
			public CognitoIdentity getIdentity() {
				return null;
			}

			@Override
			public ClientContext getClientContext() {
				return null;
			}

			@Override
			public int getRemainingTimeInMillis() {
				return 0;
			}

			@Override
			public int getMemoryLimitInMB() {
				return 0;
			}

			@Override
			public LambdaLogger getLogger() {
				return new LambdaLogger() {
					@Override
					public void log(String string) {
						System.out.println("LOG:" + string);
					}
				};
			}
		};

		DecodeMain hm = new DecodeMain();

		// Create a request hash map
		HashMap req = new HashMap();

		// Grab the args from the command line
		String msg = (args.length > 0 ? args[0] : "");
		Integer shift = Integer.parseInt(args.length > 0 ? args[1] : "");

		// Load the msg into the request hashmap
		req.put("msg", msg);
		req.put("shift", shift);

		// Report msg to stdout
		System.out.println("cmd-line param msg=" + req.get("msg"));
		System.out.println("cmd-line param shift=" + req.get("shift"));

		// Run the function
		HashMap resp = hm.handleRequest(req, c);

		// Print out function result
		System.out.println("function result:" + resp.toString());
	}
}
