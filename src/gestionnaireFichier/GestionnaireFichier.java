package gestionnaireFichier;

import jdk.nashorn.internal.parser.JSONParser;
import vls.StationVelo;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class GestionnaireFichier {

    public static String readFileFromAssets(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("assets/" + filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<StationVelo> parserFichier() {

        String jsonData = readFileFromAssets("velib.json");
        System.out.println(jsonData);
        JSONObject jobj = new JSONObject(jsonData);
        JSONArray jarr = new JSONArray(jobj.getJSONArray("main").toString());

        ArrayList<StationVelo> stationsVelo = new ArrayList<>();

        for(int i = 0; i < jarr.length(); i++) {
            JSONObject element = jarr.getJSONObject(i);

            int number = element.getInt("number");
            int bikeStands = element.getInt("bike_stands");
            String address = element.getString("address");
            int availableBikes = element.getInt("available_bikes");

            JSONObject position = element.getJSONObject("position");
            Double lng = position.getDouble("lng");
            Double lat = position.getDouble("lat");
            StationVelo.Position positionStation = new StationVelo.Position(lng, lat);

            stationsVelo.add(new StationVelo(number, bikeStands, address, positionStation, availableBikes));
        }
        System.out.println(stationsVelo);
        return stationsVelo;
    }

    public static void creerFichierConfiguration(ArrayList<Integer> numeroStations, String cheminFichier) {
        PrintWriter pw = null;
        ArrayList<String> couts = new ArrayList<>();
        couts.add("c");
        couts.add("v");
        couts.add("w");
        couts.add("k");
        int min = 100, max = 8000;
        try {
            pw = new PrintWriter(cheminFichier + "/fichier_configuration.csv", "UTF-8");
            StringBuilder sb = new StringBuilder();
            sb.append("number");
            for(String cout : couts) {
                sb.append(',');
                sb.append(cout);
            }
            sb.append('\n');

            for(Integer numero : numeroStations) {

                sb.append(numero);

                for(int i = 0; i < couts.size(); i++) {
                    sb.append(',');
                    sb.append(ThreadLocalRandom.current().nextInt(min, max + 1));
                }
                sb.append('\n');
            }

            pw.write(sb.toString());
            pw.close();
            System.out.println("done!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
