# 🧠 RiffHouse API — E-commerce REST com Java Spring Boot

API REST desenvolvida em **Java + Spring Boot**, responsável por gerenciar produtos, usuários e pedidos da plataforma **RiffHouse** — um e-commerce de instrumentos musicais.  
Projetada com foco em **boas práticas de arquitetura, segurança e integração**, esta API constitui o **back-end** do projeto Full Stack RiffHouse.

---

## 🎥 Demonstração do Projeto

[![Assista à demonstração no YouTube](https://img.youtube.com/vi/mJQsdfpHpg4/0.jpg)](https://youtu.be/mJQsdfpHpg4)

---

## 🚀 Tecnologias e Dependências Principais

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)
![H2](https://img.shields.io/badge/Database-H2-blue)


- **Java 21**
- **Spring Boot 3**
  - Spring Web  
  - Spring Data JPA  
  - Spring Validation  
  - Spring Security  
- **PostgreSQL / H2 Database**
- **Lombok**
- **JWT Authentication**
- **ModelMapper**
- **Maven**

---

## ⚙️ Funcionalidades Implementadas

✅ CRUD completo de **produtos**, **categorias** e **pagamentos** (usuários ADMIN)  
✅ CRUD e autenticação de **usuários** com **JWT**  
✅ CRUD de **pedidos**, **itens de pedido** e **endereços** (usuários autenticados)  
✅ Registro e listagem de pedidos com base no usuário logado  
✅ Integração com **PostgreSQL** e suporte para **H2** em ambiente de teste  
✅ Validações robustas com **Jakarta Validation**  
✅ Tratamento de erros padronizado com `GlobalExceptionHandler / ApiError`  
✅ Internacionalização (mensagens em inglês e português)  
✅ Estrutura preparada para **testes automatizados de API**

---

## 📁 Estrutura do Projeto
```
/src
│-- /main
│   ├── /java/br/edu/utfpr/pb/ecommerce/server_ecommerce
│   │   ├── ServerEcommerceApplication.java
│   │   ├── /config
│   │   ├── /controller
│   │   ├── /dto
│   │   ├── /exception
│   │   ├── /handler
│   │   ├── /mapper
│   │   ├── /model
│   │   ├── /repository
│   │   ├── /security
│   │   ├── /service
│   │   ├── /util
│   └── /resources
│       └── application.properties
│       └── import.sql
│       └── ValidationMessages.properties
│
└── /test/java
    └── ServerEcommerceApplicationTests.java
```

📌 **Resumo:**
- `config/` → COnfigurações globais da aplicação como ModelMapper e LocaleConfig para tradução  
- `controller/` → Endpoints REST (autenticação, produtos, pedidos, etc.)  
- `dto/` → Objetos de transferência entre camadas (requests/responses/updates)  
- `exception/` → Exceptions personalizadas para tratamento de erros 
- `handler/` → Handler global para tratamento de erros padronizados
- `mapper/` → Conversões DTO ↔ Entidades  
- `model/` → Entidades JPA  
- `repository/` → Interfaces Spring Data  
- `security/` → Configurações JWT e Spring Security  
- `service/` → Regras de negócio
- `util/` → Métodos auxiliares para validação de campos
- `application.properties` → Configurações do ambiente (DB, JWT, profiles)
- `import.sql` → Importa dados já criados no banco de dados
- `ValidationMessages.properties` → Mensagens de validação personalizadas para os campos

---

## 🧾 Endpoints Principais

| Método | Endpoint | Descrição |
|--------|-----------|------------|
| `GET` | `/products` | Lista todos os produtos |
| `GET` | `/categories` | Lista todas as categorias |
| `POST` | `/users` | Cadastra novo usuário |
| `POST` | `/login` | Autentica o usuário |
| `POST` | `/addresses` | Cria novo endereço |
| `POST` | `/orders` | Cria novo pedido |
| `GET` | `/orders` | Lista pedidos do usuário |
| `POST` | `/orderItems` | Adiciona item ao pedido |

---

## 📘 Documentação (Swagger)

Disponível após inicializar o servidor:  
🔗 **Em breve ...**

---

## 🧱 Banco de Dados

**Banco:** PostgreSQL  
**Arquivo de configuração:** `src/main/resources/application.properties`

```properties
# API CONFIGS
spring.application.name=server-ecommerce
server.port=8080

# ACTIVE PROFILES
spring.profiles.active=dev

# SECURITY CONSTANTS
jwt.secret=utfpr
# 1 dia
jwt.expiration=86400000

# H2 DATABASE
#spring.datasource.generate-unique-name=false
#spring.datasource.url=jdbc:h2:mem:testdb
#spring.h2.console.enabled=true
#spring.h2.console.path=/h2-console

# PostgresSQL DATABASE:
spring.jpa.database=postgresql
spring.datasource.url=jdbc:postgresql://localhost:5432/riffhouse
spring.datasource.username=postgres
spring.datasource.password=12345

# JPA CONFIGS
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

---

## ⚡ Como Executar Localmente

### 1️⃣ Clone o repositório:
```bash
git clone https://github.com/DarkMatter015/server-ecommerce.git
cd server-ecommerce
```

### 2️⃣ Execute o projeto:
```bash
./mvnw spring-boot:run
```

### 3️⃣ Acesse:
👉 `http://localhost:8080/`

### 4️⃣ (Opcional) Execute com Docker Compose:
Caso prefira rodar a aplicação com todas as dependências (PostgreSQL, RabbitMQ) via Docker:

```bash
docker-compose up --build -d
```

---

## 🔗 Integração com o Front-end

A API é consumida pela aplicação **RiffHouse Web**, desenvolvida em **React + Vite**.  
📦 Repositório front-end: [RiffHouse Web (React)](https://github.com/DarkMatter015/client-ecommerce)

## 🧪 Testes de API no Postman

🔗 [Testes de API no Postman](/postman/README_Postman.md)

---

## 🧠 Aprendizados e Desafios Técnicos

Durante o desenvolvimento deste projeto, pude consolidar e aprofundar meus conhecimentos em **arquitetura de APIs REST** com Java e Spring Boot, além de compreender melhor o ciclo completo de uma aplicação **Full Stack** com React no front-end.

### 🔍 Principais aprendizados
- Implementação de **CRUDs completos** com validações, tratamento de exceções e camadas bem definidas (Controller, Service e Repository).
- Utilização de **JPA/Hibernate** para mapeamento objeto-relacional e criação de relacionamentos entre entidades.
- Integração entre front e back-end via **requisições HTTP (Axios/Fetch)**, entendendo fluxos de resposta e códigos de status.
- Boas práticas com **variáveis de ambiente** e **controle de dependências Maven** e **injeções de dependência nas classes**.
- Organização do projeto com **estrutura modular e versionamento Git**, incluindo commits semânticos seguindo commit pattern e branchs bem definidas.

### ⚙️ Desafios técnicos enfrentados
- Correção de incompatibilidade entre **versão do JDK e Maven Compiler** (`release version not supported`).
- Tratamento de erros **CORS** ao conectar o React com o Spring Boot localmente.
- Decisões sobre **normalização do banco de dados** e relacionamento entre entidades (ex: Produto, Pedido e Usuário).
- Implementação inicial de **autenticação com token JWT** e fluxo de login e validação de token no front-end.

---

## 💡 Melhorias Planejadas

- 🔸 Validação de CEP via **ViaCEP** ou **BrasilAPI**  
- 🔸 Cálculo de frete baseado no endereço  
- 🔸 Implementar **refresh token JWT**  
- 🔸 Testes unitários e de integração (JUnit + Mockito)  
- ✅ Adicionar **Dockerfile** e `docker-compose.yml`
- ✅ Deploy em **Render** ou **Railway**

---

## 👨‍💻 Autor

**Lucas Matheus de Camargo**  
📎 [LinkedIn](https://www.linkedin.com/in/lucas-matheus-de-camargo-49a315236/)  
💼 Buscando oportunidades como **Desenvolvedor Java/Fullstack Júnior** e **QA Júnior**
