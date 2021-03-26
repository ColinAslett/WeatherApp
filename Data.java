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
	ArrayList<TRIPLET> NAM_DATA = new ArrayList<>();
	//HRRR Data
	ArrayList<TRIPLET> HRRR_DATA = new ArrayList<>();	
	//RAP Data
	ArrayList<TRIPLET> RAP_DATA = new ArrayList<>();
	//GFS Data
	ArrayList<TRIPLET> GFS_DATA = new ArrayList<>();
	
	
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
	//SEVERE WEATHER MODELS
	public void SEVERE_WEATHER(ArrayList<String> model_data, String model,String Start_Time){
		// TODO Auto-generated method stub
		String line;
		ArrayList<TRIPLET> temp = new ArrayList<>();
		for(int i = 0;i < model_data.size();i++){
			line = model_data.get(i);
			if(line.contains("STIM")){
				ArrayList<String> TEMP = new ArrayList<>();
				TEMP.add(model_data.get(i));
				TEMP.add(model_data.get(i+1));
				TEMP.add(model_data.get(i+2));
				TEMP.add(model_data.get(i+3));
				TEMP.add(model_data.get(i+4));
				TRIPLET T = new TRIPLET(TEMP,model,Start_Time);
				temp.add(T);
			}
		}		
		if(model.equals("NAM")){
			NAM_DATA = temp;
		}
		else if(model.equals("HRRR")){
			HRRR_DATA = temp;
		}
		else if(model.equals("RAP")){
			RAP_DATA = temp;
		}
		else if(model.equals("GFS")){
			GFS_DATA = temp;
		}
	}
	//Prints all of the data out of the models in a tabular format
	public void printSevereWeatherSummary() {
		int RAP_HRRR_TIME = Integer.parseInt(RAP_DATA.get(0).Start_Time);
		int GFS_NAM_TIME = Integer.parseInt(GFS_DATA.get(0).Start_Time);
		int DIFF;
		if(RAP_HRRR_TIME > GFS_NAM_TIME){
			DIFF = RAP_HRRR_TIME - GFS_NAM_TIME;
		}
		else{
			DIFF = (23-GFS_NAM_TIME) + RAP_HRRR_TIME;
		}
		System.out.println("PARAMETERS,GFS,NAM,HRRR,RAP,AVERAGE");
		for(int i = 0;i < GFS_DATA.size();i++){
			String GFS_FH = "N/A",NAM_FH = "N/A",HRRR_FH = "N/A",RAP_FH = "N/A";//Forecast Hour
			String GFS_SI = "N/A",NAM_SI = "N/A",HRRR_SI = "N/A",RAP_SI = "N/A";//Showalter Index
			String GFS_LI = "N/A",NAM_LI = "N/A",HRRR_LI = "N/A",RAP_LI = "N/A";//LIFTED Index
			String GFS_CP = "N/A",NAM_CP = "N/A",HRRR_CP = "N/A",RAP_CP = "N/A";//CAPE
			String GFS_BRN = "N/A",NAM_BRN = "N/A",HRRR_BRN = "N/A",RAP_BRN = "N/A";//Bulk RichardSon Number
			double SI_AVG = 0,LI_AVG = 0,CAPE_AVG = 0,BRN_AVG = 0;
			int num_models = 0;//Number of models operating at a given forecasting hour
			//Forecast Hour
			if(i < DIFF){
				GFS_FH = Integer.toString(GFS_DATA.get(i).FH);
				NAM_FH = Integer.toString(NAM_DATA.get(i).FH);
				GFS_SI = Double.toString(GFS_DATA.get(i).SI);
				NAM_SI = Double.toString(NAM_DATA.get(i).SI);
				GFS_LI = Double.toString(GFS_DATA.get(i).LI);
				NAM_LI = Double.toString(NAM_DATA.get(i).LI);
				GFS_CP = Double.toString(GFS_DATA.get(i).CAPE_VALUE);
				NAM_CP = Double.toString(NAM_DATA.get(i).CAPE_VALUE);
				GFS_BRN = Double.toString(GFS_DATA.get(i).BRN);
				NAM_BRN = Double.toString(NAM_DATA.get(i).BRN);
				//Averages
				SI_AVG += GFS_DATA.get(i).SI + NAM_DATA.get(i).SI;
				LI_AVG += GFS_DATA.get(i).LI + NAM_DATA.get(i).LI;
				CAPE_AVG += GFS_DATA.get(i).CAPE_VALUE + NAM_DATA.get(i).CAPE_VALUE;
				BRN_AVG += GFS_DATA.get(i).BRN + NAM_DATA.get(i).BRN;
				num_models = 2;
			}
			else{
				//HRRR
				if(i-DIFF < HRRR_DATA.size()){
					HRRR_FH = Integer.toString(HRRR_DATA.get(i-DIFF).FH);
					HRRR_SI = Double.toString(HRRR_DATA.get(i-DIFF).SI);
					HRRR_LI = Double.toString(HRRR_DATA.get(i-DIFF).LI);
					HRRR_CP = Double.toString(HRRR_DATA.get(i-DIFF).CAPE_VALUE);
					HRRR_BRN = Double.toString(HRRR_DATA.get(i-DIFF).BRN);
					SI_AVG += HRRR_DATA.get(i-DIFF).SI;
					LI_AVG += HRRR_DATA.get(i-DIFF).LI;
					CAPE_AVG += HRRR_DATA.get(i-DIFF).CAPE_VALUE;
					BRN_AVG += HRRR_DATA.get(i-DIFF).BRN;
					num_models++;
				}
				else{
					HRRR_FH = "?";
					HRRR_SI = "?";
					HRRR_LI = "?";
					HRRR_CP = "?";
					HRRR_BRN = "?";
				}
				//NAM
				if(i < NAM_DATA.size()){
					NAM_FH = Integer.toString(NAM_DATA.get(i).FH);
					NAM_SI = Double.toString(NAM_DATA.get(i).SI);
					NAM_LI = Double.toString(NAM_DATA.get(i).LI);
					NAM_CP = Double.toString(NAM_DATA.get(i).CAPE_VALUE);
					NAM_BRN = Double.toString(NAM_DATA.get(i-DIFF).BRN);
					SI_AVG += NAM_DATA.get(i).SI;
					LI_AVG += NAM_DATA.get(i).LI;
					CAPE_AVG += NAM_DATA.get(i).CAPE_VALUE;
					BRN_AVG += NAM_DATA.get(i-DIFF).BRN;
					num_models++;
				}
				else{
					NAM_FH = "?";
					NAM_SI = "?";
					NAM_LI = "?";
					NAM_CP = "?";
					NAM_BRN = "?";
				}
				//RAP
				if(i-DIFF < RAP_DATA.size()){
					RAP_FH = Integer.toString(RAP_DATA.get(i-DIFF).FH);
					RAP_SI = Double.toString(RAP_DATA.get(i-DIFF).SI);
					RAP_LI = Double.toString(RAP_DATA.get(i-DIFF).LI);
					RAP_CP = Double.toString(RAP_DATA.get(i-DIFF).CAPE_VALUE);
					RAP_BRN = Double.toString(RAP_DATA.get(i-DIFF).BRN);
					SI_AVG += RAP_DATA.get(i-DIFF).SI;
					LI_AVG += RAP_DATA.get(i-DIFF).LI;
					CAPE_AVG += RAP_DATA.get(i-DIFF).CAPE_VALUE;
					BRN_AVG += RAP_DATA.get(i-DIFF).BRN;
					num_models++;
				}
				else{
					RAP_FH = "?";
					RAP_SI = "?";
					RAP_LI = "?";
					RAP_CP = "?";
					RAP_BRN = "?";
				}
				//GFS
				GFS_FH = Integer.toString(GFS_DATA.get(i).FH);
				GFS_SI = Double.toString(GFS_DATA.get(i).SI);
				GFS_LI = Double.toString(GFS_DATA.get(i).LI);
				GFS_CP = Double.toString(GFS_DATA.get(i).CAPE_VALUE);
				GFS_BRN = Double.toString(GFS_DATA.get(i).BRN);
				SI_AVG += GFS_DATA.get(i).SI;
				LI_AVG += GFS_DATA.get(i).LI;
				CAPE_AVG += GFS_DATA.get(i).CAPE_VALUE;
				BRN_AVG += GFS_DATA.get(i).BRN;
				num_models++;
			}
			SI_AVG /= num_models;
			LI_AVG /= num_models;
			CAPE_AVG /= num_models;
			BRN_AVG /= num_models;
			//CSV Output
			
			//System.out.println("FH," + GFS_FH + "," + NAM_FH + "," + HRRR_FH + "," + RAP_FH);
			//System.out.println("SI," + GFS_SI + "," + NAM_SI + "," + HRRR_SI + "," + RAP_SI + "," + SI_AVG);
			//System.out.println("LI," + GFS_LI + "," + NAM_LI + "," + HRRR_LI + "," + RAP_LI + "," + LI_AVG);
			//System.out.println("CAPE," + GFS_CP + "," + NAM_CP + "," + HRRR_CP + "," + RAP_CP + "," + CAPE_AVG);
			//System.out.println("BRN," + GFS_BRN + "," + NAM_BRN + "," + HRRR_BRN + "," + RAP_BRN + "," + BRN_AVG);
			
			//Text Based Forecast
			//No Severe Weather Threats Anytime Soon
			String[] CONF_LEVEL = {"LOW","MODERATE","HIGH","VERY HIGH"};
			double[] CAPE_LEVEL = {500,1000,1500,2500}; 
			String[] CAPE_ANALYSIS = {"Marginally Unstable","Moderately Unstable","Very Unstable","Extremely Unstable"};
			double[] SI_LEVEL = {3,1,-2.5,-6};
			String[] SI_ANALYSIS = {"No threat of Severe Weather","Rain Showers with possibly some thunderstorms","Thundershowers are likely","Severe Thunderstorms are possible","Severe Thunderstorms are possible with Tornadoes a possibility"};
			double[] LI_LEVEL = {-1,-3,-5};
			String[] LI_ANALYSIS = {"No Potential for Severe Weather","Weak Potential for Severe Weather","Moderate Potential for Severe Weather","Strong Potential for Severe Weather"};
			double[] BRN_LEVEL = {10,20,45};
			String[] BRN_ANALYSIS = {"Environemnt Unlikely to Generate SuperCells, SHEAR too high but can potentially still generate Supercells","Environment Optimal for Supercell Storms","Environment able to generate SuperCell Storms","Environemnt Unlikely to Generate SuperCells, CAPE too High but some pulse storms possible"};
			
			double Forecast_Hour_Score = 0;
			int CAPE_INDEX = 0,SI_INDEX = 0,LI_INDEX = 0;
		    double BRN_INDEX = 0;
			for(int a = 0;a < CAPE_LEVEL.length;a++){
				if(CAPE_AVG >= CAPE_LEVEL[a]){
					CAPE_INDEX++;
				}
			}
			for(int a = 0;a < SI_LEVEL.length;a++){
				if(SI_AVG <= SI_LEVEL[a]){
					SI_INDEX++;
				}
			}
			for(int a = 0;a < LI_LEVEL.length;a++){
				if(LI_AVG <= LI_LEVEL[a]){
					LI_INDEX++;
				}
			}
			//BRN is the hardest one cause an average doesn't work that well, a high and a low one can put it int
			if(GFS_DATA.get(i).BRN < 10){
				BRN_INDEX += 1;
			}
			if((i < NAM_DATA.size()) && NAM_DATA.get(i).BRN < 10){
				BRN_INDEX += 1;
			}
			if((i-DIFF < HRRR_DATA.size() && i >=  DIFF) && HRRR_DATA.get(i-DIFF).BRN < 10){
				BRN_INDEX += 1;
			}
			if((i-DIFF < RAP_DATA.size() && i >=  DIFF) && RAP_DATA.get(i-DIFF).BRN < 10){
				BRN_INDEX += 1;
			}
			
			if(GFS_DATA.get(i).BRN >= 10 && GFS_DATA.get(i).BRN <= 20){
				BRN_INDEX += 4;
			}
			if((i < NAM_DATA.size()) && NAM_DATA.get(i).BRN >= 10 && NAM_DATA.get(i).BRN <= 20){
				BRN_INDEX += 4;
			}
			if((i-DIFF < HRRR_DATA.size() && i >=  DIFF) && HRRR_DATA.get(i-DIFF).BRN >= 10 && HRRR_DATA.get(i-DIFF).BRN <= 20){
				BRN_INDEX += 4;
			}
			if((i-DIFF < RAP_DATA.size() && i >=  DIFF) && RAP_DATA.get(i-DIFF).BRN >= 10 && RAP_DATA.get(i-DIFF).BRN <= 20){
				BRN_INDEX += 4;
			}
			if(GFS_DATA.get(i).BRN > 20 && GFS_DATA.get(i).BRN <= 45){
				BRN_INDEX += 2;
			}
			if((i < NAM_DATA.size()) && NAM_DATA.get(i).BRN > 20 && NAM_DATA.get(i).BRN <= 45){
				BRN_INDEX += 2;
			}
			if((i-DIFF < HRRR_DATA.size() && i >=  DIFF) && HRRR_DATA.get(i-DIFF).BRN > 20 && HRRR_DATA.get(i-DIFF).BRN <= 45){
				BRN_INDEX += 2;
			}
			if((i-DIFF < RAP_DATA.size() && i >=  DIFF) && RAP_DATA.get(i-DIFF).BRN > 20 && RAP_DATA.get(i-DIFF).BRN <= 45){
				BRN_INDEX += 2;
			}
			if(GFS_DATA.get(i).BRN > 45){
				BRN_INDEX += 1;
			}
			if((i < NAM_DATA.size()) && NAM_DATA.get(i).BRN > 45){
				BRN_INDEX += 1;
			}
			if((i-DIFF < RAP_DATA.size() && i >=  DIFF) && RAP_DATA.get(i-DIFF).BRN > 45){
				BRN_INDEX += 1;
			}
			if((i-DIFF < HRRR_DATA.size() && i >=  DIFF) && HRRR_DATA.get(i-DIFF).BRN > 45){
				BRN_INDEX += 1;
			}
			BRN_INDEX /= num_models;
			Forecast_Hour_Score = (CAPE_INDEX*SI_INDEX) + (CAPE_INDEX*LI_INDEX) + (CAPE_INDEX*BRN_INDEX);
			if(Forecast_Hour_Score >= 1){
				System.out.println("FORECAST HOUR: " + GFS_DATA.get(i).FH);
				//System.out.println("FH," + GFS_FH + "," + NAM_FH + "," + HRRR_FH + "," + RAP_FH);
				System.out.println("SI," + GFS_SI + "," + NAM_SI + "," + HRRR_SI + "," + RAP_SI + "," + SI_AVG);
				System.out.println("LI," + GFS_LI + "," + NAM_LI + "," + HRRR_LI + "," + RAP_LI + "," + LI_AVG);
				System.out.println("CAPE," + GFS_CP + "," + NAM_CP + "," + HRRR_CP + "," + RAP_CP + "," + CAPE_AVG);
				System.out.println("BRN," + GFS_BRN + "," + NAM_BRN + "," + HRRR_BRN + "," + RAP_BRN + "," + BRN_AVG);
				System.out.println("FORECAST SCORE: " + Forecast_Hour_Score);
			}
			
			//System.out.println("Confidence Level in this Forecast Hour: " + CONF_LEVEL[num_models-1]);
		}
	}
}
//Class That Deals with Severe Weather Parameters
class TRIPLET{
	ArrayList<String> raw_data = new ArrayList<>();
	String Model;
	String Start_Time;
	//Metrics
	String Forecast_Hour;
	String Showalter_Index;
	String Lifted_Index;
	String Sweat_Index;
	String Total_Total_Index;
	String CAPE;
	String Bulk_RichardSon_Number;
	int FH;
	double SI;
	double LI;
	double SWI;
	double TTI;
	double CAPE_VALUE;
	double BRN;
	String SI_ANALYSIS;
	String LI_ANALYSIS;
	String CAPE_ANALYSIS;
	String BRN_ANALYSIS;
	public TRIPLET(ArrayList<String> rd,String model,String S_T){
		raw_data = rd;
		Model = model;
		Start_Time = S_T;
		interpret_Data();
	}
	private void interpret_Data() {
		//1st Line contains Forecast Hour
		String Forecast_Hour = raw_data.get(0).substring(7);
		Forecast_Hour = Forecast_Hour.replaceAll("\\s","");
		FH = Integer.parseInt(Forecast_Hour);
		//System.out.println(Forecast_Hour);
		//Showalter Index: http://tornado.sfsu.edu/geosciences/classes/m201/buoyancy/SkewTMastery/mesoprim/skewt/ssi1.htm
		Showalter_Index = raw_data.get(1).substring(7,12);
		Showalter_Index = Showalter_Index.replaceAll("\\s", "");
		SI = Double.parseDouble(Showalter_Index);
		if(SI > 3){
			SI_ANALYSIS = "There is No Threat of Severe Weather";
		}
		else if(SI <= 3 && SI >= 1){
			SI_ANALYSIS = "Rain Showers with possibly some thunderstorms";
		}
		else if(SI <= 1 && SI >= -2.5){
			SI_ANALYSIS = "Thundershowers are likely";
		}
		else if(SI <= -2.5 && SI >= -6){
			SI_ANALYSIS = "Severe Thunderstorms are possible";
		}
		else if(SI <= -6){
			SI_ANALYSIS = "Severe Thunderstorms are possible with Tornadoes a possibility";
		}
		//Lifted Index: http://tornado.sfsu.edu/geosciences/classes/m201/buoyancy/SkewTMastery/mesoprim/skewt/li.htm
		Lifted_Index = Equal_Counter(2,1).replaceAll("\\s", "");
		LI = Double.parseDouble(Lifted_Index);
		if(LI > -1){
			LI_ANALYSIS = "No Potential for Severe Weather";
		}
		if(LI <= -1){
			LI_ANALYSIS = "Weak Potential for Severe Weather";
		}
		if(LI <= -3){
			LI_ANALYSIS = "Moderate Potential for Severe Weather";
		}
		if(LI <= -5){
			LI_ANALYSIS = "Strong Potential for Severe Weather";
		}		
		//Sweat Index: http://weather.uky.edu/about_sweat.htm#:~:text=The%20SWEAT%20Index%20evaluates%20the,between%20850%20and%20500%20mb).
		Sweat_Index = Equal_Counter(3,1).replaceAll("\\s", "");
		SWI = Double.parseDouble(Sweat_Index);
		//LCLP: https://www.weather.gov/source/zhu/ZHU_Training_Page/convective_parameters/Sounding_Stuff/MesoscaleParameters.html
		//This one seems a bit complicated, not to get but to interpret
		//Totals Total Index: http://tornado.sfsu.edu/geosciences/classes/m201/buoyancy/SkewTMastery/mesoprim/skewt/tt.htm#:~:text=Total%20Totals%20Index&text=The%20Total%20Totals%20index%20(TT,and%20the%20resulting%20TT%20value.
		Total_Total_Index = Equal_Counter(3,2).replaceAll("\\s", "");
		TTI = Double.parseDouble(Total_Total_Index);
		//CAPE: http://tornado.sfsu.edu/geosciences/classes/m201/buoyancy/SkewTMastery/mesoprim/skewt/cape.htm
		CAPE = Equal_Counter(4,2).replaceAll("\\s", "");
		CAPE_VALUE = Double.parseDouble(CAPE);
		if(CAPE_VALUE >= 0 && CAPE_VALUE < 1000 && SI <= -2.5 && LI <= -2.5){
			CAPE_ANALYSIS = CAPE + ", This would minimally support the idea of potential severe weather";
		}
		else if(CAPE_VALUE >= 1000 && CAPE_VALUE < 2000 &&  SI <= -2.5 && LI <= -2.5){
			CAPE_ANALYSIS = CAPE + ", This would moderately support the idea of potential severe weather";
		}
		else if(CAPE_VALUE >= 2000 && CAPE_VALUE < 3000 &&  SI <= -2.5 && LI <= -2.5){
			CAPE_ANALYSIS = CAPE + ", This would strongly support the idea of potential severe weather";
		}
		else if(CAPE_VALUE >= 3000 && SI <= -2.5 && LI <= -2.5){
			CAPE_ANALYSIS = CAPE + ", This would extremely  support the idea of potential severe weather";
		}
		else if(CAPE_VALUE >= 1000){
			CAPE_ANALYSIS = CAPE + ", While the SI and LI Indices don't necessairly project severe weather, CAPE Values"
					+ " are nonetheless elevated meaning a potential for severe weather remains";
		}
		else{
			CAPE_ANALYSIS = CAPE + ", This would not support the idea of potential severe weather";
		}
		//Bulk Richardson Number: http://www.theweatherprediction.com/habyhints/315/#:~:text=BRN%20(Bulk%20Richardson%20Number)%20is,height)%20in%20a%20thunderstorm%20environment.&text=The%20extreme%20CAPE%20value%20leads%20to%20the%20high%20BRN.
		Bulk_RichardSon_Number = Equal_Counter(1,4).replaceAll("\\s", "");
		BRN = Double.parseDouble(Bulk_RichardSon_Number);
		if(BRN <= 10){
			BRN_ANALYSIS = "Environemnt Unlikely to Generate SuperCells, SHEAR too high but can potentially still generate Supercells";
		} 
		else if(BRN < 20 && BRN >= 10){
			BRN_ANALYSIS = "Environment Optimal for Supercell Storms";
		}
		else if(BRN < 45 && BRN >= 20){
			BRN_ANALYSIS = "Environment able to generate SuperCell Storms";
		}
		else{
			BRN_ANALYSIS = "Environemnt Unlikely to Generate SuperCells, CAPE too High but some pulse storms";
		}
		//printSynopsis();
	}
	//Equal Counter Function
	private String Equal_Counter(int num,int line){
		String temp = "";
		int EQ_COUNTER = 0;
		int index = 0;
		for(int i = 0;i < raw_data.get(line).length();i++){
			if(raw_data.get(line).charAt(i) == '='){
				EQ_COUNTER++;
			}
			if(EQ_COUNTER==num){
				index = i+2;
				break;
			}
		}	
		if(num != 4 && line != 4){
			return (raw_data.get(line).substring(index,index+5));
		}
		else{
			return (raw_data.get(line).substring(index,raw_data.get(line).length()));
		}
	}
}
