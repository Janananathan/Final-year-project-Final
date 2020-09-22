package org.cloudbus.cloudsim.ga.algorithm;

import java.io.File;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.cloudbus.cloudsim.ga.algorithm.*;
public class Para {
	Document docConfig = null;
	private String useOption = ""; ////
	private int populationSize = 0;
	private int generationSize = 0;
	private double tournamentRate = 0.00;
	private int tournamentSize = 0;
	private double crossoverRate = 0.00;
	private double mutationRate = 0.00;
	private String selectionPolicy = "";
	private String crossoverPolicy = "";
	private String mutationPolicy = "";
	private List<Vm> vmsList = null;
	private List<Cloudlet> cloudletsList = null;

	public Para(String filename) {
		try {
			parse(filename);
		} catch (DocumentException e) {
			e.printStackTrace();
			// TODO: throw something back to the caller
		}
	}

	//genetic file as input
	// Getting para from xml
	private void parse(String filename) throws DocumentException {
		File inFile = new File(filename);
		SAXReader reader = new SAXReader();
		docConfig = reader.read(inFile);
		String pText = ""; // string used for store

		Node root = docConfig.selectSingleNode("//parameters");

		pText = root.selectSingleNode("population/size").getText();
		populationSize = Integer.parseInt(pText);


		pText = root.selectSingleNode("population/generation").getText();
		generationSize = Integer.parseInt(pText);

		pText = root.selectSingleNode("tournament/probability").getText();
		tournamentRate = Double.parseDouble(pText);

		pText = root.selectSingleNode("tournament/size").getText();
		tournamentSize = Integer.parseInt(pText);

		selectionPolicy = root.selectSingleNode("selection/policy").getText();

		crossoverPolicy = root.selectSingleNode("crossover/policy").getText();

		mutationPolicy = root.selectSingleNode("mutation/policy").getText();
	}

	//
	public void Apply(List<Vm> vmlist, List<Cloudlet> ctlist) {
		vmsList = vmlist;
		cloudletsList = ctlist;
	}

	public int GetPopulationSize() {
		return populationSize;
	}
	public List<Vm> GetVMs() {
		return vmsList;
	}

	public List<Cloudlet> GetCloudlets() {
		return cloudletsList;
	}

	public int GetGenerationSize() {
		return generationSize;
	}

	public void SetSelectionPolicy(String sepo) {
		this.selectionPolicy = sepo;
	}
        
	public String GetSelectionPolicy() {
		return selectionPolicy;
	}

	public int GetTournamentSize() {
		return tournamentSize;
	}

	public double GetTournamentRate() {
		return tournamentRate;
	}

	
	public String GetCrossoverPolicy() {
		return crossoverPolicy;
	}
}
