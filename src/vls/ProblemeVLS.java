package vls;

import java.util.ArrayList;
import java.util.Random;

import algorithms.Probleme;

import static vls.StationVelo.lienNumberId;

public class ProblemeVLS extends Probleme<Integer, ArrayList<Integer>>
{
	public ArrayList<StationVelo> stations;
	//private ArrayList<ScenarioVLS> scenarios;
	private int nbScenar;
	
	public ProblemeVLS(ArrayList<StationVelo> stations, ArrayList<ScenarioVLS> scenarios) 
	{
		this.stations = stations;
		for(int i=0;i<stations.size();i++)
		{
			stations.get(i).pbID = i;
			stations.get(i).x = stations.get(i).k/2;
			lienNumberId.put(stations.get(i).getNumber(), stations.get(i));
		}	
		this.minimize = true;
		this.scenarios = scenarios;
		nbScenar = scenarios.size();
	}
	
	public ProblemeVLS clone() 
	{
		ArrayList<StationVelo> s = new ArrayList<>();
		for(StationVelo sv : this.stations)
		{
			s.add(sv.clone());
		}
		return new ProblemeVLS(s, (ArrayList<ScenarioVLS>) scenarios);
	}
	
	@Override
	public double fonctionObj(ArrayList<Integer> vars1) 
	{
		double val = 0.0;
		for(int i=0;i<stations.size();i++)
		{
			StationVelo sv = stations.get(i);
			val += vars1.get(i)*sv.c;
		}
		for(int s=0;s<nbScenar;s++)
		{
			setScenario((ScenarioVLS) scenarios.get(s));
			int valS = 0;
			for(int i=0;i<stations.size();i++)
			{
					StationVelo sv = stations.get(i);
					for(int j=0;j<stations.size();j++)
					{
						valS += sv.v*sv.getImoins_J(j, vars1.get(i));
					}
					valS += sv.w*sv.getOmoins(vars1.get(i), sumOfBj(i));
			}
			val += valS*1.0/nbScenar;
		}
		return val;
	}

	public boolean constraints(ArrayList<Integer> vars1) 
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
				//TODO
				if(!(s.B.get(j) == s.demande.get(j)))
				{
					return false;
				}
			}
			// 1c
			int Ij = 0;
			for(int j=0;j<stations.size();j++)
			{
				Ij += s.getImoins_J(j, vars1.get(i));
			}
			if(!(s.getIplus(vars1.get(i))-Ij == vars1.get(i) - s.demande.stream().mapToInt(Integer::intValue).sum()))
			{
				return false;
			}
			//1d
			if(!(s.getOplus(vars1.get(i), sumOfBj(i))-s.getOmoins(vars1.get(i), sumOfBj(i)) == 
					s.k - vars1.get(i) + stations.get(i).B.stream().mapToInt(Integer::intValue).sum() - sumOfBj(i)))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public void voisinage(ArrayList<Integer> vars1) 
	{
		Random r = new Random();
		ArrayList<Integer> v1bis;
		int s1, s2, nbVel;
		do
		{
			v1bis = (ArrayList<Integer>) vars1.clone();
			s1 = r.nextInt(v1bis.size());
			do
			{
				s2 = r.nextInt(v1bis.size());
			}while(s2 == s1);
			nbVel = r.nextInt(v1bis.get(s1)-1)+1;
			v1bis.set(s1, v1bis.get(s1)-nbVel);
			v1bis.set(s2, v1bis.get(s2)+nbVel);
		}while(!constraints(v1bis));
		vars1.clear();
		for(Integer i : v1bis)
		{
			vars1.add(i); 
		}
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

	public int sumOfBj(int j)
	{
		int Bj = 0;
		for(int i=0;i<stations.size();i++)
		{
			Bj += stations.get(i).B.get(j);
		}
		return Bj;
	}
	
	public void setScenario(ScenarioVLS s)
	{
		for(StationVelo sv : stations)
		{
			sv.demande.clear();
			for(int i=0;i<stations.size();i++)
			{
				sv.demande.add(i, 0);
			}
		}
		for(Integer stationOriID : s.Xis.keySet()) 
		{
			StationVelo staOri = lienNumberId.get(stationOriID);
			for(Integer stationDestID : s.Xis.get(stationOriID).keySet())
			{
				StationVelo staDest = lienNumberId.get(stationOriID);
				staOri.demande.set(staDest.pbID, s.Xis.get(stationOriID).get(stationDestID));
			}
			// TODO déterminer une vraie valeur pour B
		}
		for(StationVelo stati : stations)
		{
			stati.B = (ArrayList<Integer>) stati.demande.clone();
		}
		
	}
}
