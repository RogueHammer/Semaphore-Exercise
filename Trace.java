//Steven Mare - mrxste008
//For CSC3002F - OS Assignment 2
//20 May 2018

public class Trace
{
	private static long startTime;
	
	public Trace(){
		startTime = System.currentTimeMillis();//sets time simulation starts
	}
	
	public static void print(int bID, String entity, String descrip){
			long currentTime = System.currentTimeMillis() - startTime;
			int timeInMinutes = (int)(currentTime/33);
			int hours = 9+timeInMinutes/60;//day starts at 9 am
			int minutes = timeInMinutes%60;
			
			System.out.println(String.format("%02d:%02d",hours%24,minutes)+" branch "+bID+": "+entity+" "+descrip);
	}

}