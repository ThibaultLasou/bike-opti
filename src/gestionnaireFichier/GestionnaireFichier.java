package gestionnaireFichier;

import cplex.StationVelo;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

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

}
