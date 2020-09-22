package org.cloudbus.cloudsim.other_task;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import org.cloudbus.cloudsim.other_task.createCloudlet;
import org.cloudbus.cloudsim.other_task.createVmlist;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;



public class Selective {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;
        private static double[] totalStartTime = new double[4];
	private static double[] totalDoneTime = new double[4];
	private static double[] totalActualTime =new double[4];
        private static double[] utilization =new double[4];
        static double[] vtimeList0 ;
        static double[] vtimeList1 ;
        static double[] vtimeList2 ;
         static double[] vtimeList3 ;
        static String indent="     ";
        private static List<Cloudlet> newList;
	/** The vmlist. */
	private static List<Vm> vmlist;

	private static int reqTasks = 10;
	private static int reqVms = 4;
	
	private static List<Flag> nameFlag;
	static createVmlist vg;
        static createCloudlet cg;
	/**
	 * Creates main() to run this example
	 */
	public static void main(String[] args) {

		Log.printLine("Starting Selective Algorithm...");

	        try {
	        	// First step: Initialize the CloudSim package. It should be called
	            	// before creating any entities.
	            	int num_user = 1;   // number of cloud users
	            	Calendar calendar = Calendar.getInstance();
	            	boolean trace_flag = false;  // mean trace events

	            	// Initialize the CloudSim library
	            	CloudSim.init(num_user, calendar, trace_flag);

	            	// Second step: Create Datacenters
	            	//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
	            	@SuppressWarnings("unused")
					Datacenter datacenter0 = createDatacenter("Datacenter_0");

	            	//Third step: Create Broker
	            	SelectiveBroker broker = createBroker();
	            	int brokerId = broker.getId();

	            	//Fourth step: Create one virtual machine
	            	//vmlist = new VmsCreator().createRequiredVms(reqVms, brokerId);
                        vg=new createVmlist(broker.getId());
                        cg=new createCloudlet(broker.getId());
                        vmlist= vg.vml;
                        cloudletList=cg.cl;

	            	//submit vm list to the broker
	            	broker.submitVmList(vmlist);


	            	//Fifth step: Create two Cloudlets
	            	//cloudletList = new CloudletCreator().createUserCloudlet(reqTasks, brokerId);
      	
	            	//submit cloudlet list to the broker
	            	broker.submitCloudletList(cloudletList);
	            	
    	
	            	//call the scheduling function via the broker
	            	broker.scheduleTaskstoVms();
	            	
	            	
   	
            	
	            	// Sixth step: Starts the simulation
	            	CloudSim.startSimulation();


	            	// Final step: Print results when simulation is over
	            	 newList = broker.getCloudletReceivedList();
	            	
	            	nameFlag = broker.nameList;

	            	CloudSim.stopSimulation();

	            	PrintCloudlets( nameFlag);

	            	Log.printLine("Selective Algorithm finished!");
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            Log.printLine("The simulation has been terminated due to an unexpected error");
	        }
	    }

		private static Datacenter createDatacenter(String name){
			Datacenter datacenter=new DataCenterCreator().createUserDatacenter(name, reqVms);			

	        return datacenter;

	    }

	    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	    //to the specific rules of the simulated scenario
	    private static SelectiveBroker createBroker(){

	    	SelectiveBroker broker = null;
	        try {
			broker = new SelectiveBroker("Broker");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	    	return broker;
	    }

	    /**
	     * Prints the Cloudlet objects
	     * @param list  list of Cloudlets
	     */
	    private static void PrintCloudlets(List<Flag>flag) {
		int size = newList.size();
		Cloudlet cloudlet;
                
                //EstablishmentService est = new EstablishmentService();
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time"+indent+"Algo");

		DecimalFormat dft = new DecimalFormat("###.##");
                //Host h=broker.get(0);
                 double[] pec=new double[4];
                 double[] per=new double[4];
                vtimeList0 = new double[vg.vm1(0)];
                 vtimeList1 = new double[vg.vm1(1)];
                  vtimeList2 = new double[vg.vm1(2)];
                  vtimeList3 = new double[vg.vm1(3)];
                int va=0,vb=0,vc=0,vd=0;
                 for(int i=0; i<vmlist.size(); i++)
                 {
                     if(i>=vg.st(0) && i<=vg.en(0))
                     {
                         vtimeList0[i-vg.st(0)]=0.0;
                     }
                     if(i>=vg.st(1) && i<=vg.en(1))
                     {
                         vtimeList1[i-vg.st(1)]=0.0;
                     }
                     if(i>=vg.st(2) && i<=vg.en(2))
                     {
                         vtimeList2[i-vg.st(2)]=0.0;
                     }
                     if(i>=vg.st(3) && i<=vg.en(3))
                     {
                         vtimeList3[i-vg.st(3)]=0.0;
                     }
                 }
                 double[] doneTime = new double[4];
                 double[] startTime = new double[4];
		double[] actualTime = new double[4];
         
		for (int i = 0; i < size; i++) {
			cloudlet = newList.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);
                        
			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");
                                int k=cloudlet.getVmId();
                                Vm vmx=vmlist.get(k);
                                double vw=vmx.getSize();
                                double str=cloudlet.getCloudletFileSize()*100;
                                BigDecimal sto=BigDecimal.valueOf(str/vw);
                          
                               
				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
						indent + indent + dft.format(cloudlet.getFinishTime())+indent+indent+flag.get(i).name);
                                if(cloudlet.getVmId()>=vg.st(0) && cloudlet.getVmId()<=vg.en(0) )
                                {
                                    if(va==0)
                                    {
                                        totalStartTime[0]=cloudlet.getExecStartTime();;
                                    }
                                    pec[0]++;
                                    startTime[0] = cloudlet.getExecStartTime();
                                    doneTime[0] = cloudlet.getFinishTime();
                                    actualTime[0] = cloudlet.getActualCPUTime();
                                    totalDoneTime[0] = doneTime[0]>totalDoneTime[0]?doneTime[0]:totalDoneTime[0];
                                    //System.out.println("VM 0:" + ( k-start[0]));
                                    vtimeList0[k-vg.st(0)] = vtimeList0[k-vg.st(0)]>actualTime[0]?vtimeList0[k-vg.st(0)]:actualTime[0];
                                }
                                
                                if(cloudlet.getVmId()>=vg.st(1) && cloudlet.getVmId()<=vg.en(1))
                                {
                                    pec[1]++;
                                   if(vb==0)
                                    {
                                        totalStartTime[1]=cloudlet.getExecStartTime();;
                                    }
                                    startTime[1] = cloudlet.getExecStartTime();
                                    doneTime[1] = cloudlet.getFinishTime();
                                    actualTime[1] = cloudlet.getActualCPUTime();
                                    totalDoneTime[1] = doneTime[1]>totalDoneTime[1]?doneTime[1]:totalDoneTime[1];
                                    //System.out.println("VM 1:" + ( k-start[1]));
                                    vtimeList1[k-vg.st(1)] = vtimeList1[k-vg.st(1)]>actualTime[1]?vtimeList1[k-vg.st(1)]:actualTime[1];
                                }
                                if(cloudlet.getVmId()>=vg.st(2) && cloudlet.getVmId()<=vg.en(2))
                                {
                                    pec[2]++;
                                   if(vc==0)
                                    {
                                        totalStartTime[2]=cloudlet.getExecStartTime();;
                                    }
                                    startTime[2] = cloudlet.getExecStartTime();
                                    doneTime[2] = cloudlet.getFinishTime();
                                    actualTime[2] = cloudlet.getActualCPUTime();
                                    totalDoneTime[2] = doneTime[2]>totalDoneTime[2]?doneTime[2]:totalDoneTime[2];
                                    //System.out.println("VM 2:" + ( k-start[2]));
                                    vtimeList2[k-vg.st(2)] = vtimeList2[k-vg.st(2)]>actualTime[2]?vtimeList0[k-vg.st(2)]:actualTime[2];
                                }
                                if(cloudlet.getVmId()>=vg.st(3) && cloudlet.getVmId()<=vg.en(3))
                                {
                                    pec[3]++;
                                    if(vd==0)
                                    {
                                        totalStartTime[3]=cloudlet.getExecStartTime();;
                                    }
                                    startTime[3] = cloudlet.getExecStartTime();
                                    doneTime[3] = cloudlet.getFinishTime();
                                    actualTime[3] = cloudlet.getActualCPUTime();
                                    totalDoneTime[3] = doneTime[3]>totalDoneTime[3]?doneTime[3]:totalDoneTime[3];
                                    //System.out.println("VM 3:" + ( k-start[3]));
                                    vtimeList3[k-vg.st(3)] = vtimeList3[k-vg.st(3)]>actualTime[3]?vtimeList3[k-vg.st(3)]:actualTime[3];
                                }
                                }
			}
                PrintPerformances();
                
		
	}
	   private static void PrintPerformances() {
		//Log.formatLine("Utilization = [ Total_Busy_Time / (Total_Finish_Time * Number of VMs)] * 100");
                totalDoneTime[0]-=totalStartTime[0];
                for(int i=0; i<vg.vm1(0); i++){
			totalActualTime[0] += (vtimeList0[i]-totalStartTime[0]);
                }
                utilization[0]=100*totalActualTime[0]/(totalDoneTime[0]*vg
                        .vm1(0));
                Log.formatLine(Double.toString(utilization[0]));
                totalDoneTime[1]-=totalStartTime[1];
                for(int i=0; i<vg.vm1(1); i++){
			totalActualTime[1] += (vtimeList1[i]-totalStartTime[1]);
                }
                utilization[1]=100*totalActualTime[1]/(totalDoneTime[1]*vg
                        .vm1(1));
                Log.formatLine(Double.toString(utilization[1]));
                totalDoneTime[2]-=totalStartTime[2];
                for(int i=0; i<vg.vm1(2); i++){
			totalActualTime[2] += (vtimeList2[i]-totalStartTime[2]);
                }
                utilization[2]=100*totalActualTime[2]/(totalDoneTime[2]*vg
                        .vm1(2));
                Log.formatLine(Double.toString(utilization[2]));
                totalDoneTime[3]-=totalStartTime[3];
                for(int i=0; i<vg.vm1(3); i++){
			totalActualTime[3] += (vtimeList3[i]-totalStartTime[3]);
                }
                utilization[3]=100*totalActualTime[3]/(totalDoneTime[3]*vg.vm1(3));
                Log.formatLine(Double.toString(utilization[3]));
                DecimalFormat df2 = new DecimalFormat("#.##");
                System.out.println("");
		System.out.println("");
		System.out.println(indent + indent + "=============================================");
		System.out.println("");
                System.out.println( indent+ indent+ "MakeSpan"+indent+ indent+ "Utilization"+ indent+indent+ "Busy");
                for(int i=0;i<4;i++)
                {
                    
		System.out.println( indent+ indent+df2.format(totalDoneTime[i])+ indent+indent+indent+df2.format(utilization[i])+indent+indent+ indent+df2.format(totalActualTime[i]));
                     //System.out.println(utilization[i]);
                    //if(utilization[i]>=80)
                    
                       
                      //  dynamicVm(i);
                    //}
                //}

		//double utilization = 100 * totalActualTime / (totalDoneTime * totalVMs);
		//Log.formatLine("Utilization = [ %f / ( %f * %d)] * 100 = %f", totalActualTime, totalDoneTime, totalVMs,utilization);

		
                }
		
		System.out.println("");
		System.out.println(indent + indent + "=============================================");

		System.out.println("");
		System.out.println("");
	}
}
