package helloword;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ReadFileInByteArrayWithFileInputStream {

	public static void main(String[] args) {

	}

	public static String read(File file) {
		// File file = new File("D:\\office_free_2013.exe");
		String s = "";// last day
		Object obj = null;
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			byte fileContent[] = new byte[(int) file.length()];
			fin.read(fileContent);
			s = new String(fileContent);
		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading file " + ioe);
		} finally {
			// close the streams using close method
			try {
				if (fin != null) {
					fin.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error while closing stream: " + ioe);
			}
		}
		return s;
	}
}