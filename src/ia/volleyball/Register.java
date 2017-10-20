package ia.volleyball;

import java.io.IOException;
import java.util.Scanner;

public class Register {
    private String key;
    private String city;
    private String country;
    private final int CONNECTIONS_NUMBER = 10;
    //public int available[];
    public String connected_key[];
    //public String connected_city[];
    public double weight[];
    public String wire_name[];

    public Register(){
        instantiateConnectionsArrays();
    }

    public void setData(){
        boolean set_connection = true;
        Scanner input = new Scanner(System.in);
        System.out.println("LLAVE:");
        this.key = input.next();
        System.out.println("CIUDAD:");
        this.city = input.next();
        System.out.println("PAIS:");
        this.country = input.next();
        //Set connections of reg
        for(int i = 0; i < this.CONNECTIONS_NUMBER; i++){
            if(set_connection) {
                //System.out.println("CONEXION DISPONIBLE 1:SI | 0:NO");
                //available[i] = input.nextInt();
                System.out.println("LLAVE CIUDAD");
                connected_key[i] = input.next();
                //System.out.println("CIUDAD CONECTADA:");
                //connected_city[i] = input.next();
                System.out.println("DISTANCIA (KM):");
                weight[i] = input.nextDouble();
                System.out.println("NOMBRE DEL CABLE:");
                wire_name[i] = input.next();
                System.out.println("INSERTAR OTRA CONEXION 1:SI | 0:NO");
                set_connection = (input.nextInt() == 1);
            }else{
                //available[i] = 0;
                connected_key[i] = "NULL";
                //connected_city[i] = "NULL";
                weight[i] = -1;
                wire_name[i] = "NULL";
            }
        }
    }

    public void setRegisterData(FilesManager fm){
        Register connected, node;
        boolean set_connection = true;
        boolean avialable_connection = false;
        int option;
        int nulls = 0;
        int node_connected_index = 0;
        node = new Register();
        Scanner input = new Scanner(System.in);
        System.out.println("LLAVE:");
        node.key = input.next();
        try{
            if(!fm.keyExists(node.key)){
                System.out.println("CIUDAD:");
                node.city = input.next();
                System.out.println("PAIS:");
                node.country = input.next();
                for(int i = 0; i < node.CONNECTIONS_NUMBER; i++){
                    if(set_connection){
                        //node.available[i] = 1;
                        System.out.println("LLAVE CIUDAD CONECTADA");
                        node.connected_key[i] = input.next();
                        if(fm.keyExists(node.connected_key[i])) {
                            connected = fm.getRegisterFromFile(node.connected_key[i], true);
                            for (int j = 0; j < connected.CONNECTIONS_NUMBER; j++) {
                                if (connected.connected_key[j].trim().equals("NULL")) {
                                    avialable_connection = true;
                                    node_connected_index = j;
                                    break;
                                }
                            }
                            if (avialable_connection) {
                                //node.connected_city[i] = connected.getCity().trim();
                                System.out.println("DISTANCIA ENTRE LOS NODOS (KM):");
                                node.weight[i] = input.nextDouble();
                                System.out.println("NOMBRE DEL CABLE ENTRE LOS NODOS:");
                                node.wire_name[i] = input.next();
                                //connected.available[node_connected_index] = 1;
                                connected.connected_key[node_connected_index] = node.key;
                                //connected.connected_city[node_connected_index] = node.city;
                                connected.weight[node_connected_index] = node.weight[i];
                                connected.wire_name[node_connected_index] = node.wire_name[i];
                                fm.addConnection(connected, connected.getKey().trim());
                            } else {
                                System.out.println("El nodo a conectar no tiene espacio para otra conexion");
                                //node.available[i] = 0;
                                node.connected_key[i] = "NULL";
                                //node.connected_city[i] = "NULL";
                                node.weight[i] = -1;
                                node.wire_name[i] = "NULL";
                            }

                        }else{
                            System.out.println("DISTANCIA ENTRE LOS NODOS (KM):");
                            node.weight[i] = input.nextDouble();
                            System.out.println("NOMBRE DEL CABLE ENTRE LOS NODOS:");
                            node.wire_name[i] = input.next();
                            System.out.println("El nodo al que se quiere realizar la conexion no existe en el grafo\n" +
                                    "Se pueden realizar las acciones: \n1)Crear el nodo \n2)No establcer ninguna conexion");
                            option = input.nextInt();
                            switch (option){
                                case 1:

                                    Register new_node = new Register();
                                    new_node.key = node.connected_key[i];
                                    System.out.println("CIUDAD:");
                                    new_node.city = input.next();
                                    System.out.println("PAIS:");
                                    new_node.country = input.next();
                                    //new_node.available[0] = 1;
                                    new_node.connected_key[0] = node.getKey();
                                    //new_node.connected_city[0] = node.getCity();
                                    new_node.weight[0] = node.weight[i];
                                    new_node.wire_name[0] = node.wire_name[i];
                                    for(int j = 1; j < new_node.getCONNECTIONS_NUMBER(); j++){
                                        //new_node.available[j] = 0;
                                        new_node.connected_key[j] = "NULL";
                                        //new_node.connected_city[j] = "NULL";
                                        new_node.weight[j] = -1;
                                        new_node.wire_name[j] = "NULL";
                                    }
                                    //node.connected_city[i] = new_node.getCity();
                                    fm.writeData(new_node);
                                    break;
                                case 2:
                                    //node.available[i] = 0;
                                    node.connected_key[i] = "NULL";
                                    //node.connected_city[i] = "NULL";
                                    node.weight[i] = -1;
                                    node.wire_name[i] = "NULL";
                                    nulls++;
                                    break;
                            }
                        }
                        System.out.println("INSERTAR OTRA CONEXION 1:SI | 0:NO");
                        set_connection = (input.nextInt() == 1);
                    }else{
                        //node.available[i] = 0;
                        node.connected_key[i] = "NULL";
                        //node.connected_city[i] = "NULL";
                        node.weight[i] = -1;
                        node.wire_name[i] = "NULL";
                    }
                }
                if(nulls != node.getCONNECTIONS_NUMBER()){
                    fm.writeData(node);
                }
            }else{
                System.out.println("El nodo que quieres insertar ya existe en el grafo");
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error al crear el nodo y sus conexiones");
        }
    }

    public void connectionNodeToNode(FilesManager fm) throws IOException{
        String init_key_node, final_key_node, wire_name;
        int weight, i_init_reg = 0, i_final_reg = 0, i;
        Register init_reg, final_reg;
        boolean avialable = false;
        Scanner input = new Scanner(System.in);
        System.out.println("Ingresa el costo de la conexion");
        weight = input.nextInt();
        System.out.println("Ingresa nombre del cable de la conexion");
        wire_name = input.next();
        System.out.println("Ingresa llave del nodo inicial");
        init_key_node = input.next();
        init_reg = fm.getRegisterFromFile(init_key_node, true);
        System.out.println("Ingresa llave del nodo final");
        final_key_node = input.next();
        final_reg = fm.getRegisterFromFile(final_key_node, true);

        for(i = 0; i < init_reg.getCONNECTIONS_NUMBER(); i++ ){
            if(init_reg.connected_key[i].trim().equals("NULL")){
                avialable = true;
                i_init_reg = i;
                break;
            }
        }
        if(avialable){
            avialable = false;
            for(i = 0; i < final_reg.getCONNECTIONS_NUMBER(); i++){
                if(final_reg.connected_key[i].trim().equals("NULL")){
                    avialable = true;
                    i_final_reg = i;
                    break;
                }
            }
            if(avialable){
                init_reg.connected_key[i_init_reg] = final_reg.getKey();
                //init_reg.connected_city[i_init_reg] = final_reg.getCity();
                init_reg.weight[i_init_reg] = weight;
                init_reg.wire_name[i_init_reg] = wire_name;
                final_reg.connected_key[i_final_reg] = init_reg.getKey();
                //final_reg.connected_city[i_final_reg] = init_reg.getCity();
                final_reg.weight[i_final_reg] = weight;
                final_reg.wire_name[i_final_reg] = wire_name;
                fm.writeData(init_reg);
                fm.writeData(final_reg);
            }else{
                System.out.println("No se puede crear la conexion el, el nodo final no tiene espacio");
            }
        }else{
            System.out.println("No se puede crear la conexion, el nodo inicial no tiene espacio");
        }
    }

    public void instantiateConnectionsArrays(){
        //this.available = new int[this.CONNECTIONS_NUMBER];
        this.connected_key = new String[this.CONNECTIONS_NUMBER];
        //this.connected_city = new String[this.CONNECTIONS_NUMBER];
        this.weight = new double[this.CONNECTIONS_NUMBER];
        this.wire_name = new String[this.CONNECTIONS_NUMBER];
    }

    public Register getConnectedData(int index, FilesManager fm){
        Register data = new Register();
        //data.available[index] = this.available[index];
        data.setKey(this.connected_key[index].trim());
        data.setCity(fm.getCityName(this.connected_key[index]));
        data.weight[index] = this.weight[index];
        data.wire_name[index] = this.wire_name[index].trim();
        return data;
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