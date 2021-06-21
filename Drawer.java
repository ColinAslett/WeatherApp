import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JPanel;

public class Drawer extends JPanel{
	Downloader D;
	//Misc Info
	String updated_time,faster_updated_time,Station;
	//Font Info
	Font Bold_Font = new Font("Trebuchet MS",Font.BOLD,13);
	Font Regular_Font = new Font("Trebuchet MS",Font.PLAIN,13);
	Font Small_Font = new Font("Trebuchet MS",Font.PLAIN,11);
	
	public void getMiscInfo(String time_1,String time_2,String st){
		updated_time = time_1;
		faster_updated_time = time_2;
		Station = st;
	}
	public void getDataClass(Downloader d){
		D = d;
	}
	public void paint(Graphics g){
		super.paintComponent(g);
		//good quality
		Map<?,?> desktopHints = (Map<?,?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
		Graphics2D g2d = (Graphics2D) g;
		if(desktopHints != null){
			g2d.setRenderingHints(desktopHints);
		}
		//Drawing Misc info in the top left corner
		g2d.setFont(Regular_Font);
		g2d.drawString("Data for weather station: " + Station,5,15);
		g2d.drawString("NAM + NAMNEST + GFS last update(UTC): " + updated_time,5,35);
		g2d.drawString("HRRR + RAP last update(UTC): " + faster_updated_time,5,55);
		g2d.drawLine(0, 60, this.getSize().width, 60);
		//Drawing Forecast details
		
		
		
		/*
		 * 
		 * Temperature Chart
		 *
		 */
		
		//Draw Graph, scale changes depending on max and min temperatures from all models
		
		//DrawGraph("Temperature(F)",35,550,400,100,g2d);
		
		int starting_graph_x = 35,ending_graph_x = 550;
		int starting_graph_y = 400,ending_graph_y = 100;
		int graph_y_diff = starting_graph_y - ending_graph_y;
		
		
		g2d.drawLine(starting_graph_x,starting_graph_y,starting_graph_x,ending_graph_y);
		g2d.drawLine(starting_graph_x,starting_graph_y,ending_graph_x,starting_graph_y);
		
		g2d.drawString("Temperature in Farenheit", 200, 80);
		//Math to determine the scale
		int Max_C = D.getMaxTemp();
		int Min_C = D.getMinTemp()-1;
		int C_Diff = Max_C - Min_C;
		int C_Pixel_Change = graph_y_diff/C_Diff;
		int C_divider =  (int)Math.ceil(((double)C_Diff)/10);
		
		
		for(int i = 0;i < 10;i++){
			g2d.drawLine(starting_graph_x, starting_graph_y - ((Min_C + i*(C_divider)-Min_C)*(C_Pixel_Change)), starting_graph_x-5, starting_graph_y - ((Min_C + i*(C_divider)-Min_C)*(C_Pixel_Change)));
			g2d.setFont(Small_Font);
			g2d.drawString((Min_C + i*(C_divider)) + "", 2,starting_graph_y - ((Min_C + i*(C_divider)-Min_C)*(C_Pixel_Change)));
			g2d.setFont(Regular_Font);
		}
		
		
		
		
		int faster_model_marker = 0;
		boolean faster_model_start = false;
		String GFS_T="",NAM_T="",RAP_T="",HRRR_T="",NAMNEST_T="";
		String Forecast_TD;
		int y_counter = 0;
		for(int i = 0;i < D.gfs.RT_LIST.size();i++){
			
			if(D.hrrr.RT_LIST.get(0).TD.equals(D.gfs.RT_LIST.get(i).TD)){
				faster_model_marker = i;
				faster_model_start = true;
			}
			
			Forecast_TD = D.gfs.RT_LIST.get(i).TD;
			GFS_T = D.gfs.GFS_LIST.get(i).T2MS;
			
			
			if(i < D.nam.RT_LIST.size()){
				NAM_T = D.nam.FC_LIST.get(i).T2MS;
				g2d.setColor(Color.BLUE);
				g2d.fillOval(starting_graph_x+(i*4), starting_graph_y - ((((int)Double.parseDouble(NAM_T))-Min_C)*(C_Pixel_Change)), 3, 3);
			}
			if(i < D.namnest.RT_LIST.size()){
				NAMNEST_T = D.namnest.FC_LIST.get(i).T2MS;
				g2d.setColor(Color.PINK);
				g2d.fillOval(starting_graph_x+(i*4), starting_graph_y - ((((int)Double.parseDouble(NAMNEST_T))-Min_C)*(C_Pixel_Change)), 3, 3);
			}
			if((i-faster_model_marker < D.hrrr.RT_LIST.size()) && faster_model_start == true){
				HRRR_T = D.hrrr.FC_LIST.get(i-faster_model_marker).T2MS;
				g2d.setColor(Color.BLACK);
				g2d.fillOval(starting_graph_x+(i*4), starting_graph_y - ((((int)Double.parseDouble(HRRR_T))-Min_C)*(C_Pixel_Change)), 3, 3);
			}
			if((i-faster_model_marker < D.rap.RT_LIST.size()) && faster_model_start == true){
				RAP_T = D.rap.FC_LIST.get(i-faster_model_marker).T2MS;
				g2d.setColor(Color.WHITE);
				g2d.fillOval(starting_graph_x+(i*4), starting_graph_y - ((((int)Double.parseDouble(RAP_T))-Min_C)*(C_Pixel_Change)), 3, 3);
			}
			
			//double y_pos = 400 - ((int)Double.parseDouble(GFS_T))*(6);
			g2d.setColor(Color.RED);
			g2d.fillOval(starting_graph_x+(i*4), starting_graph_y - ((((int)Double.parseDouble(GFS_T))-Min_C)*(C_Pixel_Change)), 3, 3);

			
			
			//g2d.setFont(Bold_Font);
			//g2d.drawString("Forecast Hour: " + Forecast_TD, s_x+((i/25)*425), s_y+(y_counter*y_m));
			//g2d.setFont(Regular_font);
			//g2d.drawString("Temp(C): " + GFS_T + "(GFS)," + NAM_T + "(NAM),"+NAMNEST_T+"(NAMNEST),"+HRRR_T+"(HRRR)", s_x+((i/25)*425), s_y+20+(y_counter*y_m));
			
			
			//All of these have to be reset before iterating again
			GFS_T="";
			NAM_T="";
			RAP_T="";
			HRRR_T="";
			NAMNEST_T="";
			y_counter++;
		}
		
		/*
		 * 
		 * Wind Speed Chart
		 *
		 */
		g2d.setColor(Color.BLACK);
		
		g2d.drawString("Wind Speed(MPH)", 200, 580);
		
		starting_graph_x = 35;
	    ending_graph_x = 550;
		starting_graph_y = 900;
		ending_graph_y = 600;
		graph_y_diff = starting_graph_y - ending_graph_y;
		
		
		g2d.drawLine(starting_graph_x,starting_graph_y,starting_graph_x,ending_graph_y);
		g2d.drawLine(starting_graph_x,starting_graph_y,ending_graph_x,starting_graph_y);
		
		//Math to determine the scale
		int Max_WS = D.getMaxWS();
		int Min_WS = D.getMinWS()-1;
		int WS_Diff = Max_WS - Min_WS;
		System.out.println(WS_Diff);
		int WS_Pixel_Change = graph_y_diff/WS_Diff;
		int WS_divider =  (int)Math.ceil(((double)WS_Diff)/10);
		
		for(int i = 0;i < 10;i++){
			g2d.drawLine(starting_graph_x, starting_graph_y - ((Min_WS + i*(WS_divider)-Min_WS)*(WS_Pixel_Change)), starting_graph_x-5, starting_graph_y - ((Min_WS + i*(WS_divider)-Min_WS)*(WS_Pixel_Change)));
			g2d.setFont(Small_Font);
			g2d.drawString((Min_WS + i*(WS_divider)) + "", 2,starting_graph_y - ((Min_WS + i*(WS_divider)-Min_WS)*(WS_Pixel_Change)));
			g2d.setFont(Regular_Font);
		}
		
		faster_model_marker = 0;
		faster_model_start = false;
		NAM_T="";
		RAP_T="";
		HRRR_T="";
		NAMNEST_T="";
		Forecast_TD = "";
		y_counter = 0;
		for(int i = 0;i < D.nam.RT_LIST.size();i++){
			
			if(D.hrrr.RT_LIST.get(0).TD.equals(D.nam.RT_LIST.get(i).TD)){
				faster_model_marker = i;
				faster_model_start = true;
			}
			
			Forecast_TD = D.nam.RT_LIST.get(i).TD;
			NAM_T = D.nam.FC_LIST.get(i).WIND_SPEED;
			
			if(i < D.namnest.RT_LIST.size()){
				NAMNEST_T = D.namnest.FC_LIST.get(i).WIND_SPEED;
				g2d.setColor(Color.PINK);
				g2d.fillOval(starting_graph_x+(i*4), starting_graph_y - ((((int)Double.parseDouble(NAMNEST_T))-Min_WS)*(WS_Pixel_Change)), 3, 3);
			}
			if((i-faster_model_marker < D.hrrr.RT_LIST.size()) && faster_model_start == true){
				HRRR_T = D.hrrr.FC_LIST.get(i-faster_model_marker).WIND_SPEED;
				g2d.setColor(Color.BLACK);
				g2d.fillOval(starting_graph_x+(i*4), starting_graph_y - ((((int)Double.parseDouble(HRRR_T))-Min_WS)*(WS_Pixel_Change)), 3, 3);
			}
			if((i-faster_model_marker < D.rap.RT_LIST.size()) && faster_model_start == true){
				RAP_T = D.rap.FC_LIST.get(i-faster_model_marker).WIND_SPEED;
				g2d.setColor(Color.WHITE);
				g2d.fillOval(starting_graph_x+(i*4), starting_graph_y - ((((int)Double.parseDouble(RAP_T))-Min_WS)*(WS_Pixel_Change)), 3, 3);
			}
			
			//double y_pos = 400 - ((int)Double.parseDouble(GFS_T))*(6);
			g2d.setColor(Color.BLUE);
			g2d.fillOval(starting_graph_x+(i*4), starting_graph_y - ((((int)Double.parseDouble(NAM_T))-Min_WS)*(WS_Pixel_Change)), 3, 3);

			
			
			//g2d.setFont(Bold_Font);
			//g2d.drawString("Forecast Hour: " + Forecast_TD, s_x+((i/25)*425), s_y+(y_counter*y_m));
			//g2d.setFont(Regular_font);
			//g2d.drawString("Temp(C): " + GFS_T + "(GFS)," + NAM_T + "(NAM),"+NAMNEST_T+"(NAMNEST),"+HRRR_T+"(HRRR)", s_x+((i/25)*425), s_y+20+(y_counter*y_m));
			
			
			//All of these have to be reset before iterating again
			GFS_T="";
			NAM_T="";
			RAP_T="";
			HRRR_T="";
			NAMNEST_T="";
			y_counter++;
		}
	}
	//Function that draws whatever is needed
	/*
	private void DrawGraph(String title,int starting_graph_x,int ending_graph_x,int starting_graph_y,int ending_graph_y,Graphics2D g2d){
		int graph_y_diff = starting_graph_y - ending_graph_y;
		
		//Drawing the outline of the graph
		g2d.drawLine(starting_graph_x,starting_graph_y,starting_graph_x,ending_graph_y);
		g2d.drawLine(starting_graph_x,starting_graph_y,ending_graph_x,starting_graph_y);
		g2d.drawString(title, ending_graph_y+50, starting_graph_x+50);
		
		//Getting the right information from the downloader class
		ArrayList<String> GFS_INFO = new ArrayList<>();
		ArrayList<String> NAM_INFO = new ArrayList<>();
		ArrayList<String> NAMNEST_INFO = new ArrayList<>();
		ArrayList<String> HRRR_INFO = new ArrayList<>();
		ArrayList<String> RAP_INFO = new ArrayList<>();
		int Max_C = 0,Min_C = 0;
		if(title.equals("Temperature(F)")){
			Max_C = D.getMaxTemp();
			Min_C = D.getMinTemp()-1;
			for(int i = 0;i < D.gfs.GFS_LIST.size();i++){
				//GFS_INFO.add(e)
			}
		}
		//Math to determine the scale
		int C_Diff = Max_C - Min_C;
		int C_Pixel_Change = graph_y_diff/C_Diff;
		int C_divider =  (int)Math.ceil(((double)C_Diff)/10);
		
		//Something still wrong here, but not sure exactly how to fix it
		for(int i = 0;i < 10;i++){
			g2d.drawLine(starting_graph_x, starting_graph_y - ((Min_C + i*(C_divider)-Min_C)*(C_Pixel_Change)), starting_graph_x-5, starting_graph_y - ((Min_C + i*(C_divider)-Min_C)*(C_Pixel_Change)));
			g2d.setFont(Small_Font);
			g2d.drawString((Min_C + i*(C_divider)) + "", 2,starting_graph_y - ((Min_C + i*(C_divider)-Min_C)*(C_Pixel_Change)));
			g2d.setFont(Regular_Font);
		}
		
		int faster_model_marker = 0;
		boolean faster_model_start = false;
		String NAM_T="",RAP_T="",HRRR_T="",NAMNEST_T="";
		//String Forecast_TD = "";
		//int y_counter = 0;
		
	}
	*/
}
