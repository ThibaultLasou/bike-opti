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
		for(int i=0;i<stations.size();i++)
		{
			stations.get(i).pbID = i;
		}
		this.minimize = true;
	}
	
	@Override
	public int fonctionObj(ArrayList<Integer> vars1, ArrayList<ArrayList<Integer>> vars2) 
	{
		int val = 0;
		
		for(int i=0;i<stations.size();i++)
		{
			StationVelo sv = stations.get(i);
			val += vars1.get(i)*sv.c;
			for(int j=0;j<stations.size();j++)
			{
				val += sv.v*sv.getImoins_J(j, vars1.get(i), vars2.get(i));
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
				//System.out.println(i + " : 1a");
				return false;
			}
			//1b
			for(int j=0;j<stations.size();j++)
			{
				if(!(vars2.get(i).get(j) == s.demande.get(j)))
				{
					//System.out.println(i + " : 1b");
					return false;
				}
			}
			// 1c
			int Ij = 0;
			for(int j=0;j<stations.size();j++)
			{
				Ij += s.getImoins_J(j, vars1.get(i), vars2.get(i));
			}
			if(!(s.getIplus(vars1.get(i), vars2.get(i))-Ij == vars1.get(i) - s.demande.stream().mapToInt(Integer::intValue).sum()))
			{
				//System.out.println(i + " : 1c");
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
			nbVel = r.nextInt(v1bis.get(s1)-1)+1;
			v1bis.set(s1, v1bis.get(s1)-nbVel);
			v1bis.set(s2, v1bis.get(s2)+nbVel);
			// TODO ajuster B
			//System.out.println(v1bis);
			//System.out.println(v2bis);
		}while(!constraints(v1bis, v2bis));
		vars1.clear();
		for(Integer i : v1bis)
		{
			vars1.add(i); 
		}
		vars2 = v2bis;
		System.out.println(v1bis);
		//System.out.println("i");
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