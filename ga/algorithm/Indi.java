package org.cloudbus.cloudsim.ga.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.cloudbus.cloudsim.ga.algorithm.*;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

public class Indi {
	private Indi(Indi old) {// duplication
		old.Cover(this);// this refer to dup new
	}

	private class Mapper {// implements Clonable{
		private int Id = 0;
		private Vm resource = null;
		private Cloudlet task = null;
		private String binString = "";
		private double procTime = 0.00;

		public Mapper(Vm k, Cloudlet s) {
			resource = k;
			task = s;
			Id = s.getCloudletId();
		}

		public Vm GetVM() {
			return resource;
		}

		public Cloudlet GetCloudlet() {
			return task;
		}

		public double Estimate() {
			Vm tmpVm = GetVM();
			Cloudlet tmpCt = GetCloudlet();
			procTime = tmpCt.getCloudletLength() / (tmpVm.getMips() * tmpVm.getNumberOfPes());
			return procTime;
		}

		public double GetProcTime() {
			return procTime;
		}

		public Mapper Duplicate() {
			return new Mapper(this);
		}

		public void SwapWith(Mapper old) {
			Vm tmpVm = old.resource;
			old.SetVM(this.GetVM());
			this.SetVM(tmpVm);
			return;
		}

		public void SetVM(Vm k) {
			resource = k;
		}

		private Mapper(Mapper old) {
			Id = old.Id;
			resource = old.resource;
			task = old.task;
			binString = old.binString;
			procTime = old.procTime;
		}

	}// end of Mapper

	private Random random = new Random();
	private List<Mapper> mapperList = new ArrayList<Mapper>();
	private String binaryString = "";
	private Map<Integer, ArrayList<Mapper>> geneMap = new HashMap<Integer, ArrayList<Mapper>>();
	private double doneTime = 0.00;
	private double fitValue = 0.00;
	private int selfIdx = 0;
	private static int globalIdx = 0;

	// ct,vm passess to mapper(initialization part)
	public Indi(List<Vm> vmlist, List<Cloudlet> ctlist) {
		for (Cloudlet ct : ctlist) {
			int rn = random.nextInt(vmlist.size());
			Vm vm = vmlist.get(rn);
			Mapper mapper = new Mapper(vm, ct);
			mapperList.add(mapper);
		}
	}

	// key--vmid,create two dimensional matrix,see in note
	public double Evaluate() {
		binaryString = "";

		/* build genemap */
		geneMap.clear();
		ArrayList<Mapper> tmpMapperList = null;
		for (Mapper mapper : GetMappers()) {
			Integer key = new Integer(mapper.GetVM().getId());
			if (geneMap.containsKey(key)) {
				tmpMapperList = geneMap.get(key);
				tmpMapperList.add(mapper);
			} else {
				tmpMapperList = new ArrayList<Mapper>();
				tmpMapperList.add(mapper);
				geneMap.put(key, tmpMapperList);
			}
		}
		double needTime = 0.00;
		doneTime = 0.00;
                //DoneTime--->Time taken for individual
                //NeedTime-->Time taken for single vm to complete
		for (Entry<Integer, ArrayList<Mapper>> entry : geneMap.entrySet()) {
			needTime = 0.00;
			tmpMapperList = entry.getValue();
			for (Mapper mapper : tmpMapperList) {
				needTime += mapper.Estimate();
			}
			doneTime = doneTime > needTime ? doneTime : needTime;
		}
		/* assume normal time */
		fitValue = Math.abs(doneTime - 0.00) <= 0.001 ? 1.00 : 1.00 / doneTime;
		return fitValue;
	}

	public List<Mapper> GetMappers() {
		return mapperList;
	}

	public void Show() {
		int i = 0;
		System.out.format("## Solution[%d]: Duration=%f, Fitness=%f %n", Idx(), Duration(), Fitness());
		System.out.println(" ");

		for (Entry<Integer, ArrayList<Mapper>> entry : geneMap.entrySet()) {
			double tmpTime = 0.00;
			System.out.print("VM[" + entry.getKey() + "]: ");
			for (Mapper mapper : entry.getValue()) {
				tmpTime += mapper.GetProcTime();
				System.out.format("%04d ", mapper.GetCloudlet().getCloudletId());
			}
			System.out.format("; Time=(%.4f)", tmpTime);
			System.out.println(" ");
		}
	}

	public int Idx() { // Key()
		return selfIdx;
	}

	public double Duration() {
		return doneTime;
	}

	public double Fitness() {
		return fitValue;
	}

	public Indi Duplicate(Indi... indvs) {
		Indi dup = null;
		if (indvs.length == 0) {
			dup = new Indi(this);
		} else {
			dup = indvs[0];
			this.Cover(dup);
		}
		dup.Evaluate();
		return dup;
	}

	// Best individual cloning for new generation
	private Indi Cover(Indi newObj) {
		newObj.GetMappers().clear();
		for (Mapper mapper : GetMappers()) { // old indi mappers
			newObj.GetMappers().add(mapper.Duplicate()); // return new mapper obj clone
		}
		// Todo: copy the geneMap
		newObj.fitValue = fitValue;
		newObj.doneTime = doneTime;
		return newObj;
	}

	public int GenIdx() {
		selfIdx = globalIdx++;
		return selfIdx;
	}

	public boolean crossoverOnePoint(Indi oppr) {
		Indi b1 = this;
		Indi b2 = oppr;
		int size = 0, p1 = 0;
		size = b1.GetMappers().size();
		int max = size - 1;
		int min = size / 3;
		p1 = random.nextInt((max - min) + 1) + min;
		System.out.println("random integer for one point cross over" + p1);
		for (int j = 0; j <= p1; j++) {
			Mapper g1 = b1.GetMappers().get(j);
			Mapper g2 = b2.GetMappers().get(j);
			g1.SwapWith(g2);
		}		System.out.println();
		return true;
	}

	public boolean crossoverTwoPoint(Indi oppr) {
		Indi b1 = this;
		Indi b2 = oppr;
		int size = 0, p1 = 0, p2 = 0, pp = 0;
		size = b1.GetMappers().size();
		p1 = random.nextInt(size);
		p2 = random.nextInt(size);
		if (p1 > p2) {
			pp = p2;
			p2 = p1;
			p1 = pp;
		}
                System.out.println("random integer for two point cross over" + p1+","+p2);

		for (int j = 0; j <= p1; j++) {
			Mapper g1 = b1.GetMappers().get(j);
			Mapper g2 = b2.GetMappers().get(j);
			g1.SwapWith(g2);
		}

		for (int j = p2; j < size; j++) {
			Mapper g1 = b1.GetMappers().get(j);
			Mapper g2 = b2.GetMappers().get(j);
			g1.SwapWith(g2);
		}
                System.out.println();;
		return true;
	}

	public boolean crossoverUniformly(Indi oppr) {
		double aProba = 0.5;
		double bProba = 0.5;
		Indi b1 = this;
		Indi b2 = oppr;
		if (random.nextDouble() < aProba) {
			for (int j = 0; j < b1.GetMappers().size(); j++) {
				if (random.nextFloat() < bProba) {
					b1.GetMappers().get(j).SwapWith(b2.GetMappers().get(j));
				}
			}
		}
		return true;
	}
        public boolean mutationRandomChange(){
		Indi b1 = this;
		int size = b1.GetMappers().size();
		int p1 = random.nextInt(size);
		int p2 = 0;
		do{
			p2 = random.nextInt(b1.GetMappers().size());
		}while(p1==p2);

		Mapper g1 = b1.GetMappers().get(p1);
		Mapper g2 = b1.GetMappers().get(p2);
		g1.SwapWith(g2);
		return true;
	}
        public boolean mutationInterChange(){
		int size = GetGeneMap().size();
		
		int p1 = random.nextInt(size);
		int p2 = p1;
		while(p1 == p2){
			p2 = random.nextInt(size);
		}
		System.out.println("MUTATION NUMBER "+p1+ "  "+ p2);
		List<Mapper> as1 = GetGeneMap().get(new Integer(p1));
		List<Mapper> as2 = GetGeneMap().get(new Integer(p2));

		//TODO: find VM via id
		Vm vm1 = as1.get(0).GetVM();
		Vm vm2 = as2.get(0).GetVM();
		for(Mapper mapper: as1){
			mapper.SetVM(vm2);	
		}
		for(Mapper mapper: as2){
			mapper.SetVM(vm1);
		}
		return true;
	}
        public Map<Integer, ArrayList<Mapper>> GetGeneMap (){
		return geneMap;
	}

	public List<Integer> Return() {
		List<Integer> results = new ArrayList<Integer>();
		for (Entry<Integer, ArrayList<Mapper>> entry : geneMap.entrySet()) {
			for (Mapper mapper : entry.getValue()) {
				results.add(new Integer(mapper.GetVM().getId()));
				results.add(new Integer(mapper.GetCloudlet().getCloudletId()));
			}
		}
		return results;
	}
}
