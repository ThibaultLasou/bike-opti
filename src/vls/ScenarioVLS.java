package vls;

import java.util.ArrayList;
import java.util.HashMap;

import algorithms.Scenario;

public class ScenarioVLS extends Scenario
{
	/*Liste des demandes du scénario*/
	public HashMap<Integer, HashMap<Integer, Integer>> Xis; // StationDépart, stationArrivée, quantité
	
	/*Liste des vélos loués du scénario*/
	public ArrayList<ArrayList<Integer>> Bs;
	
	public ScenarioVLS(HashMap<Integer, HashMap<Integer, Integer>> Xis)
	{
		this.Xis = Xis;
	}
}