# Dictators Club - Simple CRUD Service

A minimal Spring Boot CRUD service for a "Dictators Club" where dictators can create profiles and brag about their achievements.

## Features

- **Simple CRUD Operations**: Create, Read, Update, Delete dictators and achievements
- **Row-Level Security**: Only authors can edit/delete their own content, everyone can read
- **OIDC Authentication**: Uses external Keycloak/OIDC server for authentication
- **H2 Database**: In-memory database for simplicity
- **Sample Data**: Pre-loaded with historical dictators and their achievements

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Keycloak server (optional for testing read-only endpoints)

### Running the Application

1. **Clone and build:**
   ```bash
   mvn clean install
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application:**
   - API: http://localhost:8081
   - H2 Console: http://localhost:8081/h2-console
     - JDBC URL: `jdbc:h2:mem:dictatorsdb`
     - Username: `sa`
     - Password: `password`

### OIDC Configuration

Set the environment variable for your Keycloak server:
```bash
export OIDC_ISSUER_URI=http://your-keycloak-server:8080/realms/your-realm
```

Or modify `application.yml` directly.

## API Endpoints

### Public Endpoints (No Authentication Required)

- `GET /api/dictators` - Get all dictators
- `GET /api/dictators/{id}` - Get dictator by ID
- `GET /api/dictators/username/{username}` - Get dictator by username
- `GET /api/dictators/{id}/achievements` - Get achievements for a dictator
- `GET /api/achievements` - Get all achievements
- `GET /api/achievements/{id}` - Get achievement by ID

### Protected Endpoints (Authentication Required)

**Dictator Management:**
- `PUT /api/dictators/{id}` - Update dictator (only owner)
- `DELETE /api/dictators/{id}` - Delete dictator (only owner)

**Achievement Management:**
- `POST /api/dictators/{dictatorId}/achievements` - Create achievement (only owner)
- `PUT /api/achievements/{id}` - Update achievement (only owner)
- `DELETE /api/achievements/{id}` - Delete achievement (only owner)

### Authentication

Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

The service expects the username to be in the `preferred_username` claim of the JWT token.

## Sample Data

The application comes pre-loaded with sample dictators:
- **Napoleon Bonaparte** (username: `napoleon`)
- **Julius Caesar** (username: `caesar`)
- **Genghis Khan** (username: `genghis`)

Each has sample achievements you can view and test with.

## Example API Calls

### Get all dictators:
```bash
curl http://localhost:8081/api/dictators
```

### Get achievements for Napoleon:
```bash
curl http://localhost:8081/api/dictators/1/achievements
```

### Create a new achievement (requires authentication):
```bash
curl -X POST http://localhost:8081/api/dictators/1/achievements \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Battle of Austerlitz",
    "description": "Decisive victory against Austrian and Russian forces",
    "year": 1805
  }'
```

## Development

### Project Structure
```
src/main/java/com/dictatorsclub/
├── config/          # Security and data initialization
├── controller/      # REST controllers
├── model/          # JPA entities
├── repository/     # Data repositories
└── service/        # Business logic
```

### Database Schema
- **dictators**: id, username, name, country, description, years_in_power, created_at, updated_at
- **achievements**: id, title, description, achievement_year, dictator_id, created_at, updated_at

## Security Notes

- Row-Level Security: Users can only modify their own dictator profiles and achievements
- Public read access: All dictator profiles and achievements are publicly readable
- OIDC Integration: Relies on external authentication server for user management
- CORS enabled for SPA integration

## Troubleshooting

1. **"Authentication required" errors**: Ensure your JWT token is valid and includes the `preferred_username` claim
2. **Database issues**: Check H2 console at `/h2-console` to inspect data
3. **OIDC issues**: Verify the `OIDC_ISSUER_URI` environment variable points to your Keycloak realm

## Next Steps

- Configure your Keycloak realm and client
- Integrate with your SPA frontend
- Add more sophisticated validation and error handling
- Consider adding pagination for large datasets
