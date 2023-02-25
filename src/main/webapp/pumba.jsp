<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="modules.*"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Pumba!</title>
    <link rel="shortcut icon" href="assets/img/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="./assets/css/style.css" type="text/css" />
  </head>

  <body class="container partida">
    <header>
      <h1>Pumba!</h1>
    </header>
    <main class="partida">
      <%int iniciar = Integer.parseInt(request.getParameter("iniciar"));
        String mensaje;
        Pumba partida;
        Jugador persona = null;
        if (iniciar == 1) {
          int n = Integer.parseInt(request.getParameter("jugadores"));
          partida = new Pumba(n);
          session.setAttribute("partida", partida);
          mensaje = "Comienza el juego. Turno del \"Mano\".";
        } else {
          partida = (Pumba)session.getAttribute("partida");
          mensaje = partida.ejecutarJugada();
      }%>
      <section class="mesa">
        <div class="personas">
          <%
            for (Jugador j : partida.getJugadores()) {
              if (!j.esMaquina())
                persona = j;
              else
                out.print(j);
            }
          %>
          <div class="centro-mesa">
            <div class="mazo">
              <img class="carta marca-posicion" src="assets/img/posicion.png">
            </div>
            <div class="descartes">
              <img class="carta marca-posicion" src="assets/img/posicion.png">
            </div>
          </div>  
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
        </div>
      </section>
      <section class="display">
        <div class="div_jugador1">
          <%=persona%>
        </div>
        <div class="info">
          <div class="mensaje"><%=mensaje%></div>
          <div class="botones">
            <%
              if (mensaje.contains("FIN")) {%>
                <a class="btn" href="index.html" class="atras">Atrás</a>
            <%} else {%>
                <a class="btn" href="pumba.jsp?iniciar=0">OK</a>
                <a class="btn" href="index.html" class="atras">Atrás</a>
            <%}
            %>
          </div>
        </div>
      </section>
    </main>
    <footer>
      <div>Diseñado y desarrollado por Marina Ruiz</div>
      <small>
        Fuente:<a href="https://www.nhfournier.es/como-jugar/pumba/">www.nhfournier.es</a><br/>
      </small>
    </footer>
  </body>
</html>
