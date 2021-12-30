<%@ attribute name="salmonBoard" required="false" rtexprvalue="true" type="org.springframework.samples.upstream.board.SalmonBoard"
 description="Salmonboard to be rendered" %>
<canvas id="canvas" width="${salmonBoard.width}" height="${salmonBoard.height}"></canvas>
<img id="source" src="${salmonBoard.background}" style="display:none">

<img id="HORSE-BLACK" src="/resources/images/HORSE-BLACK.png" style="display:none">
<img id="KING-BLACK" src="/resources/images/KING-BLACK.png" style="display:none">
<img id="KING-WHITE" src="/resources/images/KING-WHITE.png" style="display:none">

<img id="SEA" src="/resources/images/KING-BLACK.png" style="display:none">



<script src="/src/main/webapp/WEB-INF/tags/piece.tag"></script>
<script src="/src/main/webapp/WEB-INF/tags/tile.tag"></script>
<script>

function loadCanvas(){
	var canvas = document.getElementById("canvas");
	var ctx = canvas.getContext("2d");
	var image = document.getElementById('source');

	ctx.drawImage(image, 0, 0, ${salmonBoard.width}, ${salmonBoard.height});
}
function lanzadera(){
	loadCanvas();
	loadPiece();
	loadTile();

}

window.onload = lanzadera;
</script>


