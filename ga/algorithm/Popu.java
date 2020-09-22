package org.cloudbus.cloudsim.ga.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.cloudbus.cloudsim.ga.algorithm.*;
public class Popu {

	private Para parameters = null;
	private int currentAge = 0;
	private List<Indi> individuals = new ArrayList<Indi>();
	private Random random = new Random();
	private List<Indi> indvSelects = new ArrayList<Indi>();
	private double[] indvFitnessPortions = null;
	private int replacement = 0;

	public Popu(Para par) {
		this.parameters = par;
	}

	public void Initialize() {
		// for whole population individuals are find
		for (int i = 0; i < parameters.GetPopulationSize(); i++) {
			Indi indv = new Indi(parameters.GetVMs(), parameters.GetCloudlets());//create Mapper List for Individual.
			indv.Evaluate();
			GetIndividuals().add(indv);
			System.out.println("Creating Individual " + i);
		}

		Indi ibest = FindTheBest(GetIndividuals());
		System.out.println("#### Initial Best Solution: age=(" + currentAge + ") ####");
		System.out.println("");
		ibest.Show();
		return;
	}

	public List<Indi> GetIndividuals() {
		return individuals;
	}

	public Indi FindTheBest(List<Indi> ilist) {
		double rmax = Double.MAX_VALUE;
		Indi indv = null;
		for (Indi tmpIndv : ilist) {
			double tmpTime = tmpIndv.Duration();
			if (tmpTime < rmax) {
				indv = tmpIndv;
				rmax = tmpTime;
			}
		}
		return indv;
	}

	public Popu Evolve() {

		Popu nextpop = new Popu(parameters);
		Indi indvAdd = null;
			indvAdd = FindTheBest(GetIndividuals()).Duplicate();// copyiny best individual
			nextpop.increase(indvAdd);
                System.out.println("The best one from pre gen is added to next gen");
		indvAdd.Show();
		System.out.println("---------------------------------------------------------------");


	

		int totalSize = 0;
		while (totalSize < parameters.GetPopulationSize()) {
			indvAdd = null;
                        System.out.println("------------------- SELECTION -------------------------");
			Selection();
				
				for (Indi indv : GetSelecteds()) {
					indvAdd = indv.Duplicate();// avoid reference
					nextpop.increase(indvAdd);
					indv.Show();
				}
			//}
                         System.out.println("----------------------------------------------------------");

			Crossover();
                        System.out.println("-----------------------After crossover--------------------");
                        for (Indi indv : GetSelecteds()) {
				indv.Evaluate();
				nextpop.increase(indv.Duplicate());
				indv.Show();
			}
                        System.out.println("-------------------MUTATION-----------------");
			Mutation();
			
			for (Indi indv : GetSelecteds()) {
				indv.Evaluate();
				nextpop.increase(indv.Duplicate());
				indv.Show();
			}
			
			
			totalSize = nextpop.individuals.size();
			System.out.println("Population size of current generation :"+nextpop.individuals.size());
		}
                System.out.println();
                System.out.println("------------------------Next Population-----------------------------------");
                for(Indi t:nextpop.individuals)
                {
                    t.Show();
                }
                System.out.println();
		return nextpop;
	}

	private Popu increase(Indi... indvss) {
		for (Indi tmpIndv : indvss) {
			tmpIndv.GenIdx();
			GetIndividuals().add(tmpIndv);
		}
		return this;
	}

	private Popu increase(List<Indi> indvlist) {
		for (Indi tmpIndv : indvlist) {
			tmpIndv.GenIdx();
			GetIndividuals().add(tmpIndv);
		}
		return this;
	}


	public List<Indi> Selection() {
		Indi ib1st = null;
		Indi ib2nd = null;
		GetSelecteds().clear();

		switch (parameters.GetSelectionPolicy()) {
		case "Randomly":
			int rn = 0;
			rn = random.nextInt(GetIndividuals().size());
			ib1st = GetIndividuals().get(rn);
			rn = random.nextInt(GetIndividuals().size());
			ib2nd = GetIndividuals().get(rn);
			break;
		case "Best-Two":
			ib1st = FindTheBest(GetIndividuals());
			ib2nd = FindSndBest(GetIndividuals());
			break;
		case "Tournament":
                    
			ib1st = selectionTournament();
			while (true) {
			ib2nd = selectionTournament();
			if (ib1st.Duration() != ib2nd.Duration())
			break;
			}
                        break;
		default:
			System.out.println("#ERROR: no such selection policy (" + parameters.GetSelectionPolicy() + ")");
			return null;
		}

		ib1st = ib1st.Duplicate();
		ib2nd = ib2nd.Duplicate();
		indvSelects.add(ib1st);
		indvSelects.add(ib2nd);
		return GetSelecteds();
	}

	public List<Indi> GetSelecteds() {
		return indvSelects;
	}

	public Indi FindSndBest(List<Indi> ilist) {
		Indi sbest = null;
		Indi fbest = null;
		fbest = FindTheBest(ilist);
		ilist.remove(fbest);
		fbest = FindTheBest(ilist);
		sbest = fbest;
		return sbest;
	}


	private Indi selectionTournament() {
		// tournament selection
		Indi ibest = null;
		Indi icheck = null;
		int rn = 0;
		List<Indi> tournamentIndvs = new ArrayList<Indi>();
		for (int j = 0; j < parameters.GetTournamentSize(); j++) {
			rn = random.nextInt(GetIndividuals().size());
			icheck = GetIndividuals().get(rn);
			tournamentIndvs.add(icheck);
		}
		double ppp = random.nextDouble();
		if (ppp < parameters.GetTournamentRate()) {
			ibest = FindTheBest(tournamentIndvs);
		} else {
			ibest = FindSndBest(tournamentIndvs);
		}
		return ibest;
	}

	public List<Indi> Crossover() {
		int isize = GetSelecteds().size();

		if (isize < 2) {
			System.out.println("#ERROR: nothing to be crossover");
			return GetSelecteds();
		}
			Indi b1 = GetSelecteds().get(0);
			Indi b2 = GetSelecteds().get(1);
			switch (parameters.GetCrossoverPolicy()) {
			case "One-Point":
				b1.crossoverOnePoint(b2);
				break;
			case "Two-Point":
				b1.crossoverTwoPoint(b2);
				break;
			case "Uniformly":
				b1.crossoverUniformly(b2);
				break;
			default:
				break;

			}
		
		return GetSelecteds();
	}
        public List<Indi> Mutation(){

		for(Indi indv: GetSelecteds()){
			switch("Interchange"){
				case "Randomchange":
                                        indv.Evaluate();//crossing genes
					indv.mutationRandomChange();
					break;
				case "Interchange":
					indv.Evaluate();//crossing genes
					indv.mutationInterChange();
					break;
				default:
					break;
			}
		}
		return GetSelecteds();
	}
        

}
