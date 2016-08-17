import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.json.JSONObject;

public class PostRequest implements Callable<Response>{	
	private static final String EMAIL_ID = "gps@surya-soft.com";
	private static final String URL = "http://surya-interview.appspot.com/message";
	
	private final String UUID;
	public PostRequest(String uuid){
		this.UUID = uuid;
	}

	/**
	 * makes post request to the URL
	 * @param uuid - user id for Surya Softwares
	 * @throws Exception while making the request
	 */
	public static void makePostRequest(String uuid) throws Exception{
		URL obj = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty( "Content-Type", "application/json" );
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		
		Map<String, String> body = new HashMap<String, String>();
		body.put("emailId", EMAIL_ID);
		body.put("uuid", uuid);
		JSONObject jsonBody = new JSONObject(body);		
		osw.write(jsonBody.toString());
		osw.flush();
		osw.close();		
	}

	@Override
	public Response call() throws Exception {
		long startTime = System.currentTimeMillis();
		makePostRequest(UUID);
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		return new Response(totalTime);
	}	
}