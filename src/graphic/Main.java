package graphic;

import vls.StationVelo;

import java.util.ArrayList;

import static gestionnaireFichier.GestionnaireFichier.creerFichierConfiguration;
import static gestionnaireFichier.GestionnaireFichier.parserFichier;
import static gestionnaireFichier.GestionnaireFichier.parserFichierConfiguration;

/**
 * @author Patrice Camousseigt
 */
public class Main {
    public static void main (String[] args){
        System.out.println("Hello World");
        //new Interface();
        /*ArrayList<StationVelo> stationVelos = parserFichier();
        ArrayList<Integer> numeroStationsVelos = new ArrayList<>();
        for(StationVelo stationVelo: stationVelos) {
            numeroStationsVelos.add(stationVelo.getNumber());
        }
        creerFichierConfiguration(numeroStationsVelos,"/home/patricecamousseigt/Bureau");*/
        parserFichierConfiguration("/home/patricecamousseigt/Bureau/","/fichier_configuration.csv");
    }
}
