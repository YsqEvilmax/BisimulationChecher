import java.io.IOException;


public class Laucher {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BisimulationChecker checker = new BisimulationChecker ();
		checker.readInput("P.txt", "Q.txt");
		checker.performBisimulation();
		checker.writeOutput("Result.txt");
		System.exit(0);
	}

}
