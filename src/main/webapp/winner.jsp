<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="modules.Utilities"%>

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

  <body class="container landing">
    <header>
      <h1>Pumba!</h1>
    </header>
    <main class="main-landing main-winner">
      <a href="index.html" class="btn">Volver a inicio</a>
      <section class="winner">
        <%
          int winner = Integer.parseInt(request.getParameter("winner"));
          if (winner == 1) {
              out.print(Utilities.printTitle("h2", "¡ENHORABUENA, HAS GANADO!"));
              out.print(Utilities.printImg("assets/img/winner.png", "Imagen de una copa"));
          } else {
              out.print(Utilities.printTitle("h2", "¡Ha ganado el jugador" + winner + "!"));
              out.print(Utilities.printImg("assets/img/winner-player.png", "Imagen de un podio"));
          }
        %>
      </section>
    </main>
    <footer>
      <div>Diseñado y desarrollado por Marina Ruiz</div>
      <small>
        Fuente:<a href="https://www.nhfournier.es/como-jugar/pumba/">www.nhfournier.es</a><br/>
        Imágenes: <a href="https://www.freepik.es/">Freepik</a>
      </small>
    </footer>
  </body>
</html>
