package lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings({"rawtypes" })
public class Manager 
{
	public String[] readFiles(String[] fileArray)
	{
		File folder = new File(System.getenv("APPDATA") + "\\.minecraft\\mods");
		File[] fileList = folder.listFiles();
		
		int fileCount= folder.list().length + 1;
		fileArray = new String[fileCount];

		int i = 1;
		
		for (File file : fileList) 
		{	
		    if (file.isFile()) 
		    {
		        fileArray[i] = file.getName();
		        i++;
		    }
		}
		return fileArray;
	}
	
	public void addMod()
	{
		String unloadedPath = System.getenv("APPDATA") + "\\.minecraft\\unloaded_mods";
		String modsPath =  System.getenv("APPDATA") + "\\.minecraft\\mods";
		
		FileFilter modFilter = new FileNameExtensionFilter("Minecraft Mods (.jar)","jar");
		JFileChooser chooser = new JFileChooser(unloadedPath);
		
		chooser.setFileFilter(modFilter);
		chooser.showOpenDialog(null);
		
		try 
		{
			Files.move(
					Paths.get(chooser.getSelectedFile().getAbsolutePath()), 
					Paths.get(modsPath + "\\" + chooser.getSelectedFile().getName()), 
					StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Debug: Successfuly moved file");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void removeMod(JList list)
	{
		String unloadedPath = System.getenv("APPDATA") + "\\.minecraft\\unloaded_mods";
		String modsPath =  System.getenv("APPDATA") + "\\.minecraft\\mods";
		
		
		try 
		{
			Files.move(
					Paths.get(modsPath + "\\" + list.getSelectedValue()), 
					Paths.get(unloadedPath + "\\" + list.getSelectedValue()), 
					StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Debug: Successfuly moved file");
		} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	@SuppressWarnings("resource")
	public List<String> chooseModList()
	{
		// Get Mods.txt
		FileFilter modFilter = new FileNameExtensionFilter("Text Document (.txt)","txt");
		JFileChooser chooser = new JFileChooser();
		
		chooser.setFileFilter(modFilter);
		chooser.showOpenDialog(null);
		
	    String modsFile = "" + chooser.getSelectedFile().getAbsolutePath() + "";
		
		// Read from file
    	List<String> modsLink = new ArrayList<String>(); 
	    try
	    {
			BufferedReader reader = new BufferedReader(new FileReader(modsFile));
			String line = reader.readLine();
			
			while (line != null)
			{
				if (!line.isEmpty()) // Skip empty lines
				{
					modsLink.add(line);
				}
				
				// Read next line
				line = reader.readLine();
			}
	    } 
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
		
		System.out.println(Arrays.toString(modsLink.toArray()));
		return modsLink;
	}	
}
