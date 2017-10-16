package ia.volleyball;

import javax.xml.bind.SchemaOutputResolver;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Register o_register = new Register();
        FilesManager o_fm = new FilesManager(o_register, "master_submarine_cables", "index_submarine_cables" );
        SearchMethods sm = new SearchMethods(o_fm);
        String key, city;
        int menu_option, another_reg;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("==========================================================");
            System.out.println("\nIngresa:\n1)Escribir en archivo\n2)Leer de manera secuencial" +
                               "\n3)Insertar datos de archivo secuencial\n4)Busqueda primero en anchura " +
                               "\n5)Busqueda primero en profundidad \n6)Salir");
            System.out.println("==========================================================");
            menu_option = input.nextInt();
            switch (menu_option) {
                case 1:
                    do{
                        try {
                            o_fm.getReg().setData();
                            o_fm.writeData(true);
                        }catch (Exception e){
                            System.out.printf("ERROR AL ESCRIBIR EN ARCHIVOS");
                            e.printStackTrace();
                        }
                        System.out.println("INSERTAR OTRA CIUDAD 1:SI | 0:NO");
                        another_reg = input.nextInt();
                    }while(another_reg == 1);
                    break;
                case 2:
                    try {
                        for (Register r: o_fm.readSequential()) {
                            System.out.println(r.getCity());
                        }
                    }catch (Exception e){
                        System.out.println("ERROR AL LEER EL ARCHIVO");
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.println("ESCRIBIR DATOS DESDE ARCHIVO SECUENCIAL");
                    try {
                        o_fm.fileSeqToSeqIndex(false);
                    }catch(Exception e){
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
                        sm.BreadthFirst(o_fm.getRegisterFromFile(key), city);
                    }catch (Exception e ){ e.printStackTrace(); }
                    break;
                case 5:
                    System.out.println("METODO DE BUSQUEDA PRIMERO EN PROFUNDIDAD");
                    System.out.println("Ingresa la llave del registro raiz:");
                    key = input.next();
                    System.out.println("Ingresa el nombre de la ciudad a buscar");
                    city = input.next();
                    try {
                        sm.DepthFirst(o_fm.getRegisterFromFile(key), city);
                    }catch (Exception e ){ e.printStackTrace(); }
                    break;
                default:
                    break;
            }
        }while (menu_option != 6);
    }
}
