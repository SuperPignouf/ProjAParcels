<?php if(!isset($_SESSION)) session_start(); ?>


<!DOCTYPE  html>


<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Nos Colis</h1>
		</header>
		
		<section> <!--Zone centrale-->
		Pour chaque colis nous affichons de gauche à droite: l'id du colis dans la base, l'id de l'ordre auquel le colis est associé, le code scannable du colis, le type de code, la masse en kg du colis, la longueur, la hauteur et la largeur en centimètres du colis, le contenu, la description, la monnaie utilisée pour la valeur déclarée et la valeur déclarée.
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
				
					if(isset($_GET["order"])){
					$response = $bdd->query('SELECT *
						FROM parcel
						WHERE CURRENT_TIMESTAMP < to_date
						AND order_id= "' . $_GET['order'] . '" 
						ORDER BY parcel_id '); // on sélectionne les colis valides pour affichage.
					}
					else{
						$response = $bdd->query('SELECT *
						FROM parcel 
						WHERE CURRENT_TIMESTAMP < to_date
						ORDER BY parcel_id '); // on sélectionne les colis valides pour affichage.
					}
					
					
					
		
					?> <table border="1" style="width:300px"><tr>
						  <th>parcel_id</th>
						  <th>order_id</th> 
						  <th>scan_code</th>
						  <th>scan_code_type</th>
						  <th>mass_kg</th>
						  <th>lenght_cm</th>
						  <th>height_cm</th>
						  <th>width_cm</th>
						  <th>content</th>
						  <th>description</th>
						  <th>declared_value_currency</th>
						  <th>declared_value</th>
						  <th>Status history</th>

						</tr> <?php
					while ($data = $response -> fetch()){
						?>
    					<tr>
   		    			<td><?php echo $data['parcel_id']; ?></td>
   		    			<td><?php echo $data['order_id']; ?></td>
   		    			<td><?php echo $data['scan_code']; ?></td>
   		    			<td><?php echo $data['scan_code_type']; ?></td>
   		    			<td><?php echo $data['mass_kg']; ?></td>
   		    			<td><?php echo $data['lenght_cm']; ?></td>
   		    			<td><?php echo $data['height_cm']; ?></td>
   		    			<td><?php echo $data['width_cm']; ?></td>
   		    			<td><?php echo $data['content']; ?></td>
   		    			<td><?php echo $data['description']; ?></td>
   		    			<td><?php echo $data['declared_value_currency']; ?></td>
   		    			<td><?php echo $data['declared_value']; ?></td>	
   		    			<td><a href= <?php echo '"seeStatus.php?id='.($data['parcel_id']).'"';?>>
   		    			<strong>History</strong></a></td>	
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
