package org.cloudbus.cloudsim.other_task;

import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.Vm;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class createVmlist
{
    public static int[] start=new int[4];
    public static int[] end=new int[4];
    public static List<Vm> vml;
     private static int [] vm1=new int[4];
    public createVmlist(int Bid)
    {
        vml=CreateVmList(Bid);
    }
    private static List<Vm> CreateVmList(int userId) 
        {
                int j=0;
                int h=0;
		// Creates a container to store VMs. This list is passed to the broker
		// later
		LinkedList<Vm> list = new LinkedList<Vm>();

                try {
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        org.w3c.dom.Document doc = dBuilder.parse("x.xml");
                        doc.getDocumentElement().normalize();
                        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                        NodeList nList = doc.getElementsByTagName("vm");
                        System.out.println("----------------------------");
         
                         for (int temp = 0; temp < nList.getLength(); temp++)
                         {
                            org.w3c.dom.Node nNode = nList.item(temp);
            
                            if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
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
                    //int vmcount = h;
                }catch (Exception e) 
                {
                    e.printStackTrace();
                }
            return list;
        }
    public int vm1(int i)
        {
            return vm1[i];
        }
    public int st(int i)
        {
            return start[i];
        }
         public int en(int i)
        {
            return end[i];
        }
}