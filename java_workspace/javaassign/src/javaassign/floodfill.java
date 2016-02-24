package javaassign;
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class floodfill

{

  

    private void fillGrid(String[][] arr, int r, int c) 

    {

        if (arr[r][c].equals("0"))

        {

            arr[r][c] = "*";

            display(arr);

 

            fillGrid(arr, r + 1, c);

            fillGrid(arr, r - 1, c);

            fillGrid(arr, r, c + 1);

            fillGrid(arr, r, c - 1);

        }

    }

   

    private void display(String[][] arr)

    {

        System.out.println("\nGrid : ");

        for (int i = 1; i < arr.length - 1; i++)

        {

            for (int j = 1; j < arr[i].length - 1; j++)

                System.out.print(arr[i][j] +" ");

            System.out.println();

        }

    }

 

    /** Main method **/

    public static void main(String[] args) 

    {

        @SuppressWarnings("resource")
		Scanner scan = new Scanner( System.in );        
        System.out.println("Flood Fill Test\n");
        System.out.println("Enter dimensions of grid");
        int M = scan.nextInt();
        int N = scan.nextInt();
        String[][] arr = new String[M + 2][N + 2];
        for (int i = 0; i < M + 2; i++)
        Arrays.fill(arr[i], "1");
        for (int i = 1; i < M + 1; i++)

        { for (int j = 1; j < N + 1; j++)

        {   
        	int aaa=ThreadLocalRandom.current().nextInt(0, 1 + 1);	
        												
        			String s=Integer.toString(aaa);
        			
        			arr[i][j] =	s;
        	        }
        }
        
        for (int i = 1; i < M + 1; i++)

        { for (int j = 1; j < N + 1; j++)

        {
        	
        	System.out.print("    " +arr[i][j]);	

        }
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        
        }

        System.out.println("Enter coordinates to start ");

        int sr = scan.nextInt();

        int sc = scan.nextInt();

 

        if (!arr[sr][sc].equals("0"))

        {

            System.out.println("Invalid coordinates");

            System.exit(0);

        }

 

        floodfill ff = new floodfill();

        ff.fillGrid(arr, sr, sc);    

 

    }    

}