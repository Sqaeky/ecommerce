# Doporučené pořadí modulů pro e-shop (Modular Monolith)

Toto je doporučené pořadí implementace modulů při stavbě e-commerce backendu.

## 1. User (začni tady)

**Proč první?**
- Téměř všechno ostatní na něm závisí (objednávky, košík, adresy, oprávnění).
- Potřebuješ autentizaci a autorizaci co nejdřív.
- Je to relativně uzavřený modul.

**Co implementovat:**
- Registrace / přihlášení (JWT)
- Základní profil uživatele
- Adresy (shipping + billing)
- Role (USER, ADMIN)

---

## 2. Catalog

**Proč druhý?**
- Je to srdce e-shopu.
- Je téměř nezávislý na ostatních modulech.
- Můžeš si hned vyzkoušet CRUD + vyhledávání + stránkování.

**Co implementovat:**
- Kategorie (stromová struktura)
- Produkty
- Obrázky produktů
- Sklad (stock + reserved quantity)

---

## 3. Cart

**Proč třetí?**
- Závisí na Catalogu (produkty) a částečně na Userovi.
- Je to logický most mezi prohlížením produktů a objednávkou.

**Co implementovat:**
- Košík (pro přihlášeného i anonymního uživatele)
- Přidání / odebrání / změna množství
- Rezervace skladu (volitelné, ale hezké)

---

## 4. Order

**Proč čtvrtý?**
- Závisí na Cart + User + Catalog.
- Tady se teprve začíná dít opravdová business logika.

**Co implementovat:**
- Vytvoření objednávky z košíku
- Order items (snapshot ceny a názvu)
- Stavy objednávky + historie stavů
- Číslo objednávky

---

## 5. Payment

**Proč poslední?**
- Závisí na Order.
- Je to nejpokročilejší část (webhooks, idempotence, retry…).

**Co implementovat:**
- Vytvoření platby
- Payment attempts
- Integrace s platební bránou (alespoň mock / Stripe test mode)
- Ošetření úspěšné/neúspěšné platby

---

## Shrnutí doporučeného pořadí

```text
1. User
2. Catalog
3. Cart
4. Order
5. Payment
```

---

## Tip pro portfolio

Až budeš mít hotový **User + Catalog**, už můžeš ukázat funkční část aplikace (produkty + přihlášení).  
To působí dobře i když zbytek ještě není hotový.
