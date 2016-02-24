package javaassign;

public class paterns_1 {

	static void printStars(int count) {
	    if (count == 0) return;
	    System.out.print("*");
	    printStars(count - 1);
	}
	
	
	public static String stars (int count) {
	
		if( count <= 0 ) return " ";
	    String p = stars(count - 1);
	    p = p + "*";
	    System.out.println(p);
	
	    return p;
	 
	  }
	public static void rep(int j,int i,int k,int min)
	{
		if(j>i)
		{
			System.out.print(" j"+j);
			j--;
			//continue;
		}
		if(k<min)
		{
			System.out.print(" k"+k);
			k++;
		}
	}
	
	//--------------------------------------------------------------------------------
static	void myloopdec(int j,int i) {
	   
		if (j==0) return;
	    System.out.print(" ");
	    myloopdec(j - 1,i);
	}
static	void myloopinc(int k,int min) {
	   
	if (k<min) return;
    System.out.print(" k");
    myloopinc(k + 1,min);
}	
	//------------------------------------------------------------------------------------------
public static void stards (int n) {
		int i=0;
		int j=0;
		int k=0;
		int min=1;	
		int height=n;
		int space=height-1;
 while(i<height)
{
	 j=space;
	// System.out.println("min"+min);
	// System.out.println("height"+height);
	// System.out.println("space"+space);
	 
		//rep(j, i, k,min);
 myloopdec(space,i);
 for(k=0;k<min;k++)
	{
		System.out.print("*");
	}
	System.out.println("");
    min=min+2;
	i++;
	
}
	   
	 
	  }
	
	
	public static void stardso (int n)
	{
	int min=1;	
	int height=n;
	int space=height-1;
	for(int i=0;i<height;i++){
		// System.out.println("min"+min);
		// System.out.println("height"+height);
		// System.out.println("space"+space);
		for(int j=space;j>i;j--)
		{
			System.out.print(" ");
		}
		for(int k=0;k<min;k++)
		{
			System.out.print("*");
		}
		min=min+2;
		System.out.println("");
		}
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//	printStars(5); 
		stars(15);
		//stards(5);
	}

}
