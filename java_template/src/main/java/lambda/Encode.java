package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import saaf.Inspector;
import saaf.Response;
import java.util.HashMap;

/**
 * uwt.caesarcipher::handleRequest
 *
 * @author Bob Schmitz
 */
public class Encode implements RequestHandler<HashMap<String, Object>, HashMap<String, Object>> {

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
		StringBuilder encryptedMessage = new StringBuilder();
		int key=(Integer) request.get("shift") % 26;

		for (char ch : message.toCharArray()) {
			if (ch >= 'a' && ch <= 'z') {
				ch = (char)(ch + key);
				if (ch > 'z')
					ch = (char)(ch - 26);
			} else if (ch >= 'A' && ch <= 'Z') {
				ch = (char)(ch + key);
				if (ch > 'Z')
					ch = (char)(ch - 26);
			}
			encryptedMessage.append(ch);
		}

		//Add custom key/value attribute to SAAF's output. (OPTIONAL)
		inspector.addAttribute("msg", encryptedMessage.toString());

		//Create and populate a separate response object for function output. (OPTIONAL)
		//Response response = new Response();
		//response.setValue(encryptedMessage.toString());

		//inspector.consumeResponse(response);
		//****************END FUNCTION IMPLEMENTATION***************************

		//Collect final information such as total runtime and cpu deltas.
		inspector.inspectAllDeltas();
		return inspector.finish();
	}
}
