package Main.JspritTest;

/*******************************************************************************
 * Copyright (C) 2013 Stefan Schroeder
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

import java.util.Arrays;
import java.util.Collection;

import jsprit.analysis.toolbox.GraphStreamViewer;
import jsprit.analysis.toolbox.Plotter;
import jsprit.analysis.toolbox.SolutionPrinter;
import jsprit.analysis.toolbox.SolutionPrinter.Print;
import jsprit.core.algorithm.VehicleRoutingAlgorithm;
import jsprit.core.algorithm.box.SchrimpfFactory;
import jsprit.core.problem.VehicleRoutingProblem;
import jsprit.core.problem.VehicleRoutingProblem.FleetSize;
import jsprit.core.problem.io.VrpXMLWriter;
import jsprit.core.problem.job.Shipment;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.vehicle.VehicleImpl;
import jsprit.core.problem.vehicle.VehicleTypeImpl;
import jsprit.core.util.Coordinate;
import jsprit.core.util.Solutions;
import jsprit.util.Examples;


public class SimpleEnRoutePickupAndDeliveryExample {

	public static void main(String[] args) {
		/*
		 * some preparation - create output folder
		 */
		Examples.createOutputFolder();

		/*
		 * build the types and vehicles from table above 
		 * here it is assumed the variable costs are dependent on distance (rather than time or any other measure)
		 * latest arrival
		 * time of 20 (which corresponds to a operation time of 1236 since the earliestStart of the vehicle is set
		 * to 0 by default).
		 */
		VehicleTypeImpl vehicleType1 = VehicleTypeImpl.Builder.newInstance("type1").addCapacityDimension(0,120).setCostPerDistance(1.0).build();
		VehicleImpl vehicle1_1 = VehicleImpl.Builder.newInstance("vehicle1_1").setStartLocationCoordinate(Coordinate.newInstance(10, 10)).setLatestArrival(20).setType(vehicleType1).build();
		VehicleImpl vehicle1_2 = VehicleImpl.Builder.newInstance("vehicle1_2").setStartLocationCoordinate(Coordinate.newInstance(10, 10)).setLatestArrival(20).setType(vehicleType1).build();

		VehicleTypeImpl vehicleType2 = VehicleTypeImpl.Builder.newInstance("type2").addCapacityDimension(0,160).setCostPerDistance(1.2).build();
		VehicleImpl vehicle2_1 = VehicleImpl.Builder.newInstance("vehicle2_1").setStartLocationCoordinate(Coordinate.newInstance(10, 10)).setLatestArrival(20).setType(vehicleType2).build();
		VehicleImpl vehicle2_2 = VehicleImpl.Builder.newInstance("vehicle2_2").setStartLocationCoordinate(Coordinate.newInstance(10, 10)).setLatestArrival(20).setType(vehicleType2).build();

		VehicleTypeImpl vehicleType3 = VehicleTypeImpl.Builder.newInstance("type3").addCapacityDimension(0,300).setCostPerDistance(1.4).build();
		VehicleImpl vehicle3 = VehicleImpl.Builder.newInstance("vehicle3").setStartLocationCoordinate(Coordinate.newInstance(10, 10)).setLatestArrival(20).setType(vehicleType3).build();


		/*
		 * build shipments at the required locations.
		 * 4 shipments
		 * 1: (5,7)->(6,9)
		 * 2: (5,13)->(6,11)
		 * 3: (15,7)->(14,9)
		 * 4: (15,13)->(14,11)
		 */

		Shipment shipment1 = Shipment.Builder.newInstance("1").addSizeDimension(0, 50).setPickupCoord(Coordinate.newInstance(5, 7)).setPickupServiceTime(1).setDeliveryCoord(Coordinate.newInstance(6, 9)).setDeliveryServiceTime(1).build();
		Shipment shipment2 = Shipment.Builder.newInstance("2").addSizeDimension(0, 70).setPickupCoord(Coordinate.newInstance(5, 13)).setPickupServiceTime(1).setDeliveryCoord(Coordinate.newInstance(6, 11)).setDeliveryServiceTime(1).build();

		Shipment shipment3 = Shipment.Builder.newInstance("3").addSizeDimension(0, 200).setPickupCoord(Coordinate.newInstance(15, 7)).setPickupServiceTime(1).setDeliveryCoord(Coordinate.newInstance(14, 9)).setDeliveryServiceTime(1).build();
		Shipment shipment4 = Shipment.Builder.newInstance("4").addSizeDimension(0, 10).setPickupCoord(Coordinate.newInstance(15, 13)).setPickupServiceTime(1).setDeliveryCoord(Coordinate.newInstance(14, 11)).setDeliveryServiceTime(1).build();

		/*
		 * build deliveries, (implicitly picked up in the depot)
		 * 1: (4,8)
		 * 2: (4,12)
		 * 3: (16,8)
		 * 4: (16,12)

		Delivery delivery1 = (Delivery) Delivery.Builder.newInstance("5").addSizeDimension(0, 1).setCoord(Coordinate.newInstance(4, 8)).build();
		Delivery delivery2 = (Delivery) Delivery.Builder.newInstance("6").addSizeDimension(0, 1).setCoord(Coordinate.newInstance(4, 12)).build();
		Delivery delivery3 = (Delivery) Delivery.Builder.newInstance("7").addSizeDimension(0, 1).setCoord(Coordinate.newInstance(16, 8)).build();
		Delivery delivery4 = (Delivery) Delivery.Builder.newInstance("8").addSizeDimension(0, 1).setCoord(Coordinate.newInstance(16, 12)).build(); */


		VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		vrpBuilder.addVehicle(vehicle1_1).addVehicle(vehicle1_2).addVehicle(vehicle2_1).addVehicle(vehicle2_2).addVehicle(vehicle3);
		vrpBuilder.setFleetSize(FleetSize.FINITE);
		/*
		 * add penalty vehicles
		 * para1 is a penalty-factor - variable costs are penalty-factor times higher than in the original vehicle type
		 * para2 is an absolute penalty-fixed costs value - if fixed costs are 0.0 in the original vehicle type, multiplying it with
		 * penalty factor does not make much sense (at least has no effect), thus penalty fixed costs are an absolute penalty value
		 */
		vrpBuilder.addPenaltyVehicles(5.0, 5000);
		vrpBuilder.addJob(shipment1).addJob(shipment2).addJob(shipment3).addJob(shipment4);

		VehicleRoutingProblem problem = vrpBuilder.build();

		/*
		 * get the algorithm out-of-the-box.
		 */
		VehicleRoutingAlgorithm algorithm = new SchrimpfFactory().createAlgorithm(problem);

		/*
		 * and search a solution
		 */
		Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();

		/*
		 * get the best
		 */
		VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

		/*
		 * write out problem and solution to xml-file
		 */
		new VrpXMLWriter(problem, solutions).write("output/shipment-problem-with-solution.xml");

		/*
		 * print nRoutes and totalCosts of bestSolution
		 */
		SolutionPrinter.print(bestSolution);

		/*
		 * plot problem without solution
		 */
		Plotter problemPlotter = new Plotter(problem);
		problemPlotter.plotShipments(true);
		problemPlotter.plot("output/simpleEnRoutePickupAndDeliveryExample_problem.png", "en-route pickup and delivery");

		/*
		 * plot problem with solution
		 */
		Plotter solutionPlotter = new Plotter(problem,Arrays.asList(Solutions.bestOf(solutions).getRoutes().iterator().next()));
		solutionPlotter.plotShipments(true);
		solutionPlotter.plot("output/simpleEnRoutePickupAndDeliveryExample_solution.png", "en-route pickup and delivery");

		new GraphStreamViewer(problem).setRenderShipments(true).display();
		SolutionPrinter.print(problem, bestSolution, Print.VERBOSE);
	}

}