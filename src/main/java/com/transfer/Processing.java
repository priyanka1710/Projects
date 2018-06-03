package com.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



/**
 * <p>Processing stores stage and request to be processed.<p>maintains status of request till which stage request has been approved.
 * <p>It also maintains request to be processed at different stages of processing engine.
 * 
 */
public class Processing {

	int status;
	String request;
    /**
     * getStatus method is use to get the stage of request
     *  @return stage of request processing.
      */
	public int getStatus() {
		return status;
	}

	/** 
	 * 
	 * setStatus method is use to set the stage of request
	 * @param stage of request processing.*/
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
     * getStatus method is use to get the request
     *  @return request to be process.
      */
	public String getRequest() {
		return request;
	}
	
	/** 
	 * 
	 * setStatus method is use to set the request
	 * @param request to be process.*/	
	public void setRequest(String request) {
		this.request = request;
	} 
/**
 * Method is to to run request in parallel which are at same stage.
 * @param meth List the list of approvers to be run in parallel.
 * @return List of status with each approver.
 * <p>Can be configured for different request types.Like "Bank Cheque Request". 
 */
	public List<Future<Boolean>> runParallel(ArrayList<String> meth)
	{
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<Boolean>> al =new ArrayList<Future<Boolean>>();
		for(final String s:meth)
		{
			al.add (executorService.submit(
					new Callable<Boolean>(){
						public Boolean call() throws Exception {
							Approver ob= ProcessingFactory.getProcessingObject(s);
							if (ob == null){
								return false;
							}
							return ob.approval(request);
						}
					}));

		}
		executorService.shutdown();
		return al;

	}

	/**
	 * <p> Method is to to run request in which is at this stage.
	 * @param s contains which approver to be executed at this stage.
	 * @return List of status with each approver.
	 *<p> Can be configured for different request types.Like "Bank Cheque Request". 
	 *
	 */
	public Boolean runLinear(String s)
	{
		Approver ob= ProcessingFactory.getProcessingObject(s);
		if (ob == null){
			return false;
		}
		return ob.approval(request);
	}

}
