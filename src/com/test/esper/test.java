package com.test.esper;

import com.test.esper.ScuRate;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class test {
	public static void main(String[] args) throws InterruptedException {    	
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();        
        EPAdministrator admin = epService.getEPAdministrator();        
        String product = ScuRate.class.getName();    
        String epl = "select respCode,count(1) from "
                + product +
        		" (respCode='00').win:length_batch(10)";
         
        EPStatement state = admin.createEPL(epl);    
        state.addListener(new AppleListener());        
        EPRuntime runtime = epService.getEPRuntime();            
        for (int i = 0; i < 10; i++) {
        	ScuRate rate = new ScuRate();
            rate.setRespCode("00");
            runtime.sendEvent(rate); 
        }              
    }    	
}

class AppleListener implements UpdateListener    
{        
	 public void update(EventBean[] newEvents, EventBean[] oldEvents)    
	    {    
	        if (newEvents != null)    
	        {    
	        	Long count = (Long) newEvents[0].get("count(1)");
	            System.out.println(count);
	        }    
	    }        
}  
