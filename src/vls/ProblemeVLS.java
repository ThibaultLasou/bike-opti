package vls;

import java.util.ArrayList;
import java.util.Random;

import algorithms.Probleme;

public class ProblemeVLS extends Probleme<Integer, ArrayList<Integer>>
{
	public ArrayList<StationVelo> stations;
	//private ArrayList<Scenario> scenario;
	
	public ProblemeVLS(ArrayList<StationVelo> stations) 
	{
		this.stations = stations;
	}
	
	@Override
	public int fonctionObj(ArrayList<Integer> vars1, ArrayList<ArrayList<Integer>> vars2) 
	{
		int val = 0;
		for(int i=0;i<stations.size();i++)
		{
			val += vars1.get(i)*stations.get(i).c;
		}
		for(StationVelo sv : stations)
		{
			for(int j=0;j<stations.size();j++)
			{
				val += sv.v*sv.getImoins_J(j);
			}
			val += sv.w*sv.getOmoins(stations);
		}
		return val;
	}

	public boolean constraints(ArrayList<Integer> vars1, ArrayList<ArrayList<Integer>> vars2) 
	{
		for(int i=0;i<vars1.size();i++)
		{
			StationVelo s = stations.get(i);
			
			// 1a
			if(!(vars1.get(i) <= s.k))
			{
				return false;
			}
			//1b
			for(int j=0;j<stations.size();j++)
			{
				if(!(vars2.get(i).get(j) == s.demande.get(j)))
				{
					return false;
				}
			}
			// 1c
			int Ij = 0;
			for(int j=0;j<stations.size();j++)
			{
				Ij += s.getImoins_J(j);
			}
			if(!(s.getIplus()-Ij == vars1.get(i) - s.demande.stream().mapToInt(Integer::intValue).sum()))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public void voisinage(ArrayList<Integer> vars1, ArrayList<ArrayList<Integer>> vars2) 
	{
		Random r = new Random();
		ArrayList<Integer> v1bis;
		ArrayList<ArrayList<Integer>> v2bis = new ArrayList<>();
		int s1, s2, nbVel;
		do
		{
			v1bis = (ArrayList<Integer>) vars1.clone();
			v2bis.clear();
			for(int i=0;i<vars2.size();i++)
			{
				v2bis.add((ArrayList<Integer>) vars2.get(i).clone());
			}
			s1 = r.nextInt(v1bis.size());
			do
			{
				s2 = r.nextInt(v1bis.size());
			}while(s2 == s1);
			nbVel = r.nextInt(v1bis.get(s1));
			v1bis.set(s1, v1bis.get(s1)-nbVel);
			v1bis.set(s2, v1bis.get(s2)+nbVel);
			// TODO ajuster B
		}while(!constraints(v1bis, v2bis));
		vars1 = v1bis;
		vars2 = v2bis;
	}

	@Override
	public ArrayList<Integer> getVarPremNiv() 
	{
		ArrayList<Integer> vars1 = new ArrayList<>();
		for(StationVelo s : stations)
		{
			vars1.add(s.x);
		}
		return vars1;
	}

	@Override
	public void setVarPremNiv(ArrayList<Integer> vars)
	{
		for(int i=0;i<stations.size();i++)
		{
			stations.get(i).x = vars.get(i);
		}
	}

	@Override
	public ArrayList<ArrayList<Integer>> getVarDeuxNiv() 
	{
		ArrayList<ArrayList<Integer>> vars2 = new ArrayList<>();
		for(StationVelo s : stations)
		{
			vars2.add(s.B);
		}
		return vars2;
	}

	@Override
	public void setVarDeuxNiv(ArrayList<ArrayList<Integer>> vars) {
		for(int i=0;i<stations.size();i++)
		{
			stations.get(i).B = vars.get(i);
		}		
	}
}