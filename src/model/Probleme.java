package model;

import java.util.ArrayList;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

public class Probleme 
{
	public IloCplex modele;
	public ArrayList<StationVelo> stations;
	
	public Probleme()
	{
		try {
			this.modele = new IloCplex();
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stations = new ArrayList<StationVelo>();
		this.stations.add(new StationVelo(0, cc, cv, cw, ck, cdemande, modele))
	}
	
	
	public static void main(String[] args) 
	{
		
	}
}