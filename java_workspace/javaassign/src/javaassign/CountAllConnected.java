package javaassign;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class CountAllConnected{
    //Everything is declared static to avoid object creation
    static int island[][];
    static int large=0,sum=0;
    static String path="",tempPath="";
    static int rows=0, cols=0;    
    static String allPath="";
 
    public static void main(String[] args) {
        ////Ask for matrix dimension,
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter no of rows");
        rows=sc.nextInt();
        System.out.println("Enter no of cols");
        cols=sc.nextInt();
 
        island=new int[rows][cols]; //create matrix with given dimension
 
        //Accept element in matrix
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){ 
                                island[i][j]=ThreadLocalRandom.current().nextInt(1, 3 + 1);	
            }
        }
        System.out.println("Island formed succesfully");
 
        //prints matrix 
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                System.out.print(island[i][j]+"  ");
            }
            System.out.println();//empty line
        }
        //Process the matrix,to calculate matrix,
        //See the power of recursion
 
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){  
                if(island[i][j]>0 ) //we got an island{
                    sum=0;
                    tempPath="["+i+"]["+j+"]";
                    if(allPath.indexOf(tempPath)==-1){
                    tempPath="";
                    find(i,j);
                    System.out.println("Weight="+sum+":: Path="+tempPath);//Display the weight of each Island
                    }
                }
 
                if(large<sum){
                   large=sum;path=tempPath;
                }
            }//inner for
     System.out.println(large+"::"+path);   }//outer for
        
    
//main
    public static void find(int i,int j){
        String currPath="["+i+"]["+j+"]";
        if(allPath.indexOf(currPath)==-1){
        sum=sum+island[i][j];
        tempPath+="["+i+"]["+j+"]";
        allPath+="["+i+"]["+j+"],";
 
        if((j<(cols-1) && island[i][j+1]>0  ) && (i<(rows-1) && island[i+1][j]>0  ) ){
            find(i+1,j);
            find(i,j+1);
        }
 
        else if(j<(cols-1) && island[i][j+1]>0  )
            find(i,j+1);
        else if(i<(rows-1) && island[i+1][j]>0  )
            find(i+1,j);
        if((j>0 && island[i][j-1]>0  ) && (i>0 && island[i-1][j]>0  ) ){
             find(i,j-1);    
             find(i-1,j);
        }
 
        else if(i>0 && j>0 && island[i-1][j]>0  )
            find(i-1,j); 
        else if(j>0 && island[i][j-1]>0  )
            find(i,j-1);
 
        }//outer if
    }//find()
} //class