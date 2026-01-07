# 🎸 RiffHouse API — E-commerce Backend

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-brightgreen?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6-green?style=for-the-badge&logo=springsecurity&logoColor=white)
![OpenFeign](https://img.shields.io/badge/OpenFeign-Declarative_Client-lightgrey?style=for-the-badge&logo=spring)

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-In_Memory-darkblue?style=for-the-badge&logo=h2)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![JPA](https://img.shields.io/badge/Spring_Data_JPA-Hibernate-gray?style=for-the-badge&logo=hibernate)

![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Messaging-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI_3-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

</div>

## 📖 Sobre o Projeto

**RiffHouse API** é um backend REST robusto desenvolvido para a plataforma de e-commerce RiffHouse, especializada em instrumentos musicais. O projeto foi construído com **Java 21** e **Spring Boot 3**, adotando padrões arquiteturais modernos para garantir escalabilidade, manutenibilidade e alta performance.

O objetivo principal é demonstrar a aplicação de conceitos avançados de engenharia de software, incluindo **Arquitetura Orientada a Eventos**, **CQRS (Command Query Responsibility Segregation)** e estratégias de **Concorrência Otimista**.

> **🌐 Live Demo:** [Acesse a API aqui](https://riffhouse-api.onrender.com/)
>
> ⚠️ **Nota:** O servidor está hospedado no plano gratuito do Render. A primeira requisição pode levar de 1 a 2 minutos para "acordar" a API.

👉 **Documentação Interativa (Swagger):** [Acesse o Swagger UI](https://riffhouse-api.onrender.com/swagger-ui.html)

---

## 🏗️ Arquitetura e Decisões Técnicas

A aplicação segue uma **Arquitetura em Camadas** com estrita separação de responsabilidades. O design foi pensado para resolver problemas reais de concorrência e escalabilidade.

### 📐 Destaques Arquiteturais
* **Abstração Genérica (CRUD Base)**: Implementação de controladores base (`BaseReadController` e `BaseWriteController`) e serviços genéricos. Isso garante padronização, reduz código duplicado (DRY) e acelera o desenvolvimento de novas entidades.
* **CQRS-Lite**: Separação física e lógica das operações de Leitura e Escrita. Isso permite otimizar consultas e comandos de forma independente, melhorando a clareza do código.
* **Concorrência Otimista (`@Version`)**: Solução para evitar conflitos de estoque (Lost Update Problem). Garante que dois usuários não consigam comprar o último item do estoque simultaneamente, utilizando versionamento de registro no banco de dados.
* **Arquitetura Orientada a Eventos (RabbitMQ)**: Processamento assíncrono para tarefas pesadas e lentas (envio de e-mails, processamento de pedidos), desacoplando o fluxo principal e melhorando o tempo de resposta para o usuário.
* **Soft Delete**: Implementação de exclusão lógica em nível de entidade base, permitindo desativar registros sem perda de histórico de dados.
* **Strategy Pattern**: Uso de interfaces genéricas `Validator<T>` para encapsular regras de negócio complexas, facilitando testes e extensão.
* **Tratamento Global de Erros**:
    * `ApiErrorDTO` para padronização de respostas JSON (RFC 7807 inspired).
    * Supressão de stacktrace em produção para segurança.
    * Exceções personalizadas de negócio.
* **Internacionalização (i18n)**: A API responde mensagens de erro e validação em **Inglês** ou **Português**, baseando-se automaticamente no header `Accept-Language`.

### 🛠️ Tecnologias
*   **Linguagem**: Java 21
*   **Framework**: Spring Boot 3.5.5 (Web, Data JPA, Security, Validation, ModelMapper, OpenFeign, AMQP, Mail)
*   **Banco de Dados**: PostgreSQL (Produção/Dev), H2 (Teste)
*   **Migração**: Flyway
*   **Mensageria**: RabbitMQ
*   **Documentação**: SpringDoc OpenAPI (Swagger UI)
*   **Containerização**: Docker e Docker Compose

---

## 🚀 Funcionalidades Principais

### 👤 Gestão de Usuários & Segurança
* **Autenticação JWT:** Login seguro, validação de token stateless e recuperação de senha via e-mail.
* **RBAC (Role-Based Access Control):** Sistema de permissões hierárquico. Usuários nascem como `USER`; apenas `ADMIN` pode elevar privilégios.
* **Proteção de Dados:** Usuários gerenciam apenas seus próprios dados sensíveis, independente do nível de acesso.

### 🛒 Core E-commerce (Pedidos & Estoque)
* **Checkout Assíncrono:** O pedido é recebido e processado em background via fila, garantindo alta disponibilidade mesmo em picos de acesso.
* **Validação de Estoque:** Checagem rigorosa de disponibilidade antes e durante o processamento do pedido.
* **Alertas de Interesse:** Usuários podem assinar notificações (`POST /alerts`) para produtos sem estoque. O sistema dispara e-mails automaticamente via RabbitMQ assim que o produto é reposto.

### 🔔 Notificações & Integrações
* **Feedback por E-mail:** Atualizações de status de pedidos e confirmações de cadastro enviadas via integração SMTP (MailHog em dev).
* **Validação de CEP e FRETE:** Integração com API externa para garantir a integridade dos endereços de entrega e cálculo de frete.

---

## 🔒 Endpoints e Controle de Acesso

| Perfil | Acesso Permitido |
| :--- | :--- |
| **Público** | • Visualizar Catálogo (`GET /products`)<br>• Registro (`POST /users`)<br>• Recuperação de Senha<br>• Cadastrar Alerta de Estoque |
| **Usuário (USER)** | • Realizar Pedidos (`POST /orders`)<br>• Gerenciar seus Endereços e Alertas<br>• Visualizar histórico de compras |
| **Admin (ADMIN)** | • Gestão de Catálogo (CRUD Produtos/Categorias)<br>• Gestão de Meios de Pagamento<br>• Gestão de Usuários |

---

## ⚙️ Configuração e Ambiente

A aplicação utiliza **Spring Profiles** para alternar comportamentos conforme o ambiente:

* 🟢 `dev`: (Default) PostgreSQL local, RabbitMQ local e MailHog para simulação de e-mails.
* 🔴 `prod`: Variáveis de ambiente, conexão segura com banco na nuvem.
* 🟡 `test`: Banco H2 em memória para testes de integração ultra-rápidos.

---

## ⚡ Como Rodar o Projeto

### Pré-requisitos
* **Docker & Docker Compose** (Método Recomendado)
* Ou: Java 21 JDK + Maven + PostgreSQL + RabbitMQ instalados localmente.

### 🐳 Via Docker (Recomendado)
Sobe toda a infraestrutura (API, Banco, Broker e Servidor de E-mail) com um comando:

```bash
# 1. Clone o repositório
git clone https://github.com/DarkMatter015/server-ecommerce.git
cd server-ecommerce

# 2. Inicie os serviços
docker-compose up --build -d
```
✅ API: http://localhost:8080 | MailHog: http://localhost:8025

### 💻 Rodar Manualmente
Caso queira rodar a aplicação via IDE, mas manter a infraestrutura no Docker:

1.  **Inicie a Infraestrutura**:
    ```bash
    docker-compose up postgres rabbitmq mailhog -d
    ```
2.  **Rode a Aplicação**:
    ```bash
    ./mvnw spring-boot:run
    ```

---

## 📚 Documentação e Consumo da API

Para facilitar a integração e o teste dos endpoints, disponibilizo duas formas principais de documentação.

### Swagger UI (OpenAPI)
A documentação interativa é gerada automaticamente e permite testar requisições diretamente pelo navegador.
👉 **Acesso Local**: `http://localhost:8080/swagger-ui.html`

### Coleção Postman
Uma coleção completa do Postman com requisições pré-configuradas está disponível no diretório `postman/`.
👉 [**Ver Guia e Download da Coleção**](postman/README_Postman.md)

---

## 📡 Padrões de Resposta (JSON)

A API segue um contrato estrito de respostas. O tratamento de erros é centralizado (`GlobalExceptionHandler`), garantindo que o client (Front-end) sempre receba um JSON previsível, mesmo em falhas críticas.

#### ✅ 201 Created (Sucesso)
Exemplo de resposta ao criar um recurso (ex: Usuário). O payload retorna os dados públicos da entidade criada.
```json
{
    "id": 3,
    "active": true,
    "displayName": "Lucas Camargo",
    "email": "decamargoluk@gmail.com",
    "cpf": "11111111111",
    "roles": [
        {
            "id": 2,
            "active": true,
            "name": "USER"
        }
    ]
}
```

#### ❌ 400 Bad Request (Erro de Validação)
Retornado quando os dados enviados violam as regras do DTO (Bean Validation). O campo `validationErrors` fornece um mapa detalhado `campo: erro` para facilitar o feedback no front-end.
```json
{
    "timestamp": 1767828796670,
    "message": "Campos preenchidos incorretamente.",
    "status": 400,
    "url": "/users",
    "validationErrors": {
        "password": "A senha deve conter pelo menos uma letra minuscula, uma maiuscula e um numero.",
        "displayName": "O nome de exibicao deve ter entre 3 e 255 caracteres."
    }
}
```

#### ⛔ 401 Unauthorized / 403 Forbidden
Ocorre quando o usuário não está autenticado ou tenta acessar um recurso de ADMIN (como `/products` POST) sem permissão.
```json
{
    "path": "/users/3",
    "error": "Unauthorized",
    "message": "Authentication credentials are missing or invalid.",
    "timestamp": "2026-01-07T23:33:59.396305900Z",
    "status": 401
}
```

#### 💥 500 Internal Server Error
Para segurança da aplicação, erros internos não expõem o stacktrace Java em produção. Uma mensagem genérica é retornada enquanto o erro real é logado no servidor.
```json
{
  "timestamp": 1709664000000,
  "message": "Ocorreu um erro interno inesperado. Tente Novamente mais tarde.",
  "status": 500,
  "url": "/orders"
}
```

---

## 🧪 Testes e Qualidade

A confiabilidade é um pilar deste projeto. Foram implementados Testes de Integração que sobem o contexto do Spring Boot para validar os fluxos de ponta a ponta.

*   **Ambiente Isolado:** Utiliza banco de dados em memória (**H2 Database**) para garantir que os testes não impactem os dados de desenvolvimento.

*   **Escopo:** Validação de regras de negócio, restrições de banco e segurança dos endpoints.

Para executar a suíte de testes:

```bash
# Rodar todos os testes
./mvnw test
```

---

## 👨‍💻 Autor

<table style="border: none;">
  <tr>
    <td width="100px" align="center">
      <img src="https://github.com/DarkMatter015.png" width="100px" style="border-radius: 50%;" alt="Avatar do Lucas"/>
    </td>
    <td style="padding-left: 15px;">
      <strong>Lucas Matheus de Camargo</strong><br>
      <i>Desenvolvedor Full Stack | QA Engineer</i><br>
      <br>
      <a href="https://www.linkedin.com/in/lucas-matheus-de-camargo-49a315236/" target="_blank">
        <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=flat&logo=linkedin&logoColor=white" alt="LinkedIn Badge">
      </a>
      <a href="https://github.com/DarkMatter015" target="_blank">
        <img src="https://img.shields.io/badge/GitHub-100000?style=flat&logo=github&logoColor=white" alt="GitHub Badge">
      </a>
    </td>
  </tr>
</table>


---

<div align="center"> <sub>Feito com ☕ e Java por Lucas Matheus.</sub> </div>
