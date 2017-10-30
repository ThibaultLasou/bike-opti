package vls;

import java.util.HashMap;

public class ScenarioVLS 
{
	public HashMap<Integer, HashMap<Integer, Integer>> Bs; // StationDépart, stationArrivée, quantité
	
	public ScenarioVLS(HashMap<Integer, HashMap<Integer, Integer>> Bs)
	{
		this.Bs = Bs;
	}
}