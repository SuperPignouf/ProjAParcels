<?php if(!isset($_SESSION)) session_start(); ?>


<!DOCTYPE  html>

<?php function redirection($url)
{
	die('<meta http-equiv="refresh" content="0;URL='.$url.'">');
}?>


<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Adding the order...</h1>
		</header>
		
		<section> <!--Zone centrale-->
		
		
		<?php if(!empty($_POST['country1']) AND !empty($_POST['country2'])  AND !empty($_POST['contact_name1']) AND !empty($_POST['contact_name2']) AND !empty($_POST['company_name1']) AND !empty($_POST['company_name2']) AND !empty($_POST['address_street1']) AND !empty($_POST['address_street2']) AND !empty($_POST['address_number1']) AND !empty($_POST['address_number2']) AND !empty($_POST['postal_code1']) AND !empty($_POST['postal_code2']) AND !empty($_POST['city1']) AND !empty($_POST['city2']) AND !empty($_POST['tel1']) AND !empty($_POST['tel2']) AND !empty($_POST['email1']) AND !empty($_POST['email2']) AND !empty($_POST['billing_currency']) AND !empty($_POST['billing_value']) AND !empty($_POST['scan_code_type']) AND !empty($_POST['scan_code']) AND !empty($_POST['mass_kg']) AND !empty($_POST['lenght_cm']) AND !empty($_POST['width_cm']) AND !empty($_POST['height_cm'])){
			
					try{	
					$bdd = new PDO('mysql:host=localhost;dbname=parcels', 'root', '', array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
					}	
					catch(Exception $e){
						die('Error : ' .$e -> getMessage());
						echo 'Something went wrong...';
					}
				}
				else {
					redirection('addOrders.php');
					exit();
				}
				$bdd->exec("SET CHARACTER SET utf8");
				
				
				$sender_id = -1;
				$sender_already_exists = false;
				$receiver_id = -1;
				$receiver_already_exists = false;
				
				
				$response1 = $bdd->query('SELECT client_id FROM client WHERE country LIKE "'. $_POST['country1']. '%" AND contact_name LIKE "'. $_POST['contact_name1']. '%" AND address_street LIKE "'. $_POST['address_street1']. '%" AND company_name LIKE "'. $_POST['company_name1']. '%" '); // On va voir si l'expéditeur existe déjà en base, de manière à ne pas l'ajouter une deuxième fois.

				$response2 = $bdd->query('SELECT client_id FROM client WHERE country LIKE "'. $_POST['country2']. '%" AND contact_name LIKE "'. $_POST['contact_name2']. '%" AND address_street LIKE "'. $_POST['address_street2']. '%" AND company_name LIKE "'. $_POST['company_name2']. '%" '); // On va voir si l'expéditeur existe déjà en base, de manière à ne pas l'ajouter une deuxième fois.
				
				while ($data = $response1 -> fetch()){
					$sender_id = $data['client_id'];
					$sender_already_exists = true;
				}
					
				while ($data = $response2 -> fetch()){
					$receiver_id = $data['client_id'];
					$receiver_already_exists = true;
				}

				$response1->closeCursor();
				$response2->closeCursor(); // Termine le traitement de la requête
		
				

				if ($sender_already_exists==false){ // Si l'expéditeur n'existe pas déjà, il faut le créer.
					$bdd->beginTransaction();
					$response = $bdd->query('INSERT INTO client (country, contact_name, company_name, address_street, address_number, postal_code, city, region, state_department, tel, fax, email, TVA_number) VALUES ("' .htmlspecialchars($_POST['country1']).'","' .htmlspecialchars($_POST['contact_name1']).'","' .htmlspecialchars($_POST['company_name1']).'","' .htmlspecialchars($_POST['address_street1']).'","' .htmlspecialchars($_POST['address_number1']).'","' .htmlspecialchars($_POST['postal_code1']).'","' .htmlspecialchars($_POST['city1']).'","' .htmlspecialchars($_POST['region1']).'","' .htmlspecialchars($_POST['state_department1']).'","' .htmlspecialchars($_POST['tel1']).'","' .htmlspecialchars($_POST['fax1']).'","' .htmlspecialchars($_POST['email1']).'","' .htmlspecialchars($_POST['TVA_number1']).'")');
					$sender_id = $bdd->lastInsertId();
				    $bdd->commit();
					$response->closeCursor();
				}

				if ($receiver_already_exists==false){ // Si le destinataire n'existe pas déjà, il faut le créer.
					$bdd->beginTransaction();
					$response = $bdd->query('INSERT INTO client (country, contact_name, company_name, address_street, address_number, postal_code, city, region, state_department, tel, fax, email, TVA_number) VALUES ("' .htmlspecialchars($_POST['country2']).'","' .htmlspecialchars($_POST['contact_name2']).'","' .htmlspecialchars($_POST['company_name2']).'","' .htmlspecialchars($_POST['address_street2']).'","' .htmlspecialchars($_POST['address_number2']).'","' .htmlspecialchars($_POST['postal_code2']).'","' .htmlspecialchars($_POST['city2']).'","' .htmlspecialchars($_POST['region2']).'","' .htmlspecialchars($_POST['state_department2']).'","' .htmlspecialchars($_POST['tel2']).'","' .htmlspecialchars($_POST['fax2']).'","' .htmlspecialchars($_POST['email2']).'","' .htmlspecialchars($_POST['TVA_number2']).'")');
					$receiver_id = $bdd->lastInsertId();
				    $bdd->commit();
					$response->closeCursor();
				}
					 
				$bdd->beginTransaction(); // Insertion de l'ordre
				$response = $bdd->query('INSERT INTO orders (sender_id, receiver_id, billing_currency, billing_value) VALUES ("'. $sender_id.'","'. $receiver_id.'","' .htmlspecialchars($_POST['billing_currency']).'","' .htmlspecialchars($_POST['billing_value']).'")');
				$order_id = $bdd->lastInsertId();
				$bdd->commit();
				$response->closeCursor(); 

				

				$bdd->beginTransaction(); // Insertion du colis
				$response = $bdd->query('INSERT INTO parcel (order_id, scan_code, scan_code_type, mass_kg, lenght_cm, height_cm, width_cm, content, description, declared_value_currency, declared_value) VALUES ("'. $order_id.'","' .htmlspecialchars($_POST['scan_code']).'","' .htmlspecialchars($_POST['scan_code_type']).'","' .htmlspecialchars($_POST['mass_kg']).'","' .htmlspecialchars($_POST['lenght_cm']).'","' .htmlspecialchars($_POST['height_cm']).'","' .htmlspecialchars($_POST['width_cm']).'","' .htmlspecialchars($_POST['content']).'","' .htmlspecialchars($_POST['description']).'","' .htmlspecialchars($_POST['declared_value_currency']).'","' .htmlspecialchars($_POST['declared_value']).'")');
				$parcel_id = $bdd->lastInsertId();
				$bdd->commit();
				$response->closeCursor(); 

				

				$bdd->beginTransaction(); // Insertion de l'état initial du colis
				$response = $bdd->query('INSERT INTO parcel_status (parcel_id, status) VALUES ("'. $parcel_id.'","sender")');
				$bdd->commit();
				$response->closeCursor(); 

				
					 
		 	redirection('welcome.php');
		 	exit();
		
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
