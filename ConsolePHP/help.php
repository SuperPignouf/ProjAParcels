<?php if(!isset($_SESSION)) session_start(); ?>

<!DOCTYPE  html>
<html>
	<head>
		<?php include("head.php"); ?>
	</head>
	
	<body>
	
		<header> <!--En-tête-->
			<h1>Aide</h1>
		</header>
		
		<section> <!--Zone centrale-->
			<p>Very helpful material. Wow.</p>
			<iframe width="960" height="720" src="http://www.youtube.com/embed/dQw4w9WgXcQ?rel=0&amp;autoplay=1" frameborder="0" allowfullscreen></iframe>		
		</section>	
		
		<nav> <!--Menu-->
			<?php include ("menu.php"); ?>
		</nav>
		
		<footer> <!--footer-->
			<?php include ("footer.php"); ?>
		</footer>
		
	</body>

</html>