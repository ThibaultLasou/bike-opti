package gestionnaireFichier;

import vls.ScenarioVLS;
import vls.StationVelo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static vls.StationVelo.ParamPremierNiveau.*;

public class GestionnaireFichier {

    public final static String NOM_DEFAUT_FICHIER_CONFIG = "/fichier_configuration.csv";
    private final static String SEPARATION = ",";
    private final static String NOUVELLE_LIGNE = ";";

    /**
     * Recupere le contenu d'un fichier en local et le transforme en chaine de caractere
     * @param path le chemin vers le fichier
     * @return le contenu du fichier sous forme de chaine de caractere
     * @throws FileNotFoundException si le fichier n'est pas trouvé
     */
    public static String readFile(String path) throws FileNotFoundException {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
            br.close();
        } catch(Exception e) {
            throw new FileNotFoundException();
        }
        return result;
    }

    /**
     * Recupere le contenu d'un fichier stocké dans les assets
     * @param filename le nom du fichier
     * @return le contenu du fichier
     * @throws FileNotFoundException si le fichier n'est pas trouvé
     */
    public static String readFileFromAssets(String filename) throws FileNotFoundException {
        return readFile("assets/" + filename);
    }

    /**
     * Parse le fichier json des stations de vélos
     * @return la liste des stations de vélos parsées
     */
    public static ArrayList<StationVelo> parserFichier() {

        ArrayList<StationVelo> stationsVelo = new ArrayList<>();
        try {
            String jsonData = readFileFromAssets("velib.json");
            JSONObject jobj = new JSONObject(jsonData);
            JSONArray jarr = new JSONArray(jobj.getJSONArray("main").toString());

            for(int i = 0; i < jarr.length(); i++) {
                JSONObject element = jarr.getJSONObject(i);

                int number = element.getInt("number");
                String nom = element.getString("name");
                int bikeStands = element.getInt("bike_stands");
                String address = element.getString("address");
                int availableBikes = element.getInt("available_bikes");
                int availableBikeStands = element.getInt("available_bike_stands");

                JSONObject position = element.getJSONObject("position");
                Double lng = position.getDouble("lng");
                Double lat = position.getDouble("lat");
                StationVelo.Position positionStation = new StationVelo.Position(lng, lat);

                stationsVelo.add(new StationVelo(nom, number, bikeStands, address, positionStation, availableBikes, availableBikeStands));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        stationsVelo.sort((lhs, rhs) -> lhs.getNumber() < rhs.getNumber() ? -1 : 1);
        return stationsVelo;
    }

    public static ArrayList<ScenarioVLS> parserScenars() {

        ArrayList<ScenarioVLS> scenarii = new ArrayList<>();
        File folder = new File("assets/scenarios");
        for(File scenar : folder.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".json") && name.startsWith("scen"))
				{
					return true;
				}
				return false;
			}
		})) 
        {
	        try {
	        	String jsonData = readFile(scenar.getAbsolutePath());
	        	HashMap<Integer, HashMap<Integer, Integer>> Bs = new HashMap<>();
	            JSONObject jobj = new JSONObject(jsonData);
	            Iterator<String> i = jobj.keys();
	            while(i.hasNext())
	            {
	            	String stationOri = i.next();
	            	HashMap<Integer, Integer> b = new HashMap<>();
	            	JSONObject oriJson = (JSONObject) jobj.get(stationOri);
	            	Iterator<String> j = oriJson.keys();
	            	while(j.hasNext())
	            	{
	            		String v = j.next();
	            		b.put(Integer.parseInt(v), oriJson.getInt(v));
	            	}
	            	Bs.put(Integer.parseInt(stationOri), b);
	            }
	            scenarii.add(new ScenarioVLS(Bs));
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
        }
        return scenarii;
    }

    /**
     * Créé un fichier de configuration
     * @param numeroStations les numeros des stations
     * @param cheminFichier le chemin vers ou creer le fichier
     * @return true si succes, false sinon
     */
    public static boolean creerFichierConfiguration(ArrayList<Integer> numeroStations, String cheminFichier) {
        PrintWriter pw = null;
        ArrayList<String> couts = new ArrayList<>();
        couts.add(varC.nom);
        couts.add(varV.nom);
        couts.add(varW.nom);
        int min = 100, max = 8000;
        try {
            pw = new PrintWriter(cheminFichier + NOM_DEFAUT_FICHIER_CONFIG, "UTF-8");
            StringBuilder sb = new StringBuilder();
            sb.append("number");
            for(String cout : couts) {
                sb.append(SEPARATION);
                sb.append(cout);
            }
            sb.append(NOUVELLE_LIGNE + '\n');

            for(Integer numero : numeroStations) {

                sb.append(numero);

                for(int i = 0; i < couts.size(); i++) {
                    sb.append(SEPARATION);
                    sb.append(ThreadLocalRandom.current().nextInt(min, max + 1));
                }
                sb.append(NOUVELLE_LIGNE + '\n');
            }

            pw.write(sb.toString());
            pw.close();
            System.out.println("done!");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Parse le fichier de configuration
     * @param chemin le chemin vers le fichier de config
     * @return les stations et les couts parsés
     */
    public static HashMap<Integer, ArrayList<Integer>> parserFichierConfiguration(String chemin) throws Exception {
        HashMap<Integer, ArrayList<Integer>> stations = new HashMap<>();
        try {
            String csvFile = readFile(chemin);
            String[] csvLignes = csvFile.split(NOUVELLE_LIGNE);
            for (int l = 1; l < csvLignes.length; l++) {
                ArrayList<Integer> couts = new ArrayList<>();
                String[] elements = csvLignes[l].split(SEPARATION);
                for (int i = 1; i < elements.length; i++) {
                    couts.add(Integer.valueOf(elements[i]));
                }
                if (elements.length > 0) {
                    stations.put(Integer.valueOf(elements[0]), couts);
                }
            }
        } catch (Exception ne) {
            throw new Exception();
        }
        return stations;
    }



}
