//Steven Mare - mrxste008
//For CSC3002F - OS Assignment 2
//20 May 2018

import java.util.concurrent.*;
import java.util.*;

class Taxi extends Thread{
	
	private Semaphore [] lock;
	private Semaphore [] embark;
	private int branchCount;
	private int personCount;
	private int currentBranch; //current branch the taxi is at
	private int direction; //which direction is taxi travelling in
	private boolean [] finished; //array of which people are finished their jobs
	private boolean complete; //has the taxi finished for the day
	
	
	public Taxi(int personCount, int branchCount){
		lock = new Semaphore[branchCount]; //semaphore amount = branch count

		try{
			for(int i=0; i<branchCount;i++){//creates and acquires initial locks
				lock[i] = new Semaphore(1,true);
				lock[i].acquire();
			}
		}
		catch(InterruptedException e){
			System.out.println(e);
		}
			
		this.branchCount = branchCount;
		this.personCount = personCount;
		currentBranch = 0;//starts at branch 0
		direction = 1;//going in positive direction
		finished = new boolean[personCount];
		Arrays.fill(finished,false);
		complete = false;
	}
	
	public void hail(int personID, int hailBranch){//hail method called by person
		try{
			Trace.print(hailBranch,"person "+personID,"hail");
			lock[hailBranch].acquire();//wait until branch lock is free to get on taxi
		}
		catch(InterruptedException e){
			System.out.println(e);
		}
	}
	
	public void request(int personID,int thisBranch, int requestedBranch){
		try{
			lock[thisBranch].release();//release lock so next person can get on or off
			Trace.print(thisBranch,"person "+personID,"request "+requestedBranch);
			lock[requestedBranch].acquire();//wait until branch lock on destination to get off taxi
			Trace.print(requestedBranch,"person "+personID,"disembark");
			lock[requestedBranch].release();//release lock so next person can get on or off
		}
		catch(InterruptedException e){
			System.out.println(e);
		}
	}
	
	public void finish(int personID){//when a person is finished for the day
		finished[personID] = true;
		for(int i=0;i<finished.length;i++){//check if all people are finsihed
			if(!finished[i]){
				complete = false;
				break;
			}
			complete = true;//all people are finished, taxi thread can terminate
		}	
	}
	
	public void run(){

		try{
			while(!complete){//continue until all person threads have ended

				if(lock[currentBranch].hasQueuedThreads())//if current branch has people waiting to get off or on
				{
					lock[currentBranch].release();//release lock to allow people on and off
					this.sleep(33);//wait 1 minute
					lock[currentBranch].acquire();//wait until lock can be reacquired				
				}

				int nextBranch = -1;
				for(int i=currentBranch;i>=0 && i<branchCount;i+=direction){//checks to see if a branch ahead needs to be driven to
					if(lock[i].hasQueuedThreads()){//branch found where people are waiting to be picked up or dropped off at
						nextBranch = i;
						break;
					}
				}
				
				if(nextBranch==-1){//if no branch ahead needs to be driven to, turns around
					direction*=-1;//change direction
					for(int i=currentBranch;i>=0 && i<branchCount;i+=direction){//checks branches in other direction to drive to
						if(lock[i].hasQueuedThreads()){//branch found where people are waiting to be picked up or dropped off at
							nextBranch = i;
							break;
						}
					}
				} 
				
				if(nextBranch!=-1){//if not idle (could possibly mean picking people up from this branch too)
					if(currentBranch != nextBranch){//if needs to drive to another branch (ie not this branch)
						Trace.print(currentBranch,"taxi","depart");
						currentBranch = nextBranch;
						this.sleep(66);//time between any branch is 2 minutes
						Trace.print(currentBranch,"taxi","arrive");
					}
				}					
			}
			Trace.print(currentBranch,"Taxi","FINISHED TAXIING");
		}
		catch(InterruptedException e){
			System.out.println(e);
		}
			
	}
	
}
