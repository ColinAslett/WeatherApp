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
				if(line.contains("SOL")){
					break;
				}
			}
			if(line.contains(station)){
				//Continue until we get to the SOL line
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
				if(line.contains("I24P9")){
					break;
				}
			}
			if(line.contains(station)){
				//Continue until we get to the SOL line
				atStation = true;
			}
			
		}
		data.NBP(NBP_RAW);
	}
}
