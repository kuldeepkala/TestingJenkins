package readkeybasic;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class readkeyAdvance {
	/**
	 * Kuldeep Own this class ... haha this was for just docs
	 * 
	 * @param args
	 */

	// List<String> list_of_line = new ArrayList<>();// yeyeye we need it

	public static List<String> readFileAndMakeItList(String file) { /*
																	 * Method
																	 * for
																	 * reading a
																	 * file
																	 */
		List<String> list_of_line = new ArrayList<>();
		try (BufferedReader br = Files.newBufferedReader(Paths.get(file),
				Charset.forName("Cp1252"))) { // <---- secure this program from
												// error of malcous functiion
												// type
			list_of_line = br.lines().collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		printCollectionsMakeEasy(list_of_line);
		return list_of_line;
	}

	private static void printCollectionsMakeEasy(List<String> listtype) { /*
																		 * print
																		 * collections
																		 */
		System.lineSeparator();
		System.out.print(System.getProperty("line.separator"));
		listtype.forEach(System.out::println);
		System.out.print(System.getProperty("line.separator"));
		System.lineSeparator(); /* i love this way */

	}

	// overload
	private static void printCollectionsMakeEasy(Set<String> settype) { /*
																		 * print
																		 * collections
																		 */
		System.lineSeparator();
		System.out.print(System.getProperty("line.separator"));
		settype.forEach(System.out::println);
		System.out.print(System.getProperty("line.separator"));
		System.lineSeparator();/* i love this way */

	}

	protected static List<String> findStringofFirstSqBreaket(
			List<String> fileline, char a, char b) { /*
													 * find string and add it
													 * into a list method
													 */
		List<String> list_of_String = new ArrayList<>();
		for (String v : fileline)

		{
			try {
				list_of_String.add(v.substring(v.indexOf(a) + 1, v.indexOf(b)));
			} catch (Exception e) { // nhi karna print .. major bhasad hai yaha
									// ...
			}
		}

		printCollectionsMakeEasy(list_of_String);

		return list_of_String;
	}

	protected static void printWithoutduplicateStringInArray(
			List<String> stringlist) { // cunning way ... just convert a list
										// into Linked hash Set

		Set<String> without_duplicate_string = new LinkedHashSet<>(stringlist);
		printCollectionsMakeEasy(without_duplicate_string);
	}

}// end of classes

