package com.transfer;

/**
 * Created new class ResearchAnalyst.
 *
 */

public class ResearchAnalyst implements Approver {

	/**
	 * approval method which takes some time to process and approve the request.
	 *
	 */

	  public boolean approval(String req){
		  try {
			Thread.sleep(1000);
			} 
		  catch (InterruptedException e) {
			e.printStackTrace();
			}
        System.out.println(" ResearchAnalyst Approved request- "+ req  );
  	  return true;
	  } 
}