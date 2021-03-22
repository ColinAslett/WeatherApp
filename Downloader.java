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
	public void NAM_THUNDER(String LINK) throws Exception{
		System.out.println(LINK);
		URL url = new URL(LINK);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while((line = br.readLine()) != null){
			if(line.contains("YYMMDD/HHMM")){//This is the other part of NAM
				break;
			}
			else{
				NAM_RAW.add(line);
			}
		}
		ArrayList<String> temp_NAM = new ArrayList<>();
		//Removing unnecessary atmosphere pressure lines
		for(int i = 0;i < NAM_RAW.size();i++){
			if(NAM_RAW.get(i).contains("STIM") || NAM_RAW.get(i).contains("SHOW") 
					|| NAM_RAW.get(i).contains("LCLP") || NAM_RAW.get(i).contains("LCLT") || NAM_RAW.get(i).contains("BRCH")){
				temp_NAM.add(NAM_RAW.get(i));
			}
		}
		data.NAM(temp_NAM);
	}
	public void HRRR_THUNDER(String LINK) throws Exception{
		System.out.println(LINK);
		URL url = new URL(LINK);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while((line = br.readLine()) != null){
			if(line.contains("YYMMDD/HHMM")){//This is the other part of NAM
				break;
			}
			else{
				HRRR_RAW.add(line);
			}
		}
		ArrayList<String> temp_NAM = new ArrayList<>();
		//Removing unnecessary atmosphere pressure lines
		for(int i = 0;i < HRRR_RAW.size();i++){
			if(HRRR_RAW.get(i).contains("STIM") || HRRR_RAW.get(i).contains("SHOW") 
					|| HRRR_RAW.get(i).contains("LCLP") || HRRR_RAW.get(i).contains("LCLT") || HRRR_RAW.get(i).contains("BRCH")){
				temp_NAM.add(HRRR_RAW.get(i));
			}
		}
		data.HRRR(temp_NAM);		
	}
}
