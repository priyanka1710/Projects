package com.transfer;

/**
 * Operations is an entity which process and approve the request.
 *
 */
public class Operations implements Approver {

	/**
	 * approval method which process and approve the request.
	 *
	 */
	public boolean approval(String req){
		System.out.println(" Operations Approved "  + req );
		return true;
	} 
}
