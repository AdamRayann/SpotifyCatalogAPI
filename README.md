## API Design and Testing for SpotifyCatalogAPI

**Project Duration:** [Specify Duration]

### Overview
Developed an enhanced version of **SpotifyCatalogAPI**, improving its design and functionality by adding new endpoints and refactoring the architecture to support multiple data sources. This redesign enables seamless switching between different backends, such as JSON files and databases, without modifying core logic. Implemented a **modular and extensible structure** to improve maintainability and scalability.

### Key Contributions
- Designed a **DataSourceService interface** to abstract data access, allowing support for both JSON and database-backed implementations.  
- Developed a **JSON-based data source** and a minimal **database-backed data source** as a proof of concept.  
- Introduced **DataSourceSelector**, enabling dynamic selection of data sources through configuration settings.  
- Implemented full **CRUD operations** for albums, artists, and songs, mirroring the **Spotify API**.  
- Ensured proper API response handling, returning **400 (Bad Request), 404 (Not Found), and 500 (Internal Server Error)** as needed.  
- Developed and executed an extensive **API test suite**, ensuring full coverage for different data sources.  
- Created a **CI pipeline using GitHub Actions** to automate API testing and maintain stability.  

### Technologies & Tools
- **Programming & Frameworks:** Java, Spring Boot  
- **Data & APIs:** REST API, JSON, PostgreSQL, Spotify API  
- **Version Control & CI/CD:** Git, GitHub Actions  
- **Project Management:** JIRA, SDLC  
- **Containerization & Testing:** Docker, Automated Testing (Unit, Integration, API)  
