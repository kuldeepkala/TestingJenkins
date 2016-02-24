package javaassign;

class patern_injava
{
	
	public static void mains() {

	     for(int i=0;i<5;i++) {
	    	 System.out.print("i"+i);
	         for(int j=0;j<5-i;j++) 
	         {
	             System.out.print("j"+j);
	         }
	        for(int k=0;k<=i;k++) 
	        {
	            System.out.print("^ ");
	        }
	        System.out.println();  
	    }

	}
	@SuppressWarnings("unused")
	public static void pGen()
	{int in=5;
	for (int i=0; i<in;i++)
	{ 
		System.out.print("i"+i);
		  //for(int j=0;j<5-i;j++)
	     //	{	
		//System.out.print("j"+j);
	   //}
		int j=0;
		j=5-in;
		
		for (int k=0;k<=i;k++)
			{
		        System.out.print("* ");
		    }
		 System.out.println();    
	    }
		
	}
	public static void main(String...a)
	{
		pGen();
		System.out.println();   
		mains();
	}
}
