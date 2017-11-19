package vls;

import java.util.ArrayList;
import java.util.Random;

import algorithms.Probleme;

public class ProblemeVLS extends Probleme<Integer, ScenarioVLS>
{
	public ArrayList<StationVelo> stations;
	
	public ProblemeVLS(ArrayList<StationVelo> stations, ArrayList<ScenarioVLS> scenarios) 
	{
		this.stations = stations;
		for(int i=0;i<stations.size();i++)
		{
			stations.get(i).pbID = i;
			stations.get(i).x = 0;
			StationVelo.lienNumberStation.put(stations.get(i).getNumber(), stations.get(i));
			StationVelo.lienIdStation.put(i, stations.get(i));
		}	
		this.minimize = true;
		this.scenarios = scenarios;
		nbScenar = scenarios.size();
		for(ScenarioVLS sce : this.scenarios)
		{
			// On initialise une station avec le maximum de vélos nécesaires dans un des scénario, sans dépasser k
			for(StationVelo sta : stations)
			{
				sta.x = Math.min(Math.max(sce.Xis.get(sta.getNumber()).values().stream().mapToInt(Integer::intValue).sum(), sta.x), sta.k);
			}
		}
		setScenario(this.scenarios.get(0));
	}
	
	// Retourne un clone du probleme, avec des stations clonées mais la meme liste de scénarios
	public ProblemeVLS clone() 
	{
		ArrayList<StationVelo> s = new ArrayList<>();
		for(StationVelo sv : this.stations)
		{
			s.add(sv.clone());
		}
		return new ProblemeVLS(s, (ArrayList<ScenarioVLS>) scenarios);
	}
	
	//Calcul de la fonction objectif
	@Override
	public double fonctionObj(ArrayList<Integer> vars1) 
	{
		System.out.println("fonctionObj ProblemeVLS ----------------------------------");
		System.out.println("fonctionObj ProblemeVLS 1");
		double val = 0.0;
		for(int i=0;i<stations.size();i++)
		{
			StationVelo sv = stations.get(i);
			val += vars1.get(i)*sv.c;
		}
		System.out.println("fonctionObj ProblemeVLS 2");
		for(int s=0;s<nbScenar;s++)
		{
			System.out.println("fonctionObj ProblemeVLS scenario : " + s);
			setScenario(scenarios.get(s));
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
		System.out.println("fonctionObj ProblemeVLS fin");
		return val;
	}

	public boolean constraints(ArrayList<Integer> vars1) 
	{
		for(int i=0;i<vars1.size();i++)
		{
			StationVelo sv = stations.get(i);
			// 1a
			if(!(vars1.get(i) <= sv.k))
			{
				return false;
			}
			for(int s=0;s<nbScenar;s++)
			{
				setScenario(scenarios.get(s));
				//1b
				for(int j=0;j<stations.size();j++)
				{
					if(!(sv.B.get(j) == sv.demande.get(j)-sv.getImoins_J(j)))
					{
						return false;
					}
				}
				// 1c
				int Ij = 0;
				for(int j=0;j<stations.size();j++)
				{
					Ij += sv.getImoins_J(j, vars1.get(i));
				}
				if(!(sv.getIplus(vars1.get(i))-Ij == vars1.get(i) - sv.demande.stream().mapToInt(Integer::intValue).sum()))
				{
					return false;
				}
				//1d
				if(!(sv.getOplus(vars1.get(i), sumOfBj(i))-sv.getOmoins(vars1.get(i), sumOfBj(i)) == 
						sv.k - vars1.get(i) + stations.get(i).B.stream().mapToInt(Integer::intValue).sum() - sumOfBj(i)))
				{
					return false;
				}
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
			do
			{
				s1 = r.nextInt(v1bis.size());
			}while(v1bis.get(s1)==0);// On ne peux retirer des vélos d'une station vide
			do
			{
				s2 = r.nextInt(v1bis.size());
			}while(s2 == s1); // on veut s1 != s2
			nbVel = r.nextInt(v1bis.get(s1))+1; // 1 <= nextInt + 1 <= v1bis.get(s1)  -- On retire entre 1 et tous les vélos
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
	public ArrayList<Integer> getVarPremNiv() {
		System.out.println("getVarPremNiv IN");
		ArrayList<Integer> vars1 = new ArrayList<>();
		for(StationVelo s : stations) {
			vars1.add(s.x);
		}
		System.out.println("getVarPremNiv OUT - vars1 size : " + vars1.size());
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
	
	public void setScenario(ScenarioVLS s, ArrayList<Integer> vars1)
	{
		System.out.println("setScenario");
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
			StationVelo staOri = StationVelo.lienNumberStation.get(stationOriID);
			for(Integer stationDestID : s.Xis.get(stationOriID).keySet())
			{
				StationVelo staDest = StationVelo.lienNumberStation.get(stationDestID);
				staOri.demande.set(staDest.pbID, s.Xis.get(stationOriID).get(stationDestID));
			}
		}

		for(StationVelo stati : stations)
		{
			int nbVelR = vars1.get(stati.pbID);
			for(int i=0;i<stati.demande.size();i++)
			{
				stati.B.add(i, Math.min(stati.demande.get(i), nbVelR));
				nbVelR -= stati.B.get(i);
			}
		}
	}
	
	public void setScenario(ScenarioVLS s)
	{
		setScenario(s, getVarPremNiv());
	}

	public void deterministe() 
	{
		nbScenar = 1;
		for(StationVelo sta : stations)
		{
			sta.x = Math.min(scenarios.get(0).Xis.get(sta.getNumber()).values().stream().mapToInt(Integer::intValue).sum(), sta.k);
		}
		setScenario(this.scenarios.get(0));
	}

	public void stochastique() 
	{
		nbScenar = scenarios.size();
		for(ScenarioVLS sce : this.scenarios)
		{
			// On initialise une station avec le maximum de vélos nécesaires dans un des scénario, sans dépasser k
			for(StationVelo sta : stations)
			{
				sta.x = Math.min(Math.max(sce.Xis.get(sta.getNumber()).values().stream().mapToInt(Integer::intValue).sum(), sta.x),sta.k);
			}
		}
		setScenario(this.scenarios.get(0));

	}

	public String toStringResults() 
	{	
		String a = "";
		for(int i=0;i<stations.size();i++)
		{
			a += stations.get(i).getNumber() + " : "+ stations.get(i).getX()+'\n';
		}
		return a;
	}
}
