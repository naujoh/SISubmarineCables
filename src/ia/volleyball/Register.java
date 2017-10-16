package ia.volleyball;

import java.util.Scanner;

public class Register {
    private String key;
    private String city;
    private String country;
    private final int CONNECTIONS_NUMBER = 10;
    public int available[];
    public String connected_key[];
    public String connected_city[];
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
                System.out.println("CONEXION DISPONIBLE 1:SI | 0:NO");
                available[i] = input.nextInt();
                System.out.println("LLAVE CIUDAD");
                connected_key[i] = input.next();
                System.out.println("CIUDAD CONECTADA:");
                connected_city[i] = input.next();
                System.out.println("DISTANCIA (KM):");
                weight[i] = input.nextDouble();
                System.out.println("NOMBRE DEL CABLE:");
                wire_name[i] = input.next();
                System.out.println("INSERTAR OTRA CONEXION 1:SI | 0:NO");
                set_connection = (input.nextInt() == 1);
            }else{
                available[i] = 0;
                connected_key[i] = "NULL";
                connected_city[i] = "NULL";
                weight[i] = -1;
                wire_name[i] = "NULL";
            }
        }
    }

    public void instantiateConnectionsArrays(){
        this.available = new int[this.CONNECTIONS_NUMBER];
        this.connected_key = new String[this.CONNECTIONS_NUMBER];
        this.connected_city = new String[this.CONNECTIONS_NUMBER];
        this.weight = new double[this.CONNECTIONS_NUMBER];
        this.wire_name = new String[this.CONNECTIONS_NUMBER];
    }

    public Register getConnectedData(int index){
        Register data = new Register();
        data.setCity(this.connected_city[index].trim());
        data.setKey(this.connected_key[index].trim());
        data.available[index] = this.available[index];
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