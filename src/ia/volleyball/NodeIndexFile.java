package ia.volleyball;

public class NodeIndexFile {
    public String key;
    public int logic_address;
    public NodeIndexFile node_left;
    public NodeIndexFile node_right;

    public NodeIndexFile(String key, int l_a){
        this.key = key;
        this.logic_address = l_a;
        this.node_left = null;
        this.node_right = null;
    }

    public void addChild(NodeIndexFile n){
        if(this.key.compareTo(n.key) > 0){
            if(this.node_left == null) {
                System.out.println("< left " + n.key);
                this.node_left = n;
            }
            else {
                this.node_left.addChild(n);
            }
        }else{
            if(this.node_right == null) {
                System.out.println("> right " + n.key);
                this.node_right = n;
            }
            else
                this.node_right.addChild(n);
        }
    }

}
