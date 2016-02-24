package javaassign;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GridHandler {

    private static GridHandler[][] matrix;
    private int i;
    private int j;
    private int value;

    
static int[][] create() {
		int row ,column=0;
		int [][] matrixa;
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		
			
		System.out.println("\nEnter number of rows :");
		row = Integer.parseInt(scan.nextLine());
		
		System.out.println("Enter number of columns :");
		column = Integer.parseInt(scan.nextLine());
		
		matrixa = new int[row][column];
			for(int i=0; i<row; i++) {
			
			for(int j=0; j<column; j++) {
			
			int aaa= ThreadLocalRandom.current().nextInt(1, 3 + 1);						
				
			matrixa[i][j]=aaa;
			}
		}
		return matrixa;
	}
    
    public static void main(String...s) {
		init(create());	
	}
    
    
    
    
    public static void init(int[][] input) {
    	System.out.println("reached init");
        int rowNumber = input.length;
        int columnNumber = input[0].length;
        matrix = new GridHandler[rowNumber][columnNumber];
        for (int r = 0; r < rowNumber; r++) {
            for (int c = 0; c < columnNumber; c++) {
                matrix[r][c] = new GridHandler(r, c, input[r][c]);
            }
        }
    }

    public static GridHandler[][] getmatrix() {
        return matrix;
    }

    public GridHandler(int i, int j, int value) {
        this.i = i;
        this.j = j;
        this.value = value;
        matrix[i][j] = this;
    }

    public int getValue() {
        return value;
    }

  //  public void setValue(value) {
      //  this.value = value;
    //}

    public int getLeftValue() throws ArrayIndexOutOfBoundsException {
        if (j == 0) {
            throw new ArrayIndexOutOfBoundsException("Left edge");
        }
        return matrix[i][j - 1].getValue();
    }

    public int getUpValue() throws ArrayIndexOutOfBoundsException {
        if (i == 0) {
            throw new ArrayIndexOutOfBoundsException("Up edge");
        }
        return matrix[i - 1][j].getValue();
    }

    public int getRightValue() throws ArrayIndexOutOfBoundsException {
        if (j == matrix[0].length - 1) {
            throw new ArrayIndexOutOfBoundsException("Right edge");
        }
        return matrix[i][j + 1].getValue();
    }
    public int getDownValue() throws ArrayIndexOutOfBoundsException {
        if (i == matrix.length - 1) {
            throw new ArrayIndexOutOfBoundsException("Down edge");
        }
        return matrix[i + 1][j].getValue();
    }

}