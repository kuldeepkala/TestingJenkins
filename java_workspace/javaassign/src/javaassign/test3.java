package javaassign;

public class test3 {
	int[] connections = new int[5];

	for (int y=0; y<array.length; y++) {
		for (int x=0; x<array[y].length; x++) {
			if ( isConnected(array[y][x]) ) 
				connections[array[y][x]]++;
		}
	}

	public boolean isConnected( int numb ) {
		if ( array[y-1][x] == numb )
			return true;
		if ( array[y+1][x] == numb )
			return true;
		if ( array[y][x-1] == numb )
			return true;
		if ( array[y][x+1] == numb )
			return true;
		return false;
	}

}
