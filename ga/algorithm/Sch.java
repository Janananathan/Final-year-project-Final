package org.cloudbus.cloudsim.ga.algorithm;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.ga.algorithm.*;
public class Sch {
	List<Vm> vmList = null;
	List<Cloudlet> ctList = null;
	Para parameters = null;

	 public Sch() {
	
	 }

	public Sch(List<Vm> vmlist, List<Cloudlet> ctlist) {
		parameters = new Para("genetic_algorithm.xml");
		parameters.Apply(vmlist, ctlist);
	}

	public List<Integer> Execute() {
		Popu lastpop = null;
		Popu nextpop = null;
		Indi bestSolution = null;
		Indi currSolution = null;
		int counter = 0;

		lastpop = new Popu(parameters);
		lastpop.Initialize(); 

		bestSolution = lastpop.FindTheBest(lastpop.GetIndividuals());
		while (counter < parameters.GetGenerationSize()) {
			nextpop = lastpop.Evolve();
			currSolution = nextpop.FindTheBest(nextpop.GetIndividuals());
			if (currSolution.Fitness() > bestSolution.Fitness()) {
				bestSolution = currSolution.Duplicate();
				counter = 0; // reset
			} else {
				counter += 1;
			}
			lastpop = nextpop;
		}

		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("########## Final Result: age=(" + counter + ") ###########");
		System.out.println("");
		bestSolution.Show();
		return bestSolution.Return();
	}
}
