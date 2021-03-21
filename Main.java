import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Main {
	//NOAA NBM Download Link
	String NOAA_NBM_BASE_LINK = "https://nomads.ncep.noaa.gov/pub/data/nccf/com/blend/prod/blend.";
	String NOAA_NBM_BASE_DATE = "20210319";//YYYYMMDD
	String NOAA_NBM_BASE_CYCLE = "1";//ANY NUMBER BETWEEN 0 and 22
	String NOAA_NBM_NBE = "blend_nbetx.t";
	String NOAA_NBM_NBH = "blend_nbhtx.t";
	String NOAA_NBM_NBP = "blend_nbptx.t";
	String NOAA_NBM_NBS = "blend_nbstx.t";
	String NOAA_NBM_NBX = "blend_nbxtx.t";
	//Data Class, this class literally holds infinite weather data
	Data data = new Data();
	//Downloading Class that handles all the downloading and parsing of the info
	Downloader downloader = new Downloader(data);
	//Station Name, Station table at: https://vlab.ncep.noaa.gov/web/mdl/nbm-stations-v4.0 or NOAA Forecast
	String STATION = "KHIO";
	//NOAA NBM Cycle Times
	int[] NBM_CYCLE_TIMES = {1,7,13,19};
	public Main() throws Exception{
		RETRIEVE_NOAA_NBM_DATA();
	}
	private void RETRIEVE_NOAA_NBM_DATA() throws Exception {
		//Getting the Hour of the day, 24HR Time
		
		Date date = new Date();

		
		//Getting the latest Cycle minus 5 hour because sometimes its late and 7 hours because UTC is 7 hours ahead of
		//PST Time
		
		//THIS WONT WORK cause it could be negative sometimes
		//NOAA_NBM_BASE_CYCLE = Integer.toString((Integer.parseInt(formattedDate) - 12));
		NOAA_NBM_BASE_CYCLE = "01";
		//Getting the Day, Month, and Year
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTime(date);
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH)+1);//Months start at 1 not 0
		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		NOAA_NBM_BASE_CYCLE = Integer.toString(cal.get(Calendar.HOUR_OF_DAY)-1);
		int temp_time = Integer.parseInt(NOAA_NBM_BASE_CYCLE);
		//NBP has special rules, needs to be on a cycle time(01,7,13,19)
		int index = 0;
		for(int i = 0;i < NBM_CYCLE_TIMES.length;i++){
			if(temp_time > NBM_CYCLE_TIMES[i]){
				index++;
			}
		}
		if(index != 0){
			NOAA_NBM_BASE_CYCLE = Integer.toString(NBM_CYCLE_TIMES[index-1]);
		}
		else{//We have to go back a day and index 3
			day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH)-1);
			NOAA_NBM_BASE_CYCLE = Integer.toString(NBM_CYCLE_TIMES[3]);
		}
		
		if(month.length() == 1){
			//There needs to be a 0 before the month if its only 1 digit long
			month = "0" + month;
		}
		NOAA_NBM_BASE_DATE = year+month+day;

		System.out.println(NOAA_NBM_BASE_DATE + " , " + NOAA_NBM_BASE_CYCLE +" UTC Time");
		//Building the Proper Strings
		String NOAA_NBM_NBE_LINK = NOAA_NBM_BASE_LINK + NOAA_NBM_BASE_DATE + "/" + NOAA_NBM_BASE_CYCLE + "/text/" + NOAA_NBM_NBE + NOAA_NBM_BASE_CYCLE + "z";
		String NOAA_NBM_NBP_LINK = NOAA_NBM_BASE_LINK + NOAA_NBM_BASE_DATE + "/" + NOAA_NBM_BASE_CYCLE + "/text/" + NOAA_NBM_NBP + NOAA_NBM_BASE_CYCLE + "z";
		
		//downloader.NOAA_NBE(NOAA_NBM_NBE_LINK,STATION);
		downloader.NOAA_NBP(NOAA_NBM_NBP_LINK,STATION);
		
		
	}
	public static void main(String[] args) throws Exception {
		Main m = new Main();
	}
}
