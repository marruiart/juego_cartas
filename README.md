<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/marruiart/juego_cartas">
    <img src="https://github.com/marruiart/juego_cartas/blob/main/src/main/webapp/assets/img/favicon.ico" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Juego de cartas - Pumba</h3>

  <p align="center">
    El proyecto 'Pumba' es una aplicación web desarrollada en JSP que permite a los usuarios jugar el popular juego de cartas 'Pumba'. Se ha desarrollado tanto la interfaz, empleando HTML y CSS, como una lógica de juego realista. El juego incluye la posibilidad de jugar contra la máquina, eligiendo entre 1 y 5 contrincantes. El código fuente del proyecto está disponible en GitHub si se desea contribuir a su mejora. 
    <br />
    <a href="https://github.com/marruiart/juego_cartas"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/marruiart/juego_cartas">View Project</a>
    ·
    <a href="https://github.com/marruiart/juego_cartas/issues">Report Bug</a>
    ·
    <a href="https://github.com/marruiart/juego_cartas/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Tabla de contenidos</summary>
  <ol>
    <li>
      <a href="#about-the-project">Acerca del proyecto</a>
      <ul>
        <li><a href="#built-with">Tecnologías utilizadas</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Cómo comenzar</a>
      <ul>
        <li><a href="#prerequisites">Prerequisitos</a></li>
        <li><a href="#installation">Instalación</a></li>
      </ul>
    </li>
    <li><a href="#usage">Uso</a></li>
    <li><a href="#contact">Contacto</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## Acerca del projecto

<img src="https://user-images.githubusercontent.com/88201067/228169186-4e3d7422-f625-43ba-b664-5e18c473eab2.png" width="500" align="center"/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Tecnologías utilizadas

<img src="https://github.com/abranhe/programming-languages-logos/blob/master/src/java/java_48x48.png" align="left"/>
<img src="https://github.com/abranhe/programming-languages-logos/blob/master/src/html/html_48x48.png" align="left"/>
<img src="https://github.com/abranhe/programming-languages-logos/blob/master/src/css/css_48x48.png" align="left"/>

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Cómo comenzar

Si se desea utilizar el proyecto de forma local, se deben seguir una serie de pasos simples tal y como se explica a continuación.

### Prerequisitos

Es necesario tener docker previamente instalado. En caso de equipos Windows, es recomendable instalar antes WSL2.
* docker: https://docs.docker.com/engine/install/
* wsl
  ```sh
  wsl --install
  ```

### Instalación

1. Descargar la imagen de docker
   ```sh
   docker pull marruiart/pumba
   ```
2. Iniciar la ejecución de la imagen
   ```sh
   docker run -it -p 8081:8080 marruiart/pumba
   ```
3. Abrir la aplicación en https://localhost:8081/juegos_cartas

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Uso

Se pueden leer las instrucciones de juego en la pantalla de inicio de la aplicación.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTACT -->
## Contacto
[![LinkedIn][linkedin-shield]](https://linkedin.com/in/marruiart)
[![gmail][gmail-shield]](mailto:marruiart@gmail.com)

Project Link: [https://github.com/marruiart/juego_cartas](https://github.com/marruiart/juego_cartas)

<p align="right">(<a href="#readme-top">back to top</a>)</p>




[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[gmail-shield]: https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white
