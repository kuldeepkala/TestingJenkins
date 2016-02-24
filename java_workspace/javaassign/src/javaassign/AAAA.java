package javaassign;

class AAAA
{
	
	public static void mains() {

	     for(int i=0;i<5;i++) {
	         for(int j=0;j<5-i;j++) {
	             System.out.print(" ");
	         }
	        for(int k=0;k<=i;k++) {
	            System.out.print("* ");
	        }
	        System.out.println();  
	    }

	}
	public static void pGen()
	{
		for (int i=0; i<5;i++)
		{
		    for (int j=0; j<5-i;j++)
		   	{	
			  System.out.print("-");
		    }
			for (int k=0;k<=i;k++)
			{
		        System.out.print("* ");
		    }
		     
	    }
		System.out.println(" ");
		}
	
	
	public static void main(String...a)
	{
		pGen();
		mains();
	}
}
