<a id="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
<!--<img src="https://github.com/glennthielman/silent-sui/blob/master/assets_orginal/favicon/android-chrome-512x512.png?raw=true" alt="Logo" width="80" height="80">-->
<h1 align="center">Belgium energy prices scraper</h1>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
      <a href="#available-endpoints">Available endpoints</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#homeassistant">HomeAssistant</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

This project will get the hourly or quarterly energy prices in Belgium of the current day and day a head (when available), based on one of the following data providers:
* [Luminus](https://my.luminusbusiness.be/market-info/nl/dynamic-prices/)
* [OCTA+](https://www.octaplus.be/dynamisch-stroomtarief-uurprijzen)
* [Eneco](https://eneco.be/nl/actuele-prijzen/)
* [Creg](https://www.creg.be/nl/consumenten/prijzen-en-tarieven/creg-tarief-voor-terugbetaling-thuisladen-bedrijfswagens)
* [Elia](https://www.elia.be/en/grid-data/transmission/day-ahead-reference-price)

The goal is to scrape one of these data endpoints into structured json format that is workable for home automation systems like HomeAssistant.

<!-- GETTING STARTED -->
## Getting Started

This project requires docker to run.  
The container does caching for fetching the data of the providers, this is to not bombard data providers with requests.  
**!!! DO NOT CHANGE THE CACHE TIMER !!!** Be mindful that services don't come for free!

### Prerequisites:

* Linux, Mac, Windows
* Docker and Docker-compose

### Installation

1. Download the recent release (DockerFile, DockerCompose and jar file)
2. Do a docker build
   ```sh
   docker build -t dynamic-prices:latest .
   ```
3. Modify docker-compose.yml
4. Start using docker
   ```sh
   docker-compose up -d
   ```

### Available endpoints

You can find the API documentation at the index page, when the container is running.

<!-- HomeAssistant setup -->
## HomeAssistant Examples
* [Sensors](https://github.com/glennthielman/belgium-energy-prices-scraper/blob/main/examples/homeassistant/sensors.yaml)
* [Dashboard](https://github.com/glennthielman/belgium-energy-prices-scraper/blob/main/examples/homeassistant/dashboard.yaml)
* Apex charts
  * [Current day](https://github.com/glennthielman/belgium-energy-prices-scraper/blob/main/examples/homeassistant/apex_chart_current_day.yaml)
  * [Day ahead](https://github.com/glennthielman/belgium-energy-prices-scraper/blob/main/examples/homeassistant/apex_chart_day_ahead.yaml)

   
<!-- ROADMAP -->
## Roadmap

- [x] Luminus integration
- [x] Octa+ integration
- [x] Eneco integration
- [x] investigate patterns to streamline integrations
- [x] Added API documentation as index
- [ ] Entsoe platform [link](https://transparency.entsoe.eu/)
- [X] Elia
- [ ] Clean up code

<!-- LICENSE -->
## License

See `LICENSE` for more information.

<!-- CONTACT -->
## Contact

Glenn Thielman - [Twitter](https://x.com/the_glenn90) - [LinkedIn](https://www.linkedin.com/in/glennthielman/)
