package ia.volleyball;

import java.io.IOException;
import java.util.Scanner;

public class Register {
    private String key;
    private String city;
    private String country;
    private final int CONNECTIONS_NUMBER = 10;
    public String connected_key[];
    public double weight[];
    public String wire_name[];

    private Scanner input;

    public Register(){
        instantiateConnectionsArrays();
        input = new Scanner(System.in);
    }

    /**
     * Add new Registers to the sequential index file
     * @param fm
     */
    public void setRegisterData(FilesManager fm){
        Register current_node, new_node;
        boolean set_connection = true, available_connection = false;
        int nulls = 0;
        current_node = new Register();
        System.out.println("LLAVE:");
        current_node.key = input.next();
        try{
            do {
                if (!fm.keyExists(current_node.key)) {
                    System.out.println("CIUDAD:");
                    current_node.city = input.next();
                    System.out.println("PAIS:");
                    current_node.country = input.next();
                    for (int i = 0; i < current_node.CONNECTIONS_NUMBER; i++) {
                        if (set_connection) {
                            System.out.println("LLAVE CIUDAD CONECTADA");
                            current_node.connected_key[i] = input.next();
                            if (fm.keyExists(current_node.connected_key[i])) {
                                fm.getRegisterFromFile(current_node.connected_key[i], true);
                                for (int j = 0; j < fm.getReg().CONNECTIONS_NUMBER; j++) {
                                    if (fm.getReg().connected_key[j].trim().equals("NULL")) {
                                        System.out.println("DISTANCIA ENTRE LOS NODOS (KM):");
                                        current_node.weight[i] = input.nextDouble();
                                        System.out.println("NOMBRE DEL CABLE ENTRE LOS NODOS:");
                                        current_node.wire_name[i] = input.next();
                                        fm.getReg().connected_key[j] = current_node.key;
                                        fm.getReg().weight[j] = current_node.weight[i];
                                        fm.getReg().wire_name[j] = current_node.wire_name[i];
                                        fm.writeDataInSpecificAddress(fm.getReg(), fm.getReg().getKey().trim());
                                        available_connection = true;
                                        break;
                                    }
                                }
                                if (!available_connection) {
                                    System.out.println("El nodo a conectar no tiene espacio para otra conexion");
                                    setNullConnections(current_node, i);
                                    nulls++;
                                }
                            } else {
                                System.out.println("El nodo al que se quiere realizar la conexion no existe en el grafo\n" +
                                        "Se pueden realizar las acciones: \n1)Crear el nodo \n2)No establcer ninguna conexion");
                                switch (input.nextInt()) {
                                    case 1:
                                        System.out.println("DISTANCIA ENTRE LOS NODOS (KM):");
                                        current_node.weight[i] = input.nextDouble();
                                        System.out.println("NOMBRE DEL CABLE ENTRE LOS NODOS:");
                                        current_node.wire_name[i] = input.next();
                                        new_node = new Register();
                                        new_node.key = current_node.connected_key[i];
                                        System.out.println("CIUDAD DEL NUEVO NODO:");
                                        new_node.city = input.next();
                                        System.out.println("PAIS DE NUEVO NODO:");
                                        new_node.country = input.next();
                                        new_node.connected_key[0] = current_node.getKey();
                                        new_node.weight[0] = current_node.weight[i];
                                        new_node.wire_name[0] = current_node.wire_name[i];
                                        for (int j = 1; j < new_node.getCONNECTIONS_NUMBER(); j++) {
                                            setNullConnections(new_node, j);
                                        }
                                        fm.writeData(new_node, false);
                                        break;
                                    case 2:
                                        setNullConnections(current_node, i);
                                        nulls++;
                                        break;
                                }
                            }
                            System.out.println("INSERTAR OTRA CONEXION 1:SI | 0:NO");
                            set_connection = (input.nextInt() == 1);
                        } else {
                            setNullConnections(current_node, i);
                            nulls++;
                        }
                    }
                    if (nulls != current_node.getCONNECTIONS_NUMBER())
                        fm.writeData(current_node, false);
                    else
                        System.out.println("No se crea el nodo por que no hay conexiones hacia otros nodos.");
                } else {
                    System.out.println("El nodo que quieres insertar ya existe en el grafo\n" +
                            "¿Crear conexion en el nodo?(1:SI | 2:NO)");
                    switch (input.nextInt()) {
                        case 1:
                            current_node = fm.getRegisterFromFile(current_node.getKey(), false);
                            for (int i = 0; i < current_node.CONNECTIONS_NUMBER; i++) {
                                if (current_node.connected_key[i].trim().equals("NULL")) {
                                    System.out.println("LLAVE DE LA CIUDAD CONECTADA:");
                                    current_node.connected_key[i] = input.next();
                                    if (!fm.keyExists(current_node.connected_key[i])) {
                                        System.out.println("El nodo al que se quiere realizar la conexion no existe en el grafo\n" +
                                                "Se pueden realizar las acciones: \n1)Crear el nodo \n2)No establecer ninguna conexion");
                                        switch (input.nextInt()) {
                                            case 1:
                                                System.out.println("DISTANCIA ENTRE LOS NODOS (KM):");
                                                current_node.weight[i] = input.nextDouble();
                                                System.out.println("NOMBRE DEL CABLE ENTRE LOS NODOS:");
                                                current_node.wire_name[i] = input.next();
                                                new_node = new Register();
                                                new_node.key = current_node.connected_key[i];
                                                System.out.println("CIUDAD DEL NUEVO NODO:");
                                                new_node.city = input.next();
                                                System.out.println("PAIS DE NUEVO NODO:");
                                                new_node.country = input.next();
                                                new_node.connected_key[0] = current_node.getKey();
                                                new_node.weight[0] = current_node.weight[i];
                                                new_node.wire_name[0] = current_node.wire_name[i];
                                                for (int j = 1; j < new_node.getCONNECTIONS_NUMBER(); j++) {
                                                    setNullConnections(new_node, j);
                                                }
                                                fm.writeData(new_node, false);
                                                System.out.println("Se agrego la conexion la conexion");
                                                System.out.println("INSERTAR OTRA CONEXION 1:SI | 0:NO");
                                                set_connection = (input.nextInt() == 1);
                                                break;
                                        }
                                    } else {
                                        System.out.println("DISTANCIA ENTRE LOS NODOS (KM):");
                                        current_node.weight[i] = input.nextDouble();
                                        System.out.println("NOMBRE DEL CABLE ENTRE LOS NODOS:");
                                        current_node.wire_name[i] = input.next();
                                        connectionNodeToNode(fm, current_node, i);
                                        System.out.println("Se agrego la conexion la conexion");
                                        System.out.println("INSERTAR OTRA CONEXION 1:SI | 0:NO");
                                        set_connection = (input.nextInt() == 1);
                                    }
                                    available_connection = true;
                                    break;
                                }
                            }
                            if (!available_connection) {
                                System.out.println("El nodo no tiene espacio para otra conexion");
                            }
                            break;
                    }
                }
            }while(set_connection);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error al crear el nodo y sus conexiones");
        }
    }

    /**
     * Connect two nodes that exists in the sequential index file
     * @param fm
     * @param r
     * @param index
     * @throws IOException
     */
    public void connectionNodeToNode(FilesManager fm, Register r, int index) throws IOException{
        int i;
        boolean available = false;
        Register connected_node;
        connected_node = fm.getRegisterFromFile(r.connected_key[index], false);

        for(i = 0; i < connected_node.getCONNECTIONS_NUMBER(); i++ ){
            if(connected_node.connected_key[i].trim().equals("NULL")){
                connected_node.connected_key[i] = r.getKey();
                connected_node.wire_name[i] = r.wire_name[index];
                connected_node.weight[i] = r.weight[index];
                available = true;
                break;
            }
        }
        if(!available){
            System.out.println("No se puede crear la conexion el, el nodo final no tiene espacio");
        }else{
            fm.writeDataInSpecificAddress(r, r.getKey());
            fm.writeDataInSpecificAddress(connected_node, connected_node.getKey());
        }
    }

    /**
     * Updates data from a register and for his connections
     * @param fm
     */
    public void updateDatadReg(FilesManager fm){
        Register update_connected;
        boolean other_field;
        int index, i;
        System.out.println("Insertar llave del registro a actualizar:");
        String option = input.next();
        try {
            fm.getRegisterFromFile(option, true);
            if (!fm.keyExists(option)) {
                System.out.println("No existe un registro con la llave ingresada");
                return;
            }
            System.out.println("Actualizar:\n1)Datos del nodo\n2)Conexiones");
            switch(input.nextInt()){
                case 1:
                    do {
                        System.out.println("1.CIUDAD\n2.PAIS");
                        switch (input.nextInt()) {
                            case 1:
                                System.out.println("Ingresa el nuevo nombre de la ciudad");
                                fm.getReg().city = input.next();
                                break;
                            case 2:
                                System.out.println("Ingresa el nuevo nombre del pais");
                                fm.getReg().country = input.next();
                                break;
                        }
                        System.out.println("Se actualizaron los datos del nodo\n¿Quieres actualizar otro campo?1:SI | 0:NO");
                        other_field = (input.nextInt() == 1);
                        fm.writeDataInSpecificAddress(fm.getReg(), fm.getReg().getKey());
                    }while(other_field);
                    break;
                case 2:
                    do {
                        System.out.println("Selecciona la conexion a actualizar");
                        for (i = 0; i < fm.getReg().getCONNECTIONS_NUMBER(); i++) {
                            if(!fm.getReg().connected_key[i].trim().equals("NULL")) {
                                System.out.println("Conexion " + (i + 1) +
                                        "\nLLAVE: " + fm.getReg().connected_key[i] +
                                        "\nPESO: " + fm.getReg().weight[i] +
                                        "\nNOMBRE DEL CABLE " + fm.getReg().wire_name[i].trim() +
                                        "\n........................................");
                            }
                        }
                        index = input.nextInt();
                        index = index - 1;
                        System.out.println("NUEVO PESO");
                        fm.getReg().weight[index] = input.nextDouble();
                        System.out.println("NUEVO NOMBRE DEL CABLE");
                        fm.getReg().wire_name[index] = input.next();
                        update_connected = fm.getRegisterFromFile(fm.getReg().connected_key[index], false);
                        for (i = 0; i < update_connected.getCONNECTIONS_NUMBER(); i++) {
                            if (update_connected.connected_key[i].equals(fm.getReg().getKey())) {
                                update_connected.wire_name[i] = fm.getReg().wire_name[index];
                                update_connected.weight[i] = fm.getReg().weight[index];
                                break;
                            }
                        }
                        fm.writeDataInSpecificAddress(fm.getReg(), fm.getReg().getKey());
                        fm.writeDataInSpecificAddress(update_connected, update_connected.getKey());
                        System.out.println("Conexión actualizada,\n¿Quieres actualizar otra conexion? 1:SI | 2:NO");
                        other_field = (input.nextInt() == 1);
                    }while(other_field);
                    break;
            }
        }catch(Exception e){ e.printStackTrace(); }
    }

    public void deleteDataReg(FilesManager fm){
        try {
            System.out.println("Inserta llave del nodo a eliminar");
            fm.getRegisterFromFile(input.next(), true);
            System.out.println("Eliminar\n1.Registro\n2.Conexiones");
            switch(input.nextInt()){
                case 1:
                    fm.getReg().setKey(fm.getReg().getKey().replace('_', '*'));
                    break;
                case 2:
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Instantiate the arrays that represents the connections between nodes
     */
    public void instantiateConnectionsArrays(){
        this.connected_key = new String[this.CONNECTIONS_NUMBER];
        this.weight = new double[this.CONNECTIONS_NUMBER];
        this.wire_name = new String[this.CONNECTIONS_NUMBER];
    }

    /**
     * Obtain a Register object from a existent object
     * @param index
     * @param fm
     * @return
     */
    public Register getConnectedData(int index, FilesManager fm){
        Register data = new Register();
        data.setKey(this.connected_key[index].trim());
        data.setCity(fm.getCityName(this.connected_key[index]));
        data.weight[index] = this.weight[index];
        data.wire_name[index] = this.wire_name[index].trim();
        return data;
    }

    /**
     * Establish the null connections of a register
     * @param r
     * @param index
     */
    public void setNullConnections(Register r, int index){
        r.connected_key[index] = "NULL";
        r.weight[index] = -1;
        r.wire_name[index] = "NULL";
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCONNECTIONS_NUMBER() {
        return CONNECTIONS_NUMBER;
    }
}