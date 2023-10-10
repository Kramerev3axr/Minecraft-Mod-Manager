import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.*;

import lib.Manager;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class managerApp 
{
    static JScrollPane scrollPane = new JScrollPane();
	static JPanel panel = new JPanel();
	static JList list;
	
	public static JButton settingsButton = new JButton(); 
	
	public static void main(String[] args)
	{
		checkModsFolders();
		gui();
	}

	public static void gui()
	{
		Manager man = new Manager();
		String[] files = {};

		JFrame frame = new JFrame("Minecraft Mod Manager");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	    try {UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");} 
	    catch(Exception ignored){}
		
        Container contentPanel = frame.getContentPane();  
        GroupLayout groupLayout = new GroupLayout(contentPanel);  
        contentPanel.setLayout(groupLayout);  
		
        //List
		list = new JList(man.readFiles(files));
		
		scrollPane.setPreferredSize(new Dimension(300, 300));
		scrollPane.setViewportView(list);
		panel.add(scrollPane);
		
		panel.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createEmptyBorder(0, 0, 10, 10),
				BorderFactory.createEtchedBorder()));
		
		frame.add(panel);
		
		//Buttons
		JButton add = new JButton("Load");
		add.setPreferredSize(new Dimension(155, 30));
		add.setFocusPainted(false);
		add.addActionListener(e -> 
		{
				man.addMod();
				refreshList();
		});
		frame.add(add);

		
		JButton remove = new JButton("Unload");
		remove.setPreferredSize(new Dimension(154, 30));
		remove.setFocusPainted(false);
		remove.addActionListener(e -> 
		{
				man.removeMod(list);				
				refreshList();
		});
		frame.add(remove);
		
		JButton download = new JButton("Download Mods From File");
		download.setPreferredSize(new Dimension(315, 30));
		download.setFocusPainted(false);
		download.addActionListener(e -> 
		{
			downloadMods(man.chooseModList());
		});
		frame.add(download);
		
		JButton openFolder = new JButton("Open Mods Folder");
		openFolder.setPreferredSize(new Dimension(275, 30));
		openFolder.setFocusPainted(false);
		openFolder.addActionListener(e -> 
		{
			try 
			{
				Desktop.getDesktop().open(new File(System.getenv("APPDATA") + "\\.minecraft\\mods"));
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		});
		frame.add(openFolder);
		
		settingsButton.setPreferredSize(new Dimension(30, 30));
		settingsButton.setFocusPainted(false);
		settingsButton.setBorderPainted(false);
		
		ImageIcon settingsIcon = new ImageIcon(managerApp.class.getResource("/images/settingsAnim1.png"));
		settingsButton.setIcon(settingsIcon);
		
		settingsButton.addActionListener(e -> 
		{
//			new SettingsPane().settings();
			changeButtonIcon();
		});
		frame.add(settingsButton);
		
		//Layout
		frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.setSize(340,475);
		frame.setVisible(true);
	}
	
	private static void refreshList()
	{
		Manager man = new Manager();
		String[] files = {};
		
		panel.remove(scrollPane);
		
		list = new JList(man.readFiles(files));
		
		scrollPane.setPreferredSize(new Dimension(300, 300));
		scrollPane.setViewportView(list);
		
		panel.add(scrollPane);
		
		panel.revalidate();
		panel.repaint();	
	}
	
	private static void checkModsFolders()
	{
		try 
		{
			Files.createDirectories(Paths.get(System.getenv("APPDATA") + "\\.minecraft\\mods"));
			Files.createDirectories(Paths.get(System.getenv("APPDATA") + "\\.minecraft\\unloaded_mods"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void changeButtonIcon()
	{
		new Thread(() -> 
		{
			int ms = 25;
			ImageIcon settingsAnim1 = new ImageIcon(managerApp.class.getResource("/images/settingsAnim1.png"));
			ImageIcon settingsAnim2 = new ImageIcon(managerApp.class.getResource("/images/settingsAnim2.png"));
			ImageIcon settingsAnim3 = new ImageIcon(managerApp.class.getResource("/images/settingsAnim3.png"));
			ImageIcon settingsAnim4 = new ImageIcon(managerApp.class.getResource("/images/settingsAnim4.png"));
			ImageIcon settingsAnim5 = new ImageIcon(managerApp.class.getResource("/images/settingsAnim5.png"));
			
			settingsButton.setIcon(settingsAnim2);
			
			try 
			{
				Thread.sleep(ms);
			} 
			catch (InterruptedException e) {}
			
			settingsButton.setIcon(settingsAnim3);
			
			try 
			{
				Thread.sleep(ms);
			} 
			catch (InterruptedException e) {}
			
			settingsButton.setIcon(settingsAnim4);
			
			try 
			{
				Thread.sleep(ms);
			} 
			catch (InterruptedException e) {}
			
			settingsButton.setIcon(settingsAnim5);
			
			try 
			{
				Thread.sleep(ms);
			} 
			catch (InterruptedException e) {}
			
			settingsButton.setIcon(settingsAnim1);
			
			try 
			{
				Thread.sleep(ms);
			} 
			catch (InterruptedException e) {}
		}).start();
	}
	
	private static void downloadMods(List<String> modsLink)
	{
		new Thread(()->
		{
			for (int i = 0; i < modsLink.size(); i += 2)
			{
				try 
				{
					String fileName = modsLink.get(i);
					int linkLength = modsLink.get(i + 1).length();
					String modCDN = "https://mediafilez.forgecdn.net/files/"
							+ modsLink.get(i+1).substring(linkLength - 7, linkLength - 3) 
							+ "/" 
							+ modsLink.get(i+1).substring(linkLength - 3)
							+ "/"
							+ fileName;
					//System.out.println(modCDN);
					Files.copy(
							new URL(modCDN).openStream(),
							Paths.get(System.getenv("APPDATA") + "\\.minecraft\\mods\\" + fileName));
					refreshList();
					
					System.out.println("Finished downloading " + fileName);
				}
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		}).start();
	}
}
