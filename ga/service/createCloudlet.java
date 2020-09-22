package org.cloudbus.cloudsim.ga.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

public class createCloudlet
{
    public static List<Cloudlet> cl;
    
    public createCloudlet(int Bid)
    {
        cl=CreateCloudlet(Bid);
    }
    public List<Cloudlet> CreateCloudlet(int brokerId)
        {
         List<Cloudlet> cloudletList = new ArrayList<Cloudlet>();

			//Cloudlet properties
                        System.out.println("Enter the Cloudlet file name:");
                        Scanner sc=new Scanner(System.in);
                        String CloudletFile=sc.next();
                        File file=new File(CloudletFile);
                        try {
                                sc=new Scanner(file);
                                int i=0;
                                while(sc.hasNext())
                                {
                                    String data=sc.next();
                                    String[] values=data.split(",");
                                    int id=i;
                                    i++;
                                    long length=Long.parseLong(values[0])/1000000;
                                    long filesize=Long.parseLong(values[1]);
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
                        return cloudletList;
        }
}