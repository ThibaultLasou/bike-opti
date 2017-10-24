package vls;

import java.util.ArrayList;
import algorithms.Probleme;

public class ProblemeVLS extends Probleme 
{
	public ArrayList<StationVelo> stations;
	//private ArrayList<Scenario> scenario;
	
	
	@Override
	public int fonctionObj(ArrayList<Integer> vars) 
	{
		int val = 0;
		for(StationVelo sv : stations)
		{
			val += sv.x*sv.c;
		}
		for(StationVelo sv : stations)
		{
			for(int j=0;j<stations.size();j++)
			{
				val += sv.v*sv.getImoins_J(j);
			}
			val += sv.w*sv.getOmoins();
		}
		return val;
	}

	@Override
	public boolean constraints(ArrayList<Integer> vars) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Integer> voisinage() {
		// TODO Auto-generated method stub
		return null;
	}
}
