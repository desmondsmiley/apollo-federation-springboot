# Apollo Federation Supergraph with Spring Boot

A complete **Apollo Federation 2** supergraph built with two Spring Boot subgraphs using the [Netflix DGS Framework](https://netflix.github.io/dgs/). This project is a beginner-friendly reference for building federated GraphQL APIs in Java.

---

## What Is Apollo Federation?

Apollo Federation lets you split a single large GraphQL API into multiple smaller services (called **subgraphs**), each owned by a different team. An **Apollo Router** (the gateway) stitches them together into one unified **supergraph** that clients query.

```
Client
  │
  ▼
Apollo Router  (port 4000)  ← single entry point
  ├── Users Subgraph        (port 8081)  ← owns User type
  └── Products Subgraph     (port 8082)  ← owns Product, Review types
```

When a client asks for a review's author, the router automatically fetches the product from the Products subgraph and the user details from the Users subgraph — then merges them into one response.

---

## Project Structure

```
apollo-federation-springboot/
├── README.md
├── supergraph.yaml          ← Rover composition config
├── router.yaml              ← Apollo Router config
├── docker-compose.yml       ← runs both subgraphs with Docker
│
├── users-subgraph/          ← Spring Boot app on port 8081
│   ├── build.gradle
│   ├── settings.gradle
│   └── src/main/
│       ├── java/com/example/users/
│       │   ├── model/User.java
│       │   ├── input/CreateUserInput.java
│       │   ├── input/UpdateUserInput.java
│       │   ├── service/UserService.java
│       │   └── datafetcher/
│       │       ├── UserQueryFetcher.java
│       │       ├── UserMutationFetcher.java
│       │       └── UserEntityFetcher.java   ← federation entity resolution
│       └── resources/
│           ├── application.yml
│           └── graphql/schema.graphqls
│
└── products-subgraph/       ← Spring Boot app on port 8082
    ├── build.gradle
    ├── settings.gradle
    └── src/main/
        ├── java/com/example/products/
        │   ├── model/{Product,Review,User}.java
        │   ├── input/{CreateProductInput,UpdateProductInput,AddReviewInput}.java
        │   ├── service/ProductService.java
        │   └── datafetcher/
        │       ├── ProductQueryFetcher.java
        │       ├── ProductMutationFetcher.java
        │       └── ProductEntityFetcher.java ← federation entity resolution
        └── resources/
            ├── application.yml
            └── graphql/schema.graphqls
```

---

## Prerequisites

Before starting, install all of the following:

### 1. Java 17+

Check if Java is installed:
```bash
java -version
```

If not installed, download from [https://adoptium.net](https://adoptium.net) and choose **Temurin 17 (LTS)**.

### 2. Gradle (via the included wrapper — no installation needed)

Both subgraphs include a Gradle wrapper (`gradlew` / `gradlew.bat`), so you do **not** need to install Gradle separately. The wrapper downloads the correct Gradle version automatically on first run.

To verify it works after cloning:
```bash
cd users-subgraph
./gradlew --version   # macOS/Linux
gradlew.bat --version # Windows
```

### 3. Rover CLI (Apollo's schema tool)

Rover is used to compose the supergraph schema from the two subgraphs.

**Install on macOS/Linux:**
```bash
curl -sSL https://rover.apollo.dev/nix/latest | sh
```

**Install on Windows (PowerShell):**
```powershell
iwr 'https://rover.apollo.dev/win/latest' | iex
```

Verify installation:
```bash
rover --version
```

> **No Apollo account needed for local development.** Rover can compose schemas locally without connecting to Apollo GraphOS.

### 4. Apollo Router

The Apollo Router is the high-performance gateway that routes requests between subgraphs.

**Download the latest binary:**

Visit [https://github.com/apollographql/router/releases](https://github.com/apollographql/router/releases) and download the binary for your OS. Place it in the project root or anywhere on your `$PATH`.

Alternatively, install with a script:

**macOS/Linux:**
```bash
curl -sSL https://router.apollo.dev/download/nix/latest | sh
```

**Windows (PowerShell):**
```powershell
iwr 'https://router.apollo.dev/download/win/latest' | iex
```

Verify:
```bash
./router --version
# or if on PATH:
router --version
```

---

## Running Everything Locally (Step by Step)

Open **three terminal windows** for this.

### Terminal 1 — Start the Users Subgraph

```bash
cd users-subgraph
./gradlew bootRun   # macOS/Linux
gradlew.bat bootRun # Windows
```

Wait until you see:
```
Started UsersSubgraphApplication in X.XXX seconds
```

The Users subgraph is now running at **http://localhost:8081/graphql**.
Open **http://localhost:8081/graphiql** in a browser to explore it directly.

### Terminal 2 — Start the Products Subgraph

```bash
cd products-subgraph
./gradlew bootRun   # macOS/Linux
gradlew.bat bootRun # Windows
```

Wait until you see:
```
Started ProductsSubgraphApplication in X.XXX seconds
```

The Products subgraph is now running at **http://localhost:8082/graphql**.
Open **http://localhost:8082/graphiql** in a browser to explore it directly.

### Terminal 3 — Compose the Supergraph and Start the Router

First, compose the supergraph schema (Rover introspects both running subgraphs):

```bash
rover supergraph compose --elv2-license accept --config supergraph.yaml > supergraph.graphql
```

You should see two `HINT` lines about unused enum types (harmless DGS defaults) and then the composed SDL printed to `supergraph.graphql`.

> **Note:** The `--elv2-license accept` flag acknowledges the [Apollo Router ELv2 license](https://www.apollographql.com/docs/resources/elastic-license-v2-faq/). It is required for composition and for running Apollo Router.

A `supergraph.graphql` file is now created in the project root.

Now start the Apollo Router pointing at that schema:

```bash
./router --supergraph supergraph.graphql --config router.yaml
```

Wait for the line: `GraphQL endpoint exposed at http://127.0.0.1:4000/ 🚀`

The router is now running at **http://localhost:4000/graphql**.
The router health check is at **http://127.0.0.1:8088/health**.

---

## Exploring the Supergraph

Open the **Apollo Sandbox** at [https://studio.apollographql.com/sandbox](https://studio.apollographql.com/sandbox) and connect it to `http://localhost:4000/graphql`.

Alternatively, if you enabled the built-in sandbox in `router.yaml`, visit **http://localhost:4000** in your browser.

### Example Queries

**Fetch all users:**
```graphql
query GetAllUsers {
  users {
    id
    name
    email
    username
  }
}
```

**Fetch all products with reviews AND the full author (from Users subgraph):**
```graphql
query GetProductsWithReviews {
  products {
    id
    name
    price
    inStock
    reviews {
      rating
      comment
      author {
        id
        name        # ← this field is resolved from the Users subgraph!
        email
        username
      }
    }
  }
}
```

**Fetch a single product:**
```graphql
query GetProduct {
  product(id: "1") {
    id
    name
    description
    price
    inStock
  }
}
```

**Filter by stock availability:**
```graphql
query InStockProducts {
  productsByStock(inStock: true) {
    id
    name
    price
  }
}
```

### Example Mutations

**Create a user:**
```graphql
mutation CreateUser {
  createUser(input: {
    name: "Diana Prince"
    email: "diana@example.com"
    username: "diana"
  }) {
    id
    name
    email
  }
}
```

**Create a product:**
```graphql
mutation CreateProduct {
  createProduct(input: {
    name: "USB-C Hub"
    description: "7-in-1 USB-C hub with HDMI, USB 3.0, and SD card reader"
    price: 39.99
    inStock: true
  }) {
    id
    name
    price
  }
}
```

**Add a review to a product:**
```graphql
mutation AddReview {
  addReview(input: {
    productId: "2"
    rating: 5
    comment: "Absolutely love this product!"
    authorId: "2"
  }) {
    id
    rating
    comment
    author {
      id
      name    # ← resolved from Users subgraph via federation
    }
  }
}
```

---

## Pre-loaded Sample Data

Both services start with in-memory sample data (no database required):

**Users:**
| ID | Name          | Email               | Username |
|----|---------------|---------------------|----------|
| 1  | Alice Johnson | alice@example.com   | alice    |
| 2  | Bob Smith     | bob@example.com     | bob      |
| 3  | Charlie Brown | charlie@example.com | charlie  |

**Products:**
| ID | Name                  | Price    | In Stock |
|----|-----------------------|----------|----------|
| 1  | Laptop Pro 15         | $1299.99 | Yes      |
| 2  | Wireless Ergonomic Mouse | $49.99 | Yes    |
| 3  | Mechanical Keyboard RGB  | $129.99 | No     |

> **Note:** Data is in-memory and resets when the services restart.

---

## Running with Docker (Optional)

If you have Docker installed, you can build and run the subgraphs in containers.

**Build and start both subgraphs:**
```bash
docker-compose up --build
```

**Wait until both containers are healthy, then in a separate terminal, compose and start the router as described above.**

Check container health:
```bash
docker-compose ps
```

Stop all containers:
```bash
docker-compose down
```

---

## How Apollo Federation Works Here

### Entity References Across Subgraphs

The `User` type is **owned** by the Users subgraph. When the Products subgraph stores a review, it only saves the author's `id`. The router automatically fetches full user data from the Users subgraph when a client requests user fields.

```
Client query:
  products { reviews { author { name } } }

Router execution plan:
  1. Fetch products+reviews from Products subgraph (gets author.id)
  2. Fetch user name from Users subgraph using author.id
  3. Merge and return to client
```

### The `@key` Directive

```graphql
# In users-subgraph — User is defined here (the "owner")
type User @key(fields: "id") {
  id: ID!
  name: String!
  email: String!
  username: String!
}

# In products-subgraph — User is referenced here (just a stub)
type User @key(fields: "id", resolvable: false) {
  id: ID!
}
```

`@key(fields: "id")` in the Users subgraph tells the router: "I can resolve a full User if you give me an `id`."

`resolvable: false` in the Products subgraph means: "I know User has an id but I cannot resolve the full type — go ask the Users subgraph."

### The `UserEntityFetcher`

When the router needs full User details (because a client asked for `author.name`), it sends a special `_entities` query to the Users subgraph:

```graphql
query {
  _entities(representations: [{ __typename: "User", id: "1" }]) {
    ... on User { id name email username }
  }
}
```

The `UserEntityFetcher.java` handles this:

```java
@DgsEntityFetcher(name = "User")
public User fetchUserById(Map<String, Object> values) {
    String id = (String) values.get("id");
    return userService.findById(id);  // looks up by id
}
```

---

## Connecting to Apollo GraphOS (Optional Cloud Features)

Apollo GraphOS is Apollo's cloud platform for managing supergraphs. It's free for small teams. With it you get schema history, change validation, metrics, and more.

### Creating an Apollo GraphOS Account

1. Go to [https://studio.apollographql.com](https://studio.apollographql.com)
2. Click **"Get started for free"**
3. Sign up with GitHub, Google, or email
4. After logging in, click **"Create a graph"**
5. Choose **"Self-hosted"** (since we're running locally)
6. Give your graph a name, e.g., `my-federation-demo`
7. Copy the **Graph API Key** and **Graph Ref** (e.g., `my-federation-demo@main`)

### Authenticate Rover with GraphOS

```bash
rover config auth
```

Paste your API key when prompted. Your key is stored in `~/.rover/config.toml`.

### Publish Subgraph Schemas to GraphOS

```bash
# Publish the users subgraph
rover subgraph publish my-federation-demo@main \
  --name users \
  --schema users-subgraph/src/main/resources/graphql/schema.graphqls \
  --routing-url http://localhost:8081/graphql

# Publish the products subgraph
rover subgraph publish my-federation-demo@main \
  --name products \
  --schema products-subgraph/src/main/resources/graphql/schema.graphqls \
  --routing-url http://localhost:8082/graphql
```

Replace `my-federation-demo@main` with your actual graph ref.

### Run the Router with GraphOS

Instead of a local schema file, run the router pointed at your cloud graph:

```bash
APOLLO_KEY=<your-api-key> \
APOLLO_GRAPH_REF=my-federation-demo@main \
./router --config router.yaml
```

The router will fetch the latest composed schema from GraphOS automatically.

---

## Troubleshooting

### Gradle dependency resolution errors

If you see Gradle errors about missing DGS versions, update the BOM version in `dependencyManagement` inside both `build.gradle` files. Check the [DGS releases page](https://github.com/Netflix/dgs-framework/releases) for the latest version compatible with your Spring Boot version.

**Compatibility reference (standalone DGS starter):**
| Spring Boot | DGS Standalone (`graphql-dgs-spring-boot-starter`) |
|-------------|-----------------------------------------------------|
| 3.3.x       | 9.x.x                                               |
| 3.2.x       | 8.x.x                                               |
| 2.7.x       | 6.x.x                                               |

> This project uses `graphql-dgs-spring-boot-starter` (not the newer `graphql-dgs-spring-graphql-starter`) because it handles Apollo Federation schema loading internally via `federation-graphql-java-support`, which correctly processes the `@link` directive required by Federation 2.

### Port already in use

If port 8081 or 8082 is in use:
```bash
# Find what's using port 8081
lsof -i :8081
# Kill it
kill -9 <PID>
```

Or change the port in `application.yml`:
```yaml
server:
  port: 8091   # change to any free port
```
Then update the corresponding entry in `supergraph.yaml`.

### Rover composition fails

Make sure both subgraphs are running before running `rover supergraph compose`. Rover needs to introspect the live services via their `_service { sdl }` endpoint.

Test that both endpoints are reachable:
```bash
curl -X POST http://localhost:8081/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ _service { sdl } }"}'

curl -X POST http://localhost:8082/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ _service { sdl } }"}'
```

Both should return a JSON response with the schema SDL.

### Router cannot reach subgraphs

If the router can't connect to a subgraph, check:
1. The subgraph is running (`curl http://localhost:8081/actuator/health`)
2. The `routing_url` in `supergraph.yaml` matches the actual subgraph address
3. No firewall blocks the ports

---

## Technology Stack

| Component        | Technology                           |
|------------------|--------------------------------------|
| Subgraph runtime | Spring Boot 3.3.6                    |
| GraphQL server   | Netflix DGS Framework 9.2.2          |
| Federation spec  | Apollo Federation 2.0                |
| Gateway          | Apollo Router                        |
| Schema tooling   | Rover CLI                            |
| Java version     | Java 17+ (tested on Java 25)         |
| Build tool       | Gradle (wrapper included)            |

---

## Next Steps

- **Add a real database:** Replace the in-memory `ConcurrentHashMap` with Spring Data JPA + PostgreSQL or MongoDB.
- **Add authentication:** Use Apollo Router's JWT authentication plugin, or add Spring Security to the subgraphs.
- **Add a third subgraph:** Try creating an `orders-subgraph` that references both `User` and `Product` entities.
- **Add subscriptions:** Use Spring's WebSocket support and DGS subscriptions for real-time updates.
- **Deploy to the cloud:** Containerize with Docker, push to a registry, and deploy to Kubernetes or a cloud provider.
