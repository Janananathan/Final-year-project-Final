package org.cloudbus.cloudsim.ga.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.cloudbus.cloudsim.ga.algorithm.*;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker1;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.cloudbus.cloudsim.ga.algorithm.Sch;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EstablishmentService {

	
        public Datacenter datacenterss = null;
        public DatacenterBroker1 datacenterbrokerss = null;
	private static int totalHosts = 0;
	private static int totalVMs = 0;
	private static int totalCloudlets = 0;
        
        private static double[] totalStartTime = new double[4];
	private static double[] totalDoneTime = new double[4];
	private static double[] totalActualTime =new double[4];
        private static double[] utilization =new double[4];
       
        private static int vmcount;

	/** The vmlist. */
	public static List<Vm> vml;
        public static List<Cloudlet> cl;
        private static List<Host> hostList = new ArrayList<Host>();
	/**
	 * Creates main() to run this example
         * 
	 */
        

	public EstablishmentService() { // filePath is xml file name
		
                        
			datacenterss = createDatacenters();
			datacenterbrokerss = createDatacenterBrokers();
                        int broke =datacenterbrokerss .getId();
	}


	public Datacenter createDatacenters() { // Node in dom4j

		List<Pe> peList = new ArrayList<Pe>();

		int mips = 1000000000;

		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
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
                String name="Datacenter_0";
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

	
	/**
	 * createHosts is used for create host and return type is host object of list
	 * 
	 * @param host
	 * @return hostList
	 */
	

	public DatacenterBroker1 createDatacenterBrokers() {
		DatacenterBroker1 broker = null;
		try {
			broker = new DatacenterBroker1("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	} 
         public Host get(int i)
         {
             return hostList.get(0);
         }
        
        
}
