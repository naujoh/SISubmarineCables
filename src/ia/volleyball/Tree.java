package ia.volleyball;

/**
 *
 * @author Emmanuel
 */
public class Tree {
    public Node root;
    public Node child_node;
    private Node pointer;

    public Tree(){
        root = null;
    }
    
    public Tree(Register r){
        root = new Node(r);
    }
    
    public void addChild(Node parent, Register r){
       if(!parent.node_childs.containsKey(r.getCity())){
         this.child_node = new Node(r,parent);
         parent.node_childs.put(r.getCity(), child_node);
       }else{
           System.out.println("La ciudad ya existe en el arbol");
       }
       this.pointer = parent;
    }

    public void getPathToRoot(String key){
        boolean limit = false;
        this.pointer = this.pointer.node_childs.get(key);
        System.out.println("Solucion:");
        while (limit == false) {
            if (pointer.parent_node == null)
                limit = true;
            System.out.printf(pointer.data.getCity().trim() + " < ");
            pointer = pointer.parent_node;
        }
        System.out.println();
    }

    public Node getPointer() {
        return pointer;
    }

    public void setPointer(Node pointer) {
        this.pointer = pointer;
    }
}
