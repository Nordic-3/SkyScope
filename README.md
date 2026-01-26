# :airplane: SkyScope 

## Explore the sky, discover the world  
Track commercial planes in real time and find cheap flights. A Spring Boot based web application with JavaScript and Bootstrap.

## :hammer_and_wrench: Technologies
- Spring Boot 3.5.6. with Java 22
- Thymeleaf
- PostgreSQL
- JavaScript with Globe.gl, leaflet and openStreetMap
- Bootstrap

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
1. Clone the repository: `git clone https://github.com/Nordic-3/SkyScope.git`
2. Setup PostgreSQL database and configure connection in `application.properties`.
3. Navigate to root folder of the project and run: `mvn clean install` to install dependencies.
4. Run the application: `mvn spring-boot:run`.
5. Note on API Keys: The application uses pre-loaded JSON responses for demonstration. Try these routes for the animated Earth: Budapest, Barcelona, London, New York, Dubai. Real-time demo available for Dubai – London.

## :test_tube: Testing
- **Unit tests:** JUnit 5 and Mockito for service layer testing.
- **Automation tests:** Selenium WebDriver for end-to-end testing of user flows. Note: if the database does not contain `automataTest@test.hu` user with `automatatest` password, some tests will fail.