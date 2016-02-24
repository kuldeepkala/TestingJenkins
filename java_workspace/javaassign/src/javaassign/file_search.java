package javaassign;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class file_search {
	
	@SuppressWarnings("null")
	public  void displayDirectoryContents(File dir) {
		try {
			//File currentDir = new File("D://bin");
			//File dir=currentDir;
			File[] files = dir.listFiles();		
			System.out.println("file ka size"+files.length);
		//	ArrayList<String> paths = new ArrayList<String>();
			String []paths = new String[files.length+1];
			System.out.println("path ka size"+paths.length);
		//	for (File file : files) {
			for(int i=0;i<=paths.length;i++){
			if (files[i].isDirectory()) {
					System.out.println("directory:" + files[i].getCanonicalPath());
					displayDirectoryContents(files[i]);

				} else {
					System.out.println("file:" + files[i].getCanonicalPath());
					//paths[file]=files;
					paths[i]=files[i].getCanonicalPath().toString();
					//paths.add(file.getCanonicalPath().toString());
				}
			}

			for (String captured : paths) {
				int a = captured.lastIndexOf('\\');
				// System.out.println("Paths : " + captured + "   / " +a +
				// "   Name of file    " +captured.substring(a + 1));
				if (captured.substring(a + 1).equals("TabOrder.dll")) {
					System.out.println("Paths : " + captured);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		System.out.print(">>"+e.getMessage());
			
		}
	}

}