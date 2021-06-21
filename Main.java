import java.awt.Graphics;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main{
	/*
	 * REFERENCES
	 * http://www.meteo.psu.edu/bufkit/bufkit_parameters.txt
	 * http://www.meteo.psu.edu/bufkit/data/GFS/18/gfs3_khio.buf
	 * http://www.meteo.psu.edu/bufkit/CONUS_NAM_12.html
	 */
	
	//Global Variables
	String TIME_OF_DAY;

	//Download Links
	String Station = "";
	String NAM_URL = "http://www.meteo.psu.edu/bufkit/data/NAM/";
	String NAMNEST_URL = "http://www.meteo.psu.edu/bufkit/data/NAMNEST/";
	String HRRR_URL = "http://www.meteo.psu.edu/bufkit/data/HRRR/";
	String RAP_URL = "http://www.meteo.psu.edu/bufkit/data/RAP/";
	String GFS_URL = "http://www.meteo.psu.edu/bufkit/data/GFS/";
	//Other Classes
	Downloader D = new Downloader();
	MainFrame MF = new MainFrame();
	public Main() throws Exception{
		Station = MF.retrieveStationName();
		GetDateAndTime();
		BuildModelDataLinks();
		MF.startDrawing(D);
		
	}
	
	//Standard Practice for weather is to use UTC
	private void GetDateAndTime(){
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Calendar C = Calendar.getInstance();
		TIME_OF_DAY = Integer.toString(C.get(Calendar.HOUR_OF_DAY));
		//System.out.println(TIME_OF_DAY);
	}
	
	private void BuildModelDataLinks() throws Exception{
		//Round Hour to nearest digit(0,6,12,18) and then go back 1 or 2 depending on which model
		//For NAM,NAMEST,GFS we subtract 7 hours and round down to be safe
		
		//NAM,GFS,NAMEST
		//Ternary Operators Reference: http://tutorials.jenkov.com/java/ternary-operator.html
		String UpdatedTime = Integer.toString((Integer.parseInt(TIME_OF_DAY)-7 < 0)?(Integer.parseInt(TIME_OF_DAY)+17):(Integer.parseInt(TIME_OF_DAY)-7));
		UpdatedTime = Integer.toString((Integer.parseInt(UpdatedTime)/6)*6);
		//The official time format is two digits long so there has to be a zero before 0 and 6
		UpdatedTime = (Integer.parseInt(UpdatedTime)<10)?("0"+UpdatedTime):(UpdatedTime);
		
		
		
		//RAP and HRRR, only a 3 hour delay in getting this info
		String FastUpdatedTime = Integer.toString((Integer.parseInt(TIME_OF_DAY)-3 < 0)?(Integer.parseInt(TIME_OF_DAY)+21):(Integer.parseInt(TIME_OF_DAY)-3));
		FastUpdatedTime = (Integer.parseInt(FastUpdatedTime)<10?("0"+FastUpdatedTime):(FastUpdatedTime));
		
		System.out.println("Weather Data for " + Station);
		System.out.println("NAM + NAMEST LAST UPDATE TIME(UTC): " + UpdatedTime);
		System.out.println("RAP + HRRRR LAST UPDATE TIME(UTC): " + FastUpdatedTime);
		
		D.DownloadModelData(NAM_URL+UpdatedTime+"/nam_"+Station+".buf","NAM");
		D.DownloadModelData(NAMNEST_URL+UpdatedTime+"/namnest_"+Station+".buf", "NAMNEST");
		D.DownloadModelData(HRRR_URL+FastUpdatedTime+"/hrrr_"+Station+".buf", "HRRR");
		D.DownloadModelData(RAP_URL+FastUpdatedTime+"/rap_"+Station+".buf", "RAP");
		D.DownloadModelData(GFS_URL+UpdatedTime+"/gfs3_"+Station+".buf", "GFS");
		
		MF.getMiscInfo(UpdatedTime,FastUpdatedTime,Station);
		
		//System.out.println(UpdatedTime);
		
		//Printing Hourly Weather Forecast
		D.printHourlyForecast();
		
		
	}
	public static void main(String[] args) throws Exception{
		Main M = new Main();
	}

}
