## ☁️ Deploy na Nuvem (Render)

O projeto já inclui um arquivo `render.yaml` para facilitar o deploy no **Render** (Plataforma PaaS).

### Passo a passo para o deploy:

1. **Crie uma conta no [Render](https://render.com/).**
2. **Configure o RabbitMQ:**
    - O Render não possui RabbitMQ nativo. Recomenda-se criar uma conta gratuita no [CloudAMQP](https://www.cloudamqp.com/).
    - Crie uma nova instância (plano "Lemur" é gratuito).
    - Na dashboard da instância, obtenha os seguintes dados: **Host**, **Port**, **User**, **Password** e **Vhost**.
3. **No Render:**
    - Vá em "Blueprints" e clique em "New Blueprint Instance".
    - Conecte o repositório do projeto.
    - O Render detectará automaticamente o arquivo `render.yaml` e sugerirá a criação dos serviços (API e Banco de Dados).
4. **Variáveis de Ambiente:**
    - Durante a configuração do Blueprint, você precisará fornecer os valores para as variáveis que não são automáticas:
        - `RABBITMQ_HOST`: Host do CloudAMQP (ex: `jackal.rmq.cloudamqp.com`).
        - `RABBITMQ_PORT`: Porta (ex: `5672`).
        - `RABBITMQ_USERNAME`: Usuário (User).
        - `RABBITMQ_PASSWORD`: Senha (Password).
        - `RABBITMQ_VHOST`: Virtual Host (geralmente igual ao User).
        - `MELHOR_ENVIO_API_TOKEN`: Seu token de produção ou sandbox da API do Melhor Envio (se aplicável).
5. **Finalizar:**
    - Clique em "Apply". O Render irá provisionar o banco de dados PostgreSQL e fazer o build da aplicação Docker.
    - Após alguns minutos, sua API estará online em uma URL pública (ex: `https://riffhouse-api.onrender.com`).

---