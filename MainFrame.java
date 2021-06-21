import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame implements ActionListener{
	//GUI stuff
	JMenuBar menuBar;
	JMenu menu1,menu2;
	
	Drawer drawer = new Drawer();
	public MainFrame(){
		
	}
	
	public String retrieveStationName(){
		initFrame();
		String temp = JOptionPane.showInputDialog(null,"Enter Station Name(khio for hillsboro)");
		return temp;
	}
	public void startDrawing(Downloader d){
		drawer.getDataClass(d);
		this.add(drawer);
		
		//Creating MenuBar
		menuBar = new JMenuBar();
		
		
		menu1 = new JMenu("Forecast Details");
		menu2 = new JMenu("Severe Weather");
		
		
		JMenuItem temperatureButton = new JMenuItem("Temperature");
		temperatureButton.addActionListener(this);
		menu1.add(temperatureButton);
		JMenuItem windspeedButton = new JMenuItem("Wind Speed");
		windspeedButton.addActionListener(this);
		menu1.add(windspeedButton);
		
		menuBar.add(menu1);
		menuBar.add(menu2);
		
		
		this.setJMenuBar(menuBar);
		
		SwingUtilities.updateComponentTreeUI(this);
		
	}
	
	public void initFrame(){
		this.setTitle("Weather Forecast Application");
		this.setSize(500,500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
	}

	public void getMiscInfo(String updatedTime, String fastUpdatedTime, String station) {
		drawer.getMiscInfo(updatedTime,fastUpdatedTime,station);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(arg0.getActionCommand());//This will get name of the button clicked, probably the easiest way to identify whats being clicked
	}
	
}
