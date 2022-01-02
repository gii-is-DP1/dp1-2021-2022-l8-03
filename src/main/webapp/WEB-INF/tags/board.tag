<%@ attribute name="salmonBoard" required="false" rtexprvalue="true" type="org.springframework.samples.upstream.salmonBoard.SalmonBoard"
 description="Salmonboard to be rendered" %>
 
<canvas id="canvas" width="${salmonBoard.width}" height="${salmonBoard.height}"></canvas>
<img id="source" src="${salmonBoard.background}" style="display:none">
<script src="/src/main/webapp/WEB-INF/tags/tile.tag"></script>
<script src="/src/main/webapp/WEB-INF/tags/piece.tag"></script>
<!-- TILES -->


<img id="SEA-0" src="/resources/images/SEA-0.png" style="display:none">
<img id="SEA-1" src="/resources/images/SEA-1.png" style="display:none">
<img id="SEA-2" src="/resources/images/SEA-2.png" style="display:none">
<img id="SEA-3" src="/resources/images/SEA-3.png" style="display:none">

<img id="BEAR-0" src="/resources/images/BEAR-0.png" style="display:none">
<img id="BEAR-1" src="/resources/images/BEAR-1.png" style="display:none">

<img id="EAGLE-0" src="/resources/images/EAGLE.png" style="display:none">
<img id="HERON-0" src="/resources/images/HERON.png" style="display:none">
<img id="ROCK-0" src="/resources/images/ROCK.png" style="display:none">
<img id="WATER-0" src="/resources/images/WATER.png" style="display:none">

<img id="SPAWN-0" src="/resources/images/SPAWN-0.png" style="display:none">
<img id="SPAWN-1" src="/resources/images/SPAWN-1.png" style="display:none">
<img id="SPAWN-2" src="/resources/images/SPAWN-2.png" style="display:none">
<img id="SPAWN-3" src="/resources/images/SPAWN-3.png" style="display:none">
<img id="SPAWN-4" src="/resources/images/SPAWN-4.png" style="display:none">

<img id="WATERFALL-0" src="/resources/images/WATERFALL-0.png" style="display:none">
<img id="WATERFALL-1" src="/resources/images/WATERFALL-1.png" style="display:none">
<img id="WATERFALL-2" src="/resources/images/WATERFALL-2.png" style="display:none">

<!-- PIECES -->
<img id="SALMON-RED-1" src="/resources/images/SALMON-RED-1.png" style="display:none">
<img id="SALMON-RED-2" src="/resources/images/SALMON-RED-2.png" style="display:none">
<img id="SALMON-BLACK-1" src="/resources/images/SALMON-BLACK-1.png" style="display:none">
<img id="SALMON-BLACK-2" src="/resources/images/SALMON-BLACK-2.png" style="display:none">
<img id="SALMON-GRAY-1" src="/resources/images/SALMON-GRAY-1.png" style="display:none">
<img id="SALMON-GRAY-2" src="/resources/images/SALMON-GRAY-2.png" style="display:none">
<img id="SALMON-GREEN-1" src="/resources/images/SALMON-GREEN-1.png" style="display:none">
<img id="SALMON-GREEN-2" src="/resources/images/SALMON-GREEN-2.png" style="display:none">
<img id="SALMON-YELLOW-1" src="/resources/images/SALMON-YELLOW-1.png" style="display:none">
<img id="SALMON-YELLOW-2" src="/resources/images/SALMON-YELLOW-2.png" style="display:none">

<img id="HORSE-BLACK" src="/resources/images/HORSE-BLACK.png" style="display:none">
<img id="KING-BLACK" src="/resources/images/KING-BLACK.png" style="display:none">

<script>
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var image = document.getElementById('source');

ctx.drawImage(image, 0, 0, ${salmonBoard.width}, ${salmonBoard.height});

</script>


