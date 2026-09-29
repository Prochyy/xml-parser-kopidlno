# XML Parser - Úloha Trixi

Tato Spring Boot aplikace stahuje zazipovaný XML soubor s adresními daty z veřejného URL, dekomprimuje jej v operační paměti a parsuje vybraná data (entity `Obec` a `CastObce`). Následně tyto záznamy bezpečně ukládá do relační databáze PostgreSQL běžící v Docker kontejneru.

## Použité technologie
* **Java 21**
* **Spring Boot 3.x** (Spring Data JPA, Hibernate)
* **PostgreSQL** (přes Docker)
* **Java DOM Parser** 
* **Maven Wrapper**

## Architektura aplikace

1. **Streamování dat:** 
   Aplikace stahuje soubor pomocí `ZipInputStream`. Data se rozbalují rovnou do paměti a předávají se rovnou parseru. Tím se šetří diskové I/O operace a aplikace nezanechává v systému žádné dočasné soubory.
2. **Parsování XML pomocí knihovny DOM:** 
   Samotné parsování využívá knihovnu DOM pro jednoduché získání elementů XML a jejich následné uložení do databáze.
3. **Automatická Správa DDL:** 
   Díky Spring Data JPA a vlastnosti `ddl-auto=update` aplikace sama zakládá a spravuje schéma v PostgreSQL.
4. **Bezpečnost (.env proměnné):** 
   Databázové heslo se předává bezpečně přes proměnné prostředí ze souboru `.env` (DB_HESLO). Maven Wrapper má k souboru přístup z launch.json (Pracoval jsem ve VS Code, lze zaměnit za přidání proměnné do Run Configuration v IntelliJ IDEA).

---

## Návod ke spuštění (macOS / Linux)

### 1. Příprava prostředí a konfigurace (.env)
Před prvním spuštěním je nutné vytvořit konfigurační soubor s heslem. V kořenovém adresáři projektu (vedle `pom.xml`) vytvořte soubor s názvem `.env` a vložte do něj následující řádek s vaším heslem:
```env
DB_PASSWORD=[vase_heslo]
```

### 2. Spuštění databáze (Docker)
Ujistěte se, že vám běží Docker. V kořenu projektu spusťte Docker Compose, který nastartuje PostgreSQL na pozadí a automaticky si načte heslo z `.env` souboru:
```bash
docker compose up -d
```

### 3. Spuštění samotné aplikace
Aplikace obsahuje Maven Wrapper, takže není nutné mít Maven nainstalovaný. Aby aplikace viděla heslo z `.env` souboru, je v launch.json reference:

```json
"envFile": "${workspaceFolder}/.env"
```
*(Po startu aplikace automaticky naváže spojení s databází, vytvoří tabulky, stáhne ZIP z URL, naparsuje XML a uloží všechny entity.).*

### 4. Ověření dat v databázi
Pro kontrolu uložení dat lze použít libovolného db klienta (v mém případě DBeaver) a připojit se na `localhost:5433`. DB=ulohaTrixi_db, User=ulohaTrixi_user

### 5. Zastavení a vyčištění
Pro zastavení databáze a kompletní smazání databázového volume spusťte:
```bash
docker compose down -v
```