import java.util.ArrayList;

public class Data {
	//NBE Data
	ArrayList<String> NBE_DAY = new ArrayList<>();
	ArrayList<String[]> NBE_UTC = new ArrayList<>();
	ArrayList<String[]> NBE_MAX_MIN = new ArrayList<>();
	ArrayList<String[]> NBE_MAX_MIN_STD = new ArrayList<>();
	ArrayList<String[]> NBE_SKY_COVER = new ArrayList<>();
	ArrayList<String[]> NBE_WIND_SPEED = new ArrayList<>();
	ArrayList<String[]> NBE_GUSTS = new ArrayList<>();
	ArrayList<String[]> NBE_12H_QPF = new ArrayList<>();
	ArrayList<String[]> NBE_12H_THUNDERSTORM = new ArrayList<>();
	ArrayList<String[]> NBE_12H_SNOW = new ArrayList<>();
	ArrayList<String[]> NBE_12H_ICE = new ArrayList<>();
	//NBP Data
	
	
	//NAM Data
	ArrayList<NAM_TRIPLET> NAM_DATA = new ArrayList<>();
	public void NBP(ArrayList<String> nbp){
		for(int i = 0;i < nbp.size();i++){
			System.out.println(nbp.get(i));
		}
	}
	//NBE
	public void NBE(ArrayList<String> nbe) {
		//Changing Raw Data to useful data
		for(int i = 0;i < nbe.size();i++){
			System.out.println(nbe.get(i));
			String[] temp = nbe.get(i).split("\\|");
			for(int a = 0;a < temp.length;a++){
				if(i == 0){
					temp[a] = temp[a].replaceAll("\\s","");
					NBE_DAY.add(temp[a]);
				}
				else{
					if(a == 0){
						//Taking out the abbreviations so we jsut have numbers
						temp[a] = temp[a].substring(4);
					}
					//Stuff
					String raw_temp[] = temp[a].split("\\s+");
					String[] extra_temp = null;
					extra_temp = new String[2];
					if(raw_temp.length > 1){
						extra_temp[0] = raw_temp[1];
					}
					if(raw_temp.length == 3){
						extra_temp[1] = raw_temp[2];
					}
					//Moving the Data to the correct ArrayList
					if(i == 1){
						NBE_UTC.add(extra_temp);
					}
					else if(i == 3){
						NBE_MAX_MIN.add(extra_temp);
					}
					else if(i == 4){
						NBE_MAX_MIN_STD.add(extra_temp);
					}
					else if(i == 9){
						NBE_SKY_COVER.add(extra_temp);
					}
					else if(i == 12){
						NBE_WIND_SPEED.add(extra_temp);
					}
					else if(i == 14){
						NBE_GUSTS.add(extra_temp);
					}
					else if(i == 17){
						NBE_12H_QPF.add(extra_temp);
					}
					else if(i == 20){
						NBE_12H_THUNDERSTORM.add(extra_temp);
					}
					else if(i == 25){
						NBE_12H_SNOW.add(extra_temp);
					}
					else if(i == 27){
						NBE_12H_ICE.add(extra_temp);
					}
				}
			}
		}
		for(int i = 0;i < NBE_MAX_MIN_STD.size();i++){
			for(int a = 0;a < NBE_MAX_MIN_STD.get(i).length;a++){
				//System.out.println(NBE_MAX_MIN_STD.get(i)[a]);
			}
			//System.out.println("*******");
		}
		printNBE();
	}
	public void printNBE(){
		for(int i = 0;i < NBE_DAY.size();i++){
			System.out.println("****************");
			
			System.out.println(NBE_DAY.get(i));
			String maxT="?",minT="?";
			int maxMax = 0,maxMin = 0,minMax = 0,minMin = 0;
			//Temperature Stuff
			maxT = NBE_MAX_MIN.get(i)[0];
			minT = NBE_MAX_MIN.get(i)[1];
			
			if(NBE_MAX_MIN_STD.get(i)[0] != null && NBE_MAX_MIN_STD.get(i)[1] != null && (maxT != null) && (minT != null)){
				maxMax = Integer.parseInt(maxT) + (3*Integer.parseInt(NBE_MAX_MIN_STD.get(i)[0]));
				maxMin = Integer.parseInt(maxT) - (3*Integer.parseInt(NBE_MAX_MIN_STD.get(i)[0]));
				minMax = Integer.parseInt(minT) + (3*Integer.parseInt(NBE_MAX_MIN_STD.get(i)[1]));
				minMin = Integer.parseInt(minT) - (3*Integer.parseInt(NBE_MAX_MIN_STD.get(i)[1]));
			}
			//Calculating Rain Total For the day
			int QPF_1 = Integer.parseInt(NBE_12H_QPF.get(i)[0]);
			int Total_QPF = QPF_1;
			if(NBE_12H_QPF.get(i)[1] != null){
				int QPF_2 = Integer.parseInt(NBE_12H_QPF.get(i)[1]);
				Total_QPF = QPF_1 + QPF_2;
			}
			//Calculating SNow Amounts for the whole day
			int S12_1 = Integer.parseInt(NBE_12H_SNOW.get(i)[0]);
			int Total_SNOW = S12_1;
			if(NBE_12H_SNOW.get(i)[1] != null){
				int S12_2 = Integer.parseInt(NBE_12H_SNOW.get(i)[1]);
				Total_SNOW = S12_1 + S12_2;
			}
			//Calculating Ice Amounts for the whole day
			int I12_1 = Integer.parseInt(NBE_12H_ICE.get(i)[0]);
			int Total_ICE = I12_1;
			if(NBE_12H_ICE.get(i)[1] != null){
				int I12_2 = Integer.parseInt(NBE_12H_SNOW.get(i)[1]);
				Total_ICE = I12_1 + I12_2;
			}
			//Max and Min Temperatures for the Day
			System.out.println("Max Temp: " + maxT);
			System.out.println("Min Temp: " + minT);
			//Range of Max and Min Temperatures within 3 Standard Deviations
			System.out.println("99.7% Confidence Max Temp: " + Integer.toString(maxMax) + " - " + Integer.toString(maxMin));
			System.out.println("99.7% Confidence Low Temp: " + Integer.toString(minMax) + " - " + Integer.toString(minMin));
			//Sky Cover 
			System.out.println("Sky Cover at 00 UTC: " + NBE_SKY_COVER.get(i)[0] + "%");
			System.out.println("Sky Cover at 12 UTC: " + NBE_SKY_COVER.get(i)[1] + "%");
			//Wind Speed
			System.out.println("Wind Speed at 00 UTC: " + NBE_WIND_SPEED.get(i)[0] + "(KNOTS)");
			System.out.println("Wind Speed at 12 UTC: " + NBE_WIND_SPEED.get(i)[1] + "(KNOTS)");
			//Wind Gusts
			System.out.println("Wind Gusts at 00 UTC: " + NBE_GUSTS.get(i)[0] + "(KNOTS)");
			System.out.println("Wind Gusts at 12 UTC: " + NBE_GUSTS.get(i)[1] + "(KNOTS)");
			//NBE_12H_QPF 
			System.out.println("0-12 UTC Time Rain Total in 1/100th Inches: " + NBE_12H_QPF.get(i)[0]);
			System.out.println("12-24 UTC Time Rain Total in 1/100th Inches: " + NBE_12H_QPF.get(i)[1]);
			System.out.println("Total Rain For Day in 1/100th Inches: " + Total_QPF);
			//ThunderStorm Probability
			System.out.println("Percent Chance of ThunderStorms between 0-12 UTC Time: " + NBE_12H_THUNDERSTORM.get(i)[0]);
			System.out.println("Percent Chance of ThunderStorms between 12-24 UTC Time: " + NBE_12H_THUNDERSTORM.get(i)[1]);
			//Snow Amounts
			System.out.println("0-12 UTC Time Snow Total in 1/10th Inches: " + NBE_12H_SNOW.get(i)[0]);
			System.out.println("12-24 UTC Time Snow Total in 1/10th Inches: " + NBE_12H_SNOW.get(i)[1]);	
			System.out.println("Total Snow in 1/10th Inches: " + Total_SNOW);
			//Ice Amounts
			System.out.println("0-12 UTC Time Ice Totals in 1/100th Inches: " + NBE_12H_ICE.get(i)[0]);
			System.out.println("12-24 UTC Time Ice Totals in 1/100th Inches: " + NBE_12H_ICE.get(i)[1]);
			System.out.println("Total Ice in 1/100th Inches: " + Total_ICE);
			
			//Guidances
			//Temperature Guidances
			if(maxT != null && minT != null){
				checkTempGuidances(Integer.parseInt(maxT),Integer.parseInt(minT),maxMax,maxMin,minMax,minMin);
			}
			//Sustained Wind, Wind Gust, Thunderstorm guidances
			if(NBE_WIND_SPEED.get(i)[0] != null && NBE_WIND_SPEED.get(i)[1] != null){
				checkStormGuidances(Math.max(Integer.parseInt(NBE_WIND_SPEED.get(i)[0]), Integer.parseInt(NBE_WIND_SPEED.get(i)[1])),
						Math.max(Integer.parseInt(NBE_GUSTS.get(i)[0]), Integer.parseInt(NBE_GUSTS.get(i)[1])),
						Math.max(Integer.parseInt(NBE_12H_THUNDERSTORM.get(i)[0]),Integer.parseInt(NBE_12H_THUNDERSTORM.get(i)[1]))
						);
			}
			//Heavy Rain, Heavy Snow
			System.out.println("****************");
		}
	}
	//Sustained Winds, Gust Winds, Thunderstorm Probabilities
	private void checkStormGuidances(int MAX_SUS_WINDS,int MAX_WIND_GUSTS,int THUNDER_PROB) {
		int[] MAX_SUS_WIND_VALUES = {25,35,45,55,65};
		int[] WIND_GUST_VALUES = {35,47,59,71,83};
		int[] THUNDER_VALUES = {15,23,31,39,47,55};
		String SUS_WIND_GUIDANCE = "";
		String WIND_GUST_GUIDANCE = "";
		String THUNDER_GUIDANCE  = "";
		for(int i = 0;i < MAX_SUS_WIND_VALUES.length;i++){
			if(MAX_SUS_WINDS >= MAX_SUS_WIND_VALUES[i]){
				SUS_WIND_GUIDANCE += "I";
			}
		}
		for(int i = 0;i < WIND_GUST_VALUES.length;i++){
			if(MAX_WIND_GUSTS >= WIND_GUST_VALUES[i]){
				WIND_GUST_GUIDANCE += "I";
			}
		}		
		for(int i = 0;i < THUNDER_VALUES.length;i++){
			if(THUNDER_PROB >= THUNDER_VALUES[i]){
				THUNDER_GUIDANCE += "I";
			}
		}		
		if(SUS_WIND_GUIDANCE.length() == 0){
			System.out.println("NO SUSTAINED WINDS GUIDANCE");
		}
		else{
			System.out.println("SUSTAINED WIND GUIDANCE LEVEL: " + SUS_WIND_GUIDANCE);
		}
		
		if(WIND_GUST_GUIDANCE.length() == 0){
			System.out.println("NO WIND GUST GUIDANCE");
		}
		else{
			System.out.println("WIND GUST GUIDANCE LEVEL: " + WIND_GUST_GUIDANCE);
		}
		if(THUNDER_GUIDANCE.length() == 0){
			System.out.println("NO THUNDERSTORM GUIDANCE");
		}
		else{
			System.out.println("THUNDERSTORM GUIDANCE LEVEL: " + THUNDER_GUIDANCE);
		}
		
	}
	private void checkTempGuidances(int maxT,int minT,int maxMax,int maxMin,int minMax,int minMin) {
		int[] HEAT_GUIDANCE_TEMPS = {95,100,105,110,115,120}; 
		int[] COLD_GUIDANCE_TEMPS = {15,5,-5,-15,-25,-35};
		String HEAT_GUIDANCE = "";
		String COLD_GUIDANCE = "";
		for(int i = 0;i < HEAT_GUIDANCE_TEMPS.length;i++){
			if(maxT >= HEAT_GUIDANCE_TEMPS[i]){
				HEAT_GUIDANCE += "I";
			}
		}
		for(int i = 0;i < COLD_GUIDANCE_TEMPS.length;i++){
			if(maxT <= COLD_GUIDANCE_TEMPS[i]){
				COLD_GUIDANCE += "I";
			}
		}
		if(HEAT_GUIDANCE.length() == 0){
			System.out.println("NO HEAT GUIDANCES");
		}
		else{
			System.out.println("HEAT GUIDANCE LEVEL: " + HEAT_GUIDANCE);
		}
		if(COLD_GUIDANCE.length() == 0){
			System.out.println("NO COLD GUIDANCES");
		}
		else{
			System.out.println("COLD GUIDANCE LEVEL: " + COLD_GUIDANCE);
		}
	}
	//NAM SEVERE WEATHER 
	public void NAM(ArrayList<String> temp_NAM) {
		// TODO Auto-generated method stub
		String line;
		for(int i = 0;i < temp_NAM.size();i++){
			line = temp_NAM.get(i);
			if(line.contains("STIM")){
				ArrayList<String> TEMP = new ArrayList<>();
				TEMP.add(temp_NAM.get(i));
				TEMP.add(temp_NAM.get(i+1));
				TEMP.add(temp_NAM.get(i+2));
				TEMP.add(temp_NAM.get(i+3));
				TEMP.add(temp_NAM.get(i+4));
				NAM_TRIPLET T = new NAM_TRIPLET(TEMP);
				NAM_DATA.add(T);
			}
		}
		
		for(int i = 0;i < NAM_DATA.size();i++){
			System.out.println("***********");
			for(int a = 0;a < NAM_DATA.get(i).raw_data.size();a++){
				System.out.println(NAM_DATA.get(i).raw_data.get(a));
			}
			System.out.println("***********");
		}
	}
}
class NAM_TRIPLET{
	ArrayList<String> raw_data = new ArrayList<>();
	//Metrics
	String Forecast_Hour;
	String Showalter_Index;
	public NAM_TRIPLET(ArrayList<String> rd){
		raw_data = rd;
		interpret_Data();
	}
	private void interpret_Data() {
		//1st Line contains Forecast Hour
		String Forecast_Hour = raw_data.get(0).substring(7);
		Forecast_Hour = Forecast_Hour.replaceAll("\\s","");
		//System.out.println(Forecast_Hour);
		//2nd Line Contains Showalter Index
		Showalter_Index = raw_data.get(1).substring(7,15);
		//System.out.println(Showalter_Index);
	}
}
