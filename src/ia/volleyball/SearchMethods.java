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
     * Calcula la ruta de un origen a un destino, siguiendo el algoritmo primero
     * en anchura
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
            System.out.println("Nodo seleccionado: " + T.root.data.getCity().trim());
            System.out.println("Comprobar: ¿" + T.root.data.getCity().trim() + " = " + node_name + "?\nSI");
            System.out.println("Solucion: " + T.root.data.getCity().trim());
            return;
        }
        System.out.println("Raiz: " + T.root.data.getCity().trim());
        S = new ArrayList<>();
        S.add(T.root);
        System.out.println("S{ " + S.get(0).data.getCity().trim() + " }");
        System.out.println("==========================================================");
        explored = new ArrayList<>();
        explored.add(T.root);
        while (!fail) {
            if (S.isEmpty()) {
                System.out.println("S{ vacia }");
                System.out.println("Se recorrieron todos los nodos y no se encontro solucion");
                fail = true;
            } else {
                node = S.get(0);
                S.remove(0);
                //explored.add(node);
                System.out.println("Nodo a expandir: " + node.data.getCity().trim() + "\nHijos:");
                for (int i = 0; i < fm.getReg().connected_key.length; i++) {
                    if (!fm.getReg().connected_key[i].trim().equals("NULL")) {
                        for (Node anExplored : explored) {
                            if (fm.getReg().connected_key[i].trim().equals(anExplored.data.getKey().trim())) {
                                repeated = true;
                            }
                        }
                        if (!repeated) {
                            System.out.println("> " + fm.getCityName(fm.getReg().connected_key[i].trim()));
                            T.addChild(node, fm.getReg().getConnectedData(i, this.fm));
                            explored.add(T.child_node);
                            System.out.println("    Comprobar: ¿" + fm.getCityName(fm.getReg().connected_key[i].trim()) + " = " + node_name + "?");
                            if (fm.getCityName(fm.getReg().connected_key[i].trim()).equals(node_name)) {
                                System.out.println("    SI");
                                System.out.println(T.getPathToRoot(fm.getReg().connected_key[i].trim()));
                                return;
                            } else {
                                System.out.println("    NO");
                                System.out.println("    S <- " + fm.getCityName(fm.getReg().connected_key[i].trim()));
                                S.add(T.getPointer().node_childs.get(fm.getReg().connected_key[i].trim()));
                            }
                        }
                        repeated = false;
                    }
                }
                System.out.printf("\n==========================================================\nS{ ");
                for(Node n : S){
                    System.out.printf(n.data.getCity().trim()+",");
                }
                System.out.printf(" }\nRecorridos{ ");
                for(Node n : explored){
                    System.out.printf(n.data.getCity().trim()+",");
                }
                System.out.printf(" }\n==========================================================\n");

                try {
                    if(S.size()!=0)
                        fm.getRegisterFromFile(S.get(0).data.getKey(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Calcula la ruta de un origen a un destino, siguiendo el algoritmo primero
     * en profundidad
     *
     * @param r
     * @param node_name
     */
    public String DepthFirst(Register r, String node_name, boolean sort_by_name) {
        Tree T;
        ArrayList<Node> W;
        ArrayList<Node> explored;
        String less_value = null;
        int index = 0;
        double less_weight = 0;
        boolean repeated = false;
        T = new Tree(r);
        if (T.root.data.getCity().trim().equals(node_name)) {
            System.out.println("Nodo seleccionado: " + T.root.data.getCity().trim());
            System.out.println("Comprobar: ¿" + T.root.data.getCity().trim() + " = " + node_name + "?\nSI");
            System.out.println("Solucion: " + T.root.data.getCity().trim());
            return "";
        }
        W = new ArrayList<>();
        System.out.println("Paso 1. \nW <-" + T.root.data.getCity().trim() + " (raiz)");
        W.add(T.root);
        explored = new ArrayList<>();
        explored.add(T.root);
        while (true) {
            //Step 2
            System.out.println("Paso 2. W = " + W.get(0).data.getCity().trim() + "\nHijos:");
            for (int i = 0; i < fm.getReg().connected_key.length; i++) {
                if (!fm.getReg().connected_key[i].trim().equals("NULL")) {
                    for (Node node : explored) {
                        if (fm.getReg().connected_key[i].trim().equals(node.data.getKey().trim())) {
                            repeated = true;
                        }
                    }
                    if (!repeated) {
                        System.out.println("> " + fm.getCityName(fm.getReg().connected_key[i].trim()));
                        System.out.println("    Comprobar: ¿" + fm.getCityName(fm.getReg().connected_key[i].trim()) + " = " + node_name + "?");
                        if (fm.getCityName(fm.getReg().connected_key[i].trim()).equals(node_name)) {
                            System.out.println("    SI");
                            T.addChild(T.getPointer(), fm.getReg().getConnectedData(i,this.fm));
                            return T.getPathToRoot(fm.getReg().connected_key[i].trim());
                        } else {
                            System.out.println("    NO");
                            if(sort_by_name) {
                                if (less_value == null) {
                                    less_value = fm.getCityName(fm.getReg().connected_key[i].trim());
                                    index = i;
                                }
                                if (less_value.compareTo(fm.getCityName(fm.getReg().connected_key[i].trim())) >= 0) {
                                    less_value = fm.getCityName(fm.getReg().connected_key[i].trim());
                                    index = i;
                                }
                            }else{
                                if (less_weight == 0) {
                                    less_weight = fm.getReg().weight[i];
                                    index = i;
                                }
                                if (less_weight > fm.getReg().weight[i] ) {
                                    less_weight = fm.getReg().weight[i];
                                    index = i;
                                }
                            }
                        }
                    }
                }
                repeated = false;
            }
            if ((less_value != null && sort_by_name) || (less_weight != 0 && !sort_by_name)) {
                if(sort_by_name)
                    System.out.println("Hijo menor:\n> "+less_value);
                else
                    System.out.println("Hijo menor:\n> "+fm.getCityName(fm.getReg().connected_key[index].trim()));
                T.addChild(W.get(0), fm.getReg().getConnectedData(index, this.fm));
                W.remove(0);
                W.add(T.getPointer().node_childs.get(fm.getReg().connected_key[index].trim()));
                System.out.println("W <- " + W.get(0).data.getCity());
                explored.add(T.getPointer().node_childs.get(fm.getReg().connected_key[index].trim()));
                T.setPointer(T.getPointer().node_childs.get(fm.getReg().connected_key[index].trim()));
                System.out.printf(" }\nRecorridos{ ");
                for(Node n : explored){
                    System.out.printf(n.data.getCity().trim()+",");
                }
                System.out.printf(" }\n==========================================================\n");
                try {
                    fm.getRegisterFromFile(W.get(0).data.getKey(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Step 3
                System.out.println("No hay hijos para agregar\nPaso 3.");
                System.out.println("W == raiz\n¿" + T.root.data.getCity().trim() + "=" + W.get(0).data.getCity().trim() + "?");
                if (T.root.data.getCity().trim().equals(W.get(0).data.getCity().trim())) {
                    return "SI\nEl proceso ha terminado y no se encontro el resultado";
                } else {
                    //Step 4
                    System.out.println("NO\nPaso 4.\nX padre de W, entonces W = X");
                    System.out.println("X = " + T.getPointer().parent_node.data.getCity().trim() + " W = " + W.get(0).data.getCity().trim());
                    W.remove(0);
                    T.setPointer(T.getPointer().parent_node);
                    W.add(T.getPointer());
                    System.out.println("W = " + W.get(0).data.getCity()+"\nIr a paso 2.");
                    try {
                        fm.getRegisterFromFile(W.get(0).data.getKey(), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            less_value = null;
            less_weight = 0;
        }
    }

    /**
     * Calcula la ruta de un origen a un destino los metodos a, además de
     * imprimir los resultados a consola
     *
     * @param origin Register
     * @param destiny Register
     * @return List< String > Con el camino del nodo origin a destiny
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

            System.out.println("");
            System.out.println("\n-----------------------");
            System.out.println("Grafos A");
            System.out.println("Entadas iniciales");
            System.out.println("Origen: " + origin.getCity().trim());
            System.out.println("Destino: " + destiny);
            System.out.println("-----------------------\n");
            System.out.println("");

            System.out.println("-----Inicialización----");
            open.put(originCity, new Data(originKey, originKey, originCity, "-", 0, 0)); //1. ABIERTOS←(origin); CERRADOS←( ); 
            System.out.println("Abiertas");
            printOpenClose(open);
            System.out.println("-----------------------\n");

            while (!open.isEmpty()) {  //2. Si ABIERTOS es la lista vacía, finalizar con fallo.
                System.out.println("\n2: Abierta no esta vacia por lo que");
                //3. EA←primer elemento de ABIERTOS.
                // Eliminar EA de ABIERTOS y llevarlo a CERRADOS.       
                ea = moveFirstTo(open, close);
                System.out.println("3. EA←primer elemento de ABIERTOS. \nEA = " + ea.getKey());
                System.out.println("Eliminar EA de ABIERTOS y llevarlo a CERRADOS. ");
                System.out.println("\nAbiertas");
                printOpenClose(open);
                System.out.println("\nCerrados");
                printOpenClose(close);

                // 4. Si EA es una meta, fin con éxito. Devolver el camino hasta la meta. 
                if (ea.getKey().trim().equals(destiny.trim())) {
                    System.out.println("\n-----Finalización----");
                    System.out.println("4: EA = Objetivo = " + ea.getKey() + " entonces");
                    // Devolver el camino hasta la meta. 
                    System.out.println("El camino de origen = " + origin.getCity().trim()
                            + "a destino = " + destiny + " es: ");
                    return getPathA(originCity, ea, open, close);
                }

                //5. Expandir nodo EA, generando todos sus sucesores como hijos de EA.
                originKey = ea.getValue().key;
                nodeChildrens = expandNodeA(originKey);
                System.out.println("\n5: EA = Sucesores de n = ");
                for (Data entry : nodeChildrens) {
                    System.out.println(entry.pCity);
                }

                System.out.println("\n ------------6. Para cada sucesor de EA = " + ea.getKey() + "-------");
                for (Data currentNode : nodeChildrens) {               //6. Para cada sucesor de EA   
                    System.out.println("q = " + currentNode.pCity);
                    System.out.println("--------------------------");
                    g = calculateG(currentNode, ea, open, close);      // a) Calcular g(q)=g(EA)+c(EA,q) 
                    System.out.println("Calcular g(q)=g(EA)+c(EA,q) = " + g);
                    if (open.containsKey(currentNode.pCity) || close.containsKey(currentNode.pCity)) {
                        //c) Si q estaba en ABIERTOSo en CERRADOS, comparar el nuevo valor f(q) con el anterior.
                        System.out.println("q = " + currentNode.key + " estaba en ABIERTOS o en CERRADOS , comparar el nuevo valor f(q) con el anterior");
                        oldG = getInitToN(currentNode, open, close);

                        if (oldG > g) {
                            //Si el nuevo es menor, colocar EA como nuevo padre y asignar el nuevo valor f(q).
                            System.out.println("g anterior = " + oldG + " fue mayor o igual que que g actual = " + g);
                            System.out.println("Por lo que se coloca EA = " + ea.getKey() + " como nuevo padre de q = "
                                    + currentNode.pCity + " en ABIERTOS o Cerrados c y ase recalculan f(q) y g(q)");
                            h = calculateH(currentNode, ea);  //calcular f(q)=g(q)+h(q),
                            f = g + h;
                            System.out.println("Calcular f(q)=g(q)+h(q)");
                            System.out.println("Donde h(q) = " + h);
                            System.out.println("Por lo que f(q) = " + f);
                            open.replace(currentNode.pCity, new Data(currentNode.pKey, ea.getValue().key, ea.getKey(), currentNode.wire, f, g));
                            System.out.println("\nAbiertas");
                            printOpenClose(open);
                            System.out.println("\nCerrados");
                            printOpenClose(open);

                            if (close.containsKey(currentNode.pKey)) { //Si estaba en CERRADOS
                                System.out.println("'nSi estaba en CERRADOS llevarle a ABIERTOS y eliminarle de CERRADOS");
                                open.put(currentNode.pCity, close.get(currentNode.pCity)); //Llevarle a ABIERTOS.
                                close.remove(currentNode.pKey); // Eliminarle de CERRADOS
                                System.out.println("\nAbiertas");
                                printOpenClose(open);
                                System.out.println("\nCerrados");
                                printOpenClose(open);
                            }

                        } else {
                            //Si el anterior es menor o igual, descartar el nodo recién generado.
                            System.out.println("g anterior = " + oldG + " fue menor que g actual = " + g);
                            System.out.println("Por lo que se descarta el nodo recién generado");
                        }

                    } else { //b) Si q no estaba en ABIERTOS ni en CERRADOS, 
                        System.out.println("q = " + currentNode.key + " no esta ni en ABIERTOS ni en CERRADOS");
                        h = calculateH(currentNode, ea);  //calcular f(q)=g(q)+h(q),
                        System.out.println("Calcular f(q)=g(q)+h(q)");

                        System.out.println("Donde h(q) = " + h);
                        f = g + h;
                        System.out.println("Por lo que f(q) = " + f);
                        //añadir q a ABIERTOS como hijo de EA, asignando los valores f(q) y g(q). 
                        open.put(currentNode.pCity, new Data(currentNode.pKey, ea.getValue().key, ea.getKey(), currentNode.wire, f, g));
                        System.out.println("Anadir q = " + currentNode.key + "como hijo de EA = " + ea.getKey()
                                + " asignando los valores de f(q) = " + f + "y g(q) = " + g);
                        System.out.println("\nAbiertas");
                        printOpenClose(open);
                    }
                    System.out.println("\n-----------------\n");
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
     * Imprime la lista ABIERTOS o CERRADOS del los métodos a
     *
     * @param hash ABIERTOS o CERRADOS
     */
    private void printOpenClose(LinkedHashMap<String, Data> hash) {
        hash.entrySet().forEach((entry) -> {
            System.out.println(entry.getKey() + "  -  " + entry.getValue().pCity + "  -  " + entry.getValue().g + "  -  " + entry.getValue().f);
        });
    }

    /**
     * Calcula h(q) para esto se hace uso de los cambios de cables entre el nodo
     * actual y su padre
     *
     * @param currentNode Nodo actual
     * @param ea
     * @return valor de h(q)
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
     * Calcular g(q) = g(EA)+ c(EA,q)
     *
     * @param currentNode Nodo actual
     * @param ea
     * @param open ABIERTOS
     * @param close CERRADOS
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
     * Expande el nodo especificado
     *
     * @param node char Nodo a expandir
     * @return List<Data> Lsta de los hijos de node
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
            if (!"NULL".trim().equals(keys[i].trim()) && keys[i].trim() != null) {
                expandedNode.add(new Data("", keys[i], data.getCityName(keys[i]), wire[i], 0.0, weight[i]));
            } else {
                break;
            }
        }
        return expandedNode;
    }

    /**
     * Obtiene g(EA) en pocas palabras obtiene el costo del nodo inicial hasta
     * el nodo n.
     *
     * @param currentNode Nodo actual
     * @param open ABIERTAS
     * @param close CERRADAS
     * @return int valor de g(EA)
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

    /**
     * Obtiene el camino resultante de la ejecución de los algoritmos a de las
     * tablas de ABIERTAS Y CERRADAS
     *
     * @param origin Nodo origen
     * @param ea
     * @param open ABIERTAS
     * @param close CERRADAS
     * @return List< String > Camino resultante del nodo origen al destino
     */
    private List<String> getPathA(String origin,
            Map.Entry<String, Data> ea,
            LinkedHashMap<String, Data> open,
            LinkedHashMap<String, Data> close) {
        List<String> path = new Stack<>();
        boolean a = false;
        String father = ea.getKey();
        path.add(father);

        while (!path.contains(origin)) {
            if (open.containsKey(father)) {
                father = open.get(father).pCity;
                path.add(father);

                if (open.get(father).f != open.get(father).g) {
                    a = true;
                }
            } else if (close.containsKey(father)) {
                father = close.get(father).pCity;
                path.add(father);
                if (close.get(father).f != close.get(father).g) {
                    a = true;
                }
            }

        }

        if (!a) {
            path.add("A*");
        } else {
            path.add("A");
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
     * Sacar el primer valor de un Hash
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
     * Sacar el primer valor de un Hash y pasarlo al final de otro hash (Usado
     * para trabajar con ABIERTA y CERRADA
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

    /**
     * Calcula la ruta de un origen a un destino los metodos a, además de
     * imprimir los resultados a consola
     *
     * @param origin Register
     * @param destiny Register
     * @return List< String > Con el camino del nodo origin a destiny
     * @throws IOException
     */
    public List<String> grafosO(Register origin, String destiny) throws IOException {
        String originKey = origin.getKey();
        if (originKey != null) {
            List<Data> abierta = new ArrayList<>();
            Data n;
            double costo;
            HashMap<String, Data> s;
            HashMap<String, Data> tablaA = new HashMap<>();

            System.out.println("");
            System.out.println("\n-----------------------");
            System.out.println("Grafos 0");
            System.out.println("Entadas iniciales");
            System.out.println("Origen: " + origin.getCity());
            System.out.println("Destino: " + destiny);
            System.out.println("-----------------------\n");
            System.out.println("");

            System.out.println("-----Inicialización----");
            abierta.add(new Data(origin.getKey(), origin.getCity())); //1: ABIERTA = (inicial); 
            System.out.println("1: ABIERTA = inicial = " + origin.getCity());
            System.out.println("-----------------------\n");

            while (!abierta.isEmpty()) {  //2: mientras NoVacía(ABIERTA) hacer  
                System.out.println("2: Abierta no esta vacia por lo que");
                n = abierta.remove(0); // n=ExtraePrimero(ABIERTA); 
                System.out.println("n = " + n.pCity);

                if (n.pCity.trim().equals(destiny.trim())) {  //4: Si EsObjetivo(n) entonces 
                    System.out.println("\n-----Finalización----");
                    System.out.println("4: n = Objetivo = " + n.pCity.trim() + " entonces");
                    //5: Devolver Camino(inicial,n); 
                    System.out.println("5:El camino de origen = " + origin.getCity()
                            + "a destino = " + destiny + " es: ");
                    return getPathO(tablaA, n.pCity, origin.getCity(), destiny);

                } //6: Fin Fs                    i 

                s = expandNodeO(n.pKey.trim()); //7: S=Sucesores(n);
                System.out.println("\n7: S = Sucesores de n = ");
                for (Map.Entry<String, Data> entry : s.entrySet()) {
                    System.out.println(entry.getKey());
                }

                if (tablaA.containsKey(n.pCity)) {
                    System.out.println("\n8:Añade S a la entrada de n en la TABLA_A");
                    tablaA.get(n.pCity).s = s; //8:Añade S a la entrada de n en la TABLA_A 
                    printTableA(tablaA);
                } else {
                    System.out.println("\n8:Añade S a la entrada de n en la TABLA_A");
                    tablaA.put(n.pCity, new Data(s, "-", "-", 0)); //8:Añade S a la entrada de n en la TABLA_A 
                    printTableA(tablaA);
                }

                System.out.println("\n-------Recorrido de hijo(s) de S = " + n.pCity + "----------\n");
                for (Map.Entry<String, Data> q : s.entrySet()) { //9: Para cada q de S hacer 
                    System.out.println("9: Para cada q de S hacer donde q = " + q.getKey());
                    System.out.println("--------------------------");
                    if (tablaA.containsKey(q.getKey())) { //10: si(qє TABLA_A) entonces 
                        System.out.println("\n10: q є TABLA_A entonces");
                        //11: Rectificar(q,n,Coste(n,q)); 
                        System.out.println("\n11: Rectificar(q,n,Coste(n,q)); donde n = " + q.getKey() + " q = " + n.pCity);
                        rectificar(tablaA, n.pCity, q);
                        System.out.println("");
                        printTableA(tablaA);
                        //12: Ordenar(ABIERTA);{si es preciso

                    } else { //13: sino
                        //14: Coloca q en la TABLA_A con
                        System.out.println("10: q ∉ TABLA_A entonces");
                        System.out.println("Coste (inicial,q)= Coste (inicial,n) + Coste(n,q)");
                        costo = calculateF(tablaA, n.pCity, q); //Coste (inicial,q)= Coste (inicial,n) + Coste(n,q)
                        System.out.println("Costo = " + costo);
                        System.out.println("Anterior (q) = n = " + n.pCity);
                        tablaA.put(q.getKey(), new Data(n.pKey, n.pCity, costo)); //Anterior(q)=n
                        System.out.println("\nAgregar a la tabla A");
                        printTableA(tablaA);
                        System.out.println("\nAgregar q a abierta");
                        abierta.add(new Data(q.getValue().pKey, q.getKey())); //ABIERTA=Mezclar(q,ABIERTA); 
                        System.out.println("\nAbierta");
                        abierta.forEach((entry) -> {
                            System.out.println(entry.pCity);
                        });
                    }
                    System.out.println("");
                }
                System.out.println("\n-----------------\n");
            }
        } else {
            System.out.println("Nodo Invalido");
        }
        System.out.println("Nodo destino no encontrado");
        return new Stack<>();
    }

    /**
     * Imprime la tabla del método grafos O
     *
     * @param tablaA
     */
    public void printTableA(HashMap<String, Data> tablaA) {
        System.out.println("Tabla A");
        String sdata;
        Boolean flag = true;
        for (Map.Entry<String, Data> entry : tablaA.entrySet()) {
            sdata = "";
            if (entry.getValue().s != null) {
                sdata += entry.getValue().s.entrySet().stream().map((ent) -> ent.getKey() + ",").reduce(sdata, String::concat);
                flag = false;
            }
            if (flag) {
                sdata = "-";
            }

            System.out.println(entry.getKey() + "  |  " + sdata + "  |  " + entry.getValue().pCity + "  |  " + entry.getValue().f);
        }

    }

    /**
     * Rectifica el camino en caso de ser necesario
     *
     * @param tablaA
     * @param n
     * @param q
     */
    private void rectificar(HashMap<String, Data> tablaA, String n, Map.Entry<String, Data> q) {

        if (tablaA.get(n).f + q.getValue().f < tablaA.get(q.getKey()).f) {
            tablaA.get(q.getKey()).f = tablaA.get(n).f + q.getValue().f;
            tablaA.get(q.getKey()).pCity = n;
            rectificarLista(tablaA, q.getKey());

        }
    }

    /**
     * Rectifica lo caminos que sean necesarios
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
     * Obtener el camino resultante de la ejecución de algoritmo O de la tablaA
     *
     * @param tablaA
     * @param n
     * @param origin origen 
     * @param destiny destino
     * @return List< String > Camino resultante del nodo origen al destino
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
     * Expande el nodo especificado
     *
     * @param node char Nodo a expandir
     * @return List<Data> Lsta de los hijos de node
     * @throws IOException
     */
    private HashMap expandNodeO(String node) throws IOException {
        Register register;
        FilesManager data = new FilesManager("master_submarine_cables", "index_submarine_cables");
        HashMap<String, Data> expandedNode = new HashMap<>();
        register = data.getRegisterFromFile(node.trim(), false);

        String[] keys = register.connected_key;
        double[] weight = register.weight;
        for (int i = 0; i < register.getCONNECTIONS_NUMBER(); i++) {
            if (!"NULL".trim().equals(keys[i].trim()) && keys[i].trim() != null) {

                expandedNode.put(data.getCityName(keys[i]), new Data(keys[i], weight[i]));
            } else {
                break;
            }
        }

        return expandedNode;
    }
}
