# ServEasy — Instruções para Claude

## Projeto

Sistema de gestão de restaurante (Spring Boot 3, Java 21, PostgreSQL). Projeto de portfólio. Objetivo: código limpo, profissional, sem code smells. **Não adicionar novas funcionalidades — apenas melhorar o que existe.**

Stack: Spring Boot 3 · JWT · Spring Security · Lombok · JPA/Hibernate · OpenAPI/Swagger · Docker

Roles: `ADMIN`, `COZINHEIRO`, `CLIENTE_ATENDENTE`

Exceções customizadas (usar sempre — nunca RuntimeException, IllegalArgumentException, IllegalStateException):
- `ResourceNotFoundException(String resourceName, Long id)` → HTTP 404
- `BusinessException(HttpStatus status, String errorCode, String message)` → HTTP do status
- `ValidationException(String field, String message)` → HTTP 400

## Agentes disponíveis

Rodar via `/agents` no Claude Code. Executar **nessa ordem**:

| # | Agente | O que faz |
|---|---|---|
| 1 | `java-smell-eliminator` | Remove @SuppressWarnings, emoji comments, Collectors.toList(), RuntimeException, campos mortos |
| 2 | `dto-contract-enforcer` | Adiciona @Valid, Bean Validation, extrai inner classes, remove raw entities dos controllers |
| 3 | `service-layer-auditor` | Substitui Optional.isPresent()/.get() por orElseThrow(), adiciona @Transactional(readOnly=true), corrige exceções |
| 4 | `controller-hardener` | Adiciona @PreAuthorize por método, HTTP 201/204, remove endpoint duplicado, deleta test-jwt.html |
| 5 | `security-config-reviewer` | Audita JWT, CORS, DataInitializer, actuator, secrets em properties |
| 6 | `test-suite-builder` | Escreve unit tests (Mockito) e MockMvc tests para todos os services/controllers |
| 7 | `api-documentation-polisher` | @Schema nos DTOs, @ApiResponse, security scheme no Swagger UI |

## Pipeline autônomo

Quando o usuário pedir para executar o pipeline completo ou "melhorar tudo", rodar os agentes na ordem acima. Cada agente deve:

1. Ler todos os arquivos relevantes antes de fazer qualquer mudança
2. Listar os problemas encontrados
3. Aplicar as correções
4. Confirmar o que foi feito

Após cada agente concluir, reportar brevemente o que foi corrigido antes de passar para o próximo.

## Regras de trabalho

- **Nunca** criar arquivos de documentação (.md) além dos já existentes
- **Nunca** adicionar features novas — só melhorar o código existente
- **Nunca** usar `@SuppressWarnings` — resolver o problema real
- **Sempre** usar `.toList()` em vez de `.collect(Collectors.toList())`
- **Sempre** usar as exceções customizadas do projeto, nunca as do Java padrão
- **Sempre** adicionar `@Transactional(readOnly = true)` em métodos de leitura
- **Sempre** retornar DTOs nos controllers, nunca entidades JPA cruas
- Sem comentários óbvios — só comentar o WHY quando genuinamente não-óbvio
- Sem emojis em código Java

## Estrutura do projeto

```
src/main/java/com/tiagoportilho/ServEasy/
  config/          ← CorsConfig, DataInitializer, OpenApiConfig, WebConfig
  controller/
    api/           ← AuthController, DashboardController, FeedbackController,
                      MenuController, OrderController, StockController, TableController
    PageController ← Thymeleaf page routing
  dto/
    request/       ← MenuItemRequest, StockItemRequest, TableRequest
    response/      ← DashboardStatsResponse, StockItemResponse, TableResponse
    (root)         ← ApiResponse, OrderRequest, OrderResponseDTO, LoginRequest/Response...
  exception/       ← BusinessException, GlobalExceptionHandler, ResourceNotFoundException, ValidationException
  model/           ← Feedback, MenuItem, MenuItemIngredient, Order, OrderItem,
                      RestaurantTable, Sale, StockItem, User
  repository/      ← um por model
  security/        ← CustomUserDetailsService, JwtAuthenticationEntryPoint,
                      JwtRequestFilter, JwtTokenProvider, SecurityConfig, UserPrincipal
  service/         ← AuthService, FeedbackService, MenuService, OrderService,
                      SaleService, StockService, TableService
  util/            ← LoggingAspect (AOP logging)
src/main/resources/
  templates/       ← Thymeleaf HTML (admin/, cozinheiro/, cliente-atendente/)
  static/          ← CSS, JS (jwt-interceptor.js é o auth client-side)
  application*.properties
```

## Problemas conhecidos (identificados em 2026-06-07)

Esses são os problemas concretos que os agentes devem resolver:

**Code smells:**
- `@SuppressWarnings("null")` em `OrderService`, `MenuService`, `GlobalExceptionHandler`
- Emoji comments `// 🔧 BUG FIX` em `OrderService`
- `Collectors.toList()` em todos os services e controllers
- `RuntimeException` em `MenuService.toggleAvailability`, `MenuService.saveMenuItemWithIngredients`, `OrderService.updateOrderStatus`
- Campo morto `OrderRequest.status` (nunca lido no service)
- Campo morto `OrderItemRequest.unitPrice` (nunca lido, preço vem do MenuItem)
- Comments de change log `// Mudança de notes para observations` (pertencem ao git)

**DTOs:**
- `StatusUpdateRequest` como inner class em `OrderController` → mover para `dto/request/`
- `OrderItemRequest` como inner class em `OrderRequest` → mover para `dto/request/`
- Falta `@Valid` em `@RequestBody` em todos os controllers
- Falta Bean Validation nas request DTOs (`@NotNull`, `@Min`, `@NotBlank`, etc.)
- Endpoints em `OrderController` retornando `ApiResponse<Order>` (raw entity) em vez de `ApiResponse<OrderResponseDTO>`
- Nomenclatura inconsistente: `MenuItemDto` vs `MenuItemSimpleDTO` vs `StockItemResponse`

**Services:**
- `Optional.isPresent()` + `.get()` anti-pattern em vez de `.orElseThrow()`
- Falta `@Transactional(readOnly = true)` em todos os métodos de leitura
- `IllegalStateException` em `OrderService` (não mapeada para status HTTP correto)
- Double DB lookup em `cancelOrder` (busca o order duas vezes)
- Validação manual em `OrderService.createOrder` que será redundante após Bean Validation

**Controllers:**
- Falta `@PreAuthorize` em todos os métodos (depende só das regras de URL no SecurityConfig)
- `POST` endpoints retornando HTTP 200 em vez de 201
- `DELETE` endpoints retornando HTTP 200 com body em vez de 204
- Dois endpoints para atualizar status: `PATCH /orders/{id}/status?status=X` E `PUT /orders/{id}/status` (duplicado)
- `log.info()` em operações CRUD rotineiras (volume de log excessivo em prod)

**Segurança:**
- `test-jwt.html` na pasta `static/` (acessível em `/test-jwt.html` em produção)
- CORS `allowCredentials(true)` desnecessário (JWT usa header, não cookie)
- HTML pages são `permitAll()` — segurança depende do JavaScript (frontend-only auth para as páginas)

**Testes:**
- Nenhum arquivo de teste visível no projeto
