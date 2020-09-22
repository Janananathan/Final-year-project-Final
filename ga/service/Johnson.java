package org.cloudbus.cloudsim.ga.service;

import java.util.*;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.ga.App;

public class Johnson
{    
   public static  List<Cloudlet>l=new ArrayList<Cloudlet>();
   public static  List<Cloudlet>jon=new ArrayList<Cloudlet>();
   private static Random random = new Random();
   static int line=0;
   public Johnson(List<Cloudlet>cl) 
   {
       jon=apply(cl);
   }
   public static List<Cloudlet> apply(List<Cloudlet>cl)
   {
           List<Integer>g1=new ArrayList<Integer>();
           List<Integer>g2=new ArrayList<Integer>();
           List<Integer>c1=new ArrayList<Integer>();
           List<Integer>c2=new ArrayList<Integer>();
           List<Double>e1=new ArrayList<Double>();
           List<Double>e2=new ArrayList<Double>();
           g1.clear();
           g2.clear();
       
       
       double delay=0.2;
       System.out.println("initial order of cloudlets");
       for(Cloudlet c: cl)
           {
               System.out.print(c.getCloudletId()+",");
               line++;
               if(line==cl.size()/10)
               {
                   System.out.println();
                   line=0;
               }
           }
       List<Vm> vm=App.revm();
       for(Cloudlet c:cl){
          // delay = random.nextDouble();
        
          
           int x=c.getCloudletId();
           int k=c.getVmId();
           Vm vmx=vm.get(k);
           long length=c.getCloudletLength();
          // double filesize=c.getCloudletFileSize()/100;
           //double bw=vmx.getCurrentAllocatedBw();
           double ips=vmx.getNumberOfPes()*vmx.getMips();
           
           double de=(((double)c.getCloudletFileSize())/100)/((double)vmx.getCurrentAllocatedBw());
            double exce =(length)/(ips);
            double transfer=de+delay;
           System.out.print(","+exce+"-"+de+"-"+transfer+","); 
            
            if(((exce)<=(transfer))){   
               g1.add(x);//ADD CloudletID
               e1.add(exce);//ADD Exceution Time
           }
           else{
              g2.add(x);
               e2.add(exce);
           }
           
       }
       
       
       while(!e1.isEmpty())
       {
           int u=e1.indexOf(Collections.min(e1));
           int k=g1.get(u);
           c1.add(k);
           e1.remove(new Double(Collections.min(e1)));
           g1.remove(new Integer(k));
       }
       line=0;
       System.out.println();
       System.out.println("------------------------------------");
       System.out.println("Size of Group 1: "+c1.size());
       System.out.println("------------------------------------");
       for(int i=0;i<c1.size();i++)
       {
           System.out.print(c1.get(i)+",");
           line++;
           if(line==c1.size()/5){
               System.out.println();
               line=0;}
       }line=0;
       System.out.println();
        //for(int i=0;i<e2.size();i++)
       while(!e2.isEmpty())
        {
           int u=e2.indexOf(Collections.max(e2));
           int k=g2.get(u);
           c2.add(k);
            e2.remove(new Double(Collections.max(e2)));
           g2.remove(new Integer(k));
           
       }
       System.out.println();
       System.out.println("------------------------------------");
       System.out.println("Size of Group 2: "+c2.size());
       System.out.println("------------------------------------");
        for(int i=0;i<c2.size();i++)
       {
           System.out.print(c2.get(i)+",");
           line++;
           if(line==c2.size()/5){
               System.out.println();
               line=0;}
       }line=0;
       System.out.println();
        for(int i=0;i<c1.size();i++)
        {
            int u=c1.get(i);
            l.add(cl.get(u));
        }
        for(int i=0;i<c2.size();i++)
        {
            int u=c2.get(i);
            l.add(cl.get(u));
        }
        System.out.println("------------------------------------");
        System.out.println("Final order of cloudlet list");
        System.out.println("------------------------------------");
        for(int i=0;i<l.size();i++)
       {
           Cloudlet t=l.get(i);
           System.out.print(t.getCloudletId()+",");
           line++;
           if(line==l.size()/10){
               System.out.println();
               line=0;}
       }
        System.out.println();
        
            
            //Collections.sort(co);
           
       return l;
   }
}
   