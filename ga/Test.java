

package org.cloudbus.cloudsim.ga;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.math.*;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.HostDynamicWorkload;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
//import org.cloudbus.cloudsim.taskscheduling.GA.Sch;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


public class Test {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;
        private static double[] totalStartTime = new double[4];
	private static double[] totalDoneTime = new double[4];
	private static double[] totalActualTime =new double[4];
        private static double[] utilization =new double[4];
        private static int [] vm1=new int[4];
        private static int vmcount;

	/** The vmlist. */
	private static List<Vm> vmlist;
        private static List<Host> hostList = new ArrayList<Host>();
	/**
	 * Creates main() to run this example
         * 
	 */
        private static int[] start=new int[4];
        private static int[] end=new int[4];
        public static void dynamicVm(int i)  
        {
          try{  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse("x.xml");
                        doc.getDocumentElement().normalize();
                        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                        NodeList nList = doc.getElementsByTagName("vm");
                        for (int temp = 0; temp < nList.getLength(); temp++)
                         {
                            Node nNode = nList.item(temp);
            
                            if (nNode.getNodeType() == Node.ELEMENT_NODE)
                            {
                                Element eElement = (Element) nNode;
                                if(Integer.parseInt(eElement.getAttribute("type"))==i)
                                {
                                    int V=Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent());
                                    V++;
                                    eElement.getElementsByTagName("number").item(0).setTextContent(Integer.toString(V));
                                }
                            }
                        
                        }
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
                String Filepath="x.xml";
		StreamResult result = new StreamResult(new File(Filepath));
		transformer.transform(source, result);
          }catch(Exception e) 
                {
                    e.printStackTrace();
                }
        }
        private static List<Vm> createVM(int userId) 
        {
                int j=0;
                int h=0;
		// Creates a container to store VMs. This list is passed to the broker
		// later
		LinkedList<Vm> list = new LinkedList<Vm>();

                try {
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse("x.xml");
                        doc.getDocumentElement().normalize();
                        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                        NodeList nList = doc.getElementsByTagName("vm");
                        System.out.println("----------------------------");
         
                         for (int temp = 0; temp < nList.getLength(); temp++)
                         {
                            Node nNode = nList.item(temp);
            
                            if (nNode.getNodeType() == Node.ELEMENT_NODE)
                            {
                                
                                Element eElement = (Element) nNode;
                                System.out.println("VMtype : " + eElement.getAttribute("type"));
                                int Vmnum=Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent());
                                int Vmmips=Integer.parseInt(eElement.getElementsByTagName("mips").item(0).getTextContent());
                                int Vmram=Integer.parseInt(eElement.getElementsByTagName("ram").item(0).getTextContent());
                                int VmpesNumber=Integer.parseInt(eElement.getElementsByTagName("pesNumber").item(0).getTextContent());
                                String Vmvmm=eElement.getElementsByTagName("vmm").item(0).getTextContent();
                                long Vmbw=Integer.parseInt(eElement.getElementsByTagName("bw").item(0).getTextContent());
                                long Vmsize=Integer.parseInt(eElement.getElementsByTagName("size").item(0).getTextContent());
                                Vm[] vm = new Vm[Vmnum];
                                start[temp]=j;
                                for(int i=0;i<Vmnum;i++)
                                {      
                                   // System.out.println(vm1[h]);
                                    vm1[h]++; 
                                    vm[i] = new Vm(j, userId, Vmmips, VmpesNumber, Vmram, Vmbw, Vmsize, Vmvmm, new CloudletSchedulerDynamicWorkload(Vmmips,VmpesNumber));
                                    list.add(vm[i]);
                                    j++;
                                   
                                }
                                System.out.println(vm1[h]);
                                h++;
                                end[temp]=j-1;
                                
                                
                            }
                        }
                         vmcount=h;
                }catch (Exception e) 
                {
                    e.printStackTrace();
                }
            return list;
        }
	public static void main(String[] args) {

		Log.printLine("Starting Cloud Sim");

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
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			//Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();

                        vmlist=createVM(brokerId);
//submit vm list to the broker
			broker.submitVmList(vmlist);


			//Fifth step: Create two Cloudlets
			cloudletList = new ArrayList<Cloudlet>();

			//Cloudlet properties
                        String str="x5.csv";
                        File file=new File(str);
                        try {
                                Scanner sc=new Scanner(file);
                                int i=0;
                                while(sc.hasNext())
                                {
                                    String data=sc.next();
                                    String[] values=data.split(",");
                                    int id=i;
                                    i++;
                                    long length=Long.parseLong(values[0])/1000000;
                                    long filesize=Long.parseLong(values[0]);
                                    long outputsize=filesize;
                                    int pesNumber=1;
                                    UtilizationModel utilizationModel = new UtilizationModelFull();
                                    Cloudlet cloudlet1 = new Cloudlet(id, length, pesNumber, filesize, outputsize, utilizationModel, utilizationModel, utilizationModel);
                                    cloudlet1.setUserId(brokerId);
                                    cloudletList.add(cloudlet1);
                                }
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                                }
			/*int id = 0;
			long length = 4000;
			long fileSize = 35000;
			long outputSize = 300;
                        int pesNumber=1;
			UtilizationModel utilizationModel = new UtilizationModelStochastic();

			Cloudlet cloudlet1 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet1.setUserId(brokerId);

			id++;
                        length = 2000;
			fileSize = 20000;
			outputSize = 300;
			Cloudlet cloudlet2 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet2.setUserId(brokerId);
                        id++;
                        length = 3000;
			fileSize = 25000;
			outputSize = 300;
			Cloudlet cloudlet3 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet3.setUserId(brokerId);
                        id++;
                        length = 4000;
			fileSize = 40000;
			outputSize = 300;
			Cloudlet cloudlet4 = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet4.setUserId(brokerId);
			//add the cloudlets to the list
			cloudletList.add(cloudlet1);
			cloudletList.add(cloudlet2);
                        cloudletList.add(cloudlet3);
                        cloudletList.add(cloudlet4);
                        */
			//submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);
                        try {
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse("x.xml");
                        doc.getDocumentElement().normalize();
                        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                        NodeList nList = doc.getElementsByTagName("vm1");
                        Node n=nList.item(0);
                        if(n!=null){
                        Element eElement = (Element) n;
                        String policyName=eElement.getElementsByTagName("bindvmcloudlet").item(0).getTextContent();
			//String policyName = n.getText();
			Class<?> PolicyClass = null; 
			try {
				PolicyClass = Class.forName(policyName); 
				Constructor<?> PolicyConstruct = PolicyClass.getConstructor(
														List.class, List.class);
				Object obj = PolicyConstruct.newInstance(vmlist, cloudletList);
				Method PolicyApproach = PolicyClass.getMethod("Execute");
				List<Integer>  results = (List<Integer>)PolicyApproach.invoke(obj);	
				for(int i=0; i<results.size(); i+=2){
					int vmId = results.get(i);
					int ctId = results.get(i+1);
					broker.bindCloudletToVm(ctId, vmId);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
                        }catch (Exception e) 
                {
                    e.printStackTrace();
                }
			
			CloudSim.startSimulation();


			// Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();

			CloudSim.stopSimulation();

                        printCloudletList(newList);

			Log.printLine("Test Code finished!");
                     
                        /*DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse("x.xml");
                        doc.getDocumentElement().normalize();
                        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                        NodeList nList = doc.getElementsByTagName("vm");
                        for (int temp = 0; temp < nList.getLength(); temp++)
                         {
                            Node nNode = nList.item(temp);
            
                            if (nNode.getNodeType() == Node.ELEMENT_NODE)
                            {
                                Element eElement = (Element) nNode;
                                if(Integer.parseInt(eElement.getAttribute("type"))==0)
                                {
                                    int V=Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent());
                                    V++;
                                    eElement.getElementsByTagName("number").item(0).setTextContent(Integer.toString(V));
                                }
                            }
                        
                        }
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
                String Filepath="x.xml";
		StreamResult result = new StreamResult(new File(Filepath));
		transformer.transform(source, result);
                        */
                }
		catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}

	private static Datacenter createDatacenter(String name){


		List<Pe> peList = new ArrayList<Pe>();

		int mips = 1000000000;

		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
                //peList.add(new Pe(1, new PeProvisionerSimple(mips)));
                //peList.add(new Pe(2, new PeProvisionerSimple(mips)));
                //peList.add(new Pe(3, new PeProvisionerSimple(mips)));
		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram =1000000000; //host memory (MB)
		long storage = 1000000000; //host storage
		int bw = 1000000000;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our first machin



		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.001;	// the cost of using storage in this resource
		double costPerBw = 0.0;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
	private static DatacenterBroker createBroker(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
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
	private static void printCloudletList(List<Cloudlet> list) throws SAXException, ParserConfigurationException {
		int size = list.size();
		Cloudlet cloudlet;
                
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time"+indent+"cpu usage"+indent+ indent+"ram usage"+ indent+indent+"Storage usage");

		DecimalFormat dft = new DecimalFormat("###.##");
                Host h=hostList.get(0);
                 double[] pec=new double[4];
                 double[] per=new double[4];
                 double[] vtimeList0 = new double[vm1[0]];
                 double[] vtimeList1 = new double[vm1[1]];
                 double[] vtimeList2 = new double[vm1[2]];
                 double[] vtimeList3 = new double[vm1[3]];
                int va=0,vb=0,vc=0,vd=0;
                 for(int i=0; i<vmlist.size(); i++)
                 {
                     if(i>=start[0] && i<=end[0])
                     {
                         vtimeList0[i-start[0]]=0.0;
                     }
                     if(i>=start[1] && i<=end[1])
                     {
                         vtimeList1[i-start[1]]=0.0;
                     }
                     if(i>=start[2] && i<=end[2])
                     {
                         vtimeList2[i-start[2]]=0.0;
                     }
                     if(i>=start[3] && i<=end[3])
                     {
                         vtimeList3[i-start[3]]=0.0;
                     }
                 }
                 double[] doneTime = new double[4];
                 double[] startTime = new double[4];
		double[] actualTime = new double[4];
         
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
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
						indent + indent + dft.format(cloudlet.getFinishTime())+indent+indent+ cloudlet.getUtilizationOfCpu(cloudlet.getFinishTime())*100+
                                                indent+indent+ cloudlet.getUtilizationOfRam(cloudlet.getFinishTime())*100+indent+sto);
                                if(cloudlet.getVmId()>=start[0] && cloudlet.getVmId()<=end[0] )
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
                                    vtimeList0[k-start[0]] = vtimeList0[k-start[0]]>actualTime[0]?vtimeList0[k-start[0]]:actualTime[0];
                                }
                                
                                if(cloudlet.getVmId()>=start[1] && cloudlet.getVmId()<=end[1])
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
                                    vtimeList1[k-start[1]] = vtimeList1[k-start[1]]>actualTime[1]?vtimeList1[k-start[1]]:actualTime[1];
                                }
                                if(cloudlet.getVmId()>=start[2] && cloudlet.getVmId()<=end[2])
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
                                    vtimeList2[k-start[2]] = vtimeList2[k-start[2]]>actualTime[2]?vtimeList0[k-start[2]]:actualTime[2];
                                }
                                if(cloudlet.getVmId()>=start[3] && cloudlet.getVmId()<=end[3])
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
                                    vtimeList3[k-start[3]] = vtimeList3[k-start[3]]>actualTime[3]?vtimeList3[k-start[3]]:actualTime[3];
                                }
                                }
			}
                totalDoneTime[0]-=totalStartTime[0];
                for(int i=0; i<vm1[0]; i++){
			totalActualTime[0] += (vtimeList0[i]-totalStartTime[0]);
                }
                utilization[0]=100*totalActualTime[0]/(totalDoneTime[0]*vm1[0]);
                totalDoneTime[1]-=totalStartTime[1];
                for(int i=0; i<vm1[1]; i++){
			totalActualTime[1] += (vtimeList1[i]-totalStartTime[1]);
                }
                utilization[1]=100*totalActualTime[1]/(totalDoneTime[1]*vm1[1]);
                totalDoneTime[2]-=totalStartTime[2];
                for(int i=0; i<vm1[2]; i++){
			totalActualTime[2] += (vtimeList2[i]-totalStartTime[2]);
                }
                utilization[2]=100*totalActualTime[2]/(totalDoneTime[2]*vm1[2]);
                totalDoneTime[3]-=totalStartTime[3];
                for(int i=0; i<vm1[3]; i++){
			totalActualTime[3] += (vtimeList3[i]-totalStartTime[3]);
                }
                utilization[3]=100*totalActualTime[3]/(totalDoneTime[3]*vm1[3]);
                for(int i=0;i<4;i++)
                {
                     System.out.println(utilization[i]);
                    if(utilization[i]>=80)
                    {
                       
                        dynamicVm(i);
                    }
                }
		}
        

	}
