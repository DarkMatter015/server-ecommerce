# 🎸 RiffHouse API — E-commerce Backend

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.12-orange?style=for-the-badge&logo=rabbitmq)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=for-the-badge&logo=docker)

## 📖 Sobre o Projeto

**RiffHouse API** é o backend REST robusto para a plataforma de e-commerce RiffHouse, especializada em instrumentos musicais. Foi construído com **Java 21** e **Spring Boot 3**, seguindo padrões arquiteturais modernos para garantir escalabilidade, manutenibilidade e desempenho.

Este projeto foi desenvolvido para demonstrar conceitos avançados de backend, incluindo **Arquitetura Orientada a Eventos** para processamento assíncrono, princípios de **CQRS** para gerenciamento de pedidos e **Autenticação JWT** segura.

---

## 🚀 Funcionalidades Principais por Módulo

### 👤 Usuários e Autenticação
*   **Cadastro**: Aberto ao público (`POST /users`).
*   **Segurança**: Login com JWT, recuperação de senha e validação de token.
*   **Perfil**: Usuários autenticados gerenciam seus dados e endereços.

### 📦 Produtos e Categorias
*   **Catálogo**: Listagem de produtos e categorias pública para todos os visitantes.
*   **Gestão (ADMIN)**: Apenas administradores podem criar, editar ou excluir produtos e categorias.
*   **Alerta de Estoque**: Usuários podem cadastrar alertas (`POST /alerts`) para serem notificados quando um produto indispovível voltar ao estoque.

### 🛒 Pedidos e Checkout
*   **Arquitetura CQRS**: Separação clara entre leitura (`ReadOrderController`) e escrita (`WriteOrderController`).
*   **Fluxo Completo**: Criação de pedido, adição de itens e integração com cálculo de frete.
*   **Assíncrono**: O processamento do pedido utiliza filas RabbitMQ para alta performance.

### 🔔 Notificações e Alertas
*   **Alertas de Produto**: Endpoint `/alerts` permite que qualquer usuário (autenticado ou não) registre interesse em produtos sem estoque.
*   **Emails**: Envio de confirmações e notificações via background workers.

---

## 🔒 Controle de Acesso e Endpoints

A aplicação utiliza Spring Security para garantir que cada recurso seja acessado apenas por quem tem permissão.

| Perfil de Acesso | Permissões / Rotas Principais |
| :--- | :--- |
| **Público (Sem Login)** | • Ver Produtos e Categorias (`GET`)<br>• Criar Conta (`POST /users`)<br>• Recuperar Senha (`/auth/**`)<br>• Cadastrar Alerta de Estoque (`POST /alerts`) |
| **Usuário Autenticado** | • Fazer Pedidos (`POST /orders`)<br>• Gerenciar Endereços (`/addresses`)<br>• Ver seus próprios pedidos<br>• Gerenciar seus alertas |
| **Administrador (ADMIN)** | • Criar/Editar/Excluir Produtos (`/products`)<br>• Criar/Editar/Excluir Categorias (`/categories`)<br>• Gerenciar Meios de Pagamento (`/payments`) |

---

## 🏗️ Arquitetura e Stack Tecnológico

A aplicação segue uma **Arquitetura em Camadas** com estrita separação de responsabilidades, aprimorada por componentes orientados a eventos.

### 🛠️ Tecnologias
*   **Linguagem**: Java 21
*   **Framework**: Spring Boot 3.5.5 (Web, Data JPA, Security, Validation, AMQP, Mail)
*   **Banco de Dados**: PostgreSQL (Produção/Dev), H2 (Teste)
*   **Migração**: Flyway
*   **Mensageria**: RabbitMQ
*   **Documentação**: SpringDoc OpenAPI (Swagger UI)
*   **Containerização**: Docker e Docker Compose

### 📐 Decisões Arquiteturais
*   **CQRS-Lite**: O domínio de Pedidos divide operações de Leitura e Escrita em controladores diferentes para otimizar desempenho e clareza.
*   **Padrão DTO**: Utiliza Objetos de Transferência de Dados (Data Transfer Objects) para toda comunicação externa, desacoplando o modelo de domínio interno do contrato da API.
*   **Padrão Strategy**: Implementa interfaces genéricas `Validator<T>` para validações de regras de negócio complexas.
*   **Isolamento de Infraestrutura**: Serviços externos (como Envio) são acessados via Interfaces/Feign Clients para facilitar mocks e testes.

---

## ⚙️ Configuração e Ambiente

A aplicação utiliza Spring Profiles para gerenciar configurações em diferentes ambientes.

### 📁 Perfis (Profiles)
*   `dev`: Ativo por padrão. Conecta ao PostgreSQL local e RabbitMQ. Usa MailHog para emails.
*   `prod`: Para deploy em produção (ex: Render). Usa variáveis de ambiente para segredos.
*   `test`: Usa banco de dados em memória H2 para testes de integração rápidos.

### 🔑 Variáveis de Ambiente (Produção)
| Variável | Descrição |
| :--- | :--- |
| `DB_HOST`, `DB_PORT`, `DB_NAME` | Detalhes de conexão do Banco de Dados |
| `DB_USER`, `DB_PASS` | Credenciais do Banco de Dados |
| `RABBITMQ_HOST`, `RABBITMQ_PORT` | Conexão RabbitMQ |
| `RABBITMQ_USERNAME`, `RABBITMQ_PASSWORD` | Credenciais RabbitMQ |
| `EMAIL_USERNAME`, `EMAIL_PASSWORD` | Credenciais SMTP para envio de emails |
| `JWT_SECRET` | Chave secreta para geração de tokens |

---

## ⚡ Começando

### Pré-requisitos
*   **Docker e Docker Compose** (Recomendado)
*   **Java 21 JDK** (Se rodar manualmente)
*   **Maven** (Wrapper incluído `./mvnw`)

### 🐳 Rodar com Docker (Recomendado)
Esta é a maneira mais fácil de iniciar toda a stack (API + DB + RabbitMQ + MailHog).

```bash
# 1. Clone o repositório
git clone https://github.com/DarkMatter015/server-ecommerce.git
cd server-ecommerce

# 2. Inicie os serviços
docker-compose up --build -d
```
A API estará disponível em: `http://localhost:8080`

### 💻 Rodar Manualmente
Se preferir rodar a aplicação localmente (ex: para debugging), você ainda precisa do PostgreSQL e RabbitMQ rodando.

1.  **Inicie a Infraestrutura**:
    ```bash
    docker-compose up postgres rabbitmq mailhog -d
    ```
2.  **Rode a Aplicação**:
    ```bash
    ./mvnw spring-boot:run
    ```

---

## 📚 Documentação e Exemplos

### Swagger UI
Documentação interativa da API disponível quando a aplicação está rodando.
👉 **URL**: `http://localhost:8080/swagger-ui.html`

### Coleção Postman
Uma coleção completa do Postman com requisições pré-configuradas está disponível no diretório `postman/`.
👉 [Ver README do Postman](postman/README_Postman.md)

### Exemplos de Respostas e Tratamento de Erros
A API utiliza um formato padronizado para tratamento de erros (`ApiErrorDTO`). Abaixo estão exemplos de respostas comuns baseadas nos DTOs reais da aplicação.

#### ✅ 201 Created (Sucesso - UserResponseDTO)
Resposta ao criar um novo usuário com sucesso.
```json
{
  "id": 1,
  "active": true,
  "displayName": "João Silva",
  "email": "joao@email.com",
  "cpf": "123.456.789-00",
  "roles": [
    {
      "id": 1,
      "name": "ROLE_USER"
    }
  ]
}
```

#### ❌ 400 Bad Request (Erro de Validação)
Exemplo real de falha na validação de campos ao tentar criar um usuário, retornando as mensagens configuradas no sistema.
```json
{
  "timestamp": 1709664000000,
  "message": "Campos inválidos",
  "status": 400,
  "url": "/users",
  "validationErrors": {
    "displayName": "O nome de exibicao deve ter entre 3 e 255 caracteres.",
    "password": "A senha deve ter pelo menos 6 caracteres."
  }
}
```

#### ⛔ 401 Unauthorized / 403 Forbidden
Ocorre quando o usuário não está autenticado ou tenta acessar um recurso de ADMIN (como `/products` POST) sem permissão.
```json
{
  "timestamp": 1709664000000,
  "message": "Acesso negado",
  "status": 403,
  "url": "/products"
}
```

#### 💥 500 Internal Server Error
Erro genérico do servidor tratado globalmente.
```json
{
  "timestamp": 1709664000000,
  "message": "Ocorreu um erro interno no servidor",
  "status": 500,
  "url": "/orders"
}
```

---

## 🧪 Testes

O projeto inclui testes de integração para garantir a confiabilidade da API.

```bash
# Rodar todos os testes
./mvnw test
```

---

## 👨‍💻 Autor

**Lucas Matheus de Camargo**
*   **LinkedIn**: [Lucas Matheus de Camargo](https://www.linkedin.com/in/lucas-matheus-de-camargo-49a315236/)
*   **Função**: Desenvolvedor Backend Java

---
*Construído com ❤️ para a Comunidade Dev.*
