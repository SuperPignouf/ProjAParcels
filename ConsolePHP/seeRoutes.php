<?php if(!isset($_SESSION)) session_start(); ?>


<!DOCTYPE  html>


<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Nos Itinéraires pour la Journée</h1>
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
				

					$response = $bdd->query('SELECT *
						FROM route 
						WHERE CURRENT_TIMESTAMP < to_date
						ORDER BY route_id '); // on sélectionne les routes valides pour affichage.
				
					
		
					?> <table border="1" style="width:300px"><tr>
						  <th>route_id</th>
						  <th>transporter_id</th> 
						  <th>vehicule_name</th>
						  <th>Steps</th>
						</tr> <?php
					while ($data = $response -> fetch()){
						?>
    					<tr>
   		    			<td><?php echo $data['route_id']; ?></td>
   		    			<td><?php echo $data['transporter_id']; ?></td>
   		    			<td><?php echo $data['vehicule_name']; ?></td>
   		    			<td><a href= <?php echo '"seeSteps.php?id='.($data['route_id']).'"';?>>
   		    			<strong>Steps</strong></a></td>
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
