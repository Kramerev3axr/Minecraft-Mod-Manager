package lib;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
					Paths.get(unloadedPath + "\\" + chooser.getSelectedFile().getName()), 
					Paths.get(modsPath + "\\" + chooser.getSelectedFile().getName()), 
					StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Debug: Successfuly moved file");
		} 
		catch (IOException e) {e.printStackTrace();}
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
}
