
# PicPay Simplificado - Desafio Backend

Este é um projeto backend desenvolvido como parte do desafio técnico do PicPay. Ele simula funcionalidades básicas de uma plataforma de pagamentos, permitindo realizar transações entre usuários.

## 🚀 Tecnologias Utilizadas

- **Java 17**: Linguagem principal do projeto.
- **Spring Boot**: Framework para facilitar o desenvolvimento backend.
- **PostgreSQL**: Banco de dados utilizado para persistência.
- **Docker**: Utilizado para empacotamento e execução da aplicação.
- **Docker Compose**: Orquestração da aplicação e banco de dados.
- **JUnit/Mockito**: Para testes unitários e mocks.

---

## ⚙️ Funcionalidades

- Criar e gerenciar usuários.
- Realizar transações entre usuários.
  - Validação de saldo do pagador.
  - Bloqueio de transações entre comerciantes.
  - Proibição de auto-transações.
- Consultar histórico de transações.
- Integração com serviços externos para autorização.

---

## 🛠️ Configuração do Projeto

### Pré-requisitos

- [Java 17](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)

### Instalação Local

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/picpay-simplificado.git
   cd picpay-simplificado
   ```

2. Compile o projeto:
   ```bash
   mvn clean install
   ```

3. Execute o projeto:
   ```bash
   java -jar target/desafio-picpay-0.0.1-SNAPSHOT.jar
   ```

### Usando Docker

1. Construa e inicie os contêineres:
   ```bash
   docker-compose up --build
   ```

2. Acesse a aplicação:
   - Backend: [http://localhost:8080](http://localhost:8080)
   - Banco de dados PostgreSQL:
     - Host: `localhost`
     - Porta: `5432`
     - Usuário: `postgres`
     - Senha: `1234567`

---

## 🔍 Endpoints Principais

### Usuários

- **Criar Usuário**: `POST /usuario`
**Consultar Lista de Usuários**: `GET /usuario`
- **Consultar Usuário por ID**: `GET /usuario/{id}`

### Transações

- **Criar Transação**: `POST /transfer`
- **Consultar Histórico Total**: `GET /transfer`
- **Consultar Histórico por ID**: `GET /transfer/{id}`

---

## 🧪 Testes

Para executar os testes unitários, rode o seguinte comando:

```bash
mvn test
```

---

## 🛡️ Regras de Negócio

- O **pagador** não pode ser um comerciante.
- Um usuário **não pode realizar uma transação para si mesmo**.
- O saldo do pagador deve ser **suficiente** para realizar a transação.
- Todas as transações dependem de autorização de um serviço externo.

