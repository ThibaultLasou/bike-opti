package cplex;

import java.util.ArrayList;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.cplex.IloCplex;

public class ProblemeCplex 
{
	public IloCplex modele;
	public ArrayList<StationVelo> stations;
	
	public ProblemeCplex()
	{
		try 
		{
			this.modele = new IloCplex();
		} 
		catch (IloException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stations = new ArrayList<StationVelo>();
		int[] demande1 = {0, 2, 2};
		this.stations.add(new StationVelo(1, 5, 8, 16, 15, demande1, this.modele));
		int[] demande2 = {8, 0, 2};
		this.stations.add(new StationVelo(2, 8, 3, 2, 15, demande2, this.modele));
		int[] demande3 = {2, 8, 0};
		this.stations.add(new StationVelo(3, 2, 13, 8, 15, demande3, this.modele));
		
		IloNumExpr[] Bjs = new IloNumExpr[this.stations.size()]; 
		
		for(int j=0;j<Bjs.length;j++)
		{
			try {
				Bjs[j] = modele.numExpr();
			} catch (IloException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(StationVelo s : this.stations)
		{
			for(int j=0;j<s.B.length;j++)
			{
				try {
					Bjs[j] = modele.sum(Bjs[j], s.B[j]);
				} catch (IloException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for(int j=0; j<this.stations.size();j++)
		{
			try {
				this.stations.get(j).setOmoins(modele, Bjs[j]);
				this.stations.get(j).setOplus(modele, Bjs[j]);
				this.stations.get(j).Oplus.toString();
				this.stations.get(j).Omoins.toString();
				
			} catch (IloException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		IloNumExpr lo;
		IloNumExpr ro;
		for(StationVelo s : this.stations)
		{
			// 1b
			for(int j=0;j<s.B.length;j++)
			{
				try 
				{
					lo = s.B[j];
					ro =  modele.diff(s.demande[j],s.Imoins[j]);
					modele.addEq(lo, ro);
				}
				catch(IloException e)
				{
					e.printStackTrace();
				}
			}	
		}
		// 1c
		for(StationVelo s : this.stations)
		{
			try 
			{
				lo = modele.diff(s.Iplus, modele.sum(s.Imoins));
				ro = modele.diff(s.x, java.util.stream.IntStream.of(s.demande).sum());
				modele.addEq(lo, ro);
			}
			catch(IloException e)
			{
				e.printStackTrace();
			}
		}
		
		// 1d
		for(StationVelo s : this.stations)
		{
			try 
			{
				lo = modele.diff(s.Oplus, s.Omoins);
				ro = modele.diff(s.k, modele.sum(s.x, modele.diff(modele.sum(s.B),java.util.stream.IntStream.of(s.demande).sum())));
				modele.addEq(0,ro);
			}
			catch(IloException e)
			{
				e.printStackTrace();
			}
		}
		
		// Fonction Objectif
		try {
			IloNumExpr fct = modele.numExpr();
			IloNumExpr ctAcq = modele.numExpr();
			IloNumExpr ctMan = modele.numExpr();
			IloNumExpr ctPla = modele.numExpr();
			
			for(StationVelo s : this.stations)
			{
				try 
				{
					ctAcq = modele.sum(ctAcq,modele.prod(s.c, s.x));
					ctMan = modele.sum(ctMan, modele.prod(s.v, modele.sum(s.Imoins)));
					ctPla = modele.sum(ctPla, modele.prod(s.w, s.Omoins));
				}
				catch(IloException e)
				{
					e.printStackTrace();
				}
			}
			fct = modele.sum(ctAcq, modele.sum(ctMan, ctPla));
			modele.addMinimize(fct);
		} catch (IloException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IloException 
	{
		ProblemeCplex p = new ProblemeCplex();
		System.out.println(p.modele.toString());
		p.modele.solve();
		System.out.println(p.modele.getObjValue());
		for(StationVelo s : p.stations)
		{
			System.out.println("------------------------");
			System.out.println(s.pbID);
			System.out.println(p.modele.getValue(s.x));
			for(int j=0;j<s.B.length;j++)
			{
				System.out.println(p.modele.getValue(s.B[j]));
			}
			System.out.println(p.modele.getValue(s.Iplus));
			for(int j=0;j<s.Imoins.length;j++)
			{
				System.out.println(p.modele.getValue(s.Imoins[j]));
			}
			//System.out.println(p.modele.getValue(s.Oplus));
			System.out.println(p.modele.getValue(s.Omoins));
		}
	}
}