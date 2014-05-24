<!DOCTYPE  html>
<?php if(!isset($_SESSION)) session_start(); ?>
<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Encodage d'Ordre de Transport</h1>
		</header>
		
		<section> <!--Zone centrale-->
			<p>Veuillez inscrire les informations sur l'expéditeur, le destinataire et le colis.</p>
			
			<form method = "post" action = "insertOrder.php">
				<p>

					<table style="width:300px">
					<tr>
					  <th>Pays de l'expéditeur*</th>
					  <th>Nom et prénom*</th>
					  <th>Nom de compagnie*</th>
					  <th>Rue*</th>
					  <th>Numéro*</th>
					  <th>Code postal*</th>
					  <th>Ville*</th>
					  <th>Région</th>
					  <th>Etat ou Département</th>
					  <th>Tel*</th>
					  <th>Fax</th>
					  <th>Email*</th>
					  <th>Numéro de TVA*</th>
					</tr>
					<tr>
					  <td><input type = "text" name = "country1" id = "country1"/></td>
					  <td><input type = "text" name = "contact_name1" id = "contact_name1"/></td>
					  <td><input type = "text" name = "company_name1" id = "company_name1"/></td>
					  <td><input type = "text" name = "address_street1" id = "address_street1"/></td>
					  <td><input type = "text" name = "address_number1" id = "address_number1"/></td>
					  <td><input type = "text" name = "postal_code1" id = "postal_code1"/></td>
					  <td><input type = "text" name = "city1" id = "city1"/></td>
					  <td><input type = "text" name = "region1" id = "region1"/></td>
					  <td><input type = "text" name = "state_department1" id = "state_department1"/></td>
					  <td><input type = "text" name = "tel1" id = "tel1"/></td>
					  <td><input type = "text" name = "fax1" id = "fax1"/></td>
					  <td><input type = "text" name = "email1" id = "email1"/></td>
					  <td><input type = "text" name = "TVA_number1" id = "TVA_number1"/></td>
					</tr>
					</table> 

					<table style="width:300px">
					<tr>
					  <th>Pays du destinataire*</th>
					  <th>Nom et prénom*</th>
					  <th>Nom de compagnie*</th>
					  <th>Rue*</th>
					  <th>Numéro*</th>
					  <th>Code postal*</th>
					  <th>Ville*</th>
					  <th>Région</th>
					  <th>Etat ou Département</th>
					  <th>Tel*</th>
					  <th>Fax</th>
					  <th>Email*</th>
					  <th>Numéro de TVA*</th>
					</tr>
					<tr>
					  <td><input type = "text" name = "country2" id = "country2"/></td>
					  <td><input type = "text" name = "contact_name2" id = "contact_name2"/></td>
					  <td><input type = "text" name = "company_name2" id = "company_name2"/></td>
					  <td><input type = "text" name = "address_street2" id = "address_street2"/></td>
					  <td><input type = "text" name = "address_number2" id = "address_number2"/></td>
					  <td><input type = "text" name = "postal_code2" id = "postal_code2"/></td>
					  <td><input type = "text" name = "city2" id = "city2"/></td>
					  <td><input type = "text" name = "region2" id = "region2"/></td>
					  <td><input type = "text" name = "state_department2" id = "state_department2"/></td>
					  <td><input type = "text" name = "tel2" id = "tel2"/></td>
					  <td><input type = "text" name = "fax2" id = "fax2"/></td>
					  <td><input type = "text" name = "email2" id = "email2"/></td>
					  <td><input type = "text" name = "TVA_number2" id = "TVA_number2"/></td>
					</tr>
					</table> 

					<table style="width:300px">
					<tr>
					  <th>Monnaie de facturation*</th>
					  <th>Valeur de facturation*</th>
					  <th>Type de code*</th>
					  <th>Code*</th>
					  <th>Masse (kg)*</th>
					  <th>Longueur (cm)*</th>
					  <th>Largeur (cm)*</th>
					  <th>Hauteur (cm)*</th>
					  <th>Contenu</th>
					  <th>Description</th>
					  <th>Monnaie de valeur déclarée</th>
					  <th>Valeur déclarée</th>
					</tr>
					<tr>
					  <td><input type = "text" name = "billing_currency" id = "billing_currency"/></td>
					  <td><input type = "text" name = "billing_value" id = "billing_value"/></td>
					  <td><input type = "text" name = "scan_code_type" id = "scan_code_type"/></td>
					  <td><input type = "text" name = "scan_code" id = "scan_code"/></td>
					  <td><input type = "text" name = "mass_kg" id = "mass_kg"/></td>
					  <td><input type = "text" name = "lenght_cm" id = "lenght_cm"/></td>
					  <td><input type = "text" name = "width_cm" id = "width_cm"/></td>
					  <td><input type = "text" name = "height_cm" id = "height_cm"/></td>
					  <td><input type = "text" name = "content" id = "content"/></td>
					  <td><input type = "text" name = "description" id = "description"/></td>
					  <td><input type = "text" name = "declared_value_currency" id = "declared_value_currency"/></td>
					  <td><input type = "text" name = "declared_value" id = "declared_value"/></td>
					</tr>
					</table> 

					<br/>
					
					<input type = "submit" value = "Submit"/>
				</p>
			</form>			
		</section>	
		
		<nav> <!--Menu-->
			<?php include ("menu.php"); ?>
		</nav>
		
		<footer> <!--footer-->
			<?php include ("footer.php"); ?>
		</footer>
		
	</body>

</html>