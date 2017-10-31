package vls;

import java.util.ArrayList;
import java.util.HashMap;

import algorithms.Scenario;

public class ScenarioVLS extends Scenario
{
	public HashMap<Integer, HashMap<Integer, Integer>> Xis; // StationDépart, stationArrivée, quantité
	public ArrayList<ArrayList<Integer>> Bs;
	
	public ScenarioVLS(HashMap<Integer, HashMap<Integer, Integer>> Xis)
	{
		this.Xis = Xis;
	}
}