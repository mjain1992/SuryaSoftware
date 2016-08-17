public class Response{
	private String uuid;
	private long responseTime;
	
	public Response(String uuid, long responseTime){
		this.uuid = uuid;
		this.responseTime = responseTime;
	}
	
	public Response(long responseTime){
		this("", responseTime);

	}
	
	public String getUuid(){
		return this.uuid;
	}
	
	public long getResponseTime(){
		return this.responseTime;
	}
}