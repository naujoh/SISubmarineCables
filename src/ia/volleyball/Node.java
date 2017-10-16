package ia.volleyball;

import java.util.HashMap;
import java.util.Map;

public class Node {
  
    public Node parent_node;
    public Register data;
    public Map<String, Node> node_childs;


    public Node(Register r){
        this.parent_node = null;
        this.data = r;
        this.node_childs = new HashMap<>();
        
    }
    
    public Node(Register r, Node parent){
      this.parent_node = parent;
      this.data = r;
      this.node_childs = new HashMap<>();
    }
}
