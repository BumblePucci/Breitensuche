import org.JSONArray;
import org.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Model extends Observable { //Enspricht dem gesamten Flughafen und -raum

    public List<Planes> pWarteList = new ArrayList<Planes>();
    public List<Planes> pExistList = new ArrayList<Planes>();


    //public Planes planes = new Planes(new String[5], 0);

    public List<Generators> gList = new ArrayList<Generators>();
    public Map<String,Nodes> nMap = new HashMap<>();
    public Map<String,Nodes> nachbarnNMap = new HashMap<>();
    public int maxplanes;

    public double bigTick = 1;
    public double partTick = bigTick/30.0;
    private int ticks = 0;

    void load_json_file(String path) throws IOException {
        String jsonString = new String(Files.readAllBytes(Paths.get(path)));
        JSONObject object=new JSONObject(jsonString);
        read_maxplanes(object);
        read_planes(object);
        read_nodes(object);
        read_generators(object);
    }

    public void read_maxplanes(JSONObject JSONInt){
        int maxplanes = JSONInt.getInt("maxplanes");
        System.out.println(maxplanes);
        this.maxplanes = maxplanes;
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
            ArrayDeque<String> awaypoints = new ArrayDeque<>();

            for (int j = 0; j < waypoints.length(); j++) {
                awaypoints.add((String) waypoints.get(j));
            }
            System.out.println(waypoints.get(0) + " " + waypoints.get(waypoints.length() - 1));

            //extract inittime from plane:
            int initTime = plane.getInt("inittime");
            System.out.println(initTime);

            //füge neue Planeobjekte mit den von JSON übergebenen Attributen der Arraylist hinzu
            Planes aplane = new Planes(awaypoints, initTime);
            pWarteList.add(aplane);

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
            ArrayDeque<String> awaypoints = new ArrayDeque<>();

            for (int j=0; j<waypoints.length(); j++) {
                awaypoints.add((String) waypoints.get(j));
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
            List<String> ato = new ArrayList<String>();
            for (int j=0; j<to.length(); j++){
                ato.add((String) to.get(j));
            }

            //extract conflicts from node

            JSONArray conflicts = new JSONArray();
            List<String> aconflicts = new ArrayList<>();
            if(node.has("conflicts")) {
                conflicts = node.getJSONArray("conflicts");
                System.out.println("conflicts:" + conflicts);

                for (int j=0; j<conflicts.length(); j++) {
                    aconflicts.add((String) conflicts.get(j));
                }
            }

            double waittime = 0.0;
            if(node.has("waittime")){
                waittime = node.getDouble("waittime");
                System.out.println("waittime = " + waittime);
            }

            String targettype = "";
            if(node.has("targettype")) {
                targettype = node.getString("targettype");
                System.out.println("targettype: " + targettype);
            }

            //System.out.println(to.get(0)+" "+to.get(to.length()-1));


            //füge neue Nodeobjekte mit den von JSON übergebenen Attributen der Arraylist hinzu
            //Nodes hnode = new Nodes(x, y, name, kind, ato);
            /*Nodes bnode = new Nodes(x, y, name, kind, ato, aconflicts);
            Nodes cnode = new Nodes(x, y, name, kind, ato, waittime);
            Nodes dnode = new Nodes(x, y, name, kind, ato, targettype);
            Nodes enode = new Nodes(x, y, name, kind, ato, aconflicts, waittime);
            Nodes fnode = new Nodes(x, y, name, kind, ato, aconflicts, targettype);
            Nodes gnode = new Nodes(x, y, name, kind, ato, waittime, targettype);
            */
            Nodes anode = new Nodes(x, y, name, kind, ato, aconflicts, waittime, targettype);

            nMap.put(name, anode);
            nachbarnNMap.put(name, anode);

        }
    }

    //Breitensuche
    public void breadthSearch(Planes plane) {
        plane.getWaypoints().removeFirst();
        Nodes current = plane.getCurrentNode(); //die aktuelle node auf der sich das plane befindet
        String target = plane.getWaypoints().peekFirst(); //der waypoint zu dem wir wollen
        List<Nodes> nodeList = new ArrayList<>(); //Liste der Node-Reihenfolge für die Planes
        List<List<String>> currentPaths = new ArrayList<>();//Wird mit allen möglichen Pfaden befüllt
        List<String> startNode = new ArrayList<>();//um current Node als String in die Liste zu setzen
        startNode.add(current.getName());//current Node name als String (damit es in die currentPaths Liste geschrieben werden kann)
        currentPaths.add(startNode);


        //geht alle wege durch und speichert jeden möglichen Weg in Listen bis der targetnode gefunden wurde
        while (!current.getTargettype().equals(target)) {
            boolean found = false;
            List<List<String>> newPaths = new ArrayList<>();
            for (List<String> currentPath : currentPaths) {//Damit currentPath bei jedem durchlauf die Listen der currentPaths erhaelt
                List<String> getTo = nMap.get(currentPath.get(currentPath.size() - 1)).getTo(); //Naechst moegliche Nodes (nachbaren) abrufen
                if (getTo.isEmpty()) {
                    currentPath.remove(currentPath.size() - 1);//loesche Letzte Stelle in der currentPath Liste, falls kein Nachbar vorhanden
                    break;//bricht aktuelle Schleife ab (Fehler Ausflug vermeiden)
                }

                for (String node : getTo) {//jedes Elemnt in getTo wird beobachtet
                    if (nMap.get(node).isVisited()) {
                        continue;//geh zum naechsten Schleifendurchgang
                    }
                    List<String> newPath = new ArrayList<>(currentPath);//kopie der currentPath Liste
                    newPath.add(node);//nachbar hinzufuegen

                    //setzt alle Nodes aus new Path auf Visited - true
                    for (String nodeName : newPath) {
                        nMap.get(nodeName).setVisited(true);
                    }
                    newPaths.add(newPath);//schreibt currentPath+nachbar Liste in newPaths
                    if (nMap.get(node).getTargettype().equals(target)) {
                        current = nMap.get(node);//setze current auf die aktuelle Node - damit while Schleife endet
                        currentPaths = new ArrayList<>(newPaths);//weist currentPaths alle Möglichen Wege zu die in newPaths gespeichert sind
                        found = true;
                        break;

                    }
                }
                //damit keine weiteren Listen mehr gesucht (und ueberschrieben) werden
                if (!found) {
                    currentPaths = new ArrayList<>(newPaths);
                }

            }
        }

        //setzt Nodes wieder auf false fuer naechsten Durchgang
        for (Nodes n : nMap.values()) {
            n.setVisited(false);
        }
        List<String> nodeNames = new ArrayList<>(currentPaths.get(currentPaths.size() - 1));//nodeName bekommt die zu letzt gefundene Liste

        //Macht String Liste zur Node Liste
        for (String nodeName : nodeNames) {
            nodeList.add(nMap.get(nodeName));
        }
        System.out.print("NodeList:");
        for (Nodes n : nodeList) {
            System.out.print(n.getName() + ",");//Namen der Node Liste ausgeben
        }
        System.out.println("");
        plane.setCurrentNode(nodeList.get(0));//Plane bekommt als currentNode die Letzte Stelle der Liste
        plane.setNextNode(nodeList.get(1));
        plane.setNodesList(nodeList);//weist dem Plane die fertige Node Liste zu
    }

    //Todo Neu
    //Generiert zufällig neue Flugzeuge und ordnet Sie ein in die Warteliste
    public void generatorFlugzeuge() {
        for (int i = 0; i <= gList.size(); i++) {
            Random ranNum = new Random();
            double random = ranNum.nextDouble();
                if (gList.get(i).getChance() >= random) {
                    Planes flieger = new Planes(gList.get(i).getWaypoints(),0);
                    pWarteList.add(flieger);
                }
        }
    }

    //todo methode die inittime der wartelisten Flugzeuge prüft und SIe dann aufs feld schickt
    public void flugzeugeStarten(){
        if(!pWarteList.isEmpty()){ // testet ob wartende Flugzeuge vorhanden
            for(int i = 0; i < pWarteList.size(); i++){
                //testet ob wartendes Flugzeug auf Spielfeld darf
                if (pWarteList.get(i).getInittime() <= ticks && pExistList.size() < maxplanes) {
                    pExistList.add(pWarteList.get(i));
                    pWarteList.remove(i);
                }
            }
        }
    }


    public double getMinX() {
        double minX = 0;
        for (String name1 : nMap.keySet()){
            Nodes n1 = nMap.get(name1);
            for (String name2 : nMap.keySet()){
                Nodes n2 = nMap.get(name2);
                if (n2.getX()<n1.getX()){
                    minX=n2.getX();
                }
            }
        }
        return minX;
    }

    /*public double getMinY(){
        double minY = 0;
        for (int j=0; j<nMap.keySet().size(); j++){
            if(j==0){
                minY = nMap.keySet();
            }
        }
    }*/

    public double getMinY(){
        double minY = 0;
        for (String name1 : nMap.keySet()){
            Nodes n1 = nMap.get(name1);
            for (String name2 : nMap.keySet()){
                Nodes n2 = nMap.get(name2);
                if (n2.getY()<n1.getY()){
                    minY=n2.getY();
                }
            }
        }
        return minY;
    }

    public double getMaxX(){
        double maxX = 0;
        for (String name1 : nMap.keySet()) {
            Nodes n1 = nMap.get(name1);
            for (String name2 : nMap.keySet()) {
                Nodes n2 = nMap.get(name2);
                if (n2.getX()>n1.getX()){
                    maxX=n2.getX();
                }
            }
        }
        return maxX;
    }

    public double getMaxY() {
        double maxY = 0;
        for (String name1 : nMap.keySet()) {
            Nodes n1 = nMap.get(name1);
            for (String name2 : nMap.keySet()) {
                Nodes n2 = nMap.get(name2);
                if (n2.getY() > n1.getY()) {
                    maxY=n2.getY();
                }
            }
        }
        return maxY;
    }


    public void moveFromNodeToNode(){
        for (Planes p : pExistList) {
            if (p.getNodesList().size()!=0) {
                p.setCurrentNode(p.getNodesList().get(0));
                p.setNextNode(p.getNodesList().get(1));
                p.getNodesList().remove(0);
            }
            else{
                if (!(p.getWaypoints().isEmpty())) {
                    breadthSearch(p);
                }
                else {
                    pExistList.remove(p);
                }
            }
        }
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
        this.ticks++;// Zählt wie oft schon aufgerufen wurde.
        generatorFlugzeuge();    //erstellt die Warteliste
        flugzeugeStarten(); // Schreibt Wartende Flugzeuge in die existliste wenn iniTime groß genug is.
        this.moveFromNodeToNode();
        System.out.println("MinX: "+getMinX());
        System.out.println("MaxX: "+getMaxX());
        System.out.println("MinY: "+getMinY());
        System.out.println("MaxY: "+getMaxY());
    }
}
