package vls;

import java.util.ArrayList;
import java.util.Random;

import algorithms.Probleme;

import static vls.StationVelo.lienNumberId;

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
			lienNumberId.put(i, stations.get(i));
		}
		this.minimize = true;
	}
	
	public ProblemeVLS clone() 
	{
		ArrayList<StationVelo> s = new ArrayList<>();
		for(StationVelo sv : this.stations)
		{
			s.add(sv.clone());
		}
		return new ProblemeVLS(s);
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
			val += sv.w*sv.getOmoins(vars1.get(i), vars2.get(i), sumOfBj(i, vars2));
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
				Ij += s.getImoins_J(j, vars1.get(i), vars2.get(i));
			}
			if(!(s.getIplus(vars1.get(i), vars2.get(i))-Ij == vars1.get(i) - s.demande.stream().mapToInt(Integer::intValue).sum()))
			{
				return false;
			}
			//1d
			if(!(s.getOplus(vars1.get(i), vars2.get(i), sumOfBj(i, vars2))-s.getOmoins(vars1.get(i), vars2.get(i), sumOfBj(i, vars2)) == 
					s.k - vars1.get(i) + vars2.get(i).stream().mapToInt(Integer::intValue).sum() - sumOfBj(i, vars2)))
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
			nbVel = r.nextInt(v1bis.get(s1)-1)+1;
			v1bis.set(s1, v1bis.get(s1)-nbVel);
			v1bis.set(s2, v1bis.get(s2)+nbVel);
			
			// On retire les vélos qu'on vient de déplacer de s1.B
			int diffB1 = v2bis.get(s1).stream().mapToInt(Integer::intValue).sum() - v1bis.get(s1); //Nb vélos loués - Nb vélos affectés  
			while(diffB1 > 0)
			{
				int b = r.nextInt(v2bis.get(s1).size());// tirage de la station vers où on retire des vélos
				v2bis.get(s1).set(b, Math.max(v2bis.get(s1).get(b)-diffB1,0)); // on retire un maximum de vélo
				diffB1 = v2bis.get(s1).stream().mapToInt(Integer::intValue).sum() - v1bis.get(s1); // on regarde combien de vélos il reste à enlever
			}
			
			// On ajoute les vélos qu'on vient de déplacer à s2.B
			int diffB2 = v1bis.get(s2) - v2bis.get(s2).stream().mapToInt(Integer::intValue).sum(); // nb de vélos affectés - nb de vélos loués
			int diffEB = stations.get(s2).demande.stream().mapToInt(Integer::intValue).sum() - v2bis.get(s2).stream().mapToInt(Integer::intValue).sum(); //nb de vélos demandés - nb de vélos loués
			while(diffB2 > 0 && diffEB != 0) // tant qu'il reste des vélos non affectés et de la demande insatisfaite
			{
				int b = r.nextInt(v2bis.get(s1).size()); // tirage de la station vers où on retire des vélos
				v2bis.get(s1).set(b, Math.min(v2bis.get(s1).get(b)+diffB2, stations.get(s2).demande.get(b))); // on rajoute un maximum de vélos
				diffB2 = v2bis.get(s1).stream().mapToInt(Integer::intValue).sum() - v1bis.get(s1); // on regarde 
			}
		}while(!constraints(v1bis, v2bis));
		vars1.clear();
		for(Integer i : v1bis)
		{
			vars1.add(i); 
		}
		vars2.clear();
		for(ArrayList<Integer> i : v2bis)
		{
			vars2.add(i); 
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
	public void setVarDeuxNiv(ArrayList<ArrayList<Integer>> vars) 
	{
		for(int i=0;i<stations.size();i++)
		{
			stations.get(i).B = vars.get(i);
		}		
	}
	
	public int sumOfBj(int j, ArrayList<ArrayList<Integer>> B)
	{
		int Bj = 0;
		for(int i=0;i<B.size();i++)
		{
			Bj += B.get(i).get(j);
		}
		return Bj;
	}
	
}