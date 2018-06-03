package com.transfer;

/** Different Request type approver like - FundManager,ResearchAnalyst will implement this interface.
 * 
 * */
public interface Approver {
	public boolean approval(String req); 
}
