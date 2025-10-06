# 🍽️ ServEasy - Sistema de Gestão de Restaurante

**Sistema completo de gestão de restaurante desenvolvido em Java Spring Boot com interface web responsiva e APIs REST.**

---

## 📋 **Índice**

- [🎯 Visão Geral](#-visão-geral)
- [🛠️ Tecnologias](#️-tecnologias)
- [✨ Funcionalidades](#-funcionalidades)
- [🏗️ Arquitetura](#️-arquitetura)
- [🗄️ Banco de Dados](#️-banco-de-dados)
- [🚀 Instalação e Execução](#-instalação-e-execução)
- [👥 Usuários Padrão](#-usuários-padrão)
- [📊 APIs REST](#-apis-rest)
- [🎨 Interface Web](#-interface-web)
- [📁 Estrutura do Projeto](#-estrutura-do-projeto)
- [🔧 Configuração](#-configuração)
- [📱 Responsividade](#-responsividade)
- [🔒 Segurança](#-segurança)
- [🧪 Testes](#-testes)

---

## 🎯 **Visão Geral**

O **ServEasy** é um sistema completo de gestão de restaurante que oferece funcionalidades para três tipos de usuários:

- 👨‍💼 **Administradores**: Gestão completa do sistema (dashboard, menu, mesas, estoque, feedbacks)
- 👨‍🍳 **Cozinheiros**: Gerenciamento de pedidos (novos, em andamento, prontos)
- 👥 **Cliente/Atendente**: Visualização do cardápio, pedidos e feedback

O sistema foi desenvolvido seguindo boas práticas de desenvolvimento, com arquitetura MVC, APIs REST, autenticação baseada em roles e interface responsiva.

---

## 🛠️ **Tecnologias**

### **Backend:**
- ☕ **Java 17** - Linguagem de programação
- 🍃 **Spring Boot 3.5.6** - Framework principal
- 🗃️ **Spring Data JPA** - Mapeamento objeto-relacional
- 🔍 **Spring Validation** - Validação de dados
- 🌐 **Spring Web** - APIs REST e MVC
- 🔧 **Spring DevTools** - Desenvolvimento

### **Frontend:**
- 🌐 **HTML5** - Estrutura das páginas
- 🎨 **CSS3** - Estilização responsiva
- ⚡ **JavaScript (ES6+)** - Interatividade
- 🅱️ **Bootstrap 5.3.2** - Framework CSS
- 🍃 **Thymeleaf** - Template engine

### **Banco de Dados:**
- 🐬 **MySQL 8.0+** - Sistema de gerenciamento de banco
- 🔗 **MySQL Connector/J** - Driver JDBC
- 🗂️ **Hibernate** - ORM

### **Build e Dependências:**
- 📦 **Maven** - Gerenciamento de dependências
- 🔨 **Lombok** - Redução de boilerplate
- 🧪 **Spring Boot Test** - Testes automatizados

---

## ✨ **Funcionalidades**

### 🔐 **Sistema de Autenticação**
- ✅ Login seguro com validação de credenciais
- ✅ Redirecionamento automático baseado em role
- ✅ Tratamento de sessões
- ✅ Página de acesso negado

### 👨‍💼 **Painel do Administrador**
- ✅ **Dashboard**: Estatísticas em tempo real (pedidos, receita, clientes, cancelamentos)
- ✅ **Gestão de Menu**: CRUD completo de itens do cardápio
- ✅ **Gestão de Mesas**: Controle de disponibilidade e ocupação
- ✅ **Gestão de Estoque**: Controle de ingredientes e quantidades
- ✅ **Visualização de Feedbacks**: Monitoramento da satisfação dos clientes

### 👨‍🍳 **Painel do Cozinheiro**
- ✅ **Novos Pedidos**: Visualização de pedidos pendentes (status: PENDING)
- ✅ **Pedidos em Andamento**: Acompanhamento de pedidos sendo preparados (status: PREPARING)
- ✅ **Pedidos Prontos**: Finalização de pedidos (status: READY)
- ✅ **Atualização de Status**: Interface intuitiva para mudança de status

### 👥 **Painel Cliente/Atendente**
- ✅ **Cardápio Interativo**: Navegação por categorias e seleção de itens
- ✅ **Sistema de Pedidos**: Criação e envio de pedidos
- ✅ **Acompanhamento**: Visualização do status dos pedidos em tempo real
- ✅ **Sistema de Feedback**: Avaliação da experiência

---

## 🏗️ **Arquitetura**

O sistema segue o padrão **MVC (Model-View-Controller)** com arquitetura em camadas:

```
┌─────────────────┐
│   Presentation  │ ← Controllers (REST + Pages)
├─────────────────┤
│    Business     │ ← Services (Lógica de Negócio)
├─────────────────┤
│  Persistence    │ ← Repositories (Spring Data JPA)
├─────────────────┤
│    Database     │ ← MySQL
└─────────────────┘
```

### **Componentes Principais:**

- **Controllers**: Gerenciam requisições HTTP (REST APIs + páginas web)
- **Services**: Contêm a lógica de negócio
- **Repositories**: Abstração para acesso aos dados
- **Models**: Entidades JPA mapeadas para o banco
- **DTOs**: Objetos de transferência de dados
- **Configuration**: Configurações e inicialização de dados

---

## 🗄️ **Banco de Dados**

### **Configuração:**
- **SGBD**: MySQL 8.0+
- **Database**: `serveasy_db`
- **Porta**: `3306`
- **Charset**: UTF-8

### **Tabelas Principais:**

#### 👥 **users** - Usuários do sistema
```sql
- id (PK), username (UNIQUE), password, role, full_name
- created_at, updated_at
- Roles: ADMIN, COZINHEIRO, CLIENTE_ATENDENTE
```

#### 🍽️ **menu_items** - Itens do cardápio
```sql
- id (PK), name, description, price, category, image_url
- is_available, created_at, updated_at
- Categorias: ENTRADAS, PRATOS_PRINCIPAIS, BEBIDAS, SOBREMESAS, LANCHES, PIZZAS
```

#### 🛎️ **orders** - Pedidos
```sql
- id (PK), table_number, customer_name, status, total_amount
- created_at, updated_at
- Status: PENDING, PREPARING, READY, DELIVERED, CANCELLED
```

#### 🪑 **tables** - Mesas do restaurante
```sql
- id (PK), table_number (UNIQUE), capacity, status
- created_at, updated_at
- Status: AVAILABLE, OCCUPIED, RESERVED, OUT_OF_SERVICE
```

#### 📦 **stock_items** - Estoque de ingredientes
```sql
- id (PK), name, quantity, unit, minimum_quantity
- supplier, last_updated
```

#### 💬 **feedback** - Avaliações dos clientes
```sql
- id (PK), customer_name, rating, comment, order_id
- created_at
```

---

## 🚀 **Instalação e Execução**

### **Pré-requisitos:**
- ☕ **Java 17+** instalado
- 🐬 **MySQL 8.0+** configurado e executando
- 📦 **Maven 3.6+** (ou usar o wrapper incluído)

### **Passo a Passo:**

#### 1. **Preparar o Banco de Dados**
```bash
# Iniciar o MySQL
mysql -u root -p

# Criar o banco (ou deixar a aplicação criar automaticamente)
CREATE DATABASE IF NOT EXISTS serveasy_db;
```

#### 2. **Clonar e Configurar o Projeto**
```bash
# Entrar no diretório do projeto
cd ServEasy

# Verificar as configurações em application.properties
# Ajustar senha do MySQL se necessário
```

#### 3. **Executar a Aplicação**

**Opção A - Usando Maven Wrapper (recomendado):**
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

**Opção B - Usando Maven instalado:**
```bash
mvn spring-boot:run
```

**Opção C - Executar via IDE:**
```bash
# Importar como projeto Maven
# Executar a classe ServEasyApplication.java
```

#### 4. **Acessar a Aplicação**
```
🌐 URL: http://localhost:8080
📱 Interface responsiva para desktop e mobile
```

---

## 👥 **Usuários Padrão**

O sistema cria automaticamente usuários de teste na primeira execução:

### 👨‍💼 **Administrador**
```
Username: admin
Password: admin123
Acesso: /admin/dashboard
```

### 👨‍🍳 **Cozinheiro**
```
Username: cozinheiro
Password: cozinha123
Acesso: /cozinheiro/novos-pedidos
```

### 👥 **Atendente**
```
Username: atendente
Password: atende123
Acesso: /cliente-atendente/cardapio
```

> 💡 **Dica**: Os usuários são criados automaticamente pelo `DataInitializer` na primeira execução.

---

## 📊 **APIs REST**

### 🔐 **Autenticação**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### 📈 **Dashboard (Admin)**
```http
GET /api/dashboard/stats
# Retorna: pedidos totais, receita, clientes únicos, avaliação média
```

### 🍽️ **Menu**
```http
GET    /api/menu                    # Listar todos os itens
GET    /api/menu/available          # Itens disponíveis
GET    /api/menu/category/{cat}     # Por categoria
POST   /api/menu                    # Criar item
PUT    /api/menu/{id}               # Atualizar item
DELETE /api/menu/{id}               # Deletar item
```

### 🛎️ **Pedidos**
```http
GET    /api/orders                  # Listar pedidos
GET    /api/orders/status/{status}  # Por status
POST   /api/orders                  # Criar pedido
PUT    /api/orders/{id}/status      # Atualizar status
```

### 🪑 **Mesas**
```http
GET    /api/tables                  # Listar mesas
GET    /api/tables/available        # Mesas disponíveis
POST   /api/tables                  # Criar mesa
PUT    /api/tables/{id}             # Atualizar mesa
PUT    /api/tables/{id}/status      # Alterar status
```

### 📦 **Estoque**
```http
GET    /api/stock                   # Listar itens
GET    /api/stock/low               # Estoque baixo
POST   /api/stock                   # Adicionar item
PUT    /api/stock/{id}              # Atualizar item
```

### 💬 **Feedbacks**
```http
GET    /api/feedbacks               # Listar feedbacks
POST   /api/feedbacks               # Criar feedback
GET    /api/feedbacks/stats         # Estatísticas
```

---

## 🎨 **Interface Web**

### **Páginas Disponíveis:**

#### 🔐 **Autenticação**
- `/` - Página de login
- `/access-denied` - Acesso negado

#### 👨‍💼 **Admin**
- `/admin/dashboard` - Dashboard com métricas
- `/admin/menu` - Gestão do cardápio
- `/admin/tables` - Gestão de mesas
- `/admin/stock` - Gestão de estoque
- `/admin/feedbacks` - Visualização de feedbacks

#### 👨‍🍳 **Cozinheiro**
- `/cozinheiro/novos-pedidos` - Pedidos pendentes
- `/cozinheiro/pedidos-andamento` - Pedidos em preparo
- `/cozinheiro/pedidos-prontos` - Pedidos finalizados

#### 👥 **Cliente/Atendente**
- `/cliente-atendente/cardapio` - Cardápio interativo
- `/cliente-atendente/seus-pedidos` - Acompanhamento
- `/cliente-atendente/feedback` - Avaliação

### **Design System:**
- 🎨 **Cores**: Gradientes modernos e paleta harmoniosa
- 📱 **Responsivo**: Layout adaptável para todos os dispositivos
- 🧭 **Navegação**: Sidebar intuitiva com indicadores visuais
- ⚡ **Interações**: Feedback visual em todas as ações
- 🔄 **Tempo Real**: Atualizações automáticas de dados

---

## 📁 **Estrutura do Projeto**

```
ServEasy/
├── 📄 pom.xml                      # Configuração Maven
├── 📄 README.md                    # Documentação
├── 📄 database_init.sql            # Script de criação do BD
├── 📄 migration_stock_items.sql    # Migration de estoque
├── 🔧 mvnw, mvnw.cmd              # Maven wrapper
│
└── src/
    ├── main/
    │   ├── java/com/tiagoportilho/ServEasy/
    │   │   ├── 📁 config/          # Configurações
    │   │   │   └── DataInitializer.java
    │   │   ├── 📁 controller/      # Controllers
    │   │   │   ├── PageController.java
    │   │   │   └── api/           # REST Controllers
    │   │   ├── 📁 dto/            # Data Transfer Objects
    │   │   ├── 📁 exception/      # Tratamento de exceções
    │   │   ├── 📁 model/          # Entidades JPA
    │   │   ├── 📁 repository/     # Repositórios
    │   │   ├── 📁 security/       # Configurações de segurança
    │   │   ├── 📁 service/        # Lógica de negócio
    │   │   ├── 📁 util/           # Utilitários
    │   │   └── ServEasyApplication.java
    │   │
    │   └── resources/
    │       ├── application.properties     # Configurações
    │       ├── 📁 static/                # Assets estáticos
    │       │   ├── scripts/              # JavaScript
    │       │   └── styles/               # CSS
    │       └── 📁 templates/             # Templates Thymeleaf
    │           ├── login.html
    │           ├── access-denied.html
    │           ├── admin/
    │           ├── cozinheiro/
    │           └── cliente-atendente/
    │
    └── test/                             # Testes automatizados
```

---

## 🔧 **Configuração**

### **application.properties**
```properties
# Aplicação
spring.application.name=ServEasy
server.port=8080

# Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/serveasy_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=300807
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Thymeleaf
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html

# DevTools
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true

# Logging
logging.level.com.tiagoportilho.ServEasy=DEBUG
```

### **Personalização:**
- 🔑 Alterar credenciais do banco em `application.properties`
- 👥 Modificar usuários padrão em `DataInitializer.java`
- 🎨 Customizar estilos em `static/styles/`
- 🔧 Ajustar configurações de segurança conforme necessário

---

## 📱 **Responsividade**

O sistema foi desenvolvido com design **mobile-first** e suporta:

- 📱 **Mobile**: 320px - 768px
- 💻 **Tablet**: 768px - 1024px
- 🖥️ **Desktop**: 1024px+

### **Características Responsivas:**
- ✅ Layout fluido e adaptável
- ✅ Sidebar colapsável em dispositivos móveis
- ✅ Tabelas responsivas com scroll horizontal
- ✅ Botões e formulários otimizados para touch
- ✅ Tipografia escalável
- ✅ Imagens responsivas

---

## 🔒 **Segurança**

### **Implementações de Segurança:**
- 🔐 **Autenticação**: Validação de credenciais no banco de dados
- 🎭 **Autorização**: Controle de acesso baseado em roles
- 🛡️ **Proteção de Rotas**: Redirecionamento para páginas apropriadas
- 🔍 **Validação de Dados**: Validações server-side com Spring Validation
- 🚫 **Tratamento de Erros**: Páginas de erro customizadas

### **Próximos Passos de Segurança:**
- 🔐 Implementar BCrypt para hash de senhas
- 🎫 Adicionar JWT para autenticação stateless
- 🛡️ Configurar Spring Security
- 🔒 Implementar HTTPS
- 📝 Adicionar logs de auditoria

---

## 🧪 **Testes**

### **Tipos de Teste Implementados:**
- ✅ **Testes de Unidade**: Services e repositories
- ✅ **Testes de Integração**: Controllers e APIs
- ✅ **Testes de Banco**: Repositories com @DataJpaTest

### **Executar Testes:**
```bash
# Todos os testes
mvn test

# Testes específicos
mvn test -Dtest=AuthServiceTest
```

---

## 🏆 **Status do Projeto**

### ✅ **Implementado:**
- Sistema de autenticação completo
- CRUD completo para todas as entidades
- APIs REST funcionais
- Interface web responsiva
- Integração com banco de dados real
- Sistema de pedidos end-to-end
- Dashboard com métricas reais
- Tratamento de erros
- Validação de dados

### 🚧 **Melhorias Futuras:**
- Implementação de Spring Security
- Sistema de notificações em tempo real
- Relatórios avançados
- Sistema de pagamento
- App mobile nativo
- Testes automatizados completos
- Deploy em cloud (Docker/Kubernetes)

---


**Desenvolvedor**: Tiago Portilho  
**Tecnologias**: Java 17, Spring Boot 3.5.6, MySQL, Bootstrap  
**Licença**: Projeto acadêmico/pessoal  

---

**🎉 Sistema ServEasy - Gestão de Restaurante Completa e Funcional! 🍽️**