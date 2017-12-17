import org.JSONArray;
import org.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Model extends Observable {

    public List<Planes> pWarteList = new ArrayList<Planes>();
    public List<Planes> pExistList = new ArrayList<Planes>();
    public List<Planes> pFertigList = new ArrayList<Planes>();

    public List<Generators> gList = new ArrayList<Generators>();

    public Map<String,Nodes> nMap = new HashMap<>();
    public Map<String,Nodes> nachbarnNMap = new HashMap<>();

    public int maxplanes;

    public double bigTick = 1;
    public double partTick = bigTick/30.0;
    private int ticks = 0;
    private int maxInit = 0;

    //JSON-File wird eingelesen
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
        this.maxplanes = maxplanes;
    }

    public void read_planes(JSONObject jsonObject){
        //Get planes from JSONObject, für alles JSON KLassen
        if (jsonObject.has("planes")) {
            JSONArray planes = jsonObject.optJSONArray("planes");

            //Iterate over all planes
            for (int i = 0; i < planes.length(); i++) {

                //description of one plane
                JSONObject plane = planes.getJSONObject(i);

                //extract waypoints from plane:
                JSONArray waypoints = plane.getJSONArray("waypoints");
                ArrayDeque<String> awaypoints = new ArrayDeque<>();

                for (int j = 0; j < waypoints.length(); j++) {
                    awaypoints.add((String) waypoints.get(j));
                }

                //extract inittime from plane:
                int initTime = plane.getInt("inittime");

                //füge neue Planesobjekte mit den von JSON übergebenen Attributen der Arraylist hinzu
                Planes aplane = new Planes(awaypoints, initTime);
                pWarteList.add(aplane);

            }
        }
    }

    public void read_generators(JSONObject jsonObject){
        //Get generators from JSONObject, für alles JSON KLassen
        JSONArray generators = jsonObject.optJSONArray("generators");
        //Iterate over all generators
        for (int i=0; i<generators.length(); i++){

            //description of one generator
            JSONObject generator = generators.getJSONObject(i);

            //extract waypoints from generator:
            JSONArray waypoints = generator.getJSONArray("waypoints");
            ArrayDeque<String> awaypoints = new ArrayDeque<>();

            for (int j=0; j<waypoints.length(); j++) {
                awaypoints.add((String) waypoints.get(j));
            }

            //extract chance from generator:
            double chance = generator.getDouble("chance");
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

            //extract x from node:
            double x = node.getDouble("x");

            //extract y from node
            double y = node.getDouble("y");

            //extract name from node
            String name = node.getString("name");

            //extract kind from node
            String kind = node.getString("kind");

            //extract to from node
            JSONArray to = node.getJSONArray("to");
            List<String> ato = new ArrayList<String>();
            for (int j=0; j<to.length(); j++){
                ato.add((String) to.get(j));
            }

            //extract conflicts from node
            JSONArray conflicts = new JSONArray();
            List<String> aconflicts = new ArrayList<>();
            if(node.has("conflicts")) {
                conflicts = node.getJSONArray("conflicts");

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

            Nodes anode = new Nodes(x, y, name, kind, ato, aconflicts, waittime, targettype);

            nMap.put(name, anode);
            nachbarnNMap.put(name, anode);

        }
    }

    //Breitensuche
    public void breadthSearch(Planes plane) {
        String firstWaypoint = plane.getWaypoints().removeFirst();
        if (plane.getWaypoints().isEmpty()) {
            return;
        }
        Nodes tmp = null;
        if (plane.getCurrentNode() == null) {
            List<Nodes> startNodes = new ArrayList<>();
            for (Nodes n : nMap.values()) {
                if (n.getTargettype().equals(firstWaypoint)) {
                    startNodes.add(n);

                }
            }
            Random randomNodeNr = new Random();
            tmp = startNodes.get(randomNodeNr.nextInt(startNodes.size()));
        }
        Nodes current = (plane.getCurrentNode() != null) ? plane.getNodesList().get(0) : tmp;//die aktuelle node auf der sich das plane befindet
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
                    continue;//bricht aktuelle Schleife ab (Fehler Ausflug vermeiden)
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
        plane.setNodesList(new ArrayList<>(nodeList));//weist dem Plane die fertige Node Liste zu
    }
    // Sucht Größte initTime der Planes
    public void maxInit (){
     if(!pWarteList.isEmpty()){
         for(int i = 0; i < pWarteList.size(); i++){
             if(maxInit < pWarteList.get(i).getInittime()){
                 maxInit = pWarteList.get(i).getInittime();
             }
         }
     }
    }

    //Generiert zufällig neue Flugzeuge und ordnet Sie ein in die Warteliste
    public void generatorFlugzeuge() {
        for (int i = 0; i < gList.size(); i++) {
            maxInit();
            Random ranNum = new Random();
            double random = ranNum.nextDouble(); //generiert zufaellige Zahl zwischen 0 und 1
                if (gList.get(i).getChance() >= random) {
                    // Erstellt neues Plane und ordnet es in Warteliste ein
                    Planes flieger = new Planes(new ArrayDeque<>(gList.get(i).getWaypoints()), maxInit);
                    pWarteList.add(flieger);
                }
        }
    }

    //methode die inittime der wartelisten Flugzeuge prüft und Sie dann aufs feld schickt
    public void flugzeugeStarten(){
        if(!pWarteList.isEmpty()){ // testet ob wartende Flugzeuge vorhanden
            for(int i = 0; i < pWarteList.size(); i++){
                //testet ob wartendes Flugzeug auf Spielfeld darf
                if (pWarteList.get(i).getInittime() <= ticks && pExistList.size() <= maxplanes) {
                    pExistList.add(pWarteList.get(i));
                    pWarteList.remove(i);
                }
            }
        }
    }

//Getter für Extremwerte der Koordinaten der Nodes
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

    //Geht durch die existierenden Planes, setzt zum gegebenen Zeitpunkt Current- und NextNodes und führt die Breitensuche aus
    public void moveFromNodeToNode(){
        for (Planes p : pExistList) {
            if (p.getNodesList().size() > 1) {
                p.setCurrentNode(p.getNodesList().get(0));
                p.setNextNode(p.getNodesList().get(1));
                p.getNodesList().remove(0);
            }
            else{
                if (!(p.getWaypoints().isEmpty())) {
                    breadthSearch(p);
                    if (p.getNodesList().size() > 1) {
                        p.getNodesList().remove(0);
                    }

                }
                else {
                   pFertigList.add(p);

                    //pExistList.remove(p);
                }
            }
        }
        //Todo löschen aus pExistList
        if(!pFertigList.isEmpty()) {
            for (int i = 0; i < pFertigList.size(); i++) {
                pExistList.remove(pFertigList.get(i));
            }
            pFertigList.clear();
        }
        //setChanged();
        //notifyObservers();
    }

    public void update(){
        //Methoden, die immer ausgeführt werden sollen.
        ticks++;// Zählt wie oft schon aufgerufen wurde.
        generatorFlugzeuge();    //erstellt die Warteliste
        flugzeugeStarten(); // Schreibt Wartende Flugzeuge in die existliste wenn iniTime groß genug ist.
        moveFromNodeToNode();
        System.out.println("Tick"+ticks);
    }
}
