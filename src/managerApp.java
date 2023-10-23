import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import lib.Manager;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class managerApp 
{
	static JProgressBar progBar = new JProgressBar();
	
    static JScrollPane scrollPane = new JScrollPane();
	static JPanel panel = new JPanel();
	static JList list;
	
	public static JButton settingsButton = new JButton(); 
	
	public static void main(String[] args)
	{
		checkModsFolders();
		gui();
	}

	private static void gui()
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
		
        // List
		list = new JList(man.readFiles(files));
		
		scrollPane.setPreferredSize(new Dimension(300, 300));
		scrollPane.setViewportView(list);
		panel.add(scrollPane);
		
		panel.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createEmptyBorder(0, 0, 10, 10),
				BorderFactory.createEtchedBorder()));
		
		frame.add(panel);
		
		// Buttons
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
		
		// Progress Bar
		progBar.setPreferredSize(new Dimension(315, 25));
		frame.add(progBar);
		
		//Layout
		frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.setSize(340,508);
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
			int modsDownloaded = 0;
			List<String> modsFailed = new ArrayList<String>();
			
			progBar.setMaximum(modsLink.size());
			progBar.setStringPainted(true);
			
			for (int i = 0; i < modsLink.size(); i++) // Loop through file
			{
				HttpResponse<String> response = null;
				try 
				{
					String api_key = "$2a$10$rVQoptJ.v8KWuPZQNSFjgu2/MRvHr45WWwiG3XMhrBtpWY1blFt42";
					
					String modID = "";
					String modFileID = "";
					
					int underscoreLoc = 0;
					int hashtagLoc = modsLink.get(i).length();
					
					for (int j = 0; j < modsLink.get(i).length(); j++) // Loop through line
					{
						if (modsLink.get(i).charAt(j) == '_')
						{
							underscoreLoc = j;
						}
						
						// Commenting System
						if (modsLink.get(i).charAt(j) == '#' && modsLink.get(i).charAt(j - 1) == ' ') // Check for space before # 
						{
							hashtagLoc = j - 1;
						}
						
						if (modsLink.get(i).charAt(j) == '#' && modsLink.get(i).charAt(j - 1) != ' ') // Check if no space before #
						{
							hashtagLoc = j;
						}
						
						modID = modsLink.get(i).substring(0, underscoreLoc);
						modFileID = modsLink.get(i).substring(underscoreLoc + 1, hashtagLoc);
					}
					
//					System.out.println("modID: " + modID);
//					System.out.println("modFileID: " + modFileID);
					
					HttpRequest request = HttpRequest.newBuilder() // API Request
							.uri(URI.create("https://api.curseforge.com/v1/mods/" + modID + "/files/" + modFileID))
							.header("Accept", "application/json")
							.header("x-api-key", api_key)
							.method("GET", HttpRequest.BodyPublishers.noBody())
							.build();
					
					response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
					
					JSONParser parser = new JSONParser(); // Parse Response
					JSONObject jsonResponse = (JSONObject) parser.parse(response.body().toString());
					
					JSONObject data = (JSONObject) jsonResponse.get("data");
					
					if (data.get("downloadUrl") != null)
					{
						String modCDN = data.get("downloadUrl").toString();
						String fileName = data.get("fileName").toString();
						
	//					System.out.println(modCDN);
						
						// Download File
						Files.copy(
								new URL(modCDN).openStream(),
								Paths.get(System.getenv("APPDATA") + "\\.minecraft\\mods\\" + fileName),
								StandardCopyOption.REPLACE_EXISTING);
						refreshList();
						
						System.out.println("Finished downloading " + fileName);
					}
					else
					{
						modsFailed.add(modsLink.get(i));
					}
					
					modsDownloaded += 1;
					
					progBar.setValue(modsDownloaded);
				}
				catch (IOException | InterruptedException | ParseException e1) 
				{
					e1.printStackTrace();
					modsFailed.add(modsLink.get(i));
				}
			}
			System.out.println("Mods downloaded: " + modsDownloaded + "/" + modsLink.size());
			
			if (modsFailed.size() > 0)
			{
				System.out.println("Mods failed: " + Arrays.toString(modsFailed.toArray()));
				try 
				{
					FileWriter writer = new FileWriter("ERROR.txt");
					writer.write("ERROR DOWNLOADING THE FOLLOWING FILES: \n\r" + Arrays.toString(modsFailed.toArray()));
					writer.close();
					
					Desktop.getDesktop().open(new File("ERROR.txt"));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
}
