 package org.cloudbus.cloudsim.ga;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker1;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.ga.algorithm.*;
import org.cloudbus.cloudsim.ga.service.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class App {

	EstablishmentService est ;
        createVmlist vo;
        createCloudlet co;
	private final String indent = "    ";
        private static List<Host> hostList = new ArrayList<Host>();
        private static double[] totalStartTime = new double[4];
	private static double[] totalDoneTime = new double[4];
	private static double[] totalActualTime =new double[4];
        private static double[] utilization =new double[4];
        private static double[] wait = new double[4];
        static HashMap<Integer,Double>D = new HashMap<Integer,Double>();
        private static int vmcount;
	private List<Cloudlet> cloudletList = null;
	private static List<Vm> vmList = null;
        private List<Cloudlet> newList;
        double[] vtimeList0 ;
        double[] vtimeList1 ;
        double[] vtimeList2 ;
        double[] vtimeList3 ;
         double[] pec=new double[4];
         double[] per=new double[4];
         

         public static void dynamicV(int i,double uti)  
        {
          try{  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse("ap.xml");
                        doc.getDocumentElement().normalize();
                        NodeList nList = doc.getElementsByTagName("vm");
                        for (int temp = 0; temp < nList.getLength(); temp++)
                         {
                            Node nNode = nList.item(temp);
            
                            if (nNode.getNodeType() == Node.ELEMENT_NODE)
                            {
                                Element eElement = (Element) nNode;
                                if(Integer.parseInt(eElement.getAttribute("type"))==i)
                                {
                                    eElement.getElementsByTagName("ut").item(0).setTextContent(Double.toString(uti));
                                }
                            }
                        
                        }
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
                String Filepath="ap.xml";
		StreamResult result = new StreamResult(new File(Filepath));
		transformer.transform(source, result);
          }catch(Exception e) 
                {
                    e.printStackTrace();
                }
        }
	public App() {
		cloudletList = new LinkedList<Cloudlet>();
		vmList = new LinkedList<Vm>();
	}

	/**
	 * LOGFILE IS USED FOR LOGGING CLOUDSET ASSIGN TO BROKERS
	 * 
	 * @param filename
	 * 
	 */
	private void Logfile(String filename) {
		OutputStream logFile = null;
		try {
			logFile = new FileOutputStream(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.setOutput(logFile);
	}

	private void Run() {
		boolean traced = false;
		int users = 1;

		Log.printLine("Starting  the program...");

		Calendar calendar = Calendar.getInstance();

		CloudSim.init(users, calendar, traced); // CLOUDSIM INITIALIZE
                est = new EstablishmentService();
		DatacenterBroker1 broker = est.datacenterbrokerss;
                vo=new createVmlist(broker.getId());
                co=new createCloudlet(broker.getId());
                vmList= vo.vml;
                cloudletList=co.cl;
                broker.submitCloudletList(cloudletList);
                broker.submitVmList(vmList);
                Sch sc=new Sch(vmList,cloudletList);
                List<Integer>  results = sc.Execute();	
                for(int i=0; i<results.size(); i+=2){
			int vmId = results.get(i);
			int ctId = results.get(i+1);
			broker.bindCloudletToVm(ctId, vmId);
		}
               /*for(int i=0;i<cloudletList.size();i++){
                   broker.bindCloudletToVm(cloudletList.get(i).getCloudletId(),0);
               }*/
                CloudSim.startSimulation();

		newList = broker.getCloudletReceivedList();
		CloudSim.stopSimulation();
		Log.printLine(App.class.getName() + " finished!");
	}

	private void PrintCloudlets() {
		int size = newList.size();
		Cloudlet cloudlet;
		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time"+indent+"cpu usage"+indent+ indent+"ram usage"+ indent+indent+"Storage usage");

		DecimalFormat dft = new DecimalFormat("###.##");
                Host h=est.get(0);
                
                vtimeList0 = new double[vo.vm1(0)];
                 vtimeList1 = new double[vo.vm1(1)];
                  vtimeList2 = new double[vo.vm1(2)];
                  vtimeList3 = new double[vo.vm1(3)];
                int va=0,vb=0,vc=0,vd=0;
                 for(int i=0; i<vmList.size(); i++)
                 {
                     if(i>=vo.st(0) && i<=vo.en(0))
                     {
                         vtimeList0[i-vo.st(0)]=0.0;
                     }
                     if(i>=vo.st(1) && i<=vo.en(1))
                     {
                         vtimeList1[i-vo.st(1)]=0.0;
                     }
                     if(i>=vo.st(2) && i<=vo.en(2))
                     {
                         vtimeList2[i-vo.st(2)]=0.0;
                     }
                     if(i>=vo.st(3) && i<=vo.en(3))
                     {
                         vtimeList3[i-vo.st(3)]=0.0;
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
                                Vm vmx=vmList.get(k);
                                double vw=vmx.getSize();
                                double str=cloudlet.getCloudletFileSize()*100;
                                BigDecimal sto=BigDecimal.valueOf(str/vw);
                          
                               
				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
						indent + indent + dft.format(cloudlet.getFinishTime())+indent+indent+ cloudlet.getUtilizationOfCpu(cloudlet.getFinishTime())*100+
                                                indent+indent+ cloudlet.getUtilizationOfRam(cloudlet.getFinishTime())*100+indent+sto);
                                if(cloudlet.getVmId()>=vo.st(0) && cloudlet.getVmId()<=vo.en(0) )
                                {
                                    if(va==0)
                                    {
                                        totalStartTime[0]=cloudlet.getExecStartTime();;
                                    }
                                    pec[0]++;
                                    wait[0]+=cloudlet.getWaitingTime();
                                    startTime[0] = cloudlet.getExecStartTime();
                                    doneTime[0] = cloudlet.getFinishTime();
                                    actualTime[0] = cloudlet.getActualCPUTime();
                                    totalDoneTime[0] = doneTime[0]>totalDoneTime[0]?doneTime[0]:totalDoneTime[0];
                                    vtimeList0[k-vo.st(0)] = vtimeList0[k-vo.st(0)]>actualTime[0]?vtimeList0[k-vo.st(0)]:actualTime[0];
                                }
                                Integer key=cloudlet.getVmId();
                                 double dura;
                                 double len=cloudlet.getCloudletLength();
                                 double mip;
                                 mip =vmx.getNumberOfPes()*vmx.getMips();
                                  dura = len/mip;
                               if(D.containsKey(key))
                               {
                                   double de=D.get(key)+dura;
                                    D.put(key,de);
                               }
                               else
                               {
                                   D.put(key,dura);
                               }
                                
                                if(cloudlet.getVmId()>=vo.st(1) && cloudlet.getVmId()<=vo.en(1))
                                {
                                    pec[1]++;
                                   if(vb==0)
                                    {
                                        totalStartTime[1]=cloudlet.getExecStartTime();;
                                    }
                                   wait[1]+=cloudlet.getWaitingTime();
                                    startTime[1] = cloudlet.getExecStartTime();
                                    doneTime[1] = cloudlet.getFinishTime();
                                    actualTime[1] = cloudlet.getActualCPUTime();
                                    totalDoneTime[1] = doneTime[1]>totalDoneTime[1]?doneTime[1]:totalDoneTime[1];
                                    //System.out.println("VM 1:" + ( k-start[1]));
                                    vtimeList1[k-vo.st(1)] = vtimeList1[k-vo.st(1)]>actualTime[1]?vtimeList1[k-vo.st(1)]:actualTime[1];
                                }
                                if(cloudlet.getVmId()>=vo.st(2) && cloudlet.getVmId()<=vo.en(2))
                                {
                                    pec[2]++;
                                   if(vc==0)
                                    {
                                        totalStartTime[2]=cloudlet.getExecStartTime();;
                                    }
                                   wait[2]+=cloudlet.getWaitingTime();
                                    startTime[2] = cloudlet.getExecStartTime();
                                    doneTime[2] = cloudlet.getFinishTime();
                                    actualTime[2] = cloudlet.getActualCPUTime();
                                    totalDoneTime[2] = doneTime[2]>totalDoneTime[2]?doneTime[2]:totalDoneTime[2];
                                    vtimeList2[k-vo.st(2)] = vtimeList2[k-vo.st(2)]>actualTime[2]?vtimeList0[k-vo.st(2)]:actualTime[2];
                                }
                                if(cloudlet.getVmId()>=vo.st(3) && cloudlet.getVmId()<=vo.en(3))
                                {
                                    pec[3]++;
                                    if(vd==0)
                                    {
                                        totalStartTime[3]=cloudlet.getExecStartTime();;
                                    }
                                    wait[3]+=cloudlet.getWaitingTime();
                                    startTime[3] = cloudlet.getExecStartTime();
                                    doneTime[3] = cloudlet.getFinishTime();
                                    actualTime[3] = cloudlet.getActualCPUTime();
                                    totalDoneTime[3] = doneTime[3]>totalDoneTime[3]?doneTime[3]:totalDoneTime[3];
                                    vtimeList3[k-vo.st(3)] = vtimeList3[k-vo.st(3)]>actualTime[3]?vtimeList3[k-vo.st(3)]:actualTime[3];
                                }
                                }
			}
                
		 Set<Map.Entry<Integer,Double>>st=D.entrySet();
                Double temp=0.0;
                for(Map.Entry<Integer,Double>me:st)
                {
                    if(me.getValue()>temp)
                    {
                        temp=me.getValue();
                    }
                }
                D.clear();
                System.out.println("DURATION :"+temp);
	}

	private void PrintPerformances() {
		Log.formatLine("Utilization = [ Total_Busy_Time / (Total_Finish_Time * Number of VMs)] * 100");
                System.out.println("Utilization = [ Total_Busy_Time / (Total_Finish_Time * Number of VMs)] * 100");
                totalDoneTime[0]-=totalStartTime[0];
                for(int i=0; i<vo.vm1(0); i++){
			totalActualTime[0] += (vtimeList0[i]-totalStartTime[0]);
                }
                utilization[0]=100*totalActualTime[0]/(totalDoneTime[0]*vo.vm1(0));
                Log.formatLine(Double.toString(utilization[0]));
                totalDoneTime[1]-=totalStartTime[1];
                for(int i=0; i<vo.vm1(1); i++){
			totalActualTime[1] += (vtimeList1[i]-totalStartTime[1]);
                }
                utilization[1]=100*totalActualTime[1]/(totalDoneTime[1]*vo.vm1(1));
                Log.formatLine(Double.toString(utilization[1]));
                totalDoneTime[2]-=totalStartTime[2];
                for(int i=0; i<vo.vm1(2); i++){
			totalActualTime[2] += (vtimeList2[i]-totalStartTime[2]);
                }
                utilization[2]=100*totalActualTime[2]/(totalDoneTime[2]*vo.vm1(2));
                Log.formatLine(Double.toString(utilization[2]));
                totalDoneTime[3]-=totalStartTime[3];
                for(int i=0; i<vo.vm1(3); i++){
			totalActualTime[3] += (vtimeList3[i]-totalStartTime[3]);
                }
            
                utilization[3]=100*totalActualTime[3]/(totalDoneTime[3]*vo.vm1(3));
                Log.formatLine(Double.toString(utilization[3]));
                System.out.println("");
		System.out.println("");
		System.out.println(indent + indent + "=============================================");
		System.out.println("");
                for(int i=0;i<4;i++)
                {
                    System.out.format("%s%s%s%s%s%s%s%s%s %n", indent, indent, "MakeSpan", indent, indent, "Utilization", indent,
				indent, "Busy");
		System.out.format("%s%s%.4f%s%s%.4f%s%s%s%.4f %n", indent, indent, totalDoneTime[i]+totalStartTime[i], indent, indent, utilization[i],
				indent, indent, indent, totalActualTime[i]);      
                        dynamicV(i,utilization[i]);
                }

		System.out.println("");
		System.out.println(indent + indent + "=============================================");

		System.out.println("");
		System.out.println("");
	}

	public static void main(String[] args) {

		App tsapp = new App();// cloudletList, vmList);
		tsapp.Logfile("logFile.txt");
		tsapp.Run();
		tsapp.PrintCloudlets();
		tsapp.PrintPerformances();
	}
        public static List<Vm> revm()
        {
            return vmList;
        }

}
