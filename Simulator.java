//Steven Mare - mrxste008
//For CSC3002F - OS Assignment 2
//20 May 2018
import java.io.*;
import java.util.*;


public class Simulator
{
	

    public static void main (String [] args)
    {
		
		if(args.length==0){
			System.out.println("No input file given");
		}
		else{
			try {				
				Scanner scanner = new Scanner(new File(args[0]));//gets input arguments from file
				Scanner splitter;
				int peopleCount = scanner.nextInt();
				int branchCount = scanner.nextInt();
				String line = scanner.nextLine();
				Person [] person = new Person[peopleCount];
				Taxi taxi = new Taxi(peopleCount,branchCount);

				for(int i=0; i<peopleCount; i++)
				{
					ArrayList<int[]> job = new ArrayList<int[]>();
					splitter = new Scanner(scanner.nextLine());//splits line into seperate integers
					splitter.useDelimiter("\\), \\(|, | \\(|\\)");
					int personID = splitter.nextInt();//first int is personID
					while(splitter.hasNext())//next ints come in pairs
					{
						int b = splitter.nextInt();//branch number
						int t = splitter.nextInt();//work in minutes
						job.add(new int[]{b,t});
					}	
					person[i] = new Person(personID,job,taxi);
				}
				scanner.close();
						
				new  Trace();//begin scanner object
				
				Thread thread;
				for(int i=0; i<peopleCount; i++)//start people threads
				{
					thread = new Thread(person[i]);
					thread.start();
				}
				
				thread = new Thread(taxi);//start taxi thread
				thread.start();
				
				
			}
			catch(FileNotFoundException e) {
				System.out.println(e);                
			}
		}
	}
}