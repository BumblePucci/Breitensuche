import javafx.scene.transform.Scale;
import org.JSONArray;
import org.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Model extends Observable { //Enspricht dem gesamten Flughafen und -raum

    static List<Planes> pList = new ArrayList<Planes>();
    /*List<Planes> pWarteList = new ArrayList<Planes>();
    List<Planes> pExistList = new ArrayList<Planes>();
    */
    static List<Generators> gList = new ArrayList<Generators>();
    static Map<String,Nodes> nMap = new HashMap<>();
    public void identify(){
        //TODO: etwas mit put um die Schlüsselnamen auf die anderen Attribute zu setzten
    }

    static JSONObject load_json_file(String path) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(path)));
        return (new JSONObject(jsonString));
    }

    static int read_maxplanes(JSONObject JSONInt){
        int maxplanes = JSONInt.getInt("maxplanes");
        System.out.println(maxplanes);
        return maxplanes;
    }

    static void read_panes(JSONObject jsonObject){
        //Get planes from JSONObject, für alles JSON KLassen
        JSONArray planes = jsonObject.optJSONArray("planes");
        //Iterate over all planes
        for (int i=0; i<planes.length(); i++){

            //description of one plane
            JSONObject plane = planes.getJSONObject(i);
            System.out.println(plane);

            //extract waypoints from plane:
            JSONArray waypoints = plane.getJSONArray("waypoints");
            String[] awaypoints = new String[waypoints.length()];

            for (int j=0; j<waypoints.length(); j++) {
                awaypoints[j] = (String) waypoints.get(j);
            }
            System.out.println(waypoints.get(0) + " " + waypoints.get(waypoints.length() - 1));

            //extract inittime from plane:
            int initTime = plane.getInt("inittime");
            System.out.println(initTime);

            //füge neue Planeobjekte mit den von JSON übergebenen Attributen der Arraylist hinzu
            Planes aplane = new Planes(awaypoints, initTime);
            pList.add(aplane);


        }
    }

    static void read_generators(JSONObject jsonObject){
        //Get generators from JSONObject, für alles JSON KLassen
        JSONArray generators = jsonObject.optJSONArray("generators");
        //Iterate over all generators
        for (int i=0; i<generators.length(); i++){

            //description of one generator
            JSONObject generator = generators.getJSONObject(i);
            System.out.println(generator);

            //extract waypoints from generator:
            JSONArray waypoints = generator.getJSONArray("waypoints");
            String[] awaypoints = new String[waypoints.length()];

            for (int j=0; j<waypoints.length(); j++) {
                awaypoints[j] = (String) waypoints.get(j);
            }
            System.out.println(waypoints.get(0) + " " + waypoints.get(waypoints.length() - 1));

            //extract chance from generator:
            double chance = generator.getInt("chance");
            System.out.println(chance);

            //füge neue Generatorobjekte mit den von JSON übergebenen Attributen der Arraylist hinzu
            Generators agenerator = new Generators(chance, awaypoints);
            gList.add(agenerator);
        }
    }


    static void read_nodes(JSONObject jsonObject){
        //Get nodes from JSONObject, für alles JSON KLassen
        JSONArray nodes = jsonObject.optJSONArray("nodes");
        //Iterate over all nodes
        for (int i=0; i<nodes.length(); i++){

            //description of one node
            JSONObject node = nodes.getJSONObject(i);
            System.out.println(node);

            //extract x from node:
            double x = node.getDouble("x");
            System.out.println(x);

            //extract y from node
            double y = node.getDouble("y");
            System.out.println(y);

            //extract name from node
            String name = node.getString("name");
            System.out.println(name);

            //extract kind from node
            String kind = node.getString("kind");
            System.out.println(kind);

            //extract to from node:
            JSONArray to = node.getJSONArray("to");
            System.out.println(to);
            List<String> ato = new ArrayList<String>();
            for (int j=0; j<to.length(); j++){
                ato.add((String) to.get(j));
            }
            //System.out.println(to.get(0)+" "+to.get(to.length()-1));

            //füge neue Nodeobjekte mit den von JSON übergebenen Attributen der Arraylist hinzu
            Nodes anode = new Nodes(x, y, name, kind, ato);
            nMap.put(name, anode);

        }
    }



    /*public void beispielmethode(){
        System.out.println("Beispielmethode");
        setChanged();
        notifyObservers();
    }*/



    /*public void update(){
        System.out.println("Methoden, die immer ausgeführt werden sollen.");
    }*/
}
