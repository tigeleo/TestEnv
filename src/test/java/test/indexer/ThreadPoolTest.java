package test.indexer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTest {

    // Maximum number of threads in thread pool
   static final int MAX_T = 6;             
 
   public static void main(String[] args)
   {
         
       // creates a thread pool with MAX_T no. of 
       // threads as the fixed pool size(Step 2)
       ExecutorService pool = Executors.newFixedThreadPool(MAX_T);  
        
       // passes the Task objects to the pool to execute (Step 3)
       ArrayList<Task> threadArr = new ArrayList<Task>(1000);       
       for(int i=0; i<1000; i++) {
    	   Task r1 = new Task("task " + i);
    	   threadArr.add(r1);
	       pool.execute(r1);

       }
		try {
			for (Iterator<Task> iterator = threadArr.iterator(); iterator.hasNext();) {
				Task element = (Task) iterator.next();
				element.join();
				Exception e = element.getE();
				if (e != null) {
					throw e;
				}
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}       
         
       // pool shutdown ( Step 4)
       pool.shutdown();  
       while(!pool.isTerminated()) {
    	   try {
			Thread.sleep(1000);
			  /* Total amount of free memory available to the JVM */
			  System.out.println("Free memory (bytes): " +  Runtime.getRuntime().freeMemory()/1024/1024);

			  /* This will return Long.MAX_VALUE if there is no preset limit */
			  long maxMemory = Runtime.getRuntime().maxMemory()/1024/1024;
			  /* Maximum amount of memory the JVM will attempt to use */
			  System.out.println("Maximum memory (bytes): " +  (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

			  /* Total memory currently in use by the JVM */
			  System.out.println("Total memory (bytes): " +  Runtime.getRuntime().totalMemory()/1024/1024);


    	   } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       }
       System.out.println("Finish pool");
   }
	
	
}
