package test.indexer;

//Java program to illustrate 
//ThreadPool

import java.util.Random;

//Task class to be executed (Step 1)
class Task extends Thread   
{
 private String name;
 private int ssize;
 private Exception e;
 
 public Task(String s)
 {
     name = s;
     this.ssize=(new Random()).nextInt(10000000)+10000000;
 }
   
 // Prints task name and sleeps for 1s
 // This Whole process is repeated 5 times
 public void run()
 {
	 StringBuffer sb = new StringBuffer();
     try
     {
         System.out.println(name+" start");
         for (int i = 0; i<=this.ssize; i++)
         {
             sb.append("x");
         }
         Thread.sleep((new Random()).nextInt(1000));
         System.out.println(name+" complete string " + sb.length());
     }
       
     catch(InterruptedException e)
     {
         e.printStackTrace();
     }
 }

	public Exception getE() {
		return e;
	}
 
 
}