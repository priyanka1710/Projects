package com.transfer;

public class ProcessingFactory {

	public static Approver getProcessingObject(String s){
		if (s.equals("Fund Manager")) 
		{ 
			System.out.println(" Ready for FundManagerApproval"  );
			return new FundManager();
		} 
		if (s.equals("Research Analyst")) 
		{ 
			System.out.println(" Ready for ResearchAnalystApproval"  );
			return new ResearchAnalyst();
		}
		if (s.equals("Division Head")) 
		{ 
			System.out.println(" Ready for DivisionHeadApproval"  );
			return new DivisionHead();
		} 
		if (s.equals("Operations")) 
		{ 
			System.out.println(" Ready for OperationsApproval"  );
			return new Operations();
		}
		return null;
			
	}
}
