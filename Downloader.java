import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Downloader {
	//Model Classes
	NAM nam = new NAM();
	NAMNEST namnest = new NAMNEST();
	HRRR hrrr = new HRRR();
	RAP rap = new RAP();
	GFS gfs = new GFS();
	//Global Variables
	String[] ThunderVar = {"STID","SLAT","STIM","SHOW","LCLP","LCLT","BRCH"};
	//ArrayLists that make life easier for the drawer class
	
	
	public Downloader(){
	}
	
	public void DownloadModelData(String link,String model) throws Exception{
		URL url = new URL(link);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		ArrayList<String> temp = new ArrayList<>();
		int count = 0;
		boolean line_break = false;
		while((line = br.readLine()) != null){
			//Don't need line 0->3
			if(count > 3){
				//Retrieving Thunder Data
				if(Arrays.stream(ThunderVar).anyMatch(line::contains)){
					//System.out.println(line);
					temp.add(line);
					
				}
				//Retrieving Forecast Details
				if(line_break == true && !line.equals("")){
					temp.add(line);
					//System.out.println(line);
				}
				//Line break so the program know new info is on its way
				if(line.contains("TD2M")){
					line_break = true;
					temp.add("***********");
					//System.out.println("********");
				}
			}
			count++;
		}
		line_break = false;
		//Sort Data
		int counter = 0;
		ArrayList<RAW_THUNDER> RT_LIST = new ArrayList<>();
		ArrayList<RAW_FORECAST> FC_LIST = new ArrayList<>();
		ArrayList<GFS_FORECAST> GFS_LIST = new ArrayList<>();//GFS's model data is a little different so we want different parameters
		//Thunder Data
		String TD = "",FH = "",SHOW = "",LIFT="",SWET="",KINX="",TOTL="",CAPE="";
		String CINS = "",BRCH = "";
		//Forecast Data
		String SKIN_TEMP="",SNOW_FALL_ONE_HOUR="",P01M="",LCLD="",MCLD="",HCLD="";
		String WIND_SPEED="",T2MS="",WXTS="",WXTP="",WXTZ="",WXTR="",VSBK="";
		for(int i = 0;i < temp.size();i++){
			line = temp.get(i);
			if(!line.contains("***")){
				String[] splitted = line.split("\\s++");
				if(counter == 0){
					if(line_break == false){
						//All we want from this line is the time
						TD = splitted[8];
					}
					else{
						//System.out.println(line);
						if(!model.equals("GFS")){
							SKIN_TEMP = splitted[4];
							SNOW_FALL_ONE_HOUR = splitted[6];
						}
					}
				}
				else if(counter == 1){
					if(line_break == false){
						//Nothing
					}
					else{
						if(!model.equals("GFS")){
							P01M = splitted[0];
							LCLD = splitted[3];
							MCLD = splitted[4];
							HCLD = splitted[5];
						}
					}
				}
				else if(counter == 2){
					if(line_break == false){
						FH = splitted[2];
					}
					else{
						//We get wind components but we have to do math to transform it into wind speed(MPH)
						if(!model.equals("GFS")){
							double U_WIND_COMPONENT = Double.parseDouble(splitted[1]);
							double V_WIND_COMPONENT = Double.parseDouble(splitted[2]);
							double WS_RAW = Math.pow(U_WIND_COMPONENT, 2) + Math.pow(V_WIND_COMPONENT, 2);
							WIND_SPEED = Double.toString(Math.sqrt(WS_RAW)*2.237);
							T2MS = splitted[5];
						}
						else{
							T2MS = splitted[1];
						}
					}
				}
				else if(counter == 3){
					if(line_break == false){
						SHOW = splitted[2];
						LIFT = splitted[5];
						SWET = splitted[8];
						KINX = splitted[11];
					}
					else{
						if(!model.equals("GFS")){
							WXTS = splitted[1];
							WXTP = splitted[2];
							WXTZ = splitted[3];
							WXTR = splitted[4];
						}
						else{
							GFS_LIST.add(new GFS_FORECAST(T2MS));
							counter = -1;
						}
					}
				}
				else if(counter == 4){
					if(line_break == false){
						TOTL = splitted[8];
						CAPE = splitted[11];
					}
					else{
						if(!model.equals("GFS")){
							VSBK = splitted[5];
						}
						/*
						for(int a = 0;a < splitted.length;a++){
							System.out.println(splitted[a]);
						}
						*/
					}
				}
				else if(counter == 5){
					if(line_break == false){
						CINS = splitted[5];
					}
					else{
						if(!model.equals("GFS")){
							FC_LIST.add(new RAW_FORECAST(SKIN_TEMP,SNOW_FALL_ONE_HOUR,P01M,LCLD,MCLD,HCLD,WIND_SPEED,T2MS,WXTS,WXTP,WXTZ,WXTR,VSBK));
							counter = -1;
						}
					}
				}
				else if(counter == 6){
					if(line_break == false){
						BRCH = splitted[2];
						//reset counter to 0 and create a new raw thunder since this forecast
						//hour is done
						RT_LIST.add(new RAW_THUNDER(TD,FH,SHOW,LIFT,SWET,KINX,TOTL,CAPE,CINS,BRCH));
						counter = -1;
					}
				}
			}
			else{
				line_break = true;
				counter = -1;
			}
			counter++;
		}
		//distributing data to the correct model
		if(model.equals("NAM")){
			nam.writeData(RT_LIST, FC_LIST);
			System.out.println("NAM DATA SUCCESFULLY DOWNLOADED");
		}
		else if(model.equals("NAMNEST")){
			namnest.writeData(RT_LIST,FC_LIST);
			System.out.println("NAMNEST DATA SUCCESFULLY DOWNLOADED");
		}
		else if(model.equals("HRRR")){
			hrrr.writeData(RT_LIST, FC_LIST);
			System.out.println("HRRR DATA SUCCESFULLY DOWNLOADED");
		}
		else if(model.equals("RAP")){
			rap.writeData(RT_LIST, FC_LIST);
			System.out.println("RAP DATA SUCCESFULLY DOWNLOADED");
		}
		else if(model.equals("GFS")){
			gfs.writeData(RT_LIST,GFS_LIST);
			System.out.println("GFS DATA SUCCESFULLY DOWNLOADED");
		}
		//rap.printForecast();
		
		
		
	}
	
	
	//Printing Out Hourly Forecast
	public void printHourlyForecast(){
		//NAM and NAMEST start way behind so print theirs first
		int faster_model_marker = 0;
		boolean faster_model_start = false;
		for(int i = 0;i < gfs.RT_LIST.size();i++){
			System.out.println("Forecast Hour: " + gfs.RT_LIST.get(i).TD);
			if(hrrr.RT_LIST.get(0).TD.equals(gfs.RT_LIST.get(i).TD)){
				faster_model_marker = i;
				faster_model_start = true;
			}
			//Printing out Temperature
			//GFS Forecast
			System.out.println("GFS: Forecast Temp(C): "+gfs.GFS_LIST.get(i).T2MS);
			//NAM Forecast
			if(i < nam.RT_LIST.size()){
				System.out.println("NAM: Forecast Temp(C): "+nam.FC_LIST.get(i).T2MS+" , RainFall(mm): "+nam.FC_LIST.get(i).P01M);
			}
			//NAMNEST Forecast
			if(i < namnest.RT_LIST.size()){
				System.out.println("NAMNEST: Forecast Temp(C): "+namnest.FC_LIST.get(i).T2MS+" , RainFall(mm): "+namnest.FC_LIST.get(i).P01M);
			}
			if((i-faster_model_marker < hrrr.RT_LIST.size()) && faster_model_start == true){
				System.out.println("HRRR: Forecast Temp(C): "+hrrr.FC_LIST.get(i-faster_model_marker).T2MS + " , Rainfall(mm): "+hrrr.FC_LIST.get(i-faster_model_marker).P01M);

			}
			if((i-faster_model_marker < rap.RT_LIST.size()) && faster_model_start == true){
				System.out.println("RAP: Forecast Temp(C): "+rap.FC_LIST.get(i-faster_model_marker).T2MS+" , Rainfall(mm): "+rap.FC_LIST.get(i-faster_model_marker).P01M);
			}
			System.out.println("*******");
		}
	}
	//Getting Max Temp, useful for graph scaling
	public int getMaxTemp() {
		int max = -1000;
		for(int i = 0;i < gfs.GFS_LIST.size();i++){
			if(((int)Double.parseDouble(gfs.GFS_LIST.get(i).T2MS)) > max){
				max = ((int)Double.parseDouble(gfs.GFS_LIST.get(i).T2MS));
			}
			if(i < hrrr.FC_LIST.size() && ((int)Double.parseDouble(hrrr.FC_LIST.get(i).T2MS)) > max){
				max = ((int)Double.parseDouble(hrrr.FC_LIST.get(i).T2MS));
			}
			if(i < nam.FC_LIST.size() && ((int)Double.parseDouble(nam.FC_LIST.get(i).T2MS)) > max){
				max = ((int)Double.parseDouble(nam.FC_LIST.get(i).T2MS));
			}
			if(i < namnest.FC_LIST.size() && ((int)Double.parseDouble(namnest.FC_LIST.get(i).T2MS)) > max){
				max = ((int)Double.parseDouble(namnest.FC_LIST.get(i).T2MS));
			}
			if(i < rap.FC_LIST.size() && ((int)Double.parseDouble(rap.FC_LIST.get(i).T2MS)) > max){
				max = ((int)Double.parseDouble(rap.FC_LIST.get(i).T2MS));
			}
		}
		return max;
	}
	//Getting Min Temp, useful for graph scaling
	public int getMinTemp() {
		int min = 1000;
		for(int i = 0;i < gfs.GFS_LIST.size();i++){
			if(((int)Double.parseDouble(gfs.GFS_LIST.get(i).T2MS)) < min){
				min = ((int)Double.parseDouble(gfs.GFS_LIST.get(i).T2MS));
			}
			if(i < hrrr.FC_LIST.size() && ((int)Double.parseDouble(hrrr.FC_LIST.get(i).T2MS)) < min){
				min = ((int)Double.parseDouble(hrrr.FC_LIST.get(i).T2MS));
			}
			if(i < nam.FC_LIST.size() && ((int)Double.parseDouble(nam.FC_LIST.get(i).T2MS)) < min){
				min = ((int)Double.parseDouble(nam.FC_LIST.get(i).T2MS));
			}
			if(i < namnest.FC_LIST.size() && ((int)Double.parseDouble(namnest.FC_LIST.get(i).T2MS)) < min){
				min = ((int)Double.parseDouble(namnest.FC_LIST.get(i).T2MS));
			}
			if(i < rap.FC_LIST.size() && ((int)Double.parseDouble(rap.FC_LIST.get(i).T2MS)) < min){
				min = ((int)Double.parseDouble(rap.FC_LIST.get(i).T2MS));
			}
		}
		return min;
	}
	//Getting max WS, useful for graph Scaling
	public int getMaxWS(){
		int max = -1000;
		for(int i = 0;i < nam.FC_LIST.size();i++){
			max = (int) ((Double.parseDouble(nam.FC_LIST.get(i).WIND_SPEED))>max?(Double.parseDouble(nam.FC_LIST.get(i).WIND_SPEED)):(max));
			if(i < hrrr.FC_LIST.size() && Double.parseDouble(hrrr.FC_LIST.get(i).WIND_SPEED) > max){
				max = (int)Double.parseDouble(hrrr.FC_LIST.get(i).WIND_SPEED);
			}
			if(i < namnest.FC_LIST.size() && Double.parseDouble(namnest.FC_LIST.get(i).WIND_SPEED) > max){
				max = (int)Double.parseDouble(namnest.FC_LIST.get(i).WIND_SPEED);
			}
			if(i < rap.FC_LIST.size() && Double.parseDouble(rap.FC_LIST.get(i).WIND_SPEED) > max){
				max = (int)Double.parseDouble(rap.FC_LIST.get(i).WIND_SPEED);
			}
			//FastUpatedTime = (Integer.parseInt(FastUpdatedTime)<10?("0"+FastUpdatedTime):(FastUpdatedTime));
		}
		return max;
	}
	public int getMinWS(){
		int min = 1000;
		for(int i = 0;i < nam.FC_LIST.size();i++){
			min = (int) ((Double.parseDouble(nam.FC_LIST.get(i).WIND_SPEED))<min?(Double.parseDouble(nam.FC_LIST.get(i).WIND_SPEED)):(min));
			if(i < hrrr.FC_LIST.size() && Double.parseDouble(hrrr.FC_LIST.get(i).WIND_SPEED) > min){
				min = (int)Double.parseDouble(hrrr.FC_LIST.get(i).WIND_SPEED);
			}
			if(i < namnest.FC_LIST.size() && Double.parseDouble(namnest.FC_LIST.get(i).WIND_SPEED) < min){
				min = (int)Double.parseDouble(namnest.FC_LIST.get(i).WIND_SPEED);
			}
			if(i < rap.FC_LIST.size() && Double.parseDouble(rap.FC_LIST.get(i).WIND_SPEED) < min){
				min = (int)Double.parseDouble(rap.FC_LIST.get(i).WIND_SPEED);
			}
			//FastUpatedTime = (Integer.parseInt(FastUpdatedTime)<10?("0"+FastUpdatedTime):(FastUpdatedTime));
		}
		return min;
	}
	
}
//Special GFS Forecast class
class GFS_FORECAST{
	String T2MS="";
	public GFS_FORECAST(String t2ms){
		T2MS = Double.toString(((Double.parseDouble(t2ms) * 1.8)+32));
	}
}
class RAW_THUNDER{
	//Thunder Data
	String TD = "",FH = "",SHOW = "",LIFT="",SWET="",KINX="",TOTL="",CAPE="";
	String CINS = "",BRCH = "";
	public RAW_THUNDER(String td,String fh,String show,String lift,String swet,String kinx,
			String totl,String cape,String cins,String brch){
		//Adjust Forecast hour from UTC to PST
		boolean day_change = false;
		int temp = Integer.parseInt(td.substring(td.length()-4,td.length()-2))-7;
		//this could all be a ternary operation but it hurts my head
		if(temp < 0){
			temp = 24 + temp;
			day_change = true;
		}
		if(day_change == true){
			int new_date = Integer.parseInt(td.substring(td.length()-7,td.length()-5))-1;
		    TD = td.substring(0,td.length()-7) + Integer.toString(new_date) + "/" + temp;
			//TD = Integer.toString(temp);
		}
		else{
			TD = td.substring(0,td.length()-4) + Integer.toString(temp);
		}		
		FH = fh;
		SHOW = show;
		LIFT = lift;
		SWET = swet;
		KINX = kinx;
		TOTL = totl;
		CAPE = cape;
		CINS = cins;
		BRCH = brch;
	}
}
class RAW_FORECAST{
	//Forecast Data
	String SKIN_TEMP="",SNOW_FALL_ONE_HOUR="",P01M="",LCLD="",MCLD="",HCLD="";
	String WIND_SPEED="",T2MS="",WXTS="",WXTP="",WXTZ="",WXTR="",VSBK="";
	public RAW_FORECAST(String skin,String snowfall,String po1m,String lcld,String mcld,
			String hcld,String windspd,String t2ms,String wxts,String wxtp,String wxtz,
			String wxtr,String vsbk){
		SKIN_TEMP = skin;
		SNOW_FALL_ONE_HOUR = snowfall;
		P01M = po1m;
		LCLD = lcld;
		MCLD = mcld;
		HCLD = hcld;
		WIND_SPEED = windspd;
		T2MS = Double.toString(((Double.parseDouble(t2ms) * 1.8)+32));
		WXTS = wxts;
		WXTP = wxtp;
		WXTZ = wxtz;
		WXTR = wxtr;
		VSBK = vsbk;
	}
}

//NAM CLASS
class NAM{
	ArrayList<RAW_THUNDER> RT_LIST;
	ArrayList<RAW_FORECAST> FC_LIST;
	public void writeData(ArrayList<RAW_THUNDER> thunder,ArrayList<RAW_FORECAST> forecast){
		RT_LIST = thunder;
		FC_LIST = forecast;
	}
}
//NAMNEST CLASS
class NAMNEST{
	ArrayList<RAW_THUNDER> RT_LIST;
	ArrayList<RAW_FORECAST> FC_LIST;
	public void writeData(ArrayList<RAW_THUNDER> thunder,ArrayList<RAW_FORECAST> forecast){
		RT_LIST = thunder;
		FC_LIST = forecast;
	}

}
//HRRR CLASS
class HRRR{
	ArrayList<RAW_THUNDER> RT_LIST;
	ArrayList<RAW_FORECAST> FC_LIST;
	public void writeData(ArrayList<RAW_THUNDER> thunder,ArrayList<RAW_FORECAST> forecast){
		RT_LIST = thunder;
		FC_LIST = forecast;
	}

}
//RAP CLASS
class RAP{
	ArrayList<RAW_THUNDER> RT_LIST;
	ArrayList<RAW_FORECAST> FC_LIST;
	public void writeData(ArrayList<RAW_THUNDER> thunder,ArrayList<RAW_FORECAST> forecast){
		RT_LIST = thunder;
		FC_LIST = forecast;
	}
}
//GFS CLASS
class GFS{
	ArrayList<RAW_THUNDER> RT_LIST;
	ArrayList<GFS_FORECAST> GFS_LIST;
	public void writeData(ArrayList<RAW_THUNDER> thunder,ArrayList<GFS_FORECAST> forecast){
		RT_LIST = thunder;
		GFS_LIST = forecast;
	}
}
