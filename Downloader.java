import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Downloader {
	//NBM ARRAYLISTs
	ArrayList<String> NBE_RAW = new ArrayList<>();
	ArrayList<String> NBP_RAW = new ArrayList<>();
	//NAM
	ArrayList<String> NAM_RAW = new ArrayList<>();
	//HRRR
	ArrayList<String> HRRR_RAW = new ArrayList<>();	
	//Data Class
	Data data;
	public Downloader(Data d){
		System.setProperty("http.agent", "Chrome");
		data = d;
	}
	public void NOAA_NBE(String LINK, String station) throws Exception{
		System.out.println(LINK);
		URL url = new URL(LINK);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		int count = 0;
		boolean atStation = false;
		while((line = br.readLine()) != null){
			if(atStation == true){
				count++;
				NBE_RAW.add(line);
				//Continue until we get to the SOL line
				if(line.contains("SOL")){
					break;
				}
			}
			if(line.contains(station)){
				atStation = true;
			}
			
		}
		data.NBE(NBE_RAW);
	}
	public void NOAA_NBP(String LINK, String station) throws Exception{
		System.out.println(LINK);
		URL url = new URL(LINK);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		int count = 0;
		boolean atStation = false;
		while((line = br.readLine()) != null){
			if(atStation == true){
				count++;
				NBP_RAW.add(line);
				//Continue until we get to the I2499 line
				if(line.contains("I24P9")){
					break;
				}
			}
			if(line.contains(station)){
				atStation = true;
			}
			
		}
		data.NBP(NBP_RAW);
	}
	//Thunder Data Downloader
	public void THUNDER_DOWNLOADER(String LINK,String model,String Start_Time) throws Exception{
		System.out.println(LINK);
		URL url = new URL(LINK);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		ArrayList<String> temp = new ArrayList<>();
		while((line = br.readLine()) != null){
			if(line.contains("YYMMDD/HHMM")){//This is the other part of NAM
				break;
			}
			else{
				temp.add(line);
			}
		}
		ArrayList<String> temp_Temp = new ArrayList<>();
		//Removing unnecessary atmosphere pressure lines
		for(int i = 0;i < temp.size();i++){
			if(temp.get(i).contains("STIM") || temp.get(i).contains("SHOW") 
					|| temp.get(i).contains("LCLP") || temp.get(i).contains("LCLT") || temp.get(i).contains("BRCH")){
				temp_Temp.add(temp.get(i));
			}
		}
		if(model.equals("NAM")){
			NAM_RAW = temp_Temp;
		}
		else if(model.equals("HRRR")){
			HRRR_RAW = temp_Temp;
		}		
		data.SEVERE_WEATHER(temp_Temp,model,Start_Time);
	}
}
