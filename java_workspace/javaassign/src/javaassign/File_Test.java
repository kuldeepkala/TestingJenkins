package javaassign;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;

public class File_Test extends file_search{

	
	public static  void filex(String path)
	{
			
	}
	
	
	
	public static void main(String...a)
	{
		filex("D://bin/bin/Debug/WebDriver.dll");
		File currentDir = new File("D://bin");
		
		new File_Test().displayDirectoryContents(currentDir);
	}
	
	}
