//abstract class. sets id for each RouteNode object
public abstract class RouteNode {
    public String id;

    public RouteNode(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }



    
}
