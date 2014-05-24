<?php if(!isset($_SESSION)) session_start(); ?>


<!DOCTYPE  html>


<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Etapes de la Route</h1>
		</header>
		
		<section> <!--Zone centrale-->
		
			<?php
			
			//------------------------------------------------
			// Connection avec la base de données.
			//------------------------------------------------

				
					try{
					$bdd = new PDO('mysql:host=localhost;dbname=parcels', 'root', '', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
					}	
					catch(Exception $e){
						die('Error : ' .$e -> getMessage());
						echo 'Something went wrong...';
					}
				
				
				$bdd->exec("SET CHARACTER SET utf8");
				
					if(isset($_GET['id'])){
					$response = $bdd->query('SELECT *
						FROM step
						WHERE route_id= "' . $_GET['id'] . '" 
						ORDER BY step_number '); // on sélectionne les étapes de la route pour affichage.
					}
					else{
						header('Location : welcome.php');
						exit();
					}
					
					
					
		
					?> <table border="1" style="width:300px"><tr>
						  <th>step_number</th>
						  <th>latitude</th> 
						  <th>longitude</th>
						  <th>arrival_time</th>
						  <th>departure_time</th>
						  <th>performed</th>
						</tr> <?php
					while ($data = $response -> fetch()){
						?>
    					<tr>
   		    			<td><?php echo $data['step_number']; ?></td>
   		    			<td><?php echo $data['latitude']; ?></td>
   		    			<td><?php echo $data['longitude']; ?></td>
   		    			<td><?php echo $data['arrival_time']; ?></td>
   		    			<td><?php echo $data['departure_time']; ?></td>
   		    			<td><?php echo $data['performed']; ?></td>
    					</tr>
						<?php
					}
					
					$response->closeCursor(); // Termine le traitement de la requête				
			?>	

		</section>	
		
		<nav> <!--Menu-->
			<?php include ("menu.php"); ?>
		</nav>
		
		<footer> <!--Footer-->
			<?php include ("footer.php"); ?>
		</footer>
		
	</body>

</html>
