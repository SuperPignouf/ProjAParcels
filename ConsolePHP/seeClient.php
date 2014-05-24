<?php if(!isset($_SESSION)) session_start(); ?>


<!DOCTYPE  html>


<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Nos Clients</h1>
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
				
					if(isset($_GET['id'])){
					$response = $bdd->query('SELECT *
						FROM client
						WHERE CURRENT_TIMESTAMP < to_date
						AND client_id= "' . $_GET['id'] . '" 
						ORDER BY client_id '); // on sélectionne les clients valides pour affichage.
					}
					else{
						$response = $bdd->query('SELECT *
						FROM client 
						WHERE CURRENT_TIMESTAMP < to_date
						ORDER BY client_id '); // on sélectionne les clients valides pour affichage.
					}
					
					
					
		
					?> <table border="1" style="width:300px"><tr>
						  <th>client_id</th>
						  <th>country</th> 
						  <th>contact_name</th>
						  <th>company_name</th>
						  <th>address_street</th>
						  <th>address_number</th>
						  <th>postal_code</th>
						  <th>city</th>
						  <th>region</th>
						  <th>state_department</th>
						  <th>tel</th>
						  <th>fax</th>
						  <th>email</th>
						  <th>TVA_number</th>
						</tr> <?php
					while ($data = $response -> fetch()){
						?>
    					<tr>
   		    			<td><?php echo $data['client_id']; ?></td>
   		    			<td><?php echo $data['country']; ?></td>
   		    			<td><?php echo $data['contact_name']; ?></td>
   		    			<td><?php echo $data['company_name']; ?></td>
   		    			<td><?php echo $data['address_street']; ?></td>
   		    			<td><?php echo $data['address_number']; ?></td>
   		    			<td><?php echo $data['postal_code']; ?></td>
   		    			<td><?php echo $data['city']; ?></td>
   		    			<td><?php echo $data['region']; ?></td>
   		    			<td><?php echo $data['state_department']; ?></td>
   		    			<td><?php echo $data['tel']; ?></td>
   		    			<td><?php echo $data['fax']; ?></td>	
   		    			<td><?php echo $data['email']; ?></td>	
   		    			<td><?php echo $data['TVA_number']; ?></td>		
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
