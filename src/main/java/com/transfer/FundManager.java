package com.transfer;

/**
 *  FundManager is an entity which process and approve the request.
 *
 */
public class FundManager implements Approver {
	/**
	 * approval method which process and approve the request.
	 *
	 */
	public boolean approval(String req){
		try {
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		} 
		System.out.println(" FundManager Approved request - " + req  );
		return true;
	} 
}
