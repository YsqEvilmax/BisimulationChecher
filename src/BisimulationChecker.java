import java.io.*;
import java.util.*;
/**
 * 
 */

/**
 * This class can build state machines based on files, check bisimulation 
 * equivalence and return the result.
 * 
 * @author Shawn
 *
 */
public class BisimulationChecker {
	/**
	 * This is the constructor of the class, doing nothing but initialize 
	 *  the members like P, Q, allActions and allTrans
	 */
	BisimulationChecker()
	{
		this.P = new Graph("Process P");
		this.Q = new Graph("Process Q");
		this.allActions = new HashSet<String>();
		this.allTrans = new HashSet<Edge>(); 
	}
	
	/**
	 * This method reads the contents of the files pointed to by the Strings
	 * fileP and fileQ. If any argument is null (or a file with that name does
	 * not exist), the user is (repeatedly) asked to enter a filename until a
	 * valid file is found.
	 * 
	 * @param fileP
	 * @param fileQ
	 */
	public void readInput(String fileP, String fileQ) throws IOException
	{
		File fP = ValidFile(fileP);
		File fQ = ValidFile(fileQ);	
		
		BuildGraph(fP, P);
		BuildGraph(fQ, Q);		
	}
	
	/**
	 * Saves the results of the bisimulation by overwriting the contents of the
	 * file pointed to by the String filename. If the filename is null then user
	 * is asked to enter a filename on the console.
	 * 
	 * @param filename
	 */
	public void writeOutput(String filename) throws IOException
	{
        File file=new File(filename);
        if(!file.exists())
            file.createNewFile();
        
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			//start a new line: \n on Linux; \r\n on Windows
			bw.write("Process P\r\n");
			bw.write(P.toString());
			bw.write("Process Q\r\n");
			bw.write(Q.toString());
			bw.write("Bisimulation Results\r\n");
			bw.write(this.toString());
			bw.write("Bisimulation Answer\r\n");
			bw.write(isBisimilar() ? "Yes" : "No");
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	/**
	 * This method perform the bisimulaiton equivalence algorithm, checking the 
	 * member machines P and Q and storing the result.
	 */
	public void performBisimulation()
	{
		Set<Set<Vertex>> r1 = new HashSet<Set<Vertex>>();	
		Set<Vertex> temp = new HashSet<Vertex>();
		temp.addAll(P.vertexs);
		temp.addAll(Q.vertexs);
		r1.add(temp);
		
		Set<Set<Vertex>> r = new HashSet<Set<Vertex>>(r1);	;
		
		Set<Set<Vertex>> waiting = new HashSet<Set<Vertex>>(r1);
		
		allActions.addAll(P.actions);
		allActions.addAll(Q.actions);
		
        allTrans.addAll(P.getEdges());
        allTrans.addAll(Q.getEdges());
		
		while(!waiting.isEmpty())
		{
			Set<Vertex> partitionPrime = waiting.iterator().next();
			waiting.remove(partitionPrime);
			
			for(String action : allActions)
			{
				Set<Set<Vertex>> matchP = new HashSet<Set<Vertex>>();
				
                for (Set<Vertex> partition : r) 
                {
                    Set<Vertex> taP = Split(partition, partitionPrime, action).get(0);

                    if (!taP.isEmpty() && !taP.equals(partition))
                    {
                        matchP.add(partition);
                    }
                }
                
                for (Set<Vertex> partition : matchP) 
                {
                    List<Set<Vertex>> splited = Split(partition, partitionPrime, action);

                    r.remove(partition);
                    r.addAll(splited);

                    waiting.remove(partition);
                    waiting.addAll(splited);
                }
			}
		}
		CheckResult = r;
	}
	
	/**
	 * This method split Set A based on action from A to B. The result is put in
	 * a list, where first element is the subset of A which can be satisfied by action
	 * a; second is the subset which cannot.
	 * 
	 * @param A
	 * @param B
	 * @param action
	 * @return
	 */
	public List<Set<Vertex>> Split(Set<Vertex> A, Set<Vertex> B, String action)
	{
		//Normally, the result only contains two elements, first is the set which can be transfromed by action; second is the set for the rest
		List<Set<Vertex>> result = new LinkedList<Set<Vertex>>();
		
		Set<Vertex> can = new HashSet<Vertex>();
		Set<Vertex> cannot = new HashSet<Vertex>();
		
        for (Vertex a : A) 
        {
            boolean found = false;
            for ( Vertex b : B)
            {
                if (allTrans.contains(new Edge(a, b, action)))
                {
                    found = true;
                    break;
                }
            }

            (found ? can : cannot).add(a);
        }
        
        result.add(can);
        result.add(cannot);
        return result;
	}
	
	/**
	 * This method check whether the result after performing bisimulation algorithm
	 * is bisimilar or not.
	 * 
	 * @return
	 */
	public boolean isBisimilar() 
	{
		boolean isBisimilar = false;

		for (Set<Vertex> partition : CheckResult)
		{
			boolean fromP = false, fromQ = false;

			for (Vertex state : partition)
			{
				fromP = fromP || P.vertexs.contains(state);
				fromQ = fromQ || Q.vertexs.contains(state);
			}

			isBisimilar = fromP && fromQ;

			//if there is any pair of states not bisimilar, the result is bisimilar.
			if (!isBisimilar) break;
		}
		return isBisimilar;
	}

	/**
	 * This method translate checking result into string. 
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (Set<Vertex> partition : CheckResult)
		{
			String temp = new String();
			for (Vertex state : partition) 
			{
				temp += "," + state.toString();
			}
	    	if(!temp.isEmpty())
	    		temp = temp.substring(1, temp.length());
	    	 sb.append(temp);
	    	 sb.append("\r\n");
		}
		return sb.toString();
	};
	
	/**
	 * This method justify whether the file path is available or not. 
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private File ValidFile(String filePath) throws IOException
	{
		File f = new File(filePath);
		while(filePath.isEmpty() || !f.exists())
		{
		      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		      System.out.printf("Previous file is invalid, please enter a new one for %s:", filePath);
		      filePath = br.readLine();
		      f = new File(filePath);
		}
		return f;
	}
	
	/**
	 * This method build the member machine g, which fundamentally is a  graph, 
	 * based on the file content.
	 * 
	 * @param f
	 * @param g
	 * @throws IOException
	 */
	private void BuildGraph(File f, Graph g) throws IOException
	{
		try {
			Finder<Vertex> finder = new Finder<Vertex>(g.vertexs);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = br.readLine();
			while(!line.contains("!"))
			{
				String[] s = line.split(",");
				int stateFrom = Integer.parseInt(s[0]);
				Vertex from = new Vertex(stateFrom, g.getName());
				s = s[1].split(":");
				String trans = s[0];
				int stateTo = Integer.parseInt(s[1]);
				Vertex to = new Vertex(stateTo, g.getName());
				if (!g.vertexs.contains(from)) g.vertexs.add(from);
				if (!g.vertexs.contains(to)) g.vertexs.add(to);
				g.DirectedLink(finder.Find(from), finder.Find(to), trans);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	//This member is the state machine P.
	private Graph P;
	//This member is the state machine Q.
	private Graph Q;
	//This member keeps all the actions transformed between P and Q.
	private Set<String> allActions;
	//This member stores all the transition in P and Q.
    private Set<Edge> allTrans; 	
    //This member stores the result after performing bisimulation equivalence algorithm.
	private Set<Set<Vertex>> CheckResult;
}

/**
 * This generic class perform finding a certain element from a collection
 * 
 * @author Shawn
 *
 * @param <T>
 */
class Finder<T>
{
	Finder(){}
	Finder(Collection<T> C)
	{
		this();
		this.C = C;
	}
	
	/**
	 * This method return the element in a collection which is equal to t.
	 * 
	 * @param t
	 * @return
	 */
	public T Find(T t)
	{
		T result = null;
		for(T temp : C)
		{
			if(temp.equals(t))
			{
				result = temp;
				break;
			}
		}
		return result;
	}
	
	public Collection<T> C;
}
