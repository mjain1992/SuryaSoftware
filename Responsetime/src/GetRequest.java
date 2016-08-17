import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;

public class GetRequest implements Callable<Response>{	
	private static final String EMAIL_ID = "gps@surya-soft.com";
	private static final String URL = "http://surya-interview.appspot.com/message";

	/**
	 * makes get request to the URL
	 * @return uuid from the request's response
	 * @throws Exception while making the request
	 */
	public static String makeGetRequest() throws Exception{
		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("X-Surya-Email-Id", EMAIL_ID);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		String uuid = "";
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			JSONObject jsonResponse = new JSONObject(inputLine);
			uuid = (String)jsonResponse.get("uuid");
		}

		in.close();
		return uuid; 		
	}

	@Override
	public Response call() throws Exception {
		long startTime = System.currentTimeMillis();
		String uuid =  makeGetRequest();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		return new Response(uuid, totalTime);
	}

}