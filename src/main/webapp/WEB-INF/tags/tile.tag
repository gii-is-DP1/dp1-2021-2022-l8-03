<%@ attribute name="size" required="true" rtexprvalue="true" 
 description="Size of the tile to show" %>
 <%@ attribute name="tile" required="true" rtexprvalue="true" type="org.springframework.samples.upstream.tile.Tile"
 description="Tile to be rendered" %>
 <script>
 function loadTile(){
	 var canvas = document.getElementById("canvas");
	 var ctx = canvas.getContext("2d");
	 var image = document.getElementById('${tile.tileType}');
	 ctx.drawImage(image,${tile.getPositionXInPixels(size)},${tile.getPositionYInPixels(size)},${size},${size});
 }
 </script>