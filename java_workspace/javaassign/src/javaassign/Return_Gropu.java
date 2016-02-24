package javaassign;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
/**
* @Copyrights reservered with the author.
* @author riaz
* @Email riaz.2012@gmal.com
* @version 1.0
*/

public class Return_Gropu
{
	
	

  Scanner s;

  int array[][];
  char cf[][];

  int rows=0,col=0;
  int sum,high;

	int[][] create(int row, int column ) {
		Scanner scan = new Scanner(System.in);
		
		int [][] matrix = new int[row][column];
		System.out.println("Enter the data :");
 		for(int i=0; i<row; i++) {
			for(int j=0; j<column; j++) {
			int aaa= ThreadLocalRandom.current().nextInt(1, 3 + 1);						
			
			matrix[i][j]=aaa;
			}
		}
		return matrix;
	}
	
  
  
  
  Return_Gropu()
  {
      int i,j;

      s=new Scanner(System.in);


 
   System.out.print("Rows:");
   rows=s.nextInt();
   System.out.print("Columns:");
   col=s.nextInt();
   array=new int[rows][col];
   cf=new char[rows][col];
   
   init_flag();

array=create(rows, col);  

   for(i=0;i<rows;i++)
   {
         for(j=0;j<col;j++)
            System.out.print("\t"+array[i][j]);

            System.out.println();
   }


       System.out.println("\nThe longest is:" + heaviest());

  }

  public static void main(String []args)
  {
          new Return_Gropu();
  }

  int heaviest()
  {

      int i,j;
  high=0;


  for(i=0;i<rows;i++)
  {
      for(j=0;j<col;j++)
      {
          if(array[i][j]>0&&cf[i][j]=='o')
          {

          sum=0;
          sum=move(i,j);
          if(sum>high)
              high=sum;
          System.out.println("}sum: "+sum);
          }
      }
  }


  return high;
  }

  void init_flag()
  {
      int i,j;

  for(i=0;i<rows;i++)
      for(j=0;j<col;j++)
          cf[i][j]='o';

  }

  int move(int r,int c)
  {
  int i,j;

  if(r<rows && c <col)
  {
      i=r;
      j=c;
  }
  else return 0;

          if(array[i][j]>0 && cf[i][j]=='o')
          {
              sum+=array[i][j];
              System.out.print("{a["+i+"]["+j+"],");
              move(i,j+1);    //move left
              move(i+1,j);   //move south
              cf[i][j]='x';

          }
          else
          {
              return 0;
          }


      return sum;

  }


}
