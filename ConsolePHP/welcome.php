<?php if(!isset($_SESSION)) session_start(); ?>

<!DOCTYPE  html>

<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Bienvenue Visiteur !</h1>
		</header>
		
		<section> <!--Zone centrale-->
			<p>Ici vous pouvez encoder des ordres de transport ainsi que visualiser le contenu de la base de données Parcel on The Move.</p>	
		</section>	
		
		<nav> <!--Menu-->
			<?php include ("menu.php"); ?>
		</nav>
		
		<footer> <!--Footer-->
			<?php include ("footer.php"); ?>
		</footer>
		
	</body>

</html>