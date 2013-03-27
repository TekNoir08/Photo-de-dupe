import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.*;




public class FileManager {

	private File inputDirectory;
	private File outputDirectory;
	private Map<String, Photo> photoMap = new HashMap<String, Photo>();
	private List<String> duplicateList = new ArrayList<String>();
	private int numberOfDuplicates;
	
	public FileManager(String filePath, String outputPath) 
	{
		inputDirectory = new File(filePath);
		outputDirectory = new File(outputPath);
		test(inputDirectory);
		printPhotoMap();
		printDuplicateList();
		//outputMap();// uncomment to copy files to output directory
		
	}
	
	 
	private void test(File parentNode) // maybe change this to not take the file and just get it from the variables already there 
	{
		if(parentNode.isDirectory())
		{
			System.out.println(parentNode + " is directory");
			File directory[] = parentNode.listFiles();
			for(File childNode : directory)
			{
				test(childNode);
			}
		}
		else
		{
			try{
				System.out.println(parentNode.getName() + " " + getCheckSum(parentNode));
				System.out.println();
				String checkSum = getCheckSum(parentNode);
				checkIfDuplicate(checkSum, parentNode);
				//adds item to the map using checksum as the key and a Photo object as the value
				photoMap.put(checkSum, new Photo(parentNode, checkSum));
				
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String getCheckSum(File fileName) throws NoSuchAlgorithmException, IOException //this throws need to be removed
	{
		MessageDigest md = MessageDigest.getInstance("SHA1"); // tests need to be done to see if this is the fastest hash function or are there better available
		FileInputStream fis = new FileInputStream(fileName); //this need to be in a try catch!!
		byte[] dataBytes = new byte[1024];
		int nread = 0;
		
		while((nread = fis.read(dataBytes)) != -1)
		{
			md.update(dataBytes, 0, nread);
		};
		
		byte[] mdBytes = md.digest();
		
		//convert to hex format
		StringBuffer sb = new StringBuffer("");
		for(int i = 0; i<mdBytes.length; i++)
		{
			sb.append(Integer.toString((mdBytes[i]& 0xff) + 0x100, 16).substring(1));
		}
		
		
		return sb.toString();
		
	}

	private void printPhotoMap() 
	{
		System.out.println("test map");
		
		for(String s : photoMap.keySet())
		{
			System.out.println(photoMap.get(s).getName()  + " " + photoMap.get(s).getFilePath());
		}
	}
	
	//copies the files to the output directory
	private void outputMap() 
	{
		System.out.println("copying files to " + outputDirectory.getAbsolutePath());
		
		for(String s : photoMap.keySet()) {
			copyFile(photoMap.get(s).getFile(), outputDirectory);
		}
	}
	
	/**	handles the copying of the file into the destination directory using FileUtils from the 
	 *	apache library (see http://commons.apache.org/io/). This method also creates a directory 
	 * 	if the destination directory does not exist.
	 */
	private void copyFile(File sourceFile, File destDirectory) 
	{
		try {
			FileUtils.copyFileToDirectory(sourceFile, destDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkIfDuplicate(String checkSum, File file) 
	{
		if(photoMap.containsKey(checkSum)) {
			duplicateList.add(file.getPath());
			numberOfDuplicates++;
		}
	}
	
	private void printDuplicateList() 
	{
		System.out.println("The following " + numberOfDuplicates + " files are duplicated");
		
		for(String file : duplicateList) {
			System.out.println(file);
		}
	}
}
