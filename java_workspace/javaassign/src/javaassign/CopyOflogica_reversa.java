package javaassign;


import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
 
public class CopyOflogica_reversa {
	
	Scanner scan;
	String matrix[][];
	int row, column;
 
	/*  public int getLeftValue() throws ArrayIndexOutOfBoundsException {
	        if (column == 0) {
	            throw new ArrayIndexOutOfBoundsException("Left edge");
	        }
	        return matrix[row][column - 1].getValue().;
	    }

	    public int getUpValue() throws ArrayIndexOutOfBoundsException {
	        if (row == 0) {
	            throw new ArrayIndexOutOfBoundsException("Up edge");
	        }
	        return matrix[row-1][column].getValue();
	    }

	    public int getRightValue() throws ArrayIndexOutOfBoundsException {
	        if (column == matrix[0].length - 1) {
	            throw new ArrayIndexOutOfBoundsException("Right edge");
	        }
	        return matrix[row][column + 1].getValue();
	    }
	    public int getDownValue() throws ArrayIndexOutOfBoundsException {
	        if (row == matrix.length - 1) {
	            throw new ArrayIndexOutOfBoundsException("Down edge");
	        }
	        return matrix[row + 1][column].getValue();
	    }
	
	
	*/
	
	
	
	
	void create() {
		scan = new Scanner(System.in);
		System.out.println("\nEnter number of rows :");
		row = Integer.parseInt(scan.nextLine());
		System.out.println("Enter number of columns :");
		column = Integer.parseInt(scan.nextLine());
		matrix = new String[row][column];
		System.out.println("Enter the data :");
 		for(int i=0; i<row; i++) {
			for(int j=0; j<column; j++) {
			int aaa= ThreadLocalRandom.current().nextInt(1, 3 + 1);						
			String s=Integer.toString(aaa);
			matrix[i][j]=s;
			}
		}
	}
	
	void display() {
		System.out.println("\nThe Matrix is :");
			for(int i=0; i<row; i++) {
		    	for(int j=0; j<column; j++)           {
				System.out.print("\t" + matrix[i][j]);}
			System.out.println();
		}
		}
	
		
	void replacer()throws ArrayIndexOutOfBoundsException
	{
		System.out.println("\nEnter of row number :");
		int rowno = Integer.parseInt(scan.nextLine());
		
		System.out.println("Enter number column number :");
		int columnno = Integer.parseInt(scan.nextLine());
        
		replaceRsa(rowno,columnno);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	void replaceRsa1(int rowno,int columnno)throws ArrayIndexOutOfBoundsException
	{
		
		
	}
	
	
	
	
	
	
	void replaceRsa(int rowno,int columnno)throws ArrayIndexOutOfBoundsException
	{
		
	try 
	{
		for(int i=0; i<matrix.length; i++)//f1
		{			
		   for(int j=0; j<matrix[0].length ; j++)//f2 
		 {
		if(matrix[i][j].equals(matrix[rowno][columnno]))
		{
			i=rowno;
			j=columnno;
			matrix[rowno][columnno]="*";
			
		}
		    
	  }//f2
		    		
		    }//f1
		for(int i=0; i<matrix.length; i++) {
	    	for(int j=0; j<matrix[0].length; j++)           {
			System.out.print("\t" + matrix[i][j]);}
		System.out.println();
	}
	}//try
	catch (Exception e) 
	{
		System.out.print(e.getMessage());
    }
}//endmethod rsa
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	void replaceRs(int rowno,int columnno)throws ArrayIndexOutOfBoundsException
	{
				
		String que[][]=new String [row][column];//stack
		for(int i=0; i<row; i++) {			
	    for(int pi=0; pi<column; pi++) {
		if(rowno!=0 && rowno!=row && columnno!=0 && columnno!=column )
		 {
			System.out.print("------");
			if(matrix[rowno+1][columnno].equals(matrix[rowno][columnno]))
			{
				System.out.print("   down  ");
				matrix[rowno+1][columnno]="*";
				//que[i][pi]=matrix[rowno+1][columnno];
			}
			
			if(matrix[rowno-1][columnno].equals(matrix[rowno][columnno]))
			{
				System.out.print("   up   ");
				matrix[rowno-1][columnno]="*";
				//que[i][pi]=matrix[rowno-1][columnno];
			}
			if(matrix[rowno][columnno+1].equals(matrix[rowno][columnno]))
			{
				System.out.print("   right   ");
			matrix[rowno][columnno+1]="*";
				//que[i][pi]=matrix[rowno][columnno+1];
			}
			
			if(matrix[rowno][columnno-1].equals(matrix[rowno][columnno]))
			{
				System.out.print("   left   ");
			matrix[rowno][columnno-1]="*";
				//que[i][pi]=matrix[rowno][columnno-1];
			}
			matrix[rowno][columnno]="*";
			
		}
		
		else
		{
			System.out.print("i am on edge");
		}
		
	
         		
}	
}
}
			//System.out.println();
		//}}
		
		
		
	

public static void main(String args[]) {
		
		CopyOflogica_reversa obj = new CopyOflogica_reversa();
		obj.create();
		obj.display();
		obj.replacer();
	



}
}