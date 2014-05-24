<?php if(!isset($_SESSION)) session_start(); ?>


<!DOCTYPE  html>


<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Nos Ordres de Transport</h1>
		</header>
		
		<section> <!--Zone centrale-->
		Pour chaque colis nous affichons de gauche à droite: l'id de l'ordre, l'id du client émetteur, l'id du client récepteur, la monnaie et le prix du service.
		Les colis concernés par l'ordre sont disponibles grâce à un clic sur l'id de l'ordre.
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
						FROM orders 
						WHERE CURRENT_TIMESTAMP < to_date
						ORDER BY order_id '); // on sélectionne les ordres de transport valides pour affichage.
				
					
		
					?> <table border="1" style="width:300px"><tr>
						  <th>order_id</th>
						  <th>sender_id</th> 
						  <th>receiver_id</th>
						  <th>billing_currency</th>
						  <th>billing_value</th>
						</tr> <?php
					while ($data = $response -> fetch()){
						?>
    					<tr>
   		    			<td><a href= <?php echo '"seeParcels.php?order='.($data['order_id']).'"';?>>
   		    			<strong><?php echo $data['order_id']; ?></strong></a></td>
   		    			<td><a href= <?php echo '"seeClient.php?id='.($data['sender_id']).'"';?>>
   		    			<strong><?php echo $data['sender_id']; ?></strong></a></td>
   		    			<td><a href= <?php echo '"seeClient.php?id='.($data['receiver_id']).'"';?>>
   		    			<strong><?php echo $data['receiver_id']; ?></strong></a></td>
   		    			<td><?php echo $data['billing_currency']; ?></td>
   		    			<td><?php echo $data['billing_value']; ?></td>

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
