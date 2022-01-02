<%@ attribute name="size" required="true" rtexprvalue="true" 
 description="Size of the piece to show" %>
 <%@ attribute name="piece" required="true" rtexprvalue="true" type="org.springframework.samples.upstream.piece.Piece"
 description="Piece to be rendered" %>
 <script>
 var canvas = document.getElementById("canvas");
 var ctx = canvas.getContext("2d");
 var image = document.getElementById('SALMON-${piece.color}-${piece.numSalmon}');
 image.globalCompositeOperation ="destination-over";
 ctx.drawImage(image,${piece.getPositionYInPixels(size)},${piece.getPositionXInPixels(size)},38,38);
 </script>