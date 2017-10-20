package ia.volleyball;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

public class SearchMethods {

    private FilesManager fm;

    public SearchMethods(FilesManager fm) {
        this.fm = fm;
    }

    /**
     * Calcula la ruta de un origen a un destino, siguiendo el algoritmo primero en anchura
     *
     * @param r
     * @param node_name
     */
    public void BreadthFirst(Register r, String node_name) {
        Tree T;
        ArrayList<Node> S;
        ArrayList<Node> explored;
        Node node;
        boolean fail = false;
        boolean repeated = false;
        T = new Tree(r);
        if (T.root.data.getCity().trim().equals(node_name)) {
            System.out.println("Solucion: " + T.root.data.getCity().trim());
            return;
        }
        S = new ArrayList<>();
        S.add(T.root);
        explored = new ArrayList<>();
        while (!fail) {
            if (S.isEmpty()) {
                System.out.println("Se recorrieron todos los nodos");
                fail = true;
            } else {
                node = S.get(0);
                S.remove(0);
                explored.add(node);
                for (int i = 0; i < fm.getReg().connected_key.length; i++) {
                    if (!fm.getReg().connected_key[i].trim().equals("NULL")) {
                        for (Node anExplored : explored) {
                            if (fm.getReg().connected_key[i].trim().equals(anExplored.data.getKey().trim())) {
                                repeated = true;
                            }
                        }
                        if (!repeated) {
                            T.addChild(node, fm.getReg().getConnectedData(i, this.fm));
                            if (fm.getCityName(fm.getReg().connected_key[i].trim()).equals(node_name)) {
                                T.getPathToRoot(fm.getReg().connected_key[i].trim());
                                return;
                            } else {
                                System.out.println("CC>" + fm.getCityName(fm.getReg().connected_key[i].trim()));
                                S.add(T.getPointer().node_childs.get(fm.getReg().connected_key[i].trim()));
                            }
                        }
                        repeated = false;
                    }
                }
                try {
                    fm.getRegisterFromFile(S.get(0).data.getKey(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Calcula la ruta de un origen a un destino, siguiendo el algoritmo primero en profundidad
     *
     * @param r
     * @param node_name
     */
    public void DepthFirst(Register r, String node_name) {
        Tree T;
        ArrayList<Node> W;
        ArrayList<Node> explored;
        String less_value = null;
        int index = 0;
        boolean repeated = false;
        T = new Tree(r);
        if (T.root.data.getCity().trim().equals(node_name)) {
            System.out.println("Solucion: " + T.root.data.getCity().trim());
            return;
        }
        W = new ArrayList<>();
        W.add(T.root);
        explored = new ArrayList<>();
        explored.add(T.root);
        while (true) {
            //Step 2
            for (int i = 0; i < fm.getReg().connected_key.length; i++) {
                if (!fm.getReg().connected_key[i].trim().equals("NULL")) {
                    for (Node node : explored) {
                        if (fm.getReg().connected_key[i].trim().equals(node.data.getKey().trim())) {
                            repeated = true;
                        }
                    }
                    if (!repeated) {
                        if (fm.getCityName(fm.getReg().connected_key[i].trim()).equals(node_name)) {
                            T.addChild(T.getPointer(), fm.getReg().getConnectedData(i,this.fm));
                            T.getPathToRoot(fm.getCityName(fm.getReg().connected_key[i].trim()).trim());
                            return;
                        } else {
                            if (less_value == null) {
                                less_value = fm.getCityName(fm.getReg().connected_key[i].trim());
                            }
                            if (less_value.compareTo(fm.getCityName(fm.getReg().connected_key[i].trim())) >= 0) {
                                less_value = fm.getCityName(fm.getReg().connected_key[i].trim());
                                index = i;
                            }
                        }
                    }
                }
                repeated = false;
            }
            if (less_value != null) {
                T.addChild(W.get(0), fm.getReg().getConnectedData(index, this.fm));
                W.remove(0);
                W.add(T.getPointer().node_childs.get(less_value));
                explored.add(T.getPointer().node_childs.get(less_value));
                T.setPointer(T.getPointer().node_childs.get(less_value));
                try {
                    fm.getRegisterFromFile(W.get(0).data.getKey(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Step 3
                if (T.root.data.getCity().trim().equals(W.get(0).data.getCity().trim())) {
                    System.out.println("\nEl proceso ha terminado y no se encontro el resultado");
                    return;
                } else {
                    //Step 4
                    W.remove(0);
                    T.setPointer(T.getPointer().parent_node);
                    W.add(T.getPointer());
                    try {
                        fm.getRegisterFromFile(W.get(0).data.getKey(), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            less_value = null;
        }
    }
    /**
     * Calcula la ruta de un origen a un destino por el método primero el mejor
     *
     * @param origin char
     * @param destiny char
     * @return Stack< String >
     * @throws IOException
     */
    public List<String> grafosA(Register origin, String destiny) throws IOException {
        String originKey = origin.getKey();
        String originCity = origin.getCity();
        if (originKey != null) {
            LinkedHashMap<String, Data> open = new LinkedHashMap<>();  //Lista de abiertos
            LinkedHashMap<String, Data> close = new LinkedHashMap<>(); //Lista de cerrados
            List<Data> nodeChildrens;
            Map.Entry<String, Data> ea;
            double g;
            double f;
            double h;
            double oldG;
            int cont = 0;

            open.put(originCity, new Data(originKey, originKey, originCity, "-", 0, 0)); //1. ABIERTOS←(origin); CERRADOS←( );

            while (!open.isEmpty()) {  //2. Si ABIERTOS es la lista vacía, finalizar con fallo.
                //3. EA←primer elemento de ABIERTOS.
                // Eliminar EA de ABIERTOS y llevarlo a CERRADOS.
                ea = moveFirstTo(open, close);

                // 4. Si EA es una meta, fin con éxito. Devolver el camino hasta la meta.
                if (ea.getKey().trim().equals(destiny.trim())) {
                    // Devolver el camino hasta la meta.
                    return getPath(destiny, originCity, ea, open, close, cont);
                }

                //5. Expandir nodo EA, generando todos sus sucesores como hijos de EA.
                originKey = ea.getValue().key;
                nodeChildrens = expandNodeA(originKey);

                for (Data currentNode : nodeChildrens) {               //6. Para cada sucesor de EA
                    g = calculateG(currentNode, ea, open, close);      // a) Calcular g(q)=g(EA)+c(EA,q)
                    if (open.containsKey(currentNode.pCity) || close.containsKey(currentNode.pCity)) {
                        //c) Si q estaba en ABIERTOSo en CERRADOS, comparar el nuevo valor f(q) con el anterior.
                        oldG = getInitToN(currentNode, open, close);
                        if (oldG < g) {
                            h = calculateH(currentNode, ea);  //calcular f(q)=g(q)+h(q),
                            f = g + h;
                            //Si el nuevo es menor, colocar EA como nuevo padre y asignar el nuevo valor f(q).
                            open.replace(currentNode.pCity, new Data(currentNode.pKey, ea.getValue().key, ea.getKey(), currentNode.wire, f, g));
                            if (close.containsKey(currentNode.pKey)) { //Si estaba en CERRADOS
                                open.put(currentNode.pCity, close.get(currentNode.pCity)); //Llevarle a ABIERTOS.
                                close.remove(currentNode.pKey); // Eiminarle de CERRADOS
                            }

                        }   //Si el anterior es menor o igual, descartar el nodo recién generado.

                    } else { //b) Si q no estaba en ABIERTOS ni en CERRADOS,
                        h = calculateH(currentNode, ea);  //calcular f(q)=g(q)+h(q),
                        if (h != 0.0) {
                            cont++;
                        }
                        f = g + h;
                        //añadir q a ABIERTOS como hijo de EA, asignando los valores f(q) y g(q).
                        open.put(currentNode.pCity, new Data(currentNode.pKey, ea.getValue().key, ea.getKey(), currentNode.wire, f, g));

                    }
                }

                // 7. Reordenar ABIERTOSsegún valores crecientes de f.
                open = sortHashAscendingly(open);

            }
        } else {
//            throw new RuntimeException("Llave invalida");
            System.out.println("Nodo Invalido");

        }
        System.out.println("Nodo destino no encontrado");
//        throw new RuntimeException("Nodo no encontrado");
        return new Stack<>();

    }

    /**
     * Calcular g(q)=g(EA)+c(EA,q)
     *
     * @param currentNode
     * @param ea
     * @return int
     */
    private double calculateH(Data currentNode, Map.Entry<String, Data> ea) {
        String nextCable = currentNode.wire;
        String currentCable = ea.getValue().wire;
        if (currentCable.equals("-")) {
            return 0.0;
        } else if (!currentCable.equals(nextCable)) {
            return currentNode.g * 0.3;
        }
        return 0.0;

    }

    /**
     * Calcular h(q)
     *
     * @param currentNode
     * @param ea
     * @param open
     * @param close
     * @return
     */
    private double calculateG(Data currentNode, Map.Entry<String, Data> ea,
                              LinkedHashMap<String, Data> open,
                              LinkedHashMap<String, Data> close) {

        if (open.containsKey(ea.getKey())) {
            return currentNode.g + open.get(ea.getKey()).g;
        } else if (close.containsKey(ea.getKey())) {
            return currentNode.g + close.get(ea.getKey()).g;
        } else {
            return 0;
        }

    }

    /**
     * Obtener una lista de nodos expandidos a partir de un padre
     *
     * @param node char
     * @return List<Data>
     * @throws IOException
     */
    private List<Data> expandNodeA(String node) throws IOException {
        Register register;
        FilesManager data = new FilesManager("master_submarine_cables", "index_submarine_cables");
        List<Data> expandedNode = new ArrayList<>();
        register = data.getRegisterFromFile(node.trim(), false);

        String[] keys = register.connected_key;
        double[] weight = register.weight;
        String[] wire = register.wire_name;
        for (int i = 0; i < register.getCONNECTIONS_NUMBER(); i++) {
            if (!"NULL".equals(keys[i]) && keys[i] != null) {
                expandedNode.add(new Data("", keys[i], fm.getCityName(keys[i]), wire[i], 0.0, weight[i]));
            }
        }
        return expandedNode;
    }

    /**
     * Obtener el costo del nodo inicial hasta el nodo n
     *
     * @param currentNode
     * @param open
     * @param close
     * @return int
     */
    private double getInitToN(Data currentNode,
                              LinkedHashMap<String, Data> open,
                              LinkedHashMap<String, Data> close
    ) {
        if (open.containsKey(currentNode.pCity)) {
            return currentNode.f + open.get(currentNode.pCity).f;
        } else if (close.containsKey(currentNode.pCity)) {
            return currentNode.f + close.get(currentNode.pCity).f;
        } else {
            return 0;
        }

    }

    private List<String> getPath(String destiny, String origin,
                                 Map.Entry<String, Data> ea,
                                 LinkedHashMap<String, Data> open,
                                 LinkedHashMap<String, Data> close, int cont) {
        List<String> path = new Stack<>();

//        path.add(destiny);
        if (cont == 0) {
            path.add("A*");
        } else {
            path.add("A");
        }
        String father = ea.getKey();
        path.add(father);

        while (!path.contains(origin)) {
            if (open.containsKey(father)) {
                father = open.get(father).pCity;
                path.add(father);
            } else if (close.containsKey(father)) {
                father = close.get(father).pCity;
                path.add(father);
            }

        }

        return path;
    }

    /**
     * Método utilizado para ordenar ABIERTA
     *
     * @param map LinkedHashMap<String, Data>
     * @return LinkedHashMap
     */
    private LinkedHashMap sortHashAscendingly(LinkedHashMap map) {
        // Método para ordenar listas de menor a mayor
        Comparator<Map.Entry<String, Data>> valueComparator = (Map.Entry<String, Data> e1, Map.Entry<String, Data> e2) -> {
            Double v1 = e1.getValue().f;
            Double v2 = e2.getValue().f;
            return v1.compareTo(v2);
        };

        // El método de ordenamiento necesita una lista, por lo que primero convertimos Set a una lista en Java
        List<Map.Entry<String, Data>> listOfEntries = new ArrayList<>(map.entrySet());

        // Oredenar la lista usando el comprador anteriormente definido
        Collections.sort(listOfEntries, valueComparator);

        // Copiar las entradas de la lista al HashMap
        LinkedHashMap sortedByValue = new LinkedHashMap<>(listOfEntries.size());
        listOfEntries.forEach((entry) -> {
            sortedByValue.put(entry.getKey(), entry.getValue());
        });

        return sortedByValue;
    }

    /**
     * Sacar un valor de un nodo seleccionado de una lista de nodos
     *
     * @param map HashMap
     * @return Map.Entry
     */
    private Map.Entry pop(HashMap map) {
        try {
            Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
            map.remove(entry.getKey());
            return entry;
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Sacaer un valor de una lista y moverlo a otra (Usado para trabajar con
     * ABIERTA y CERRADA
     *
     * @param from HashMap
     * @param to HashMap
     * @return Map.Entry
     */
    private Map.Entry moveFirstTo(HashMap from, HashMap to) {
        Map.Entry entry = pop(from);
        if (entry != null) {
            to.put(entry.getKey(), entry.getValue());
            return entry;
        }
        return null;

    }

    public List<String> grafosO(Register origin, String destiny) throws IOException {
        String originKey = origin.getKey();
        if (originKey != null) {
            List<Data> abierta = new ArrayList<>();
            Data n;
            double costo;
            HashMap<String, Data> s;
            HashMap<String, Data> tablaA = new HashMap<>();

            abierta.add(new Data(origin.getKey(), origin.getCity())); //1: ABIERTA = (inicial);

            while (!abierta.isEmpty()) {  //2: mientras NoVacía(ABIERTA) hacer
                n = abierta.remove(0); // n=ExtraePrimero(ABIERTA);
                if (n.pCity.trim().equals(destiny.trim())) {  //4: Si EsObjetivo(n) entonces
                    //5: Devolver Camino(inicial,n);
                    return getPathO(tablaA, n.pCity, origin.getCity(), destiny);
                } //6: Fin Fs                    i

                s = expandNodeO(n.pKey.trim()); //7: S=Sucesores(n);
                if (tablaA.containsKey(n.pCity)) {
                    tablaA.get(n.pCity).s = s; //8:Añade S a la entrada de n en la TABLA_A
                } else {
                    tablaA.put(n.pCity, new Data(s, "-", "-", 0)); //8:Añade S a la entrada de n en la TABLA_A
                }

                for (Map.Entry<String, Data> q : s.entrySet()) { //9: Para cada q de S hacer
                    if (tablaA.containsKey(q.getKey())) { //10: si(qє TABLA_A) entonces
                        //11: Rectificar(q,n,Coste(n,q));
                        rectificar(tablaA, n.pCity, q);
                        //12: Ordenar(ABIERTA);{si es preciso

                    } else { //13: sino
                        //14: Coloca q en la TABLA_A con
                        costo = calculateF(tablaA, n.pCity, q); //Coste (inicial,q)= Coste (inicial,n) + Coste(n,q)
                        tablaA.put(q.getKey(), new Data(n.pKey, n.pCity, costo)); //Anterior(q)=n
                        abierta.add(new Data(q.getValue().pKey, q.getKey())); //ABIERTA=Mezclar(q,ABIERTA);
                    }
                }
            }
        }else{
            System.out.println("Nodo Invalido");
        }
        System.out.println("Nodo destino no encontrado");
        return new Stack<>();
    }

    private void rectificar(HashMap<String, Data> tablaA, String n, Map.Entry<String, Data> q) {

        if (tablaA.get(n).f + q.getValue().f < tablaA.get(q.getKey()).f) {
            tablaA.get(q.getKey()).f = tablaA.get(n).f + q.getValue().f;
            tablaA.get(q.getKey()).pCity = n;
            rectificarLista(tablaA, q.getKey());

        }
    }

    /**
     *
     * @param tablaA
     * @param n
     */
    private void rectificarLista(HashMap<String, Data> tablaA, String n) {
        if (tablaA.get(n).s != null) {
            tablaA.get(n).s.entrySet().forEach((q) -> {
                rectificar(tablaA, n, q);
            });
        }

    }

    /**
     * Obtener la ruta del nodo final al nodo inicial
     *
     * @param destiny
     * @param origin
     * @return
     */
    private List<String> getPathO(HashMap<String, Data> tablaA, String n, String origin, String destiny) {
        List<String> path = new Stack<>();
        path.add(destiny);
//        path.add(n);
        String father = n;

        while (!father.trim().equals(origin.trim())) {
            father = tablaA.get(father).pCity;
            path.add(father);
        }

        return path;
    }

    /**
     * Calcula el valor de f siguiendo con > costo(inicial, n) +
     * costo(n,nodo_actual)
     *
     * @param tablaA
     * @param n String
     * @param q Entry< String, Integer >
     * @return
     */
    private double calculateF(HashMap<String, Data> tablaA, String n, Map.Entry<String, Data> q) {
        return tablaA.get(n).f + q.getValue().f;

    }

    /**
     * Obtener una lista de nodos expandidos a partir de un padre
     *
     * @param node
     * @return
     * @throws IOException
     */
    private HashMap expandNodeO(String node) throws IOException {
        Register register;
        FilesManager data = new FilesManager("master_submarine_cables", "index_submarine_cables");
        HashMap<String, Data> expandedNode = new HashMap<>();
        register = data.getRegisterFromFile(node, false);

        String[] keys = register.connected_key;
        double[] weight = register.weight;
        for (int i = 0; i < register.getCONNECTIONS_NUMBER(); i++) {
            if (!"NULL".equals(keys[i]) && keys[i] != null) {
                expandedNode.put(fm.getCityName(keys[i]), new Data(keys[i], weight[i]));
            }
        }

        return expandedNode;
    }

}
