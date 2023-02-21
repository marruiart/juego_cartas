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
      Jugador jugador;
      if (iniciar == 1) {
        int n = Integer.parseInt(request.getParameter("jugadores"));
        partida = new Pumba(n);
        session.setAttribute("partida", partida);
        jugador = partida.getJugadorTurno();
        mensaje = "Comienza el juego";
      } else {
        partida = (Pumba)session.getAttribute("partida");
        jugador = partida.getJugadorTurno();
        jugador.soltarCarta();
        jugador.robarCarta();
        int turno = partida.setTurno();
        mensaje = "Turno del jugador " + (turno + 1);
      }
      for (Jugador j : partida.getJugadores()) {
          out.print(j);
      }
    %>
    <h3>Mazo de cartas</h3>
    <%
      for (Carta c : partida.getMazo()) {
        out.print(c);
      }
    %>
    <h3>Descartes</h3>
    <%
      for (Carta c : partida.getDescartes()) {
        out.print(c);
      }
    %>
    <br/>
    <input type="text" readonly value="<%=mensaje%>">
    <a href="pumba.jsp?iniciar=0">OK</a>
    <a href="index.html" class="atras">Atrás</a>
    <footer>Autora: Marina Ruiz Artacho</footer>
  </body>
</html>
