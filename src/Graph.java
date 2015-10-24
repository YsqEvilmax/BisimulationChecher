import java.util.*;
/**
 * 
 */
/**
 * This class is an abstract of state 
 * @author Shawn
 *
 */
class Vertex
{
	/**
	 * This method is constructor, initialize neighbourhoods and children.
	 */
    public Vertex()
    {
        this.neighbourhoods = new LinkedList<Edge>();
        this.children = new LinkedList<Edge>();
    }

    /**
     * Create a new vertex with id
     * @param id
     */
    public Vertex(int id)
    {
    	this();
        this.ID = id;
    }
    
    /**
     * Create a new vertex with id and value
     * @param id
     * @param value
     */
    public Vertex(int id, String value)
    {
    	this(id);
        this.value = value;
    }

    /**
     * Link this vertex to another vertex by an edge with value 
     * @param v
     * @param value
     * @return
     */
    public Edge LinkTo(Vertex v, String value)
    {
        Edge e = new Edge(this, v, value);
        this.neighbourhoods.add(e);
        return e;
    }

    //id of this vertex
    public int ID;
    //value of this vertex
    public String value;
    //collection of all the vertexes which are linked with this vertex
    public List<Edge> neighbourhoods ;
    //parent vertex which links to this one
    public Edge parent;
    //children vertexes which are linked by this vertex
    public List<Edge> children;

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[] {value, String.format("%d", this.ID)});
    }

    @Override
    public boolean equals(Object rhs) {
        Vertex r = (Vertex) rhs;
        return (this.ID == r.ID) && (this.value == r.value);
    }
    
    @Override
	public String toString()
    {
    	return String.format("%d", this.ID);
    }
}

/**
 * This class acts as a edge links two vertexes
 * 
 * @author Shawn
 *
 */
class Edge
{
	/**
	 * constructor
	 */
    public Edge()
    {
    }

    /**
     * Create a new edge with value v
     * @param v
     */
    public Edge(String v)
    {
    	this();
        this.value = v;
    }

    /**
     * Create a new edge with value v and then use it to link two vertex f and t
     * @param f
     * @param t
     * @param v
     */
    public Edge(Vertex f, Vertex t, String v)
    {
    	this(v);
        Link(f, t);
    }

    /**
     * Link vertex f to vertex t
     * @param f
     * @param t
     */
    public void Link(Vertex f, Vertex t)
    {
        this.from = f;
        this.to = t;
    }

    //The opposite edge
    public Edge piar;
    //The vertex from
    public Vertex from;
    //The vertex to
    public Vertex to;
    //The value of this edge
    public String value;
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[] { this.from.toString(), this.to.toString(), this.value });
    }

    @Override
    public boolean equals(Object rhs) {
        Edge r = (Edge) rhs;
        return this.from.equals(r.from) && this.to.equals(r.to) && this.value.equals(r.value);
    }
    
    @Override
	public String toString()
    {
    	String describe = new String();
    	describe = String.format("(%d,%s,%d)", from.ID, value, to.ID);
    	return describe;
    }
}

/**
 * This class is an abstract of machine
 * 
 * @author Shawn
 *
 */
class Graph
{
	/**
	 * Constructor£¬ initialize vertexs and actions
	 */
    public Graph()
    {
        this.vertexs = new HashSet<Vertex>();
        this.actions = new HashSet<String>();
    }
    
    /**
     * Create a new graph with an identical name
     * @param name
     */
    public Graph(String name)
    {
    	this();
    	this.name = name;
    }

    /**
     * Link vertex v1 to v2 together by an edge with value trans 
     * @param v1
     * @param v2
     * @param trans
     * @return
     */
    public Edge DirectedLink(Vertex v1, Vertex v2, String trans )
    {
        if (!vertexs.contains(v1)) vertexs.add(v1);
        if (!vertexs.contains(v2)) vertexs.add(v2);
        actions.add(trans);
        return v1.LinkTo(v2, trans);
    }

    /**
     * Link vertex v1 and v2 mutually by an edge with values value1 and value2
     * @param v1
     * @param v2
     * @param value1
     * @param value2
     */
    public void UndirectedLink(Vertex v1, Vertex v2, String value1, String value2)
    {
        Edge e1 = DirectedLink(v1, v2, value1);
        Edge e2 = DirectedLink(v2, v1, value2);
        e1.piar = e2;
        e2.piar = e1;
    }

    //all the vertexes in this graph
    public HashSet<Vertex> vertexs;
    //all the values the edges in this graph have
    public HashSet<String> actions;

    /**
     * Return all the edges in this graph
     * @return
     */
    public Set<Edge> getEdges()
    {
    	Set<Edge> edges = new HashSet<Edge>();
    	for(Vertex v : vertexs)
    	{
    		edges.addAll(v.neighbourhoods);
    	}
    	return edges;
    }
    
    //name of this graph
    private String name;
    public String getName(){return name;}
    
    @Override
	public String toString()
    {
    	 StringBuilder sb = new StringBuilder();
    	 sb.append(stateToString());
    	 sb.append("\r\n");
    	 sb.append(actionToString());
    	 sb.append("\r\n");
    	 sb.append(transitionToString());
    	 sb.append("\r\n");
    	 return sb.toString();
    }
    
    public String stateToString()
    {
    	String describe = new String();
    	for(Vertex v : vertexs)
    	{
    		describe += "," + Integer.toString(v.ID);
    	}
    	if(!describe.isEmpty())
    		describe = describe.substring(1, describe.length());
    	return "S=" + describe;
    }
    
    public String actionToString()
    {
    	String describe = new String();
    	for(String s : actions)
    	{
    		if(!s.isEmpty())
    			describe += "," + s;
    	}
    	if(!describe.isEmpty())
    		describe = describe.substring(1, describe.length());
    	return "A=" + describe;    	
    }
    
    public String transitionToString()
    {
    	String describe = new String();
    	for(Edge e : this.getEdges())
    	{
    		describe += "," +e.toString();
    	}
    	if(!describe.isEmpty())
    		describe = describe.substring(1, describe.length());
    	return "T=" + describe; 
    }
}