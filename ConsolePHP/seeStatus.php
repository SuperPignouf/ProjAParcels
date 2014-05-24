<?php if(!isset($_SESSION)) session_start(); ?>


<!DOCTYPE  html>


<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Historique du Colis</h1>
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
						FROM parcel_status
						WHERE parcel_id= "' . $_GET['id'] . '" 
						ORDER BY from_date '); // on sélectionne l'historique du colis pour affichage.
					}
					else{
						header('Location : welcome.php');
						exit();
					}
					
					
					
		
					?> <table border="1" style="width:300px"><tr>
						  <th>status</th>
						  <th>comment</th> 
						  <th>latitude</th> 
						  <th>longitude</th> 
						  <th>from_date</th>
						  <th>to_date</th>
						</tr> <?php
					while ($data = $response -> fetch()){
						?>
    					<tr>
   		    			<td><?php echo $data['status']; ?></td>
   		    			<td><?php echo $data['comment']; ?></td>
   		    			<td><?php echo $data['latitude']; ?></td>
   		    			<td><?php echo $data['longitude']; ?></td>
   		    			<td><?php echo $data['from_date']; ?></td>
   		    			<td><?php echo $data['to_date']; ?></td>
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
