package ia.volleyball;

import java.util.ArrayList;

public class SearchMethods {
    private FilesManager fm;

    public SearchMethods(FilesManager fm){
        this.fm = fm;
    }

    public void BreadthFirst(Register r, String node_name){
        Tree T;
        ArrayList<Node> S;
        ArrayList<Node> explored;
        Node node;
        boolean fail = false;
        boolean repeated = false;
        T = new Tree(r);
        if(T.root.data.getCity().trim().equals(node_name)){
            System.out.println("Solucion: " + T.root.data.getCity().trim());
            return;
        }
        S = new ArrayList<>();
        S.add(T.root);
        explored = new ArrayList<>();
        while(!fail){
           if(S.isEmpty()){
               System.out.println("Se recorrieron todos los nodos");
               fail = true;
           }else {
               node = S.get(0);
               S.remove(0);
               explored.add(node);
               for (int i = 0; i < fm.getReg().connected_key.length; i++) {
                   if(fm.getReg().available[i] != 0) {
                       for (Node anExplored : explored) {
                           if (fm.getReg().connected_city[i].trim().equals(anExplored.data.getCity().trim()))
                               repeated = true;
                       }
                       if (!repeated) {
                           T.addChild(node, fm.getReg().getConnectedData(i));
                           if (fm.getReg().connected_city[i].trim().equals(node_name)) {
                               T.getPathToRoot(fm.getReg().connected_city[i].trim());
                               return;
                           } else {
                               S.add(T.getPointer().node_childs.get(fm.getReg().connected_city[i].trim()));
                           }
                       }
                       repeated = false;
                   }
               }
               try {
                   System.out.println(S.size());
                   fm.getRegisterFromFile(S.get(0).data.getKey());
               }catch (Exception e) { e.printStackTrace(); }
           }
        }
    }

    public void DepthFirst(Register r, String node_name){
        Tree T;
        ArrayList<Node> W;
        ArrayList<Node> explored;
        String less_value = null;
        int index = 0;
        boolean repeated = false;
        T = new Tree(r);
        if(T.root.data.getCity().trim().equals(node_name)){
            System.out.println("Solucion: " + T.root.data.getCity().trim());
            return;
        }
        W = new ArrayList<>();
        W.add(T.root);
        explored = new ArrayList<>();
        explored.add(T.root);
        //System.out.printf(W.get(0).data.getCity().trim() + " > ");
        while(true) {
            //Step 2
            for (int i = 0; i < fm.getReg().connected_key.length; i++) {
                if(fm.getReg().available[i] != 0) {
                    for (Node node : explored) {
                        if (fm.getReg().connected_city[i].trim().equals(node.data.getCity().trim()))
                            repeated = true;
                    }
                    if (!repeated) {
                        if(fm.getReg().connected_city[i].trim().equals(node_name)){
                            //System.out.println(fm.getReg().connected_city[i].trim());
                            T.addChild(T.getPointer(), fm.getReg().getConnectedData(i));
                            T.getPathToRoot(fm.getReg().connected_city[i].trim());
                            return;
                        }else {
                            if (less_value == null)
                                less_value = fm.getReg().connected_city[i].trim();
                            if (less_value.compareTo(fm.getReg().connected_city[i].trim()) >= 0) {
                                less_value = fm.getReg().connected_city[i].trim();
                                index = i;
                            }
                        }
                    }
                }
                repeated = false;
            }
            if(less_value != null) {
                T.addChild(W.get(0), fm.getReg().getConnectedData(index));
                W.remove(0);
                W.add(T.getPointer().node_childs.get(less_value));
                explored.add(T.getPointer().node_childs.get(less_value));
                T.setPointer(T.getPointer().node_childs.get(less_value));
                //System.out.printf(W.get(0).data.getCity().trim() + " > ");
                try {
                    fm.getRegisterFromFile(W.get(0).data.getKey());
                }catch (Exception e){ e.printStackTrace(); }
            }else {
                //Step 3
                if (T.root.data.getCity().trim().equals(W.get(0).data.getCity().trim())) {
                    System.out.printf(W.get(0).data.getCity().trim());
                    System.out.println("\nEl proceso ha terminado y no se encontro el resultado");
                    return;
                } else {
                    //Step 4
                    W.remove(0);
                    T.setPointer(T.getPointer().parent_node);
                    W.add(T.getPointer());
                    try {
                        fm.getRegisterFromFile(W.get(0).data.getKey());
                    }catch (Exception e) { e.printStackTrace(); }
                }
            }
            less_value = null;
        }
    }
}
