package ia.volleyball;

public class TreeIndexFile {
    private NodeIndexFile root;

    public TreeIndexFile(){
        root = null;
    }

    public void addNode(NodeIndexFile n){
        if(this.root == null)
            this.root = n;
        else{
            System.out.println("Root > " + this.root.key);
            this.root.addChild(n);
        }
    }


    public void printTree(){
        System.out.println(this.root.key);
        System.out.println("Hijo izquierda");
        if(this.root.node_left!=null)
            System.out.println(this.root.node_left.key);
        else
            System.out.println("null");
        System.out.println("Hijo derecha");
        if(this.root.node_right != null)
            System.out.println(this.root.node_right.key);
        else
            System.out.println("null");
    }
}
