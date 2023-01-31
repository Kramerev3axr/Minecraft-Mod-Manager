import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import lib.Manager;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class managerApp 
{
	static JPanel panel = new JPanel();
	static JList list;
	

	public static void main(String[] args)
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
		list.setPreferredSize(new Dimension(300, 300));
		panel.add(list);
		
		panel.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createEmptyBorder(0, 0, 10, 10),
				BorderFactory.createEtchedBorder()));
		
		frame.add(panel);
		
		//Buttons
		JButton add = new JButton("Add");
		add.setPreferredSize(new Dimension(155, 30));
		add.setFocusPainted(false);
		add.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				man.addMod();
				refreshList();
			}
		});
		frame.add(add);

		
		JButton remove = new JButton("Remove");
		remove.setPreferredSize(new Dimension(154, 30));
		remove.setFocusPainted(false);
		remove.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				man.removeMod(list);
				refreshList();
			}
		});
		frame.add(remove);
		
		//Layout
		
		frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.setSize(340,415);
		frame.setVisible(true);
	}
	
	public static void refreshList()
	{
		Manager man = new Manager();
		String[] files = {};
		
		panel.remove(list);
		
		list = new JList(man.readFiles(files));
		list.setPreferredSize(new Dimension(300, 300));
		
		panel.add(list);
		
		panel.revalidate();
		panel.repaint();
			
	}
}
