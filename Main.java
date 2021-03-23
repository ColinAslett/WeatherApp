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
	//Global Variables for Date and Time
	Date date = new Date();
	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	String year,month,day;
	String UTC_TIME;
	//Data Class, this class literally holds infinite weather data
	Data data = new Data();
	//Downloading Class that handles all the downloading and parsing of the info
	Downloader downloader = new Downloader(data);
	//Station Name, Station table at: https://vlab.ncep.noaa.gov/web/mdl/nbm-stations-v4.0 or NOAA Forecast
	String STATION = "KMGM";
	//NOAA NBM Cycle Times
	int[] NBM_CYCLE_TIMES = {1,7,13,19};
	//NAM THUNDER STUFF and Cycle Times
	String NAM_THUNDER_BASE_LINK = "http://www.meteo.psu.edu/bufkit/data/NAM/";
	int[] NAM_CYCLE_TIMES = {00,06,12,18};
	//HRRR THUNDER STUFF
	String HRRR_THUNDER_BASE_LINK = "http://www.meteo.psu.edu/bufkit/data/HRRR/";
	//RAP THUNDER STUFF
	String RAP_THUNDER_BASE_LINK = "http://www.meteo.psu.edu/bufkit/data/RAP/";
	//GFS THUNDER STUFF
	String GFS_THUNDER_BASE_LINK = "http://www.meteo.psu.edu/bufkit/data/GFS/";
	//19/hrrr_khio.buf
	public Main() throws Exception{
		//Getting the Date and Time
		
		cal.setTime(date);
		year = Integer.toString(cal.get(Calendar.YEAR));
		month = Integer.toString(cal.get(Calendar.MONTH)+1);//Months start at 1 not 0
		day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		
		//RETRIEVE_NOAA_NBM_DATA();
		//RETRIEVE_NOAA_MOS_DATA();
		RETRIEVE_NAM_THUNDER();
		RETRIEVE_HRRR_THUNDER();
		RETRIEVE_RAP_THUNDER();
		RETRIEVE_GFS_THUNDER();
		data.printSevereWeatherSummary();
	}
	//Retrieving Severe Weather Info From GFS
	private void RETRIEVE_GFS_THUNDER() throws Exception{
		//Example Link: http://www.meteo.psu.edu/bufkit/data/NAM/18/nam_khio.buf
		//Adjusting to Correct Cycle Time
		String GFS_TIME = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		int index = 0;
		for(int i = 0;i < NAM_CYCLE_TIMES.length;i++){
			if(Integer.parseInt(GFS_TIME) > NAM_CYCLE_TIMES[i]+2){
				index++;
			}
		}
		if(index == 1 || index == 0){
			GFS_TIME = Integer.toString(NAM_CYCLE_TIMES[3]);
		}
		else{
			GFS_TIME = Integer.toString(NAM_CYCLE_TIMES[index-2]);
		}
		if(GFS_TIME.equals("6") || GFS_TIME.equals("0")){
			GFS_TIME = "0" + GFS_TIME;
		}
		//Building the Link
		String NAM_LINK = GFS_THUNDER_BASE_LINK + GFS_TIME + "/gfs3_" + STATION.toLowerCase() + ".buf";
		downloader.THUNDER_DOWNLOADER(NAM_LINK,"GFS",GFS_TIME);
	}
	//Retrieving Severe Weather Infor From RAP
	private void RETRIEVE_RAP_THUNDER() throws Exception{
		String RAP_TIME = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		if(Integer.parseInt(RAP_TIME) <= 2){//about a 2hr delay in HRRR info
			RAP_TIME = Integer.toString(21+Integer.parseInt(RAP_TIME));
		}
		else{
			RAP_TIME = Integer.toString(cal.get(Calendar.HOUR_OF_DAY)-2);
		}
		String RAP_LINK = RAP_THUNDER_BASE_LINK + RAP_TIME + "/rap_" + STATION.toLowerCase() + ".buf";
		downloader.THUNDER_DOWNLOADER(RAP_LINK,"RAP",RAP_TIME);
	}	
	//Retrieving Severe Weather Info From HRRR
	private void RETRIEVE_HRRR_THUNDER() throws Exception{
		String HRRR_TIME = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		if(Integer.parseInt(HRRR_TIME) <= 2){//about a 2hr delay in HRRR info
			HRRR_TIME = Integer.toString(21+Integer.parseInt(HRRR_TIME));
		}
		else{
			HRRR_TIME = Integer.toString(cal.get(Calendar.HOUR_OF_DAY)-2);
		}
		String HRRR_LINK = HRRR_THUNDER_BASE_LINK + HRRR_TIME + "/hrrr_" + STATION.toLowerCase() + ".buf";
		downloader.THUNDER_DOWNLOADER(HRRR_LINK,"HRRR",HRRR_TIME);
	}
	//Retrieving Severe Weather Info From NAM
	private void RETRIEVE_NAM_THUNDER() throws Exception{
		//Example Link: http://www.meteo.psu.edu/bufkit/data/NAM/18/nam_khio.buf
		//Adjusting to Correct Cycle Time
		String NAM_TIME = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		int index = 0;
		for(int i = 0;i < NAM_CYCLE_TIMES.length;i++){
			if(Integer.parseInt(NAM_TIME) > NAM_CYCLE_TIMES[i]+2){
				index++;
			}
		}
		if(index == 1 || index == 0){
			NAM_TIME = Integer.toString(NAM_CYCLE_TIMES[3]);
		}
		else{
			NAM_TIME = Integer.toString(NAM_CYCLE_TIMES[index-2]);
		}
		if(NAM_TIME.equals("6") || NAM_TIME.equals("0")){
			NAM_TIME = "0" + NAM_TIME;
		}
		//Building the Link
		String NAM_LINK = NAM_THUNDER_BASE_LINK + NAM_TIME + "/nam_" + STATION.toLowerCase() + ".buf";
		downloader.THUNDER_DOWNLOADER(NAM_LINK,"NAM",NAM_TIME);
	}
	//NOAA MOS RETRIEVAL, NAM AND GFS(SHORT AND LONG RANGE)
	private void RETRIEVE_NOAA_MOS_DATA() {
		//Key Guide: https://vlab.ncep.noaa.gov/web/mdl/met-card
		//GFS SHORT and LONG RANGE MOS(MAV AND MEX): https://ftpprd.ncep.noaa.gov/data/nccf/com/gfs/prod/
		//NAM MOS: https://ftpprd.ncep.noaa.gov/data/nccf/com/nam/prod/
		
	}
	//NOAA NBM RETRIEVAL
	private void RETRIEVE_NOAA_NBM_DATA() throws Exception {
		//NBP has special rules, needs to be on a cycle time(01,7,13,19)
		NOAA_NBM_BASE_CYCLE = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		int temp_time = Integer.parseInt(NOAA_NBM_BASE_CYCLE);
		int index = 0;
		for(int i = 0;i < NBM_CYCLE_TIMES.length;i++){
			if(temp_time > NBM_CYCLE_TIMES[i]){
				index++;
			}
		}
		if(index != 0){
			NOAA_NBM_BASE_CYCLE = Integer.toString(NBM_CYCLE_TIMES[index-1]);
		}
		else{
			day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH)-1);
			NOAA_NBM_BASE_CYCLE = Integer.toString(NBM_CYCLE_TIMES[3]);
		}
		
		if(month.length() == 1){
			month = "0" + month;
		}
		NOAA_NBM_BASE_DATE = year+month+day;

		System.out.println(NOAA_NBM_BASE_DATE + " , " + NOAA_NBM_BASE_CYCLE +" UTC Time");
		//Building the Proper Strings
		String NOAA_NBM_NBE_LINK = NOAA_NBM_BASE_LINK + NOAA_NBM_BASE_DATE + "/" + NOAA_NBM_BASE_CYCLE + "/text/" + NOAA_NBM_NBE + NOAA_NBM_BASE_CYCLE + "z";
		String NOAA_NBM_NBP_LINK = NOAA_NBM_BASE_LINK + NOAA_NBM_BASE_DATE + "/" + NOAA_NBM_BASE_CYCLE + "/text/" + NOAA_NBM_NBP + NOAA_NBM_BASE_CYCLE + "z";
		
		//downloader.NOAA_NBE(NOAA_NBM_NBE_LINK,STATION);
		downloader.NOAA_NBE(NOAA_NBM_NBE_LINK,STATION);
		
		
	}
	public static void main(String[] args) throws Exception {
		Main m = new Main();
	}
}
