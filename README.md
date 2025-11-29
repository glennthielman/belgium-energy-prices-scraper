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

This project will get the hourly energy prices in Belgium of the current day and day a head (after 14:00), based of the following data providers:
* [Luminus](https://my.luminusbusiness.be/market-info/nl/dynamic-prices/).  
* [OCTA+](https://www.octaplus.be/dynamisch-stroomtarief-uurprijzen)
* [Eneco](https://eneco.be/nl/actuele-prijzen/)

The goal is to scrape one of these data endpoints into structured json format that is workable for home automation systems like HomeAssistant.

### Available endpoints

first you will need to select a data provider using the following luminus, octaplus, eneco.
you will need to replace that in url, for example of you want the eneco prices its "eneco/prices"

* **<< dataprovider >>/prices**  
Will give an hourly list of all prices, example:
```json
"prices": [
    {
      "startTime": "2025-11-22T00:00:00",
      "endTime": "2025-11-22T01:00:00",
      "priceKwH": 0.09951,
      "priceMwH": 99.51
    },
    {
      "startTime": "2025-11-22T01:00:00",
      "endTime": "2025-11-22T02:00:00",
      "priceKwH": 0.09432,
      "priceMwH": 94.32
    }, ....
]
"pricesDayAhead": [
    {
        "startTime": "2025-11-23T00:00:00",
        "endTime": "2025-11-23T01:00:00",
        "priceKwH": 0.08153,
        "priceMwH": 81.53
    },
    {
        "startTime": "2025-11-23T01:00:00",
        "endTime": "2025-11-23T02:00:00",
        "priceKwH": 0.07435,
        "priceMwH": 74.35
    }, ....
]
```

* **<< dataprovider >>/highlights?datetime=<< Current timestamp >>**  
Will give a short summary of the day. In order to get the current price, you will need to provide a timestamp for example: /highlights?datetime=2025-11-22T08:39
```json
{
  "calculations": {
    "averagePriceKwH": 0.09138125,
    "averagePriceMwH": 91.38125
  },
  "currentPrice": {
    "startTime": "2025-11-22T08:00:00",
    "endTime": "2025-11-22T09:00:00",
    "priceKwH": 0.10419,
    "priceMwH": 104.19
  },
  "maxPrice": {
    "startTime": "2025-11-22T07:00:00",
    "endTime": "2025-11-22T08:00:00",
    "priceKwH": 0.11021,
    "priceMwH": 110.21
  },
  "minPrice": {
    "startTime": "2025-11-22T13:00:00",
    "endTime": "2025-11-22T14:00:00",
    "priceKwH": 0.07686,
    "priceMwH": 76.86
  }
}
```

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

<!-- HomeAssistant setup -->
## HomeAssistant

### config

You will need to add the following rest config and for the resource replace the "<<<<<< URL >>>>>" with your server url. 
```yaml
rest:
  - resource: <<<<<<URL>>>>>/prices
    scan_interval: 60
    sensor: 
      - name: DyEn prices
        unique_id: dyen_prices_overview
        value_template: 'OK'
        json_attributes:
          - prices
          - pricesDayAhead
  - resource: <<<<<<URL>>>>>/highlights
      params:
        datetime: "{{now().strftime('%Y-%m-%dT%H:%M')}}"
      scan_interval: 60
      sensor:
        - name: DyEn current price
          unique_id: dyen_current_price
          unit_of_measurement: "EUR"
          value_template: "{{ value_json.currentPrice.priceKwH }}"
        - name: DyEn lowest price
          unique_id: dyen_lowest_price
          unit_of_measurement: "EUR"
          value_template: "{{ value_json.minPrice.priceKwH }}"
        - name: DyEn lowest start time
          unique_id: dyen_lowest_start_time
          device_class: "Timestamp"
          value_template: "{{ as_local(strptime(value_json.minPrice.startTime, '%Y-%m-%dT%H:%M:%S')) }}"
        - name: DyEn lowest end time
          unique_id: dyen_lowest_end_time
          device_class: "Timestamp"
          value_template: "{{ as_local(strptime(value_json.minPrice.endTime, '%Y-%m-%dT%H:%M:%S')) }}"
        - name: DyEn highest price
          unique_id: dyen_highest_price
          unit_of_measurement: "EUR"
          value_template: "{{ value_json.maxPrice.priceKwH }}"
        - name: DyEn highest start time
          unique_id: dyen_highest_start_time
          device_class: "Timestamp"
          value_template: "{{ as_local(strptime(value_json.maxPrice.startTime, '%Y-%m-%dT%H:%M:%S')) }}"
        - name: DyEn highest end time
          unique_id: dyen_highest_end_time
          device_class: "Timestamp"
          value_template: "{{ as_local(strptime(value_json.maxPrice.endTime, '%Y-%m-%dT%H:%M:%S')) }}"
        - name: DyEn average price
          unique_id: dyen_average_price
          unit_of_measurement: "EUR"
          value_template: "{{ value_json.calculations.averagePriceKwH }}"
```

### Dashboard elements
```yaml
type: entities
entities:
  - entity: sensor.dyen_current_price
    name: Current price
  - entity: sensor.dyen_lowest_price
    name: Lowest price
  - entity: sensor.dyen_lowest_start_time
    name: Lowest start time
  - entity: sensor.dyen_highest_price
    name: Highest price
  - entity: sensor.dyen_highest_start_time
    name: Highest start time
  - entity: sensor.dyen_average_price
    name: Average price
title: Dynamic Electicity Prices
```

### Apex charts
Current day
```yaml
type: custom:apexcharts-card
graph_span: 24h
experimental:
  color_threshold: true
header:
  title: Energy Price (Today)
  show: true
  show_states: true
span:
  start: day
now:
  show: true
  label: now
  color: darkblue
series:
  - entity: sensor.dyen_prices
    type: column
    extend_to: end
    unit: €/kWh
    float_precision: 3
    yaxis_id: Price
    show:
      in_header: before_now
      extremas: true
      header_color_threshold: true
    color_threshold:
      - value: 2
        color: 00ed01
      - value: 4
        color: 3af901
      - value: 6
        color: 87fa00
      - value: 8
        color: cefb02
      - value: 10
        color: eeff00
      - value: 12
        color: ffde1a
      - value: 14
        color: ffa700
      - value: 16
        color: ff8d00
      - value: 18
        color: ff7400
      - value: 20
        color: ff4d00
      - value: 22
        color: ff4d00
      - value: 24
        color: ff0000
      - value: 26
        color: e60000
      - value: 28
        color: cc0000
      - value: 30
        color: b30000
      - value: 32
        color: "990000"
      - value: 34
        color: "800000"
      - value: 36
        color: "660000"
      - value: 38
        color: 4d0000
      - value: 40
        color: "330000"
    data_generator: |
      return entity.attributes.prices.map((entry) => {
        return [new Date(entry.startTime), entry.priceKwH*100];
      });
    group_by:
      func: avg
      duration: 1h
yaxis:
  - id: Price
    decimals: 0
    apex_config:
      title:
        text: ¢/kWh
      tickAmount: 5
apex_config:
  legend:
    show: false
  tooltip:
    x:
      show: true
      format: HH:00 - HH:59
```
Day ahead

```yaml
type: custom:apexcharts-card
graph_span: 24h
experimental:
  color_threshold: true
header:
  title: Energy Price (tomorrow)
  show: true
span:
  start: day
  offset: +1d
now:
  show: true
  label: now
  color: darkblue
series:
  - entity: sensor.dyen_prices
    type: column
    extend_to: end
    unit: €/kWh
    float_precision: 3
    yaxis_id: Price
    show:
      in_header: before_now
      extremas: true
      header_color_threshold: true
    color_threshold:
      - value: 2
        color: 00ed01
      - value: 4
        color: 3af901
      - value: 6
        color: 87fa00
      - value: 8
        color: cefb02
      - value: 10
        color: eeff00
      - value: 12
        color: ffde1a
      - value: 14
        color: ffa700
      - value: 16
        color: ff8d00
      - value: 18
        color: ff7400
      - value: 20
        color: ff4d00
      - value: 22
        color: ff4d00
      - value: 24
        color: ff0000
      - value: 26
        color: e60000
      - value: 28
        color: cc0000
      - value: 30
        color: b30000
      - value: 32
        color: "990000"
      - value: 34
        color: "800000"
      - value: 36
        color: "660000"
      - value: 38
        color: 4d0000
      - value: 40
        color: "330000"
    data_generator: |
      return entity.attributes.pricesDayAhead.map((entry) => {
        return [new Date(entry.startTime), entry.priceKwH*100];
      });
    group_by:
      func: avg
      duration: 1h
yaxis:
  - id: Price
    decimals: 0
    apex_config:
      title:
        text: ¢/kWh
      tickAmount: 5
apex_config:
  legend:
    show: false
  tooltip:
    x:
      show: true
      format: HH:00 - HH:59
```


<!-- ROADMAP -->
## Roadmap

- [x] Luminus integration
- [x] Octa+ integration
- [x] Eneco integration
- [ ] Clean up code
- [ ] investigate patterns to streamline integrations

<!-- LICENSE -->
## License

See `LICENSE` for more information.

<!-- CONTACT -->
## Contact

Glenn Thielman - [Twitter](https://x.com/the_glenn90) - [LinkedIn](https://www.linkedin.com/in/glennthielman/)
