package vls;

import vls.ProblemeVLS;
import vls.StationVelo;

import java.util.ArrayList;

import algorithms.Recuit;

/**
 * @author Patrice Camousseigt
 */
public class MainVLS
{
/*    public static void main (String[] args)
    {
        ArrayList<StationVelo> svs = new ArrayList<>();
        svs.add(new StationVelo(0,0,"",null,0));
        svs.add(new StationVelo(0,0,"",null,0));
        svs.add(new StationVelo(0,0,"",null,0));
        svs.get(0).k = 15;
        svs.get(0).c = 5;
        svs.get(0).v = 8;
        svs.get(0).w = 16;
        svs.get(0).demande = new ArrayList<>();
        svs.get(0).demande.add(0,0);
        svs.get(0).demande.add(1,2);
        svs.get(0).demande.add(2,2);
        svs.get(0).x = 10;
        svs.get(0).B = new ArrayList<>();
        svs.get(0).B.add(0,0);
        svs.get(0).B.add(1,2);
        svs.get(0).B.add(2,2);
        
        svs.get(1).k = 15;
        svs.get(1).c = 8;
        svs.get(1).v = 3;
        svs.get(1).w = 2;
        svs.get(1).demande = new ArrayList<>();
        svs.get(1).demande.add(0,8);
        svs.get(1).demande.add(1,0);
        svs.get(1).demande.add(2,2);
        svs.get(1).x = 10;
        svs.get(1).B = new ArrayList<>();
        svs.get(1).B.add(0,8);
        svs.get(1).B.add(1,0);
        svs.get(1).B.add(2,2);
        
        svs.get(2).k = 15;
        svs.get(2).c = 2;
        svs.get(2).v = 13;
        svs.get(2).w = 8;
        svs.get(2).demande = new ArrayList<>();
        svs.get(2).demande.add(0,2);
        svs.get(2).demande.add(1,8);
        svs.get(2).demande.add(2,0);
        svs.get(2).x = 10;
        svs.get(2).B = new ArrayList<>();
        svs.get(2).B.add(0,2);
        svs.get(2).B.add(1,8);
        svs.get(2).B.add(2,0);
        
        ProblemeVLS p = new ProblemeVLS(svs, null);
        Recuit<Integer, ArrayList<Integer>> r = new Recuit<Integer, ArrayList<Integer>>(p, 5, 5, 0.8f);
        
        System.out.println(p.stations.get(0).getOmoins(p.sumOfBj(0, p.getVarDeuxNiv())));
        
        r.solve();
        
        for(StationVelo s : p.stations)
        {
        	System.out.println(s.pbID + " : " + s.x);
        }
    }*/
}
