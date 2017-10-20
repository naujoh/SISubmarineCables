package ia.volleyball;

import javax.xml.bind.SchemaOutputResolver;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Register o_register = new Register();
        FilesManager o_fm = new FilesManager(o_register, "master_submarine_cables", "index_submarine_cables");
        SearchMethods sm = new SearchMethods(o_fm);
        String key, city;
        String origin, destiny;
        int menu_option, another_reg;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("==========================================================");
            System.out.println("\nIngresa:\n1)Escribir en archivo\n2)Leer de manera secuencial"
                    + "\n3)Insertar datos de archivo secuencial\n4)Busqueda primero en anchura "
                    + "\n5)Busqueda primero en profundidad \n6)Grafos O \n7)Algoritmo A y A* \n8)Salir");
            System.out.println("==========================================================");
            menu_option = input.nextInt();
            switch (menu_option) {
                case 1:
                    try {
                        System.out.println("Escribir en archivo:\n 1)Insertar nodo\n 2)Crear conexion entre nodos");
                        switch (input.nextInt()) {
                            case 1:
                                o_fm.getReg().setRegisterData(o_fm);
                                break;
                            case 2:
                                o_fm.getReg().connectionNodeToNode(o_fm);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.printf("ERROR AL ESCRIBIR EN ARCHIVOS");
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        for (Register r : o_fm.readSequential()) {
                            //System.out.println("----------------------------------------------------------");
                            System.out.println("LLAVE: " + r.getKey().trim());
                            System.out.println("CIUDAD: " + r.getCity().trim());
                            System.out.println("PAIS: " + r.getCountry().trim());
                            System.out.println("CONEXIONES:");
                            for (int i = 0; i < r.getCONNECTIONS_NUMBER(); i++) {
                                if (!r.connected_key[i].trim().equals("NULL")) {
                                    System.out.println("LLAVE: " + r.connected_key[i].trim());
                                    System.out.println("DISTANCIA (KM): " + r.weight[i]);
                                    System.out.println("CABLE: " + r.wire_name[i].trim());
                                    System.out.println("..........................................................");
                                }
                            }
                            System.out.println("==========================================================");
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR AL LEER EL ARCHIVO");
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.println("ESCRIBIR DATOS DESDE ARCHIVO SECUENCIAL");
                    try {
                        o_fm.fileSeqToSeqIndex(false);
                    } catch (Exception e) {
                        System.out.println("OCURRIO UN ERROR AL LEER EL ARCHIVO DE MANERA SCUENCIAL");
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    System.out.println("METODO DE BUSQUEDA PRIMERO EN ANCHURA");
                    System.out.println("Ingresa la llave del registro raiz:");
                    key = input.next();
                    System.out.println("Ingresa el nombre de la ciudad a buscar");
                    city = input.next();
                    try {
                        sm.BreadthFirst(o_fm.getRegisterFromFile(key, true), city);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    String sort_name, sort_weight;
                    System.out.println("METODO DE BUSQUEDA PRIMERO EN PROFUNDIDAD");
                    System.out.println("Ingresa la llave del registro raiz:");
                    key = input.next();
                    System.out.println("Ingresa el nombre de la ciudad a buscar :");
                    city = input.next();
                    try {
                        sort_name = sm.DepthFirst(o_fm.getRegisterFromFile(key, true), city, true);
                        sort_weight = sm.DepthFirst(o_fm.getRegisterFromFile(key, true), city, false);
                        System.out.println("Considerando el peso como el nombre de la ciudad\n"+sort_name);
                        System.out.println("Considerando el peso como la longitud de cable\n"+sort_weight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    System.out.println("Metodo de Busqueda Grafos O");
                    System.out.println("Ingresa la llave del registro raiz:");
                    origin = input.next();
                    System.out.println("Ingrese el Nodo Destino");
                    destiny = input.next();
                    try {
                        List<String> result = sm.grafosO(o_fm.getRegisterFromFile(origin, false), destiny);
                        Object path [] = result.toArray();
                        for(int i = path.length - 1; i >= 0; i--){
                            System.out.println(path[i]);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    System.out.println("Metodo de los Algoritmos A y A*");
                    System.out.println("Ingrese la llave del registro raiz :");
                    key = input.next();
                    System.out.println("Ingrese el Nodo Destino");
                    destiny = input.next();
                    try {
                        List<String> result = sm.grafosA(o_fm.getRegisterFromFile(key, false), destiny);
                        Object path [] = result.toArray();
                        for(int i = path.length - 1; i >= 0; i--){
                            System.out.println(path[i]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                default:
                    break;
            }
        } while (menu_option != 8);
    }
}
