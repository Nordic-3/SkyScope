# :airplane: SkyScope 

## Explore the sky, discover the world  
Track commercial planes in real time and find cheap flights. A Spring Boot based web application with JavaScript and Bootstrap.

## :hammer_and_wrench: Technologies
- Spring Boot 3.5.6. with Java 21
- Thymeleaf
- Keycloak
- JavaScript with Globe.gl, leaflet and openStreetMap
- Bootstrap
- Playwright

## :sparkles: Key features
- **Advanced Dynamic Filtering:** Beyond basic price filters, I implemented: <br>
  **Aircraft Type:** For aviation enthusiasts (like myself).<br>
  **Transfer Duration:** Specifically for families with toddlers to find the most comfortable routes.<br>
  **Technical note:** Filters are generated dynamically based on search results for a quick overview. 

- **Real-time tracking:** Just search for an offer and see the plane's live position on a map.
<p align="center">
  <img src="assets/tracking.gif" alt="tracking demo" width="800">
</p>


- **Visual Experience:** Developed an animated Earth visualization on the search page that dynamically connects the origin and destination cities with a flight path.
<p align="center">
  <img src="assets/globe.gif" alt="globe demo" width="800">
</p>

- **Third-Party Integrations:**<br>
  **Amadeus API:** Used for fetching real-time flight data and actual pricing.<br>
  **OpenSky API:** Used for real-time plane position.<br>
  **GeoNames API:** Used for fetching city coordinates.<br>
  **Stripe Payment Europe:** Integrated for the checkout flow (payment and order creation are mocked for demo purposes).

## :rocket: Setup Instructions
1. **Clone the repository and navigate to the project folder:**
   ```bash
   git clone [https://github.com/Nordic-3/SkyScope.git](https://github.com/Nordic-3/SkyScope.git)
   cd SkyScope
2. Export `KEYCLOAK_SECRET` environment variable.
3. Run: `mvn clean install` to install dependencies.
4. To start the application execute: `docker compose up`
5. Note on API Keys: The application uses pre-loaded JSON responses for demonstration. Try these routes for the animated 
Earth: Budapest, Barcelona, London, New York, Dubai. Real-time demo available for Dubai – London. If you want to use 
real API please fill `application-dev.properties.example` with real API keys and change `application.properties` `use_api` value to true.

## :test_tube: Testing
- **Unit tests:** JUnit 5 and Mockito for service layer testing.
- **Automation tests:** Playwright for end-to-end testing of user flows. To run the automation tests, first start the application and then execute: `mvn verify -Pautomata-tests`.