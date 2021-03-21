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
				checkTempGuidances(Integer.parseInt(maxT),Integer.parseInt(minT),maxMax,maxMin,minMax,minMin);
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
			
			System.out.println("*******");
		}
	}
	//This Function can probably be condensed
	private void checkTempGuidances(int maxT,int minT,int maxMax,int maxMin,int minMax,int minMin) {
		//Heat Tiers
		String HeatTier="?";
		if(maxT >= 90){
			HeatTier="I";
			if(maxT >= 95){
				HeatTier="II";
			}
			if(maxT >= 100){
				HeatTier="III";
			}
			if(maxT>= 105){
				HeatTier="IV";
			}
			if(maxT>= 110){
				HeatTier="V";
			}
		}
		//Cold Tiers
		String ColdTier="?";
		if(minT <= 25){
			ColdTier="I";
			if(minT <= 15){
				ColdTier="II";
			}
			if(minT <= 5){
				ColdTier="III";
			}
			if(minT <= -5){
				ColdTier="IV";
			}
			if(minT <= -15){
				ColdTier="V";
			}
		}
		if(HeatTier.equals("?")){
			System.out.println("No Heat Guidances");
		}
		else{
			System.out.println("Heat Guidance Tier: " + HeatTier);
		}
		if(ColdTier.equals("?")){
			System.out.println("No Cold Guidances");
		}
		else{
			System.out.println("Cold Guidance Tier: " + ColdTier);
		}
	}
}
