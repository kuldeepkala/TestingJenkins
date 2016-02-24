package javaassign;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/* Name of the class has to be "Main" only if the class is public. */
 class mainclass {


	public  void fillMatrix(int row, int col)

	{
//fill	
	    int [][] matrix = new int[row][col];
	  	for(int i=0;i<matrix.length;i++)
	    {
        for(int j=0;j<matrix[i].length;i++)
		{
	      matrix[i][j] =random(0,3);
		}
		      
        }
//print	
	    System.out.println();
		for(int i=0;i< matrix.length; i++)
		{
		for(int j=0; j< matrix[i].length; j++)
		{  System.out.println(matrix[i][j]);
		}
		System.out.println(); 
		}
	}

	
	
	public static int random(int x , int y)
	{
		
	int a=ThreadLocalRandom.current().nextInt(x, y + 1);
	return a;
	}
	
	
	
	
	public static void printMatrix(int [][] a)

	{

	}

	
public static void main(String...s ) {
	System.out.println("Enter Size of row and column");

	// System.out.print(enterNo());

	   new mainclass().fillMatrix(mainclass.enterNo(),mainclass.enterNo());
	
}
		

	public static int enterNo()

	{

		@SuppressWarnings("resource")

		Scanner scanner = new Scanner(System.in);

		System.out.println(">>");

		return scanner.nextInt();

	}



}

