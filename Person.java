//Steven Mare - mrxste008
//For CSC3002F - OS Assignment 2
//20 May 2018

import java.util.*;

class Person extends Thread{
	
	private ArrayList<int[]> job;
	private Taxi taxi;
	private int personID;
	private int currentBranch;
	
	public Person(int id,ArrayList<int[]> j,Taxi t){
		job = j;
		personID = id;
		taxi = t;
		currentBranch = 0;
	}
	
	
	public void run(){
		try{
			for(int i=0; i<job.size(); i++){//cycles through jobs
				taxi.hail(personID,currentBranch);//waits for taxi
				taxi.request(personID,currentBranch,job.get(i)[0]);//waits to be dropped
				currentBranch = job.get(i)[0];//sets current branch to where they are dropped
				this.sleep(33*job.get(i)[1]);//works at branch for so many minutes
			}
			if(currentBranch!=0){//if last branch is not hq
				taxi.hail(personID,currentBranch);//person wishes to return to hq at end of jobs
				taxi.request(personID,currentBranch,0);//arrives at hq
			}
			taxi.finish(personID);//person tells taxi they are done for the day
		}
		catch(InterruptedException e){
			System.out.println(e);
		}
			
		
	}
}