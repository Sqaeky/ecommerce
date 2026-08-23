# Software architecture (Kotlin + Spring)

## Modular monolith

## Modules

- Orders (Objednávky a jejich průběh)
- Users (autentizace, role, zákazníci)
- catalog (produkty, kategorie, varianty)
- Inventory (skladové zásoby a rezervace)
- Payments (platby, refundace)
- Cart  (košík)
- Notifications (emaily a další notifikace)

### (Možná do budoucna)

- Pricing (ceny, slevy, kupóny, daně?)
- Shipping (doprava, doručovací metody, tracking)

## Praktické pravidlo

- Každý modul by měl mít vlastní strukturu například:

```text
orders/
    api/
    application/
    domain/
    infrastructure/
```

- domain nezávisí na jiných modulech
- modul nesmí číst tabulky jiného modulu přímo
- komunikace mezi moduly probíhá přes veřejné služby, rozhraní nebo události
- entity z jednoho modulu se neposílají přímo do jiného modulu
- mezi moduly se používají jednoduchá DTO nebo ID hodnoty

## Implementace

### Fáze 1

- users
- catalog
- inventory
- cart

### Fáze 2

- orders
- checkout orchestrace
- rezervace skladu
- snapshot položek a cen

### Fáze 3

- Payments (mock)
- webhooky
- změny stavů objednávek
- uvolnění rezervace při neúspěšné platbě

### Fáze 4

- Notifications
- email po vytvoření objednávky nebo úspěšné platbě

### Fáze 5

- rozšíření o jiné moduly

## Users

- registrace, přihlášení
- hesla, role, oprávnění
- zákaznický profil
- fakturační a doručovací adresy
- případně anonymní návštěvník

## Catalog

- produkty
- kategorie
- varianty, například velikost a barva
- popis, obrázky, a základní metadata
- publikování a deaktivace produktů
- případně základní cenu

## Inventory

- skladové množství
- rezervace položek
- uvolnění rezervace
- odečtení zboží po úspěšné objednávce
- doplnění zásob
- ochranu před prodejem nedostupného zboží

### Pravidla

- rezervace vzniká až po checkoutu
- košík by neměl trvale rezervovat sklad
- rezervace má expiraci (např. 15 minut)

## Cart

- vytvoření košíku
- přidání a odebrání položky
- změnu množství
- načtení obsahu košíku
- kontrolu, zda jsou položky stále aktivní
- propojení s uživatelem nebo anonymní session

### Pozor při checkoutu!

- kontrola jestli zboží stále existuje
- jestli je dostupné množství
- jaká je aktuální cena
- zda lze objednávku vytvořit

## Orders

- vytvoření objednávky
- položky objednávky
- snapshot názvu, varianty a ceny
- stav objednávky
- historie změn
- zrušení objednávky
- zobrazení objednávek zákazníkovi a administrátorovi

### ENUM stavů

```text
CREATED
PENDING_PAYMENT
PAID
PROCESSING
SHIPPED
COMPLETED
CANCELLED
ERROR
```

Objednávka musí uchovat cenu a název v okamžiku nákupu. Nesmí se později přepočítavat podle aktuálního katalogu!

## Payments

- vytvoření platební relace
- potvrzení platby
- webhooky
- refundace
- stav platby
- idempotenci webhooků (ať už to je cokoli)
- propojení platby s objednávkou

Po úspěchu publikuje Kafka/RabbitMQ zprávu 

## Notifications

- potvrzení objednávky
- potvrzení platby
- oznámení o odeslání
- reset hesla
- admin notifikace
- opakované pokusy při selhání emailu