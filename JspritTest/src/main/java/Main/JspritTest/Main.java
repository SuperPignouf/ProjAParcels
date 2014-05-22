package Main.JspritTest;

import java.io.IOException;
import java.sql.SQLException;

import solver.PDPSolver;
import dbHandler.ProblemBuilder;

/**
 * Classe main.
 * @author Aurelien Plisnier
 */
public class Main {

	public static void main(String[] args) {		
		try {
			/*
			 * On instancie un resolveur de PDP en lui passant un PDP en parametre.
			 * Le PDP est construit a partir des ordres de transports courants 
			 * enregistres en base de donnees.
			 */
			new PDPSolver(new ProblemBuilder().build());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}