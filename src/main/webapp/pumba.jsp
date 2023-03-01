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

  <body class="container game">
    <header>
      <h1>Pumba!</h1>
    </header>
    <main class="main-game">
      <% int start = Integer.parseInt(request.getParameter("start"));
        String message;
        Pumba game;
        Player personPlayer = null;
        if (start == 1) {
          int n = Integer.parseInt(request.getParameter("players"));
          game = new Pumba(n);
          session.setAttribute("game", game);
          message = "Comienza el juego. Turno del \"Mano\".";
        } else {
          String playedCard = request.getParameter("card");
          game = (Pumba)session.getAttribute("game");
          message = game.runPlay(playedCard);
      }%>
      <section class="table">
        <div class="all-players">
          <%
            for (Player j : game.getPlayers()) {
              if (!j.isMachine())
                personPlayer = j;
              else
                out.print(j);
            }
          %>
          <div class="positional-marks">
            <div class="mark-draw-pile">
              <img class="card" src="assets/img/positional-mark.png">
            </div>
            <div class="mark-discards-pile">
              <img class="card" src="assets/img/positional-mark.png">
            </div>
          </div>  
          <div class="table-center">
            <div class="draw-pile">
            <%
              for (Card c : game.getDrawPile()) {
                out.print(c);
              }
            %>
            </div>
            <div class="discards-pile">
            <%
              for (Card c : game.getDiscardPile()) {
                out.print(c);
              }
            %>
            </div>
          </div>
          <div class="drawing-link">
            <div class="link-draw-pile">
              <a href="pumba.jsp?start=0&amp;card=draw_card" class="card"></a>
            </div>
          </div>  
        </div>
      </section>
      <section class="display">
        <%
          out.print(Utilities.printDiv(personPlayer, "div_jugador1"));
        %>
        <div class="info">
          <div class="play-info">
            <div class="played-suit">
              <% String suit = game.getSuitOnPlay(); %>
              <div class="suit"><%=(suit == null ? "" : suit)%></div>
              <%=game.getSuitImg()%>
            </div>
            <div class="message"><%=message%></div>
          </div>
          <div class="buttons">
            <%
              if (message.contains("FIN")) {%>
                <a class="btn" href="index.html">Atrás</a>
            <%} else {%>
                <a class="btn" href="pumba.jsp?start=0">OK</a>
                <a class="btn" href="index.html">Atrás</a>
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
