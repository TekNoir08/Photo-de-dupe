import java.io.File;


public class Photo {

	private String fileName;
	private String checkSum;
	private String filePath;
	private File file;
	
	public Photo(File file, String checkSum)
	{
		this.file = file;
		this.fileName = file.getName();
		this.checkSum = checkSum;
		this.filePath = file.getAbsolutePath();
	}
	
	public String getName()
	{
		return fileName;
	}
	
	public String getCheckSum()
	{
		return checkSum;
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public File getFile()
	{
		return file;
	}
	
}
