<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="modules.*"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Pumba</title>
    <link rel="stylesheet" href="./assets/css/style.css" type="text/css" />
  </head>

  <body>
    <h1>Partida</h1>
    <%
      int iniciar = Integer.parseInt(request.getParameter("iniciar"));
      String mensaje;
      Pumba partida;
      if (iniciar == 1) {
        int n = Integer.parseInt(request.getParameter("jugadores"));
        partida = new Pumba(n);
        session.setAttribute("partida", partida);
        mensaje = "Comienza el juego. Turno del \"Mano\".";
      } else {
        partida = (Pumba)session.getAttribute("partida");
        mensaje = partida.ejecutarJugada();
      }
      for (Jugador j : partida.getJugadores()) {
          out.print(j);
      }
    %>
    <div class="centro-mesa">
      <div class="mazo">
      <%
        for (Carta c : partida.getMazo()) {
          out.print(c);
        }
      %>
      </div>
      <div class="descartes">
      <%
        for (Carta c : partida.getDescartes()) {
          out.print(c);
        }
      %>
      </div>
      </div>
    <br/>
    <div class="mensaje"><%=mensaje%></div>
    <%
    if (mensaje.contains("FIN")) {%>
      <a href="index.html" class="atras">Atrás</a>
    <%} else {%>
      <a href="pumba.jsp?iniciar=0">OK</a>
      <a href="index.html" class="atras">Atrás</a>
    <%}
    %>
    <footer>Autora: Marina Ruiz Artacho</footer>
  </body>
</html>
