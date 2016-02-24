package com.crestech.opkey.plugin.functiondispatch;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.clapper.util.classutil.AbstractClassFilter;
import org.clapper.util.classutil.AndClassFilter;
import org.clapper.util.classutil.ClassFilter;
import org.clapper.util.classutil.ClassFinder;
import org.clapper.util.classutil.ClassInfo;
import org.clapper.util.classutil.ClassLoaderBuilder;
import org.clapper.util.classutil.InterfaceOnlyClassFilter;
import org.clapper.util.classutil.NotClassFilter;
import org.clapper.util.classutil.SubclassClassFilter;

import com.crestech.opkey.plugin.Keyword;
import com.crestech.opkey.plugin.KeywordLibrary;
import com.crestech.opkey.plugin.communication.contracts.functionresult.FunctionResult;

public class LibraryLocator {

	protected Set<Class<?>> knownClasses = new HashSet<Class<?>>();
	protected Map<Class<?>, KeywordLibrary> InstantiatedClasses = new HashMap<Class<?>, KeywordLibrary>();
	protected Logger logger = Logger.getLogger(this.getClass().getName());

	
	
	
	public LibraryLocator(String ... classPath ) {
		String workingDirectory = System.getProperty("user.dir");
		logger.fine("Working Directory: " + workingDirectory);
		logger.finer("Start locating libraries in directories and sub-directories");
		
		if(classPath.length == 0)
			classPath = new String[]{workingDirectory};
			
		File[] classpathFiles = getFileInClassPath(classPath);	
		
		/*the ClassLoaderbuilder will create a special URLClassLoader
		 *  which can load classes from all the watched locations*/ 

		ClassLoaderBuilder builder = new ClassLoaderBuilder(); 
		builder.add(classpathFiles);
		ClassLoader loader = builder.createClassLoader();

		/*the ClassFinder will attempt to find classes 
		 * which exist in the watched locations and which 
		 * qualify for being a FunctionLibrary*/

		ClassFinder finder = new ClassFinder();
		
		for(File f1 : classpathFiles) {
			String absolutePath = f1.getAbsolutePath();
			String relativePath = absolutePath.replace(workingDirectory, "~");
			
			logger.finer("Located: " + relativePath);
			finder.add(f1);
		}
		
		loadClasses(finder, loader);
	}
	
	
	private void loadClasses(ClassFinder finder, ClassLoader loader) {
		ClassFilter filter = new AndClassFilter(
				// Must not be an interface
				new NotClassFilter(new InterfaceOnlyClassFilter()),

				// Must implement the FunctionLibrary interface new
				new SubclassClassFilter(KeywordLibrary.class),

				// Must not be abstract
				new NotClassFilter(new AbstractClassFilter()));

		logger.info("Analyzing libraries. This may take some time. Please wait...");
		Collection<ClassInfo> foundClasses = new ArrayList<ClassInfo>();		
		finder.findClasses(foundClasses, filter);
		logger.fine("Done. " + foundClasses.size() + " libraries are worth loading.");

		for(ClassInfo classInfo : foundClasses) {
			try { //to load the class in memory
				
				logger.finer("Loading: " + classInfo.getClassName() + ".class");
				Class<?> c = Class.forName(classInfo.getClassName(), true, loader);
				knownClasses.add(c);

			} catch (ClassNotFoundException | NoClassDefFoundError e) {
				logger.log(Level.WARNING, "Failed to resolve dependencies for '" + classInfo.getClassName() + "' \n", e);

			} catch (Throwable e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

	
	
	
	
	
	
	
	public Tuple<Method, KeywordLibrary> Locate(String methodName) {
		for(Class<?> candidateClass : knownClasses) {
			//logger.info("Searching for " + methodName + " in " + candidateClass.getName());

			try { 
				/*many a things may go wrong. we are iterating over methods 
				 * of a dynamically loaded class. these methods may attempt
				 *  to load other classes through their signature. if even 
				 *  one of such class does not exist, a nasty exception will
				 *   occur.*/ 
				Method[] allMethods = candidateClass.getMethods();
				
				for(Method candidateMethod :  allMethods) {
					if((candidateMethod.getName().equals(methodName) 
							                 ||
							(candidateMethod.isAnnotationPresent(Keyword.class) && (candidateMethod.getAnnotation(Keyword.class).value().equals(methodName)) ))
							                 &&
							candidateMethod.getReturnType().equals(FunctionResult.class)) {

						/*we have located our method*/
						logger.fine("Located " + candidateMethod.getName() + " in " + candidateClass.getName());

						if(! InstantiatedClasses.containsKey(candidateClass)) {
							/*instantiate the class*/
							
							try { // to instantiate the keyword library.
								logger.fine("Instantiating " + candidateClass);
								KeywordLibrary lib = (KeywordLibrary) candidateClass.newInstance();								
								InstantiatedClasses.put(candidateClass, lib);

							} catch (InstantiationException | IllegalAccessException e) {									
								StringWriter sw = new StringWriter();
								e.printStackTrace(new PrintWriter(sw));
								String exceptionAsString = sw.toString();
								
								logger.severe(exceptionAsString);
							}						
						}

						KeywordLibrary lib = InstantiatedClasses.get(candidateClass);
						return new Tuple<Method, KeywordLibrary>(candidateMethod, lib);
					}
				}
			} catch (Throwable e) {
				logger.fine("Problem analyzing " + candidateClass.getName());
				logger.warning(e.toString());
			}
		}
		return null;
	}
	
		
	
	
	
	
	
	
	private File[] getFileInClassPath(String[] classPath) {
		ArrayList<File> allFiles = new ArrayList<File>();
		
		List<String> tempList = Arrays.asList(classPath);
		List<String> classpathList = new ArrayList<>(tempList);
		
		if(classpathList.contains(".") && classpathList.contains(".."))
			classpathList.remove(".");
		
		for(String filePath : classpathList) {
			File file = new File(filePath);
			
			if(file.exists() &&  ! file.isHidden()) {
				allFiles.add(file);
				
				if(file.isDirectory()) {
					//load all jars available in the directory
					processDirectories(allFiles, file);
				}
			}
		}	
		
		return (allFiles.toArray(new File[0])) ;
	}
	
	
	
	
	private void processDirectories(ArrayList<File> allFiles, File directory) {
		if(directory.isHidden())
			return;
			
		//allFiles.add(directory);
		
		File[] jarsAndDirs = directory.listFiles(new FilenameFilter(){
			
			@Override 
			public boolean accept(File dir, String name) {
				return (name.endsWith(".jar") 
						    ||
						new File(dir, name).isDirectory() );
			}});

		for(File file : jarsAndDirs) {
			if(file.isDirectory())
				processDirectories(allFiles, file);
			
			else //must be a JAR
				allFiles.add(file);
		}
	}
}
