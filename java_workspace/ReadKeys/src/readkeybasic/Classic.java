package readkeybasic;

public class Classic extends readkeyAdvance{

	public static void main(String[] args) {
		String fileName = "D://testcase.txt"; 
		char a='[';
		char b=']';
		printWithoutduplicateStringInArray(findStringofFirstSqBreaket(readFileAndMakeItList(fileName),a,b));
	}

}
