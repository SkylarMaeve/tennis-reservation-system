# LLM Used - Gemini 3.1 Pro

> ### Potřebuju udělat REST API aplikaci v Javě, za použití frameworku Springboot. Jak funguje springboot? Jak funguje REST API? Jaké vrstvy v aplikaci budou potřeba?

**REST API** (Representational State Transfer) je architektonický styl pro komunikaci přes síť, postavený na protokolu HTTP. Místo toho, aby aplikace vracela grafické uživatelské rozhraní, vrací pouze čistá data (nejčastěji ve formátu JSON). Funguje na principu klient-server: klient (např. mobilní nebo webová aplikace) pošle HTTP požadavek a server odpoví. Akce se rozlišují pomocí HTTP metod:

- **GET:** Získání dat (např. seznam uživatelů).
- **POST:** Vytvoření nového záznamu.
- **PUT:** Úprava existujícího záznamu.
- **DELETE:** Smazání záznamu.

**Spring Boot** je framework pro Javu, který maximálně zjednodušuje tvorbu samostatně běžících aplikací. V ekosystému Javy plní v podstatě totožnou roli jako ASP.NET Core v C#. Nemusíš ručně konfigurovat aplikační servery (obsahuje vestavěný Tomcat) ani složitě propojovat knihovny. Obsahuje tzv. "starters", což jsou balíčky závislostí, které automaticky nakonfigurují aplikaci podle toho, co do ní přidáš (např. přidáš balíček pro databázi a Spring Boot sám nastaví připojení).

Pro udržení čistého kódu a oddělení odpovědností se Spring Boot aplikace standardně dělí do následujících vrstev:

- **1. Entity / Model:** Jednoduché Java třídy (POJO), které reprezentují strukturu dat v databázi. Pomocí anotací (např. `@Entity`) se mapují přímo na databázové tabulky.
- **2. Repository (Datová vrstva):** Rozhraní, která se starají o komunikaci s databází. Zde se využívá Spring Data JPA, což je nástroj pracující na stejném principu jako Entity Framework Core. Většinou stačí jen vytvořit interface a framework sám vygeneruje SQL dotazy pro základní operace (CRUD).
- **3. Service (Byznysová vrstva):** Mozek aplikace. Zde probíhají veškeré výpočty, validace dat, ověřování oprávnění a volání Repository vrstvy. Service vrstva neví nic o HTTP požadavcích, řeší čistě logiku domény.
- **4. Controller (Prezentační vrstva):** Vstupní bod pro REST API. Controllery přijímají HTTP požadavky zvenčí, převezmou parametry, zavolají příslušnou Service vrstvu a nakonec vrátí výsledek zpět klientovi s odpovídajícím HTTP status kódem (např. `200 OK` nebo `404 Not Found`).

Díky tomuto rozvrstvení Controller pouze přesměrovává provoz, Service řeší logiku a Repository ukládá data.

> ### Dej mi příklad Anotací databáze, a příklad Controlleru.

Tady jsou praktické ukázky. V Javě a Spring Bootu se konfigurace velmi často řeší právě pomocí anotací (značek se zavináčem), které frameworku říkají, jak se má daná třída nebo metoda chovat.

### 1. Databázové anotace (Entita)

Představ si, že chceme do databáze ukládat uživatele. K tomu vytvoříme obyčejnou Java třídu a přidáme jí anotace z knihovny JPA (Java Persistence API), podle kterých Spring automaticky vytvoří nebo napojí databázovou tabulku.

```java
import jakarta.persistence.*;

@Entity
@Table(name = "uzivatele")
public class Uzivatel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String jmeno;

    @Column(unique = true, nullable = false)
    private String email;

    // V čisté Javě zde musíš vygenerovat Gettery a Settery.
    // (V praxi se často používá knihovna Lombok a anotace @Data,
    // která je vygeneruje skrytě na pozadí, podobně jako properties v C#).

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJmeno() { return jmeno; }
    public void setJmeno(String jmeno) { this.jmeno = jmeno; }
}

```

**Co dělají jednotlivé anotace:**

- `@Entity`: Značí, že tato třída je mapovaná na databázovou tabulku.
- `@Table(name = "uzivatele")`: Nepovinné. Definuje přesný název tabulky v databázi. Bez toho by se tabulka jmenovala `Uzivatel`.
- `@Id`: Označuje, že tento atribut je primární klíč tabulky.
- `@GeneratedValue(...)`: Říká, jak se má primární klíč generovat (zde `IDENTITY` znamená automatické číslování v databázi, tzv. auto-increment).
- `@Column(...)`: Nastavuje chování sloupce (zda může být prázdný, maximální délku textu, nebo zda musí být hodnota unikátní).

---

### 2. Ukázka REST Controlleru

Controller je "brána" do tvé aplikace. Poslouchá na určité webové adrese a zpracovává příchozí HTTP požadavky.

```java
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/uzivatele")
public class UzivatelController {

    // V reálné aplikaci bys zde měla naimportovanou Service vrstvu
    // private final UzivatelService service;

    // Odchytává požadavek: GET na adresu /api/uzivatele
    @GetMapping
    public List<Uzivatel> ziskejVsechnyUzivatele() {
        // Zde bys zavolala service.ziskejVsechny()
        return List.of(); // Vrací prázdný seznam pro ukázku
    }

    // Odchytává požadavek: GET na adresu /api/uzivatele/{id} (např. /api/uzivatele/5)
    @GetMapping("/{id}")
    public ResponseEntity<Uzivatel> ziskejUzivatelePodleId(@PathVariable Long id) {
        // Třída ResponseEntity umožňuje nastavit jak samotná data, tak HTTP status kód (např. 200 OK)
        Uzivatel nalezenyUzivatel = new Uzivatel();
        return ResponseEntity.ok(nalezenyUzivatel);
    }

    // Odchytává požadavek: POST na adresu /api/uzivatele
    @PostMapping
    public ResponseEntity<Uzivatel> vytvorUzivatele(@RequestBody Uzivatel novyUzivatel) {
        // @RequestBody automaticky převede příchozí JSON z klienta na Java objekt Uzivatel

        // Zde bys zavolala např. service.ulozUzivatele(novyUzivatel)

        // Vrací vytvořeného uživatele a HTTP status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(novyUzivatel);
    }
}

```

**Co dělají jednotlivé anotace v Controlleru:**

- `@RestController`: Říká Springu, že data vrácená z metod se mají automaticky formátovat do JSONu a poslat zpět klientovi.
- `@RequestMapping("/api/uzivatele")`: Nastavuje základní část URL adresy pro všechny metody v této třídě.
- `@GetMapping` / `@PostMapping`: Specifikuje, na jakou HTTP metodu (GET, POST...) daná funkce reaguje.
- `@PathVariable`: Vezme hodnotu přímo z URL adresy (to, co je ve složených závorkách `{id}`) a vloží ji do proměnné.
- `@RequestBody`: Vezme tělo požadavku (typicky JSON, který pošle klient) a Spring ho automaticky "přeloží" do zadaného Java objektu.

> ### V aplikaci potřebuju použít H2 in memory databázi

Použití H2 in-memory databáze je ve Spring Bootu extrémně přímočaré. Protože běží v operační paměti, při každém restartu aplikace se vyčistí, což je ideální pro vývoj a testování, kdy nechceš řešit instalaci a migraci klasického databázového serveru (jako PostgreSQL nebo MySQL).

K jejímu zprovoznění stačí udělat dva kroky: přidat závislost a upravit konfigurační soubor.

### 1. Přidání závislosti (Dependency)

Pokud používáš **Maven** (soubor `pom.xml`), přidej do bloku `<dependencies>` následující XML. Tím si Spring Boot sám stáhne H2 databázi:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Pokud ji ještě nemáš, budeš potřebovat i Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

```

_(Pokud používáš **Gradle** v `build.gradle`, přidáš: `runtimeOnly 'com.h2database:h2'`)_

### 2. Konfigurace (application.properties)

Ve složce `src/main/resources` najdeš soubor `application.properties` (nebo `application.yml`). Vlož do něj toto nastavení:

```properties
# Povolení webové konzole H2 (abys viděla data v prohlížeči)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Nastavení připojení k in-memory databázi
spring.datasource.url=jdbc:h2:mem:mojedatabaze
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Nařízení pro JPA/Hibernate, aby automaticky vytvořilo tabulky z tvých @Entity tříd
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

```

**Co se stane po spuštění aplikace?**

1. Spring Boot automaticky vytvoří H2 databázi v paměti s názvem `mojedatabaze`.
2. Projde všechny tvé třídy označené anotací `@Entity` (např. třídu `Uzivatel` z předchozí ukázky).
3. Na pozadí vygeneruje a spustí SQL příkazy pro vytvoření příslušných tabulek. Díky `spring.jpa.show-sql=true` tyto SQL příkazy uvidíš přímo ve výpisu (logu) v konzoli.

---

### Jak si prohlédnout uložená data

Ačkoliv je databáze pouze v paměti, Spring Boot ti automaticky spustí webového klienta pro její správu.

1. Spusť svou aplikaci.
2. V prohlížeči jdi na adresu: **`http://localhost:8080/h2-console`**
3. Na přihlašovací obrazovce zadej do pole **JDBC URL** hodnotu z konfigurace, tedy `jdbc:h2:mem:mojedatabaze`.
4. Uživatelské jméno je `sa`, heslo nech prázdné a klikni na Connect. Uvidíš webové rozhraní, kde si můžeš napřímo psát SQL dotazy a kontrolovat, co tvůj Controller a Service reálně do databáze uložily.

> ### Potřebuju aby aplikace měla login a rozlišvala mezi rolemi USER a ADMIN. K tomu se použije JWT token. Uživatel se přihlásí přes Basic autorizaci, následně po odbržení tokenu bud emoci přistupovat k API, jina dostane chybu UNAUTHORIZED. USER bud emít přístup k pouze READ operacím, s jendou vyjímkou (jeden konkrtérní endpoint) ADMIN Role bude mít přístup ke všem endpoints. Zároveň potřebuju aby endpoint /api/auth/login byl dostupný všem, i bez autorizace (očividně, když tento endpoint slouží k autorizaci)

Pro implementaci tohoto scénáře přidáme do projektu **Spring Security** a knihovnu pro práci s JWT. Architektura bude fungovat takto: Spring Security zablokuje veškerý provoz. Následně vytvoříme konfiguraci, která veřejně zpřístupní pouze `/api/auth/login`, vypne klasické sessions (REST API musí být _stateless_) a zavede vlastní filtr, který u každého požadavku zkontroluje přítomnost JWT tokenu.

Tady je rozpad toho, jak to poskládat, krok za krokem.

### 1. Potřebné závislosti (Maven)

Do `pom.xml` je nutné přidat Spring Security a knihovnu JJWT pro generování tokenů:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<!-- Knihovny pro JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

```

### 2. Hlavní bezpečnostní konfigurace (SecurityFilterChain)

Zde definuješ přesná pravidla pro endpointy a role. Spring Security ve verzi 3+ používá k zápisu pravidel moderní lambda syntaxi.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // private final JwtFilter jwtFilter; (vysvětleno níže)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Vypnutí CSRF ochrany (pro stateless REST API s tokeny není potřeba)
            .csrf(csrf -> csrf.disable())

            // 2. Nastavení session na STATELESS (API si nesmí pamatovat uživatele mezi requesty)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 3. Pravidla pro endpointy
            .authorizeHttpRequests(auth -> auth
                // Login je dostupný všem bez přihlášení
                .requestMatchers("/api/auth/login").permitAll()

                // USER může dělat jakékoliv READ (GET) operace
                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")

                // Zde je ta tvá jedna výjimka (např. POST na konkrétní endpoint pro USERa)
                .requestMatchers(HttpMethod.POST, "/api/moje-vyjimka").hasAnyRole("USER", "ADMIN")

                // Všechny ostatní požadavky (POST, PUT, DELETE na cokoliv jiného) vyžadují ADMIN
                .requestMatchers("/api/**").hasRole("ADMIN")

                // Cokoliv dalšího musí být alespoň přihlášeno
                .anyRequest().authenticated()
            );

            // 4. Zařazení vlastního JWT filtru před standardní Spring Security filtr
            // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

```

> **Pozor na detail:** Pokud v konfiguraci použiješ `hasRole("USER")`, Spring na pozadí očekává, že v databázi/tokenu má role prefix `ROLE_`, tedy `ROLE_USER`.

### 3. Login Controller (Získání tokenu)

Tento endpoint přijme **Basic Auth** hlavičku (což je base64 zakódované jméno a heslo), ověří ji a vrátí vygenerovaný JWT token.

```java
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // private final JwtService jwtService;
    // private final UzivatelRepository repository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {

        // 1. Kontrola, zda přišla Basic hlavička
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chybí Basic autentizace");
        }

        // 2. Dekódování Base64 (Basic dXppdmF0ZWw6aGVzbG8= -> uzivatel:heslo)
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] parts = credentials.split(":", 2);
        String jmeno = parts[0];
        String heslo = parts[1];

        // 3. Zde bys ověřila v databázi, zda uživatel existuje a heslo sedí (ideálně hashované!)
        // boolean isValid = authService.verify(jmeno, heslo);
        boolean jeSpravne = jmeno.equals("admin") && heslo.equals("tajneheslo"); // Příklad!

        if (jeSpravne) {
            // 4. Pokud je vše OK, vygeneruješ JWT token (uvnitř bude uložena i role)
            // String token = jwtService.generateToken(jmeno, "ROLE_ADMIN");
            String token = "tady.bude.tvuj_skutecny_jwt_token";
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Špatné jméno nebo heslo");
        }
    }
}

```

### 4. Co se děje po loginu (JWT Filtr)

Aby zbytek API fungoval a vracel správně `401 UNAUTHORIZED` bez tokenu, budeš potřebovat vytvořit třídu (např. `JwtFilter`), která dědí z `OncePerRequestFilter`.

Tento filtr zachytí každý příchozí požadavek (kromě `/login`), podívá se, jestli klient poslal hlavičku `Authorization: Bearer <token>`, zkontroluje platnost tokenu a jeho podpis. Pokud je token platný, filtr z něj přečte roli (např. `ROLE_USER`) a řekne Spring Security: _"Tento člověk je v pořádku, pusť ho dál s těmito právy."_ Pokud token chybí nebo je expirovaný, požadavek je okamžitě odmítnut.

> ### Napiš mi JWT filter

Ve světě .NET aplikací se tomuto vzoru říká Middleware – komponenta, která stojí v cestě HTTP požadavku, může si ho prohlédnout, upravit, nebo ho úplně zastavit. Ve Spring Bootu se pro tento účel nejčastěji dědí ze třídy `OncePerRequestFilter`, která zaručuje, že se kód spustí pro každý příchozí požadavek právě jednou.

Tento filtr přečte hlavičku `Authorization`, vysekne z ní slovo "Bearer", ověří platnost tokenu a pokud je vše v pořádku, zapíše uživatele a jeho role (např. `ROLE_ADMIN`) do bezpečnostního kontextu Springu.

```java
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // Pomocná třída, která řeší kryptografii a parsování (vysvětlena níže)
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Získání hlavičky Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Pokud hlavička chybí nebo nemá správný formát, filtr končí a posílá request dál
        // (Spring Security ho následně zablokuje, pokud endpoint vyžaduje přihlášení)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Odstranění prefixu "Bearer " (prvních 7 znaků)
        String token = authHeader.substring(7);

        try {
            // 4. Pokud je token platný, vytáhneme z něj jméno a role
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                List<String> roles = jwtService.extractRoles(token);

                // 5. Spring Security očekává role zabalené v objektech GrantedAuthority
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList());

                // 6. Vytvoření objektu, který reprezentuje přihlášeného uživatele
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // 7. Zápis uživatele do globálního kontextu. Tím Springu řekneme: "Tento request je autentizován".
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Token expiroval, má špatný podpis, nebo je poškozený.
            // Záměrně neděláme nic – kontext zůstane prázdný a Spring Security vrátí 401 UNAUTHORIZED.
            System.out.println("Neplatný JWT token: " + e.getMessage());
        }

        // 8. Odeslání zpracování dalšímu filtru v řadě
        filterChain.doFilter(request, response);
    }
}

```

Aby to celé fungovalo, filtr využívá závislost `JwtService`. Správná praxe diktuje oddělit logiku HTTP požadavku (Filtr) od kryptografické logiky parsování tokenu (Service).

Třída `JwtService` bude obsahovat metody, které za pomoci knihovny JJWT podepíšou a rozšifrují samotný text tokenu pomocí tajného klíče (tzv. secret key).

> ### Napiš i třídu JWT Service, a SecurityCOnfig

Tady jsou obě zbývající třídy. Nejprve si připravíme `JwtService`, která funguje jako "kryptografická továrna" pro tvoje tokeny. Poté to všechno spojíme dohromady v `SecurityConfig`, kde zaregistrujeme náš předchozí `JwtFilter`.

### 1. Třída JwtService (Kryptografie a parsování)

Tato třída využívá knihovnu JJWT. Token se skládá z hlavičky, těla (tzv. Claims, kde jsou uložena data jako jméno a role) a digitálního podpisu. Podpis se počítá pomocí tajného klíče (Secret Key), díky kterému aplikace pozná, že token nebyl po cestě zfalšován.

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    // V reálné aplikaci by tento klíč NIKDY neměl být přímo v kódu!
    // Měla bys ho načítat z application.properties (např. pomocí @Value) nebo proměnných prostředí.
    // Musí to být dostatečně dlouhý Base64 string (pro algoritmus HS256 alespoň 256 bitů).
    private static final String SECRET_KEY = "VG9obGVKZVRham55S2xpY1Byb01vamVBcGlNdXNpQnl0RG9zdGF0ZWNuZURsb3VoeQ==";

    // Platnost tokenu (např. 24 hodin v milisekundách)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // --- METODY PRO GENEROVÁNÍ TOKENU ---

    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        // Do tokenu si můžeme přidat vlastní data (tzv. claims)
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username) // Hlavní identifikátor uživatele
                .setIssuedAt(new Date(System.currentTimeMillis())) // Čas vydání
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Čas expirace
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Podpis klíčem a algoritmem
                .compact();
    }

    // --- METODY PRO ČTENÍ A OVĚŘOVÁNÍ TOKENU ---

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    public boolean isTokenValid(String token) {
        try {
            // Pokud parsování projde bez výjimky a token není expirovaný, je platný
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Pomocná metoda pro získání šifrovacího klíče ve správném formátu pro JJWT
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

```

### 2. Třída SecurityConfig (Finální propojení)

Tady definujeme přesná přístupová práva, která jsi specifikovala (Login veřejně, USER má READ + jednu výjimku, ADMIN má vše). Důležité je zaregistrovat náš `JwtFilter`, aby se spouštěl před standardním procesem ověřování jména a hesla.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // Spring automaticky injektuje náš hotový JwtFilter díky anotaci @Component u filtru
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Zrušení CSRF, pro stateless API s tokeny to není potřeba
            .csrf(csrf -> csrf.disable())

            // Nastavení na STATELESS - Spring si nesmí vytvářet HTTP sessions
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Konfigurace přístupů k jednotlivým URL
            .authorizeHttpRequests(auth -> auth
                // 1. Přihlášení je dostupné pro všechny
                .requestMatchers("/api/auth/login").permitAll()

                // 2. USER (i ADMIN) má přístup ke všem GET operacím (READ operace)
                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")

                // 3. Tvoje jedna specifická výjimka pro roli USER (např. odeslání nějakého formuláře)
                .requestMatchers(HttpMethod.POST, "/api/moje-vyjimka").hasAnyRole("USER", "ADMIN")

                // 4. Všechny ostatní operace na API (POST, PUT, DELETE) může dělat jen ADMIN
                .requestMatchers("/api/**").hasRole("ADMIN")

                // Pojistka: cokoliv jiného by mělo být alespoň přihlášeno
                .anyRequest().authenticated()
            )

            // ZAŘAZENÍ FILTRU: Náš JwtFilter se musí spustit PŘED standardním filtrem
            // Spring Security. Díky tomu zachytíme token dřív, než Spring API zablokuje.
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

```

S tímhle nastavením máš kompletní bezpečnostní štít. Když klient zavolá tvé API s hlavičkou `Authorization: Bearer <token>`, požadavek projde přes `JwtFilter`. Ten pomocí `JwtService` zkontroluje podpis a platnost. Pokud je vše v pořádku, vytáhne roli a pošle požadavek dál. Následně se uplatní pravidla v `SecurityConfig`, která požadavek propustí až k samotnému Controlleru, případně vrátí `403 Forbidden` (pokud klient nemá potřebnou roli) nebo `401 Unauthorized` (pokud token chybí či expiroval).

> ### Secret Key pro JWT Token, délka platnosti tokenu a délka refreshu mají být součástí externí konfigurace

To je naprosto správný a bezpečný přístup. Udržovat hesla a klíče přímo ve zdrojovém kódu (hardcoded) je bezpečnostní riziko a navíc to znemožňuje měnit konfiguraci pro různá prostředí (např. lokální vývoj vs. produkce) bez přepisování kódu.

Ve Spring Bootu k tomu slouží soubor `application.properties` (nebo `.yml`) a anotace `@Value`, kterou hodnoty bezpečně "vstříkneš" (injectneš) do své služby.

Tady je návod, jak to krok za krokem upravit.

### 1. Úprava application.properties

Do souboru `src/main/resources/application.properties` si nadefinuj vlastní klíče a hodnoty. Čas uvádíme v milisekundách (např. 24 hodin = 86 400 000 ms, 7 dní = 604 800 000 ms).

```properties
# --- JWT Konfigurace ---
# Tajný klíč (v produkci by se měl načítat z proměnné prostředí)
security.jwt.secret-key=VG9obGVKZVRham55S2xpY1Byb01vamVBcGlNdXNpQnl0RG9zdGF0ZWNuZURsb3VoeQ==

# Délka platnosti klasického Access Tokenu (např. 15 minut nebo 24 hodin)
security.jwt.expiration-time=86400000

# Délka platnosti Refresh Tokenu (např. 7 dní)
security.jwt.refresh-time=604800000

```

_(Tip: V produkčním prostředí pak Spring Bootu jednoduše podstrčíš tuto hodnotu přes proměnnou prostředí operačního systému, např. `SECURITY_JWT_SECRET_KEY=tvoje_realne_heslo`, a framework ji automaticky nahradí.)_

### 2. Přepracování třídy JwtService

Nyní upravíme `JwtService`. Odstraníme statické konstanty a nahradíme je běžnými proměnnými třídy. Pomocí anotace `@Value` řekneme Springu, aby do nich při startu aplikace vložil hodnoty ze souboru properties.

Jelikož jsi zmínila i refresh token, přidala jsem rovnou metodu `generateRefreshToken`, abys viděla, jak se druhá délka platnosti využije.

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    // Spring automaticky dosadí hodnoty z application.properties
    // Pozor: proměnné už nesmí být 'static', jinak do nich @Value nic nevloží!

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-time}")
    private long refreshExpiration;

    // --- METODY PRO GENEROVÁNÍ ---

    // Vygenerování běžného Access Tokenu (s rolemi a kratší platností)
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return buildToken(claims, username, jwtExpiration);
    }

    // Vygenerování Refresh Tokenu (obvykle neobsahuje role, jen jméno a delší platnost)
    public String generateRefreshToken(String username) {
        return buildToken(new HashMap<>(), username, refreshExpiration);
    }

    // Společná privátní metoda pro sestavení tokenu, ať se neopakuje kód
    private String buildToken(Map<String, Object> extraClaims, String username, long expirationTime) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- METODY PRO ČTENÍ A OVĚŘOVÁNÍ ---

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

```

Tímto způsobem je tvůj kód mnohem čistší, bezpečnější a připravený na produkční nasazení. Kdykoliv budeš chtít prodloužit platnost tokenu, prostě jen upravíš textový konfigurační soubor a aplikaci restartuješ – nemusíš vůbec zasahovat do zkompilované Javy.

> ### Když v aplikaci dojde k nějaké chybě, zakízané operaci, odlišně od autoruzace, potřebuju aby controller vrátil HTTP kód 400

Pokud chceš ve Spring Bootu zajistit, aby různé aplikační chyby a nepovolené operace (které nesouvisejí přímo s bezpečností/autorizací) vracely `400 Bad Request`, nejlepším řešením je použít **globální zachytávání chyb** pomocí anotace `@ControllerAdvice`.

Tento přístup ti umožní napsat chybovou logiku na jedno místo. Jakmile kdekoliv ve tvé aplikaci (v Controlleru nebo v Service vrstvě) vyhodíš specifickou výjimku, Spring ji zachytí, zpracuje a klientovi odešle naformátovanou chybovou odpověď s HTTP kódem 400.

Tady je návod, jak to elegantně postavit:

### 1. Vytvoření vlastní výjimky (Custom Exception)

Nejprve si vytvoříš vlastní typ výjimky. Kdykoliv v aplikaci narazíš na nějakou zakázanou operaci (např. uživatel chce smazat položku, která je svázaná s jinými daty), vyhodíš tuto výjimku.

```java
// Běžná výjimka pro byznysové chyby, které znamenají Bad Request (400)
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}

```

### 2. Globální Handler (ControllerAdvice)

Nyní vytvoříme třídu s anotací `@ControllerAdvice` (nebo `@RestControllerAdvice`). Ta "naslouchá" celé aplikaci. Jakmile je vyhozena výjimka, najde odpovídající metodu a pošle výsledek klientovi.

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Tato metoda se zavolá, kdykoliv kdekoliv v kódu uděláš: throw new BadRequestException("...");
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException ex) {

        // Vytvoříme úhledné JSON tělo odpovědi
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value()); // 400
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage()); // Předáme chybovou hlášku

        // Můžeš zde pro debugování i logovat do konzole, jak jsme řešili u 401:
        // ex.printStackTrace();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Můžeš přidat další handlery. Například pokud chceš 404 pro nenalezená data:
    /*
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        // Podobná logika, ale vracíš HttpStatus.NOT_FOUND (404)
    }
    */
}

```

### 3. Použití v praxi (v Service vrstvě)

Nyní už v Controlleru nemusíš psát hromadu `try-catch` bloků. Jen zavoláš Service. Pokud Service zjistí porušení pravidel, jednoduše vyhodí tvou výjimku a Spring už sám vrátí stav 400.

```java
import org.springframework.stereotype.Service;

@Service
public class PolozkaService {

    public void zpracujOperaci(Long id) {
        // Příklad byznysové kontroly
        boolean jeOperacePovolena = zkontrolujPovoleni(id);

        if (!jeOperacePovolena) {
            // ZDE VYHODÍME VÝJIMKU. ControllerAdvice ji chytí a vrátí 400.
            throw new BadRequestException("Tuto položku nelze zpracovat, protože nesplňuje podmínky.");
        }

        // Pokračování v normální logice...
    }

    private boolean zkontrolujPovoleni(Long id) {
        return false; // Simulace selhání
    }
}

```

**Výsledek pro klienta (REST API Response):**
Pokud klient zavolá endpoint, který vyvolá tuto chybu, dostane zpět čistý JSON s HTTP statusem `400 Bad Request`:

```json
{
  "timestamp": "2026-09-08T15:12:23.000",
  "status": 400,
  "error": "Bad Request",
  "message": "Tuto položku nelze zpracovat, protože nesplňuje podmínky."
}
```

> ### DAO vrstva nesmí používat Spring Data JPA . Jak implementovat DAO vrstvu? Všimla jsem si že se používá nějaký EntityManager

Použití **EntityManageru** je přesně ta "zlatá střední cesta". Využíváš standardní specifikaci JPA (Java Persistence API, na pozadí nejčastěji poháněnou frameworkem Hibernate), takže mapuješ databázi na objekty. Zároveň ale nepoužíváš Spring Data JPA (žádné automaticky generované interfejsy) – veškerou DAO logiku a dotazy si píšeš a řídíš sama.

Oproti `JdbcTemplate` se ti vrací obrovská výhoda: nemusíš psát čisté SQL a ručně mapovat sloupce. Pracuješ rovnou s Java objekty, ale pomocí vlastnoručně napsané třídy.

### 1. Návrat k Entitě (@Entity)

Protože EntityManager pracuje na bázi ORM (objektově-relační mapování), tvůj model musí mít opět JPA anotace (stejné, jako jsme měli úplně na začátku). Databáze (H2) si díky nim opět dokáže sama vygenerovat tabulky, takže nepotřebuješ `schema.sql`.

```java
import jakarta.persistence.*;

@Entity
@Table(name = "uzivatele")
public class Uzivatel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // další atributy (jmeno, email, role), gettery a settery...
}

```

### 2. Implementace DAO pomocí EntityManageru

Vytvoříš třídu s anotací `@Repository`. Místo klasického injektování přes konstruktor zde použiješ specifickou anotaci `@PersistenceContext`, která Springu řekne, aby ti dodal aktivní instanci `EntityManageru`.

**Kritické pravidlo:** Veškeré operace, které mění stav databáze (Insert, Update, Delete), musí běžet v transakci. Proto DAO vrstvu (nebo volající Service vrstvu) musíme označit anotací `@Transactional`.

```java
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional // Zajišťuje, že se operace obalí do databázové transakce
public class UzivatelDao {

    @PersistenceContext
    private EntityManager entityManager;

    // --- C (Create): Vložení záznamu ---
    public Uzivatel vlozUzivatele(Uzivatel uzivatel) {
        // Metoda persist vezme nový objekt a uloží ho.
        // Zároveň mu automaticky přiřadí vygenerované ID.
        entityManager.persist(uzivatel);
        return uzivatel;
    }

    // --- R (Read): Hledání podle primárního klíče ---
    public Optional<Uzivatel> najdiPodleId(Long id) {
        Uzivatel uzivatel = entityManager.find(Uzivatel.class, id);
        return Optional.ofNullable(uzivatel);
    }

    // --- R (Read): Vlastní dotaz s využitím JPQL ---
    public Optional<Uzivatel> najdiPodleJmena(String jmeno) {
        // Pozor: V JPQL nepíšeš názvy tabulek, ale NÁZVY TŘÍD a ATRIBUTŮ!
        String jpql = "SELECT u FROM Uzivatel u WHERE u.jmeno = :hledaneJmeno";

        try {
            Uzivatel uzivatel = entityManager.createQuery(jpql, Uzivatel.class)
                    .setParameter("hledaneJmeno", jmeno)
                    .getSingleResult(); // Vyhodí NoResultException, pokud nic nenajde
            return Optional.of(uzivatel);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // --- R (Read): Získání všech záznamů ---
    public List<Uzivatel> ziskejVsechny() {
        return entityManager.createQuery("SELECT u FROM Uzivatel u", Uzivatel.class)
                .getResultList();
    }

    // --- U (Update): Úprava záznamu ---
    public Uzivatel upravUzivatele(Uzivatel uzivatel) {
        // Metoda merge vezme objekt (který už má nastavené ID),
        // najde ho v databázi a aktualizuje jeho hodnoty.
        return entityManager.merge(uzivatel);
    }

    // --- D (Delete): Smazání záznamu ---
    public void smazUzivatele(Long id) {
        // Pro smazání musíme Entitu nejprve dostat do tzv. "managed" stavu
        // entityManager.getReference nevytahuje z DB všechna data, jen si vytvoří proxy odkaz
        Uzivatel reference = entityManager.getReference(Uzivatel.class, id);
        entityManager.remove(reference);
    }
}

```

### Proč zvolit tohle řešení?

- **Žádný SQL dialekt:** Všimni si JPQL dotazu (`SELECT u FROM Uzivatel u`). Nejsi závislá na syntaxi H2 nebo PostgreSQL. Dotaz je objektový a Hibernate ho při spuštění přeloží do správného SQL podle toho, jakou databázi zrovna používáš.

> ### Service layer mezi DAO a Controllery, pracuje a volá DAO vrstvu a vrací DTO objekty controllerům?

Přesně tak. To je naprosto ukázkový návrhový vzor pro moderní backendové aplikace.

Každá vrstva má jasně vymezenou zodpovědnost a funguje jako "překladatel" pro tu nadřazenou:

- **Controller** řeší pouze komunikaci ven (HTTP hlavičky, status kódy). Nezná databázi.
- **DAO (Repository)** řeší pouze komunikaci s databází (SQL/JPQL, Entity). Nezná HTTP a nezná DTO.
- **Service vrstva** stojí uprostřed. Je to mozek. Zde dochází k validaci byznys pravidel, orchestraci (volání více DAO vrstev najednou) a především k onomu **překladu mezi Entitami (databázovým modelem) a DTO (přepravním modelem pro klienta)**.

Tady je ukázka, jak taková Service vrstva vypadá v praxi, včetně navázání na dříve vytvořenou `BadRequestException`.

### 1. Definice DTO

Nejprve potřebujeme objekty, které budeme vracet klientovi. Klient nemusí vidět všechno (např. vnitřní systémové flagy, data vytvoření nebo hesla).

```java
// DTO pro vytvoření (klient neposílá ID)
public record UzivatelCreateDto(String jmeno, String email, String role) {}

// DTO pro vrácení klientovi (obsahuje už i ID)
public record UzivatelResponseDto(Long id, String jmeno, String email, String role) {}

```

### 2. Implementace Service vrstvy

Service vrstva dostane do konstruktoru naše DAO. Zároveň zde vytvoříme jednoduchou pomocnou metodu, která přeloží Entitu do `UzivatelResponseDto`. (V praxi se k tomuto překladu často používají knihovny jako _MapStruct_, ale pro začátek je čistší to napsat ručně).

```java
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
// @Transactional zde znamená, že všechny operace uvnitř jedné metody proběhnou v jedné databázové transakci.
// Pokud cokoliv spadne (např. vyhodíme výjimku), žádná data se do DB neuloží.
@Transactional
public class UzivatelService {

    private final UzivatelDao uzivatelDao;

    public UzivatelService(UzivatelDao uzivatelDao) {
        this.uzivatelDao = uzivatelDao;
    }

    // --- ZÍSKÁNÍ DAT ---

    public List<UzivatelResponseDto> ziskejVsechny() {
        return uzivatelDao.ziskejVsechny().stream()
                .map(this::mapujNaDto)
                .collect(Collectors.toList());
    }

    public UzivatelResponseDto ziskejPodleId(Long id) {
        // Zkusíme najít entitu. Pokud DAO vrátí Optional.empty(), vyhodíme výjimku
        Uzivatel entita = uzivatelDao.najdiPodleId(id)
                .orElseThrow(() -> new BadRequestException("Uživatel s ID " + id + " neexistuje."));

        return mapujNaDto(entita);
    }

    // --- VYTVOŘENÍ DAT (S BYZNYS LOGIKOU) ---

    public UzivatelResponseDto vytvorUzivatele(UzivatelCreateDto dto) {
        // 1. Byznys validace (např. nepovolíme vytvořit uživatele s administrátorskou rolí přes běžné API)
        if ("ADMIN".equalsIgnoreCase(dto.role())) {
            throw new BadRequestException("Z bezpečnostních důvodů nelze vytvořit roli ADMIN napřímo.");
        }

        // 2. Kontrola duplicit
        if (uzivatelDao.najdiPodleJmena(dto.jmeno()).isPresent()) {
            throw new BadRequestException("Uživatel se jménem '" + dto.jmeno() + "' již existuje.");
        }

        // 3. Překlad DTO -> Entita (příprava pro uložení)
        Uzivatel novaEntita = new Uzivatel();
        novaEntita.setJmeno(dto.jmeno());
        novaEntita.setEmail(dto.email());
        novaEntita.setRole(dto.role().toUpperCase()); // Normalizace dat

        // 4. Volání DAO (po provedení bude mít 'ulozenaEntita' naplněné ID)
        Uzivatel ulozenaEntita = uzivatelDao.vlozUzivatele(novaEntita);

        // 5. Překlad Entita -> DTO pro vrácení zpět
        return mapujNaDto(ulozenaEntita);
    }

    // --- POMOCNÁ METODA PRO MAPOVÁNÍ ---

    private UzivatelResponseDto mapujNaDto(Uzivatel uzivatel) {
        return new UzivatelResponseDto(
                uzivatel.getId(),
                uzivatel.getJmeno(),
                uzivatel.getEmail(),
                uzivatel.getRole()
        );
    }
}

```

### 3. Zapojení do Controlleru

A na závěr ukázka, jak se tím neuvěřitelně "vyčistí" Controller. Controller se už vůbec nestará o databázi ani o chyby. Jen přijme požadavek, zavolá Service a výsledek zabalí do správného HTTP statusu. O případné chyby se postará náš předchozí `GlobalExceptionHandler`.

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/uzivatele")
public class UzivatelController {

    private final UzivatelService uzivatelService;

    public UzivatelController(UzivatelService uzivatelService) {
        this.uzivatelService = uzivatelService;
    }

    @GetMapping
    public ResponseEntity<List<UzivatelResponseDto>> getAll() {
        return ResponseEntity.ok(uzivatelService.ziskejVsechny());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UzivatelResponseDto> getById(@PathVariable Long id) {
        // Pokud service vyhodí BadRequestException, Controller Advice to automaticky odchytí a vrátí 400
        return ResponseEntity.ok(uzivatelService.ziskejPodleId(id));
    }

    @PostMapping
    public ResponseEntity<UzivatelResponseDto> create(@RequestBody UzivatelCreateDto dto) {
        UzivatelResponseDto vytvorenyUzivatel = uzivatelService.vytvorUzivatele(dto);
        // Po vytvoření správně vracíme HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(vytvorenyUzivatel);
    }
}

```

Tímto jsi dosáhla robustní a plně oddělené architektury, která se bude snadno udržovat a testovat.

> ### Co to jsou @Bean?
>
> Anotace `@Bean` je v podstatě "recept" pro Spring, jak má vytvořit a nakonfigurovat konkrétní objekt, o který se pak bude sám starat.

Když nad metodu napíšeš `@Bean`, říkáš frameworku: _"Zavolej tuto metodu, vezmi objekt, který vrátí, a ulož si ho do svého vnitřního registru (tzv. Application Contextu). Kdykoliv ho bude nějaká jiná část aplikace potřebovat, rovnou jí ho předej."_ Těmto objektům spravovaným Springem se říká **Spring Beans**.

Zásadní rozdíl oproti anotacím jako `@Service`, `@Repository` nebo `@Controller` spočívá v tom, na co je aplikuješ:

- **`@Service` / `@Component`:** Píšeš nad **vlastní třídy**, které jsi sama naprogramovala (např. `UzivatelService`). Spring si tvůj kód sám projde a objekty z nich vytvoří.
- **`@Bean`:** Píšeš nad **metody**, typicky uvnitř třídy označené `@Configuration`. Používá se hlavně tehdy, když potřebuješ do Springu zapojit **třídu z cizí knihovny**, do jejíhož zdrojového kódu nemůžeš zasáhnout a připsat tam `@Component`.

### Příklad z praxe: Šifrování hesel

Když jsi dělala přihlašování, potřebovala jsi ověřovat hesla. Ve Spring Security k tomu slouží objekt `PasswordEncoder` (např. realizovaný přes Bcrypt). Protože je to třída z knihovny Springu, nemůžeš ji upravit. Vytvoříš si tedy konfigurační třídu a v ní `@Bean` metodu:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityAppConfig {

    // Tato metoda řekne Springu, jak má vytvořit PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

```

Jakmile toto napíšeš, Spring si při startu aplikace zavolá metodu `passwordEncoder()`, vytvoří instanci `BCryptPasswordEncoder` a uloží si ji.

Když pak budeš potřebovat šifrovat hesla ve své Service vrstvě, jednoduše si tento "Bean" vyžádáš v konstruktoru (tzv. Dependency Injection) a Spring ti ho automaticky dodá:

```java
@Service
public class UzivatelService {

    private final PasswordEncoder passwordEncoder; // Spring dodá náš @Bean

    // Spring vidí konstruktor a řekne si: "Aha, potřebuje PasswordEncoder. Mám ho v registru!"
    public UzivatelService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void zaregistruj(String heslo) {
        String zasifrovane = passwordEncoder.encode(heslo);
        // ... uložení do DB
    }
}

```

Díky `@Bean` tak máš plnou kontrolu nad tím, jak přesně se objekty z cizích knihoven (nebo složitě konfigurované vlastní objekty) v aplikaci vytvářejí.

> ### Jak implementovat Liquibase na správu databáze?

Liquibase funguje jako verzovací systém (Git) pro tvou databázi. Místo abys spoléhala na to, že Hibernate (nebo `schema.sql`) nějak tabulky vytvoří, definuješ strukturu pomocí migračních skriptů. Získáš tak jistotu, že se databáze vždy sestaví ve správném pořadí, a možnost změny vracet zpět (rollback).

Protože jsi upustila od automatického generování tabulek přes Spring Data JPA, je nasazení Liquibase logickým a profesionálním krokem.

### 1. Přidání závislosti

Do souboru `pom.xml` přidej samotnou knihovnu Liquibase:

```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>

```

### 2. Úprava application.properties

Musíš Spring Bootu říct, kde najde hlavní soubor s migracemi, a zároveň **vypnout** případné generování tabulek z JPA (aby se Hibernate a Liquibase nepraly o to, kdo tabulku vytvoří).

```properties
# Vypnutí automatického generování tabulek přes Hibernate
spring.jpa.hibernate.ddl-auto=validate

# Cesta k hlavnímu souboru Liquibase (výchozí lokace)
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml

```

### 3. Vytvoření Master souboru

Liquibase používá tzv. "Master" soubor, který slouží jako rozcestník. Sám o sobě tabulky nevytváří, ale určuje pořadí, v jakém se mají spouštět jednotlivé migrační skripty.

Vytvoř složku `src/main/resources/db/changelog/` a v ní soubor **`db.changelog-master.xml`**:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">

    <!-- Zde budeš postupně přidávat odkazy na své SQL skripty -->
    <include file="db/changelog/changes/001-vytvoreni-tabulky-uzivatele.sql"/>

</databaseChangeLog>

```

### 4. Vytvoření samotné migrace (Formatted SQL)

Nejjednodušší a nejčitelnější způsob, jak v Liquibase psát změny, je formátované SQL.

Vytvoř podsložku `changes` a do ní soubor **`001-vytvoreni-tabulky-uzivatele.sql`**:

```sql
-- liquibase formatted sql

-- changeset nikol:1
CREATE TABLE uzivatele (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    jmeno VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- rollback DROP TABLE uzivatele;

```

**Co znamenají speciální komentáře?**

- `-- liquibase formatted sql`: Říká parseru, že toto není obyčejný SQL soubor, ale Liquibase migrace.
- `-- changeset nikol:1`: Unikátní identifikátor změny (autor:ID). Můžeš použít např. datum `nikol:2026-09-08-1`.
- `-- rollback ...`: Instrukce, co má Liquibase udělat, pokud bys potřebovala tuto migraci v budoucnu vrátit zpět (undo).

### Jak to celé funguje po startu

Když nyní spustíš aplikaci, Spring Boot předá řízení Liquibase. Ten udělá následující:

1. V tvé in-memory H2 databázi vytvoří dvě interní tabulky: `DATABASECHANGELOG` a `DATABASECHANGELOGLOCK`.
2. Projde tvůj Master soubor a najde skript `001...sql`.
3. Podívá se do tabulky `DATABASECHANGELOG`, jestli už byl _changeset_ `nikol:1` spuštěn.
4. Pokud ne, spustí `CREATE TABLE uzivatele` a zapíše si do logu, že tuto migraci úspěšně provedl.

Protože používáš in-memory databázi, která se při každém vypnutí smaže, Liquibase při každém startu proběhne od začátku a čistě ti připraví celou strukturu databáze.

> ### databáze má používat hibernate dialog

Hibernate **Dialect** (dialekt) funguje jako překladatel mezi objektovým světem Javy (EntityManagerem) a specifickou syntaxí konkrétní databáze. I když je SQL standard, každá databáze (H2, PostgreSQL, MySQL) má drobné odchylky – a právě dialekt říká frameworku Hibernate, jak má JPQL dotazy správně zformátovat do nativního SQL.

I když se ho Spring Boot často snaží odhadnout automaticky z připojovací URL, jeho explicitní nastavení předchází nečekaným chybám. Otevři soubor `src/main/resources/application.properties` a doplň tuto konfiguraci:

```properties
# Nastavení správného dialektu pro H2 databázi
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# Volitelně: Formátování SQL v konzoli pro lepší čitelnost
spring.jpa.properties.hibernate.format_sql=true

```

- **`hibernate.dialect`**: Definuje přesnou třídu překladače. Pokud bys v budoucnu vyměnila H2 za produkční databázi, jednoduše tuto hodnotu změníš (např. na `org.hibernate.dialect.PostgreSQLDialect`). Veškerý tvůj kód v DAO vrstvě zůstane naprosto nedotčený.
- **`hibernate.format_sql`**: Protože už máš z předchozích kroků zapnuté logování dotazů (`spring.jpa.show-sql=true`), tento parametr zajistí, že se vygenerované SQL nevypíše do konzole jako jeden dlouhý a nepřehledný text, ale úhledně se odřádkuje.

> ### jak ot ovlivní liquibase? lze se vyhnout sql?

Nastavení Hibernate dialektu Liquibase **nijak neovlivní**. Jsou to dva nezávislé nástroje, které pracují v jiné fázi životního cyklu aplikace:

1. **Liquibase běží jako první** hned po spuštění aplikace. Typ databáze (zda je to H2 nebo PostgreSQL) si zjistí sám z `spring.datasource.url` a podle toho upravuje své vnitřní chování.
2. **Hibernate běží až potom.** Jakmile Liquibase připraví tabulky, nastartuje se Hibernate (EntityManager), přečte si svůj dialekt z `application.properties` a začne do tabulek posílat data.

**Ano, psaní SQL se v Liquibase dá zcela vyhnout.**

Pokud nechceš psát surové SQL příkazy (tzv. Formatted SQL), můžeš strukturu databáze definovat databázově nezávisle pomocí **XML** nebo **YAML**. Funguje to podobně jako ORM – ty napíšeš, jaké sloupce tabulka má, a Liquibase sám na pozadí vygeneruje správné SQL pro databázi, ke které je zrovna připojený. Obrovskou výhodou je, že u XML formátu umí Liquibase u většiny příkazů (jako je `createTable` nebo `addColumn`) sám vygenerovat i `rollback`, takže ho nemusíš psát ručně.

Takhle by vypadalo vytvoření tabulky `uzivatele` pomocí **XML** namísto SQL:

Vytvoř soubor `001-vytvoreni-tabulky-uzivatele.xml` ve složce `changes`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">

    <changeSet id="1" author="nikol">
        <createTable tableName="uzivatele">
            <column name="id" type="BIGINT" autoIncrement="true">
                <constraints primaryKey="true" nullable="false"/>
            </column>

            <column name="jmeno" type="VARCHAR(50)">
                <constraints nullable="false" unique="true"/>
            </column>

            <column name="email" type="VARCHAR(100)">
                <constraints nullable="false"/>
            </column>

            <column name="role" type="VARCHAR(20)">
                <constraints nullable="false"/>
            </column>
        </createTable>
    </changeSet>

</databaseChangeLog>

```

Následně jen v tvém hlavním rozcestníku `db.changelog-master.xml` změníš koncovku odkazovaného souboru:

```xml
<include file="db/changelog/changes/001-vytvoreni-tabulky-uzivatele.xml"/>

```

> ### Potřebuju aby byla možnost aktivovat seeding databáze nějakými iniciálními daty. Jak to bude vypadat přes Liquibase?

Pro volitelné plnění databáze (seeding) nabízí Liquibase vynikající funkci – **Kontexty (Contexts)**. Změnám v Liquibase můžeš přiřadit štítek (např. `dev`, `test`, `seeding`), a Spring Bootu pak v konfiguraci řekneš, které štítky má při startu aktivovat.

Pokud aplikaci nasadíš do produkce bez tohoto štítku, testovací data se nikdy nevloží.

**1. Vytvoření skriptu pro vložení dat (XML)**

Vytvoř nový soubor, například `002-vlozeni-testovacich-dat.xml` ve složce `changes`. Zásadní je zde atribut `context="seeding"` uvnitř tagu `changeSet`. Opět se vyhneme SQL a použijeme čisté XML tagy `<insert>`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">

    <!-- Tento changeset se spustí POUZE, pokud je aktivní kontext 'seeding' -->
    <changeSet id="2" author="nikol" context="seeding">
        
        <insert tableName="uzivatele">
            <!-- ID vynecháváme, databáze ho vygeneruje sama přes auto-increment -->
            <column name="jmeno" value="admin"/>
            <column name="email" value="admin@mojeapp.cz"/>
            <column name="role" value="ADMIN"/>
        </insert>

        <insert tableName="uzivatele">
            <column name="jmeno" value="test_user"/>
            <column name="email" value="user@mojeapp.cz"/>
            <column name="role" value="USER"/>
        </insert>

    </changeSet>

</databaseChangeLog>

```

**2. Zařazení do Master souboru**

Tento nový soubor musíš zaregistrovat ve svém hlavním `db.changelog-master.xml` hned pod vytvoření tabulky:

```xml
    <include file="db/changelog/changes/001-vytvoreni-tabulky-uzivatele.xml"/>
    <include file="db/changelog/changes/002-vlozeni-testovacich-dat.xml"/>

```

**3. Aktivace seedingu ve Spring Bootu**

Nyní už jen stačí Spring Bootu říct, aby tento kontext při startu zapnul. Do `application.properties` přidej tento řádek:

```properties
# Aktivuje vybrané kontexty. Lze zadat i více hodnot oddělených čárkou (např. dev,seeding)
spring.liquibase.contexts=seeding

```

**Jak s tím pracovat v praxi:**

* Při **lokálním vývoji** si necháš `spring.liquibase.contexts=seeding` zapnutý, takže po startu v H2 databázi ihned najdeš hotového admina.
* Pokud chceš aplikaci spustit a **otestovat čistou databázi**, jednoduše tento řádek z `application.properties` smažeš (nebo ho zakomentuješ). Liquibase tabulku vytvoří, ale druhý skript přeskočí.
* Při **nasazení do produkce** (přes proměnné prostředí) nastavíš `SPRING_LIQUIBASE_CONTEXTS=prod`. Skript se seedingem se bude ignorovat a produkční databáze zůstane čistá.


> ### lze tuto knfiguraci nějak vložit do run commandu v maven?

Ano, je to naprosto běžná praxe. Zabráníš tím tomu, abys omylem commitnula zapnutý seeding do Gitu, a umožníš dynamické spouštění podle potřeby.

Hodnoty z `application.properties` můžeš přepsat přímo z příkazové řádky. Pokud spouštíš aplikaci přes Maven plugin, použiješ parametr `-Dspring-boot.run.arguments`:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.liquibase.contexts=seeding"

```

**Jak to funguje:**

* Vše, co začíná dvěma pomlčkami (`--`), Spring Boot automaticky zachytí.
* Najde odpovídající klíč (`spring.liquibase.contexts`) a jeho hodnotu nadřadí nad to, co máš napsané v `application.properties`.
* Pokud bys potřebovala předat více parametrů, oddělíš je mezerou: `...arguments="--prop1=a --prop2=b"`.

---

**Spouštění zkompilované aplikace (Produkce)**

Když aplikaci zkompiluješ (např. přes `mvn clean package`) a získáš výsledný `.jar` soubor, Maven už k běhu nepotřebuješ. Parametry pak předáváš úplně stejně, přímo programu Java:

```bash
java -jar target/tvoje-aplikace.jar --spring.liquibase.contexts=seeding

```

Tímto způsobem se nejčastěji řeší konfigurace na produkčních serverech nebo v Docker kontejnerech – hesla k databázi nebo JWT klíče nejsou v kódu, ale předají se aplikaci bezpečně až při jejím spuštění.



> ### Jak napsat dokuemntaci Springboot REST projektu?

Dokumentace REST API ve Spring Bootu se standardně dělí na interaktivní dokumentaci koncových bodů (vygenerovanou automaticky) a textovou dokumentaci projektu pro vývojáře. Tady je ověřený postup, jak pokrýt obojí.

### 1. Interaktivní API dokumentace (OpenAPI / Swagger)

Dnes se už API dokumentace nepíše ručně do Wordu. Nejlepším standardem je knihovna **Springdoc OpenAPI**, která si sama přečte tvé Controllery a vygeneruje interaktivní webovou stránku (Swagger UI), kde si lze API rovnou naklikat a vyzkoušet.

**Přidání závislosti (pom.xml):**

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>

```

**Anotace v Controlleru:**
Knihovna funguje i bez anotací (sama zjistí, jaké máš endpointy a jaká DTO vracíš), ale pomocí anotací přidáš lidské popisy:

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/uzivatele")
@Tag(name = "Uživatelé", description = "Endpointy pro správu uživatelů")
public class UzivatelController {

    @GetMapping("/{id}")
    @Operation(summary = "Získání uživatele", description = "Vrátí uživatele podle jeho databázového ID.")
    public ResponseEntity<UzivatelResponseDto> getById(@PathVariable Long id) {
        // ...
    }
}

```

Po spuštění aplikace pak stačí v prohlížeči otevřít `http://localhost:8080/swagger-ui.html` a uvidíš grafické rozhraní.

### 2. Soubor README.md (Srdce repozitáře)

Každý projekt by měl mít v kořenovém adresáři soubor `README.md`. Slouží jako vstupní brána pro každého, kdo si tvůj kód stáhne. Měl by obsahovat:

* **Představení:** Co aplikace dělá (např. "REST API pro správu uživatelů s JWT autorizací").
* **Prerekvizity:** Co musí mít člověk nainstalováno (např. Java 21, Maven).
* **Konfigurace (Environment):** Seznam proměnných, které je třeba nastavit v `application.properties` (např. tvůj `security.jwt.secret-key`).
* **Jak aplikaci spustit:** Přesné příkazy (např. ten tvůj `mvn spring-boot:run -Dspring-boot.run.arguments="..."`).
* **Architektura:** Stručný výčet použitých technologií (Spring Boot 3, H2, Liquibase, JWT).

### 3. Kódová dokumentace (JavaDoc)

V moderním Spring Bootu se nekomentují gettery, settery ani jasné metody (`ziskejVsechny`). Kód by měl vysvětlovat sám sebe. Klasický JavaDoc (komentáře začínající `/**`) se píše převážně do **Service vrstvy** tam, kde je složitá byznys logika, kterou nelze snadno pochopit jen z názvu metody (např. proč se určitá data filtrují specifickým způsobem).

### 4. Exportovaná kolekce (Postman / Bruno)

Pro usnadnění života sobě nebo frontend vývojářům je skvělou praxí naklikat si všechny své endpointy v nástroji jako Postman (nebo v open-source variantách jako Bruno či Insomnia), uložit je do kolekce a tento `.json` soubor vložit přímo do repozitáře (např. do složky `docs/`).



> ### Jak exportovat a  přiložit kolekci z Postmana?

Export postmaní kolekce a její vložení do projektu je otázka několika kliknutí. Tímto způsobem komukoliv dalšímu (nebo i sobě za půl roku) ušetříš hodiny času s ručním vypisováním URL adres a JSON těl požadavků.

### 1. Export kolekce z aplikace Postman

1. Otevři Postman a v levém panelu najdi svou vytvořenou kolekci s uloženými endpointy.
2. Klikni na **ikonu tří teček (...)** vedle názvu kolekce.
3. V nabídce vyber **Export**.
4. Zobrazí se dialogové okno s výběrem formátu. Nech zvolenou doporučenou možnost **Collection v2.1 (recommended)** a klikni na *Export*.
5. Ulož soubor (např. `moje-api-kolekce.json`) někam na disk.

### 2. Umístění souboru do Spring Boot projektu

Nejlepší praxí je nevkládat tyto soubory přímo do složky se zdrojovým kódem Javy (`src/main/java`), protože s kompilací aplikace nijak nesouvisí. Místo toho se vytváří samostatná složka v kořenovém adresáři projektu (tam, kde leží `pom.xml`).

Vytvoř strukturu například takto:

```text
muj-spring-projekt/
├── src/
├── pom.xml
├── README.md
└── docs/
    └── postman/
        └── moje-api-kolekce.json

```

### 3. Zmínka v README.md

Aby ostatní vývojáři věděli, že tam soubor je a co s ním mají dělat, přidej do svého `README.md` krátkou sekci:

```markdown
## 🛠 Testování API (Postman)
V repozitáři je přiložena hotová kolekce pro Postman, která obsahuje všechny předpřipravené endpointy včetně ukázkových dat pro vytvoření uživatele a přihlášení.

**Jak ji použít:**
1. Otevři Postman a vlevo nahoře klikni na **Import**.
2. Vyber soubor `docs/postman/moje-api-kolekce.json`.
3. Kolekce se objeví v levém panelu a můžeš rovnou testovat.

```

---

> **Důležitý tip pro JWT a Proměnné (Environments):**
> Pokud v Postmanu používáš takzvaná *Environments* (proměnné, například `{{base_url}}` nebo `{{jwt_token}}`), musíš vyexportovat i ty. Dělá se to v záložce *Environments* kliknutím na tři tečky a *Export*. Získáš tak druhý JSON soubor.
> **Pozor:** Před exportem se ujisti, že v prostředí nemáš uložená žádná produkční hesla nebo skutečné tajné klíče. Do Gitu patří jen "dummy" testovací data!



> ### jak otestovat Service Vrstvu s Mockito? Jka otestovat Controllers?

Testování ve Spring Bootu se standardně dělí do dvou úrovní. Service vrstvu testuješ pomocí čistých Unit testů (s využitím Mockito) bez startování samotného Springu. Controllery testuješ pomocí speciální zkrácené verze Spring kontextu (`@WebMvcTest`), která nastartuje pouze webovou vrstvu a namockuje vše pod ní.

Takhle to postavíš v praxi.

### 1. Testování Service vrstvy (Mockito)

Cílem je ověřit pouze tvou byznys logiku (např. tvou metodu `delete`). Protože nechceš připojovat skutečnou databázi, pomocí anotace `@Mock` "předstíráš" (namockuješ) chování `CourtDao`. Anotace `@InjectMocks` se pak postará o to, že tyto falešné závislosti vloží do tvé testované Service vrstvy.

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Toto řekne JUnitu, aby povolil Mockito anotace
@ExtendWith(MockitoExtension.class)
class CourtServiceTest {

    @Mock
    private CourtDao courtDao; // Falešná databáze

    @InjectMocks
    private CourtService courtService; // Skutečná testovaná služba

    @Test
    void delete_VratiChybu_KdyzMaKurtBudouciRezervace() {
        // 1. GIVEN: Příprava dat a chování mocků
        Court court = new Court();
        court.setId(1L);
        
        // Když se Service zeptá na kurt s ID 1, vrať tento objekt
        when(courtDao.findById(1L)).thenReturn(Optional.of(court));
        // Když se Service zeptá na počet rezervací, vrať číslo 3 (simulujeme problém)
        when(courtDao.courtUsedInFuture(1L)).thenReturn(3);
        when(courtDao.courtUsedInFutureCost(1L)).thenReturn(new BigDecimal("1500"));

        // 2. WHEN & THEN: Zavoláme metodu a očekáváme výjimku
        CourtException exception = assertThrows(CourtException.class, () -> {
            courtService.delete(1L);
        });

        // 3. Ověření chybové hlášky
        assertTrue(exception.getMessage().contains("Solve the: 3 reservation(s) first"));
        
        // Ověříme, že metoda delete nad DAO se NIKDY nezavolala
        verify(courtDao, never()).delete(any());
    }

    @Test
    void delete_SmazeKurt_KdyzNejsouZadneRezervace() {
        Court court = new Court();
        court.setId(1L);
        
        when(courtDao.findById(1L)).thenReturn(Optional.of(court));
        when(courtDao.courtUsedInFuture(1L)).thenReturn(0); // Žádné rezervace

        // Act
        courtService.delete(1L);

        // Assert: Ověříme, že se opravdu zavolalo fyzické smazání nad databází
        verify(courtDao, times(1)).delete(court);
    }
}

```

### 2. Testování Controllerů (MockMvc)

Zde chceš ověřit, že Controller správně reaguje na HTTP požadavky, vrací správné HTTP status kódy (např. 200 OK nebo 400 Bad Request) a správně mapuje Java objekty do JSON formátu.

Používá se anotace `@WebMvcTest`. Ta spustí jen webovou část aplikace. Pomocí `@MockBean` nahradíš skutečnou Service vrstvu falešnou, abys opět nemusela řešit databázi. Nástroj `MockMvc` pak slouží k "odpalování" virtuálních HTTP požadavků.

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourtController.class) // Nastartuje pouze tento Controller
class CourtControllerTest {

    @Autowired
    private MockMvc mockMvc; // Zajišťuje virtuální HTTP volání

    @MockBean
    private CourtService courtService; // Nahradí skutečnou Service vrstvu

    @Test
    void get_VratiKurt_KdyzExistuje() throws Exception {
        // GIVEN: Připravíme si DTO, které nám "jakoby" vrátí Service vrstva
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(5);
        // (předpokládáme, že ID nastavuje jiný setter nebo je v konstruktoru)

        when(courtService.get(1L)).thenReturn(dto);

        // WHEN & THEN: Zavoláme GET /api/courts/1 a kontrolujeme JSON
        mockMvc.perform(get("/api/courts/1")
                .contentType(MediaType.APPLICATION_JSON))
                
                // Očekáváme HTTP 200 OK
                .andExpect(status().isOk())
                
                // Očekáváme, že v JSONu bude atribut "courtNumber" s hodnotou 5
                // Výraz $ představuje kořen JSON dokumentu
                .andExpect(jsonPath("$.courtNumber").value(5));
    }

    @Test
    void get_Vrati400_KdyzServiceVyhodiVyjimku() throws Exception {
        // GIVEN: Service vrstva vyhodí tvou CourtException
        when(courtService.get(99L)).thenThrow(new CourtException("Court not found"));

        // WHEN & THEN: Controller (přes GlobalExceptionHandler) by to měl přeložit na 400 Bad Request
        mockMvc.perform(get("/api/courts/99"))
                .andExpect(status().isBadRequest());
    }
}

```

> ### Lze otestovat Controllers i běžným Mockito?

Ano, naprosto bez problémů. Controller je ve své podstatě úplně obyčejná Java třída. Můžeš k němu přistupovat úplně stejně jako k Service vrstvě – vytvořit instanci, podstrčit namockovanou Service a zavolat jeho metodu napřímo.

Kód takového testu vypadá téměř totožně jako test Service vrstvy:

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourtControllerPlainTest {

    @Mock
    private CourtService courtService;

    @InjectMocks
    private CourtController courtController;

    @Test
    void get_VratiOkResponseEntity_KdyzJeVseVPoradku() {
        // GIVEN
        CourtDto dto = new CourtDto();
        dto.setCourtNumber(5);
        when(courtService.get(1L)).thenReturn(dto);

        // WHEN: Voláme metodu přímo jako v čisté Javě, žádné HTTP
        ResponseEntity<CourtDto> response = courtController.get(1L);

        // THEN: Kontrolujeme samotný objekt ResponseEntity
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().getCourtNumber());
    }
}

```

### Proč se ale v praxi většinou používá MockMvc?

Ačkoliv je tento čistý Mockito test neuvěřitelně rychlý (nestartuje se žádný Spring kontext), **netestuje samotnou webovou vrstvu aplikace**. Skrývá totiž tři velká rizika, která uvidíš až při reálném spuštění API:

* **Netestuje JSON:** Čisté Mockito ověří, že Controller vrací Java objekt `CourtDto`. Neověří ale, jestli ho umí knihovna Jackson správně přeložit do JSONu (zda ti nechybí gettery, výchozí konstruktor, nebo se nezacyklila vazba).
* **Netestuje anotace:** Pokud zapomeneš nad parametr metody v Controlleru napsat `@PathVariable Long id` nebo `@RequestBody`, čistý Mockito test projde na 100 %, protože metodu voláš s předaným parametrem v Javě. Reálné HTTP volání ale selže.
* **Netestuje chybové stavy (GlobalExceptionHandler):** Pokud Service vyhodí tvou `CourtException`, čistý Mockito test zkrátka vyhodí výjimku a "spadne". Neověří, že tvůj `@RestControllerAdvice` tuto výjimku zachytí a správně ji přetransformuje do JSONu s kódem `400 Bad Request`.

**Závěr:** Čisté Mockito je ideální na Service vrstvu (byznys logiku). Na Controllery použij `MockMvc` s anotací `@WebMvcTest`. Testuješ s ním skutečné HTTP rozhraní, překlad do JSONu a zpracování chyb, aniž bys musela zvedat celou databázi.


> ### Jsou potřeba testy s MockMvc, když jsme si funkcionalitu ověřila přes Postmana a opravila detaily?

Ruční proklikání API v Postmanu je skvělé pro okamžité ověření během vývoje, ale automatizované testy jako MockMvc to z dlouhodobého hlediska nenahradí. Slouží totiž úplně jinému účelu: **ochraně kódu do budoucna**.

Zde jsou hlavní důvody, proč se bez nich spolehlivý backend neobejde:

* **Prevence regrese:** Za měsíc přidáš novou funkci, upravíš databázový model nebo zaktualizuješ Spring Boot na novější verzi. Pokud nemáš testy, musela bys v Postmanu znovu ručně proklikat všechny endpointy, abys měla jistotu, že úprava nerozbila něco jiného. MockMvc testy tento problém odhalí za zlomek sekundy.
* **Automatizace (CI/CD):** V praxi se kód testuje a nasazuje přes automatizované linky (např. GitHub Actions nebo GitLab CI). Tyto systémy před každým schválením kódu spustí všechny tvé testy. Pokud nějaký MockMvc test selže, systém zablokuje nasazení chybné verze.
* **Refaktorování bez strachu:** Když máš napsané testy, můžeš s klidem přepsat vnitřní logiku, zrychlit SQL dotazy nebo rozdělit velkou Service vrstvu na dvě menší. Pokud testy po tvém zásahu stále svítí zeleně, víš s jistotou, že API se navenek chová stále stejně.
* **Otestování všech okrajových případů:** V Postmanu vývojář typicky otestuje hlavní úspěšný scénář a možná jednu chybu. V MockMvc si velmi snadno napíšeš rychlé testy pro chybějící autorizační tokeny, špatně formátované JSONy nebo specifické výjimky (např. mazání kurtu s budoucími rezervacemi), a tyto scénáře se budou ověřovat při každém spuštění už navždy.

Pokud v Postmanu nepíšeš přímo automatizované testovací skripty (které se pak pouští hromadně přes příkazovou řádku nástrojem Newman), slouží tvá Postman kolekce primárně jako dokumentace a nástroj pro manuální exploraci. MockMvc je tvůj skutečný hlídač kvality a stability.




> ### Jak vypadaly testy pro auth controller?

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String authHeader) {
        if (!authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String base64Credentials = authHeader.substring(6).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            String[] values = credentials.split(":", 2); // pole [username, password]

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(values[0], values[1]));

            UserDetails user = userDetailsService.loadUserByUsername(values[0]);
            String token = jwtService.generateToken(user);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
```

Pokud chceš čistý unit test **pouze s Mockito** (bez Spring kontextu, bez `MockMvc` a bez startování jakýchkoliv frameworků), controller otestuješ jako běžnou Java třídu a metodu `login(...)` zavoláš přímo.


```java
package skylarmaeve.tennisreservationsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import skylarmaeve.tennisreservationsystem.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthController authController;

    private String createBasicAuthHeader(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void login_ValidCredentials_ShouldReturnOkWithBearerToken() {
        String username = "testuser";
        String password = "password123";
        String dummyJwt = "mocked.jwt.token";

        UserDetails userDetails = new User(username, password, Collections.emptyList());

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(dummyJwt);

        String header = createBasicAuthHeader(username, password);
        ResponseEntity<Void> response = authController.login(header);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getHeaders().get(HttpHeaders.AUTHORIZATION));
        assertEquals("Bearer " + dummyJwt, response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    void login_MissingOrInvalidAuthHeaderFormat_ShouldReturnUnauthorized() {
        // Hlavička nezačíná na "Basic " (pokryje první if)
        ResponseEntity<Void> response = authController.login("Bearer some-token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verifyNoInteractions(authenticationManager, userDetailsService, jwtService);
    }

    @Test
    void login_InvalidCredentials_ShouldReturnUnauthorized() {
        String username = "testuser";
        String password = "wrongpassword";

        // Vyhození výjimky při autentizaci (spadne do bloku catch)
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        String header = createBasicAuthHeader(username, password);
        ResponseEntity<Void> response = authController.login(header);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_MalformedBase64_ShouldTriggerCatchAndReturnUnauthorized() {
        // Neplatný Base64 string shodí dekodér a otestuje větev catch
        ResponseEntity<Void> response = authController.login("Basic @@@not-base-64@@@");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verifyNoInteractions(authenticationManager, userDetailsService, jwtService);
    }
}

```

Tento přístup nepotřebuje žádné Spring Boot testovací závislosti, neřeší konfiguraci `SecurityFilterChain` ani porty a IDE ti díky němu naměří plné řádkové pokrytí metody `login`.


> ### can it instead use actual JWT service?

Ano, můžeš místo `@Mock` pro `JwtService` použít buď reálnou instanci vytvořenou ručně přes `new JwtService(...)`, nebo ji obalit pomocí `@Spy`.

Pokud je tvá `JwtService` čistá Java třída (nebo má konstruktor, do kterého jí předáš potřebné parametry, jako je např. secret key a expirace), stačí ji jednoduše nainstanciovat přímo.

Zde je upravený test:

```java
package skylarmaeve.tennisreservationsystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import skylarmaeve.tennisreservationsystem.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    // Skutečná služba místo mocku
    private JwtService jwtService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        // Inicializuj reálnou instanci JwtService.
        // Pokud tvůj konstruktor vyžaduje parametry (např. secret key), předej je zde:
        // jwtService = new JwtService("tvujVelmiDlouhySecretKeyProPodpisMinimalne256BituDlouhy");
        jwtService = new JwtService();

        // Ruční složení controlleru s reálnou JwtService a zbylými mocky
        authController = new AuthController(authenticationManager, jwtService, userDetailsService);
    }

    private String createBasicAuthHeader(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void login_ValidCredentials_ShouldReturnRealJwtToken() {
        String username = "testuser";
        String password = "password123";

        UserDetails userDetails = new User(username, password, Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        String header = createBasicAuthHeader(username, password);
        ResponseEntity<Void> response = authController.login(header);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String authHeader = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        assertNotNull(authHeader);
        assertTrue(authHeader.startsWith("Bearer "));

        // Získání reálného tokenu (za slovem "Bearer ")
        String token = authHeader.substring(7);

        // JWT se skládá ze tří částí oddělených tečkou: header.payload.signature
        assertEquals(3, token.split("\\.").length);

        // Můžeš navíc použít samotnou metodu z JwtService k ověření, že token je validní pro daného usera:
        // assertEquals(username, jwtService.extractUsername(token));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(username);
    }

    @Test
    void login_MissingOrInvalidAuthHeaderFormat_ShouldReturnUnauthorized() {
        ResponseEntity<Void> response = authController.login("Bearer some-token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verifyNoInteractions(authenticationManager, userDetailsService);
    }

    @Test
    void login_InvalidCredentials_ShouldReturnUnauthorized() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        String header = createBasicAuthHeader("testuser", "wrongpassword");
        ResponseEntity<Void> response = authController.login(header);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void login_MalformedBase64_ShouldTriggerCatchAndReturnUnauthorized() {
        ResponseEntity<Void> response = authController.login("Basic @@@not-base-64@@@");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verifyNoInteractions(authenticationManager, userDetailsService);
    }
}

```

### Co se tím mění:

1. **Code Coverage:** Tento test ti kromě řádků v `AuthController` zároveň započítá a pokryje i řádky uvnitř `JwtService.generateToken(...)`, protože se reálně vykonají.
2. **Klíče a konfigurace:** Pokud tvá `JwtService` bere secret key ze souboru `application.properties` přes `@Value("${jwt.secret}")`, při volání bezparametrického `new JwtService()` může být hodnota `null`. V takovém případě jí secret nastav buď v konstruktoru, nebo pomocí setteru.



> ### Potřebuju nastavit v JWT Service secretKey a jwtExpiration, v kodu to tahá data z application.properties.
```java
@Service

public class JwtService {



@Value("${jwt.secret}")

private String secretKey;



@Value("${jwt.access-token-expiration}")

private long jwtExpiration;
```


Vzhledem k tomu, že tvá `JwtService` čte `secretKey` a `jwtExpiration` přes privátní pole s anotací `@Value`, při volání čistého `new JwtService()` v unit testu by obě pole zůstala nevyplněná (`null` a `0`), což by způsobilo pád na `NullPointerException`.

Pro unit test máš dvě elegantní řešení:

---

### Řešení 1: Nastavení polí pomocí `ReflectionTestUtils` (bez úpravy kódu servisy)

Spring poskytuje testovací utilitu `ReflectionTestUtils.setField(...)`, která umí bezpečně nastavit hodnoty privátních polí v instanci:

```java
package skylarmaeve.tennisreservationsystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import skylarmaeve.tennisreservationsystem.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    private JwtService jwtService;
    private AuthController authController;

    // HMAC-SHA256 vyžaduje klíč o délce minimálně 256 bitů (32 znaků)
    private static final String TEST_SECRET = "superSecretKeyThatIsAtLeast32BytesLongForHmacSha256!";
    private static final long TEST_EXPIRATION = 3600000; // 1 hodina v ms

    @BeforeEach
    void setUp() {
        // 1. Vytvoříme reálnou instanci JwtService
        jwtService = new JwtService();

        // 2. Nastavíme privátní pole, která jinak plní Spring přes @Value
        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", TEST_EXPIRATION);

        // 3. Poskládáme controller
        authController = new AuthController(authenticationManager, jwtService, userDetailsService);
    }

    private String createBasicAuthHeader(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void login_ValidCredentials_ShouldReturnRealJwtToken() {
        String username = "testuser";
        String password = "password123";

        // Pozor: V JwtService máš volání userDetails.getAuthorities().iterator().next(),
        // proto role nesmí být prázdná, jinak hodí NoSuchElementException!
        UserDetails userDetails = new User(
                username, 
                password, 
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        ResponseEntity<Void> response = authController.login(createBasicAuthHeader(username, password));

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String authHeader = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        assertNotNull(authHeader);
        assertTrue(authHeader.startsWith("Bearer "));

        String token = authHeader.substring(7);

        // Ověříme, že reálná JwtService umí token zpětně přečíst a ověřit
        assertEquals(username, jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(username);
    }
}

```

---


*Důležitá poznámka k tvé implementaci:* V metodě `generateToken` máš řádek:

```java
.claim("role", userDetails.getAuthorities().iterator().next().getAuthority())

```

V testech vždy vytvářej `UserDetails` s alespoň jednou autoritou (např. `List.of(new SimpleGrantedAuthority("ROLE_USER"))`). Kdybys předala prázdný seznam `Collections.emptyList()`, `iterator().next()` by spadl na chybě `NoSuchElementException`.



