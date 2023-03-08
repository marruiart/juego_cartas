<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="modules.*"%>
<%@page import="modules.Enums.Suits"%>

<!DOCTYPE html>
<html lang="es">
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
      <% 
        Integer start = Integer.parseInt(request.getParameter("start"));
        String round = request.getParameter("round");
        String message;
        Pumba game;
        Player personPlayer = null;
        String playedCard = null;
        String drawPlayer = null;

        if (start == 1) {
          int n = Integer.parseInt(request.getParameter("players"));
          game = new Pumba(n);
          session.setAttribute("game", game);
          message = "Comienza el juego. Turno del \"Mano\".";
        } else if (start != 1 && round != null && Integer.parseInt(round) >= 1) {
          game = (Pumba)session.getAttribute("game");
          String removed = PumbaUtilities.restartRound(game);
          message = String.format("%sComienza la ronda %d. Turno de %s (Mano).", removed, game.getRound(), game.getPlayerOfTurn().getPlayerName(true));
        } else {
            playedCard = request.getParameter("card");
            drawPlayer = request.getParameter("draw_player");
            String changedSuit = request.getParameter("suits");
            if (playedCard != null)
              playedCard = Utilities.getPlayerName(playedCard);
            if (drawPlayer != null)
              drawPlayer = Utilities.getPlayerName(drawPlayer);
            game = (Pumba)session.getAttribute("game");
            message = game.runPlay(playedCard, changedSuit, drawPlayer);
      }%>
      <section class="table">
        <div class="all-players">
          <%
            personPlayer = game.getPlayers().get(0);
            out.print(Utilities.printPlayers(game));
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
              <%=Utilities.printCards(game, game.getDrawPile().getCards())%>
            </div>
            <div class="discards-pile">
              <%=Utilities.printCards(game, game.getDiscardPile())%>
            </div>
          </div>
          <div class="drawing-link">
            <div class="link-draw-pile">
              <%  
                boolean enableDrawPile = PumbaUtilities.isDrawPileEnabled(game, personPlayer);
                out.print(Utilities.printAnchor("pumba.jsp?start=0&card=draw_card", "", "card", enableDrawPile ? "" : "disabled"));
              %>
            </div>
          </div>
          <%
            boolean activePumba = personPlayer.isPumbaTime();
            out.print(Utilities.printImg("assets/img/dir"+game.getPlayDirection() +".png", "Flecha de dirección del juego", "arrow-img", "top"));
            out.print(Utilities.printImg("assets/img/dir"+game.getPlayDirection() +".png", "Flecha de dirección del juego", "arrow-img", "bottom"));
            out.print(Utilities.printAnchor("pumba.jsp?start=0", "¡PUMBA!", "pumba-btn", activePumba ? "" : "disabled"));
          %>  
        </div>
      </section>
      <section class="display">
        <%=Utilities.printDiv(personPlayer, "div_jugador1")%>
        <div class="info">
          <div class="play-info">
            <form class="played-suit" method="GET" action="pumba.jsp">
              <% 
                out.print(Utilities.printInput("hidden", "start", "0"));
                out.print(Utilities.printInput("hidden", "card", (start == 1) ? null : playedCard));
                boolean playerDrawing = (message.contains("Elige al jugador que chupa 1 carta."));
                boolean changeOfSuit = false;
                if (playerDrawing) {
                  out.print(Utilities.printSelect("draw_player", game.getAllPlayersNames(), "select-suit"));
                } else {
                  changeOfSuit = (message.contains("Elige el cambio de palo."));
                  out.print(Utilities.printSelect("suits", changeOfSuit, (start == 1) ? null : Suits.getAllSuits(), 
                    game.getSuitOnPlay(), "select-suit"));
                }
                if (playerDrawing || changeOfSuit)
                  out.print(Utilities.printButton("submit", "OK", "btn", "select-btn"));
                else
                  out.print(game.getSuitImg());
              %>
            </form>
            <div class="message"><%=message%></div>
          </div>
          <div class="buttons">
            <%
              if (message.contains("FIN")) {
                  out.print(Utilities.printAnchor("winner.jsp?winner=1", "PREMIO", "btn"));
              } else if (message.contains("has sido eliminado")) {
                  out.print(Utilities.printAnchor("index.html", "Terminar", "btn"));
              } else if (message.contains("RONDA")) {
                  out.print(Utilities.printAnchor("pumba.jsp?start=0&round=1", "Sí", "btn"));
                  out.print(Utilities.printAnchor("winner.jsp?winner=" + PumbaUtilities.getWinner(), "No", "btn"));
                  game.isScoreRound = false;
              } else {
                  out.print(Utilities.printAnchor("pumba.jsp?start=0", "Siguiente", "btn", (game.getTurn() == 0) ? "disabled" : ""));
                  out.print(Utilities.printAnchor("index.html", "Terminar", "btn"));
              }
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
