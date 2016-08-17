import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ResponseTime{
	public static final int TOTAL_REQUEST = 100;

	public static void main(String[] args){
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		try{
			//Making get requests
			List<Callable<Response>> tasks = new ArrayList<Callable<Response>>(10);
			for (int i = 0; i < TOTAL_REQUEST; i++) {
				tasks.add(new GetRequest());
			}
			List<Future<Response>> responses = executor.invokeAll(tasks);
			String uuid = responses.get(0).get().getUuid();
	
			//Processing request of get requests
			printStats("Get", responses);
			
			//Making post request now
			tasks = new ArrayList<Callable<Response>>(10);
			for (int i = 0; i < TOTAL_REQUEST; i++) {
				tasks.add(new PostRequest(uuid));
			}
			responses = executor.invokeAll(tasks);
			
			//Processing request of post requests
			printStats("Post", responses);
		
		} catch(InterruptedException e){
			System.out.println("The execution of the program got interrupted " + e.getStackTrace());
		} catch(ExecutionException e){
			System.out.println("Some exception occured while executing the program " + e.getStackTrace());
		} catch(Exception e){
			System.out.println("Unchecked exception occured " + e.getStackTrace());
		}
		executor.shutdown();
	}
	
	/**
	 * Print the statistics of the calls made.
	 * @param method : Name of the method call, like Get or Post
	 * @param responses : list of the response of the get/post calls made
	 * @throws ExecutionException While processing the response 
	 * @throws InterruptedException While processing the response
	 */
	private static void printStats(String method, List<Future<Response>> responses) throws InterruptedException, ExecutionException{
		System.out.println("Total requests made: " + responses.size());
		long[] resTime = new long[TOTAL_REQUEST];
		long totalResponseTime = 0l;
		
		int idx = 0;
		for(Future<Response> res : responses){
			resTime[idx] = res.get().getResponseTime();
			totalResponseTime  = totalResponseTime + res.get().getResponseTime();
			idx++;
		}
		
		int[] times = {10,50,90,95,99};
		for(int time:times){
			System.out.println("Response time for "+ time +"th "+ method +" request: " + resTime[time-1] + "ms");
		}
		
		double mean = getMean((double)totalResponseTime);
		System.out.println("Mean response time for " + method + " requests: " + String.format("%.2f", mean) + "ms");
		
		double stdDev = getStdDev(mean, resTime);
		System.out.println("Standard deviation for " + method + " requests: " + String.format("%.2f", stdDev) + "ms");
	}
	
	/**
	 * Calculate mean from the total sum, and assuming the size to be 100.
	 * @param total : total sum of all the elements
	 * @return mean value for the sum
	 */
	private static double getMean(double total){
		return total/(double)TOTAL_REQUEST;
	}
	
	/**
	 * calculate the standard deviation from the mean value.
	 * @param mean : mean of the array elements
	 * @return standard deviation for the array
	 */
	private static double getStdDev(double mean, long[] resTime){
		double temp = 0;
        for(long time:resTime){
        	double val = (double)time;
            temp += (val-mean)*(val-mean);
        }
        return temp/(double)TOTAL_REQUEST;
        
	}
}