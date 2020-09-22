package org.cloudbus.cloudsim.ga.service;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
      public static List<Vm> cl;
    public  static List<Vm>y=new ArrayList<Vm>();
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
                        org.w3c.dom.Document doc = dBuilder.parse("ap.xml");
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
                                double uti=Double.valueOf(eElement.getElementsByTagName("ut").item(0).getTextContent());
                                int Vmnum=Integer.parseInt(eElement.getElementsByTagName("number").item(0).getTextContent());
                                int Vmmips=Integer.parseInt(eElement.getElementsByTagName("mips").item(0).getTextContent());
                                int Vmram=Integer.parseInt(eElement.getElementsByTagName("ram").item(0).getTextContent());
                                int VmpesNumber=Integer.parseInt(eElement.getElementsByTagName("pesNumber").item(0).getTextContent());
                                String Vmvmm=eElement.getElementsByTagName("vmm").item(0).getTextContent();
                                long Vmbw=Integer.parseInt(eElement.getElementsByTagName("bw").item(0).getTextContent());
                                long Vmsize=Integer.parseInt(eElement.getElementsByTagName("size").item(0).getTextContent());
                                if(uti>=80)
                                {
                                    Vmnum+=1;
                                    eElement.getElementsByTagName("number").item(0).setTextContent(Integer.toString(Vmnum));
                                    System.out.println("Vm is increased because it uses more the 80% in last process");
                                }
                                Vm[] vm = new Vm[Vmnum];
                                start[h]=j;
                                for(int i=0;i<Vmnum;i++)
                                {      
                                   // System.out.println(vm1[h]);
                                    vm1[h]++; 
                                    vm[i] = new Vm(j, userId, Vmmips, VmpesNumber, Vmram, Vmbw, Vmsize, Vmvmm, new CloudletSchedulerDynamicWorkload(Vmmips,VmpesNumber));
                                    list.add(vm[i]);
                                    j++;
                                   
                                }
                                System.out.println("Number Vm created: "+vm1[h]);
                                end[h]=j-1;
                                h++;
                                
                                
                            }
                        }
                          TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
                String Filepath="ap.xml";
		StreamResult result = new StreamResult(new File(Filepath));
		transformer.transform(source, result);
                    //int vmcount = h;
                }catch (Exception e) 
                {
                    e.printStackTrace();
                }
            return list;
        }
    public static int vm1(int i)
        {
            return vm1[i];
        }
    public static int st(int i)
        {
            return start[i];
        }
         public static int en(int i)
        {
            return end[i];
        }
         
}