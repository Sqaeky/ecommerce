# Pomůcka ke struktuře modulu

Každý modul může mít tuto strukturu:

```text
orders/
├── api/
├── application/
├── domain/
└── infrastructure/
```

## `domain`

Jádro business logiky. Obsahuje pravidla, která nemají záviset na Springu ani databázi.

- entity a agregáty
- value objects
- doménové služby
- business pravidla
- doménové události
- repository rozhraní

```kotlin
class Order(
	val id: OrderId,
	private var status: OrderStatus
) {
	fun markAsPaid() {
		check(status == OrderStatus.PENDING_PAYMENT)
		status = OrderStatus.PAID
	}
}
```

## `application`

Obsahuje jednotlivé use-cases aplikace:

- vytvoření objednávky
- přidání produktu do košíku
- zaplacení objednávky
- zrušení objednávky

Application vrstva řídí postup:

1. načte potřebná data
2. zavolá doménovou logiku
3. komunikuje s ostatními moduly
4. uloží výsledek
5. případně publikuje událost

```kotlin
@Service
class CreateOrderHandler(
	private val orderRepository: OrderRepository,
	private val inventory: InventoryPort
) {
	fun handle(command: CreateOrderCommand): OrderId {
		inventory.reserve(command.items)

		val order = Order.create(command.items)
		orderRepository.save(order)

		return order.id
	}
}
```

## `api`

Veřejné rozhraní modulu. Určuje, jak s modulem komunikují ostatní části aplikace.

Může obsahovat:

- REST controllery
- request a response DTO
- veřejná service rozhraní
- příkazy a dotazy
- události určené pro jiné moduly

```kotlin
interface OrdersApi {
	fun findOrder(orderId: OrderId): OrderSummary
}
```

Ostatní moduly by měly používat `OrdersApi`, nikoliv přímo `OrderEntity` nebo repository z modulu `Orders`.

## `infrastructure`

Technická implementace a napojení na okolní svět.

Obsahuje například:

- JPA entity
- Spring Data repository
- databázové mapování
- HTTP klienty
- platební brány
- messaging
- e-mailové adaptéry
- konfiguraci

```kotlin
@Repository
class JpaOrderRepository(
	private val repository: SpringDataOrderRepository
) : OrderRepository {

	override fun save(order: Order): Order {
		return repository.save(order.toEntity()).toDomain()
	}
}
```

Doména zná pouze rozhraní `OrderRepository`. Neví, že implementace používá JPA.

## Základní pravidla závislostí

```text
api -> application -> domain
						 ^
						 |
				   infrastructure
```

- `domain` nezávisí na ostatních vrstvách
- `application` používá `domain`
- `api` volá `application`
- `infrastructure` implementuje rozhraní z `domain` nebo `application`
- modul nemá přímo číst tabulky jiného modulu
- mezi moduly se používají veřejná API, DTO, ID nebo události

## Příklad modulu `orders`

```text
orders/
├── api/
│   ├── OrdersApi.kt
│   ├── OrderController.kt
│   └── OrderResponse.kt
├── application/
│   ├── CreateOrderHandler.kt
│   └── CreateOrderCommand.kt
├── domain/
│   ├── Order.kt
│   ├── OrderItem.kt
│   ├── OrderStatus.kt
│   └── OrderRepository.kt
└── infrastructure/
	├── OrderJpaEntity.kt
	├── SpringDataOrderRepository.kt
	└── JpaOrderRepository.kt
```

Nejdůležitější je oddělit:

- business pravidla od technologií
- veřejné rozhraní od interní implementace
- databázový model od doménového modelu
- jednotlivé moduly mezi sebou

Například `Cart` může volat veřejné `CatalogApi`, ale neměla by importovat `CatalogProductEntity`.
