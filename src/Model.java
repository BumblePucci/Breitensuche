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

    public List<Planes> pList = new ArrayList<>();
    /*List<Planes> pWarteList = new ArrayList<Planes>();
    List<Planes> pExistList = new ArrayList<Planes>();
    */

    //public Planes planes = new Planes(new String[5], 0);

    public List<Generators> gList = new ArrayList<Generators>();
    public Map<String,Nodes> nMap = new HashMap<>();
    public Map<String,Nodes> nnMap = new HashMap<>();



    JSONObject load_json_file(String path) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(path)));
        JSONObject object=new JSONObject(jsonString);
        read_maxplanes(object);
        read_planes(object);
        read_nodes(object);
        read_generators(object);
        return (new JSONObject(jsonString));
    }

    public int read_maxplanes(JSONObject JSONInt){
        int maxplanes = JSONInt.getInt("maxplanes");
        System.out.println(maxplanes);
        return maxplanes;
    }

    public void read_planes(JSONObject jsonObject){
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

    public void read_generators(JSONObject jsonObject){
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


    public void read_nodes(JSONObject jsonObject){
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

            //extract to from node
            JSONArray to = node.getJSONArray("to");
            System.out.println(to);
            List<String> ato = new ArrayList<>();
            for (int j=0; j<to.length(); j++){
                ato.add((String) to.get(j));
            }

            //extract conflicts from node

            JSONArray conflicts = new JSONArray();
            List<String> aconflicts = new ArrayList<>();
            if(node.has("conflicts")) {
                node.getJSONArray("conflicts");

                for (int j=0; j<conflicts.length(); j++) {
                    aconflicts.add((String) conflicts.get(j));
                }
            }

            double waittime = 0.0;
            if(node.has("waittime")){
                waittime = node.getDouble("waittime");
            }

            String targettype = "";
            if(node.has("targettype")) {
                targettype = node.getString("targettype");
            }

            //System.out.println(to.get(0)+" "+to.get(to.length()-1));

            //füge neue Nodeobjekte mit den von JSON übergebenen Attributen der Arraylist hinzu
            //Nodes anode = new Nodes(x, y, name, kind, ato);
            /*Nodes bnode = new Nodes(x, y, name, kind, ato, aconflicts);
            Nodes cnode = new Nodes(x, y, name, kind, ato, waittime);
            */
            //Nodes dnode = new Nodes(x, y, name, kind, ato, targettype);
            /*Nodes enode = new Nodes(x, y, name, kind, ato, aconflicts, waittime);
            Nodes fnode = new Nodes(x, y, name, kind, ato, aconflicts, targettype);
            Nodes gnode = new Nodes(x, y, name, kind, ato, waittime, targettype);*/

            Nodes hnode = new Nodes(x, y, name, kind, ato, aconflicts, waittime, targettype);

            nMap.put(name, hnode);
            /*nMap.put(name, bnode);
            nMap.put(name, cnode);
            nMap.put(name, dnode);
            nMap.put(name, enode);
            nMap.put(name, fnode);
            nMap.put(name, gnode);
            nMap.put(name, hnode);
            */
            nnMap.put(name, hnode);

        }
    }

    public void move(){

        /*for (Planes p : pList){
            p.setNodesList((List<Nodes>) nMap);
            p.getNodesList().add(nMap.targettype);
        }*/

        /*for (String name : nMap.keySet()){
            Nodes nodes = nMap.get(name);
            for (Planes p : pList) {
                p.setPx(30*nodes.getX()+300);
                p.setPy(30*nodes.getY()+300);

                for (int i = 0; i < nodes.getTo().size(); i++) {
                    String keyName = nodes.getTo().get(i);
                    Nodes nachbarn = nnMap.get(keyName);
                    p.setPx(30*nachbarn.getX()+290);
                    p.setPy(30*nachbarn.getY()+290);
                    name = keyName;
                    //gc.strokeLine(30*nodes.getX()+305, 30*nodes.getY()+305, 30*nachbarn.getX()+305,30*nachbarn.getY()+305);
                }
            }
        }*/
        setChanged();
        notifyObservers();
    }



    /*public void beispielmethode(){
        System.out.println("Beispielmethode");
        setChanged();
        notifyObservers();
    }*/



    public void update(){
        //Methoden, die immer ausgeführt werden sollen.
        this.move();
    }
}
