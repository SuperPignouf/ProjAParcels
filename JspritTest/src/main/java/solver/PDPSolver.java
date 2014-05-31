package solver;

import java.sql.SQLException;
import java.util.Collection;

import jsprit.analysis.toolbox.SolutionPrinter;
import jsprit.analysis.toolbox.SolutionPrinter.Print;
import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.box.SchrimpfFactory;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.util.Solutions;
import dbHandler.RouteStorer;

/**
 * Cette classe se charge de calculer un itineraire optimal pour un probleme de routage donne.
 */
public class PDPSolver {

	/**
	 * Constructeur.
	 * @see Main.JspritTest.Main#main(String[])
	 * @param problem Le probleme a resoudre.
	 */
	public PDPSolver(VehicleRoutingProblem problem) throws SQLException{
		/*
		 * Instanciation de l'algorithme.
		 */
		VehicleRoutingAlgorithm algorithm = new SchrimpfFactory().createAlgorithm(problem);
		
		/**
		 * Recherche de solution
		 */
		Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
		
		/*
		 * On garde la meilleure solution.
		 */
		VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
		
		/*
		 * Et on la stocke en base de donnees.
		 */
		new RouteStorer().store(bestSolution);

		/*
		 * Affichage en console de la solution.
		 */
		SolutionPrinter.print(problem, bestSolution, Print.VERBOSE);
	}

}
