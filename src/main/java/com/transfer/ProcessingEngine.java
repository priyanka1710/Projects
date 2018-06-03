package com.transfer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import org.h2.jdbc.JdbcSQLException;

/**
    * <p>Class creates connection with H2 database which is light weight and can be embedded in Java Program.   
    * <p>Works on type of request we want to process like 'Trade Fund Transfer'. All other requests are configurable.
*/
public class ProcessingEngine {

	/**
	 * <p> In requestName variable,  we can define type of request to be processed.
	 * If process with request name is configured in our database then we fetch which rule to be triggered for this request. 
	 * Otherwise, we can configure our system and process for new request name.
	 * <p> According to rule, fetch different stages for that rule from table TXN_RULE and process of approval begins.
	 * <p> Table TXN_REQUEST_TYPE, defined with REQ_ID, REQUEST_NAME and RULE_NAME i.e for REQUEST_NAME('Trader Fund Request'), we want RULE_NAME('Rule 1')
	 * to be fired.
	 * <p> In Table TXN_RULE, we can define in which stage which entity will approve in that Rule. At any stage,multiple approvals can approve.   
	 * <p> Hence , design is configurable.
	 * <p> For new process (request type), we have to create its entity and add it in Tables.
	 * <ul compact>
	 * <li> Assumption : Input Request is of String type.
	 * <li>              : Single user can add in database tables like either admin or operational team.   </ul>
	*/
	public static void main(String[] args) {

		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        String url = "jdbc:h2:tcp://localhost:9092/./testdb";
        String user = "sa";
        String passwd = "";
        try (
        		Connection con = DriverManager.getConnection(url, user, passwd);
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE );
        	 ) {
        	    ResultSet rs=null;


        	    String requestName="Trader Fund Request";
        	    int reqId = 0;
        	    String ruleName =null;
    		    try{
        	      rs = st.executeQuery("SELECT REQ_ID,RULE_NAME FROM TXN_REQUEST_TYPE WHERE REQUEST_NAME ="+ "'" +requestName +"'");
        	      rs.next();
            	  reqId=rs.getInt(1);
          	      ruleName=rs.getString(2);
                  System.out.println("For "+ requestName + " executing " + ruleName);
                  rs.close();
        	    }
    		    catch (JdbcSQLException ex) {
               	  System.out.println("Please configure system for request name " + requestName );
    		      return;
    		    }
    		 
                rs = st.executeQuery("SELECT count(distinct stage) FROM TXN_RULE WHERE REQ_ID =" + reqId +" AND RULE_NAME ="+ "'"+ruleName+"'") ;
            	rs.next();
        	    int noOfRows=rs.getInt(1);
        	    rs.close();
                
                Processing ob =new Processing();
                ob.setRequest("Trader wants to transfer Rs.1000");
                ob.setStatus(0);
                
                if (noOfRows==0){
                	System.out.println("Trader Fund Request has no rules defined for " + ruleName);	
                } 
                else{
                	System.out.println("-----------Request Ready for Approvals-------------");
	                int stage = 1;
	                System.out.println("Trader Fund Request required " + noOfRows + " stages for approval");
	                while (stage<=noOfRows) { 
				                rs = st.executeQuery("SELECT APPROVAL FROM TXN_RULE WHERE REQ_ID =" + reqId +" AND STAGE = "+ stage +" AND RULE_NAME ="+"'"+ruleName+"'") ;
				            	rs.last();
				            	int flag = rs.getRow(); //1- For Linear 2- For Parallel
				            	rs.first();
				            	
				            	if (flag>1)
				            	{   
			                       	ArrayList<String> al =new ArrayList<String>();
				                  
				                    do{
				                     al.add(rs.getString(1));
				                    }  while (rs.next()); 
				                    System.out.println("Trader Fund Request at stage - " + stage + " will do parallel processing at  " + al);
				                    	       			            
				            	    List<Future<Boolean>> result =   ob.runParallel(al);
				                    int flag1=1;
				                    for(Future<Boolean> obj: result)  
				                    {  
				                      if( obj.get().compareTo(false)==0) 
				                    	 flag1=0;
				                    }
				                    if (flag1==1)
				                    { 
                                      ob.setStatus(stage++); 
				                    }
				                    else break;
				               
				            	}
				            	else
					            {   System.out.println("Trader Fund Request at stage - " + stage + " will do linear processing at " + rs.getString(1));
		       			            Boolean result =   ob.runLinear(rs.getString(1)); 
				                
					                if (result.equals(true))
					                {  
					                     ob.setStatus(stage++); 
					                }
					                else break;
				            	}
				            	rs.close();
			        }			       	   
			        if (stage>noOfRows){
			        	System.out.println("-----------Ready for Disbursment-------------");
					}
			        else{
			        	
			        	System.out.println("Trader Fund Request Disapproved");
			            //Can return till where approver has approved.
			        }
			   }	            
        	    
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(ProcessingEngine.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } 
        catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    }
}