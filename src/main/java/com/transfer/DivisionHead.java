package com.transfer;

/**
 * DivisionHead is an entity which process and approve the request.
 *
 */
public class DivisionHead  implements Approver {

	/**
	 * approval method which process and approve the request.
	 *
	 */
	public boolean approval(String req){
		System.out.println(" DivisionHead Approved request- " + req  );
		return true;
	} 
}