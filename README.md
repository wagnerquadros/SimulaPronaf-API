<div align="center">

<img src="src/main/resources/static/logo.svg" alt="SimulaPRONAF Logo" width="180"/>

# SimulaPRONAF API

**Backend da aplicação Android SimulaPRONAF — simulação de crédito rural para Agricultura Familiar**

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?style=flat-square&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-red?style=flat-square&logo=apachemaven)

</div>

---

## Sobre o projeto

O **SimulaPRONAF API** é o backend de uma aplicação digital voltada à simulação de crédito rural para agricultores familiares.
O projeto nasceu da necessidade de democratizar o acesso às informações e regras do **PRONAF** (Programa Nacional de Fortalecimento 
da Agricultura Familiar), tornando mais simples e transparente o processo de simulação de financiamentos agrícolas.

O sistema une conhecimento técnico de backend moderno com profundo conhecimento de negócio sobre crédito rural, criando uma
base sólida para um produto digital no agro.

API REST desenvolvida em Java + Spring Boot para servir o aplicativo Android **SimulaPRONAF**. O app permite que agricultores 
familiares consultem as linhas de crédito do PRONAF, entendam as condições de financiamento e simulem operações de crédito rural 
diretamente pelo celular.

O acesso é feito via **login com Google**. O backend valida o token do Google, cria o usuário caso necessário e emite um **JWT próprio** para as próximas requisições.

### Problema que resolve

Agricultores familiares frequentemente têm dificuldade em entender as regras e modalidades do PRONAF, os valores e limites de 
crédito disponíveis, as taxas de juros e condições de financiamento, e a elegibilidade para cada linha de crédito. Esta API 
fornece a base para que interfaces digitais (web/mobile) possam apresentar essas informações de forma clara e acessível.


---

## Stack

| | |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 4.0.5 |
| Segurança | Spring Security + JWT (RSA com chave própria) |
| Login social | Google OAuth 2.0 |
| Banco de dados | PostgreSQL |
| Migrations | Flyway |
| Persistência | Spring Data JPA |
| Build | Maven Wrapper |
| Utilitários | Lombok |

---

## Estrutura do projeto

```
src/main/java/com/wagnerquadros/simulapronaf/
│
├── autenticacao/               # Login com Google e emissão de JWT
│   ├── controller/
│   ├── dto/
│   └── service/                # ValidadorTokenGoogleService, JwtService, AutenticacaoService
│
├── credito/                    # Linhas de crédito e itens do PRONAF
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── enums/                  
│   ├── mapper/
│   ├── repository/
│   └── service/
│
├── usuarios/                   # Cadastro e consulta de usuários
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── mapper/
│   ├── repository/
│   └── service/
│
└── infraestrutura/             # Configurações transversais
    ├── configuracao/security/  # SecurityConfig, JwtConfig
    ├── exception/              # GlobalExceptionHandler e exceções customizadas
    └── mapper/                 # Interface base EntityMapper

src/main/resources/
├── application.properties
├── certs/                      # Chaves RSA para assinar/validar o JWT
└── db/migration/               # Scripts Flyway
```

---

## Banco de dados

O schema é gerenciado automaticamente pelo **Flyway** na inicialização da aplicação. As principais tabelas são:

- `usuario` — dados do usuário autenticado via Google
- `linha_credito` — as linhas do PRONAF (ex: PRONAF Custeio, PRONAF Mais Alimentos)
- `item_linha_credito` — modalidades dentro de cada linha, com limite, juros, prazo e carência

---

## Endpoints

### Autenticação — `/auth`

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `POST` | `/auth/google` | Recebe o ID Token do Google, valida e retorna um JWT próprio | ❌ Público |

### Linhas de Crédito — `/credito/linhas`

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `GET` | `/credito/linhas` | Lista todas as linhas de crédito (resumo) | ✅ JWT |
| `GET` | `/credito/linhas?tipo={tipo}` | Filtra por tipo (`CUSTEIO`, `INVESTIMENTO`, etc.) | ✅ JWT |
| `GET` | `/credito/linhas/{id}` | Retorna detalhes completos de uma linha, incluindo seus itens | ✅ JWT |

### Itens de Linha de Crédito — `/credito/itens`

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `GET` | `/credito/itens/{id}` | Retorna um item pelo ID | ✅ JWT |
| `GET` | `/credito/itens/codigo/{codigo}` | Retorna um item pelo código | ✅ JWT |

### Usuários — `/usuarios`

| Método | Endpoint       | Descrição                       | Auth |
|--------|----------------|---------------------------------|------|
| `GET` | `/usuarios/me` | Retorna dados do usuário logado | ✅ JWT |


---

## Fluxo de autenticação

```
App Android
    │
    ├─► Login com Google → obtém ID Token
    │
    └─► POST /auth/google  { "idToken": "..." }
                │
                ▼
        Backend valida token com Google
                │
                ▼
        Busca ou cria o usuário no banco
                │
                ▼
        Emite JWT assinado com chave RSA própria
                │
                ▼
        Retorna { "token": "..." }
                │
    App usa o JWT no header de todas as próximas requisições
    Authorization: Bearer <token>
```

### Fluxo de Login com Google

1. O cliente (frontend/mobile) realiza o login com o Google e obtém um **ID Token** do Google.
2. Esse token é enviado para o endpoint de autenticação da API.
3. O backend **valida o token** diretamente com os servidores do Google usando `google-api-client`.
4. Após validação bem-sucedida, a API emite um **JWT próprio** assinado.
5. Todas as requisições subsequentes devem incluir o JWT no header `Authorization`.

---

## Como rodar localmente

Você pode rodar o projeto de duas formas:

- **modo tradicional**: Java + Maven na máquina
- **modo containerizado**: usando Docker

### Pré-requisitos

Para rodar sem Docker:
- Java 17
- PostgreSQL
- uma conta Google Cloud com Client ID OAuth configurado

Para rodar com Docker:
- Docker instalado
- acesso a um PostgreSQL em execução (local ou remoto)
- uma conta Google Cloud com Client ID OAuth configurado

### 1. Clone o repositório

```bash
git clone https://github.com/wagnerquadros/SimulaPronaf-API.git
cd SimulaPronaf-API
```

### 2. Crie o banco de dados

```sql
CREATE DATABASE simulapronaf;
```

### 3. Configure as variáveis de ambiente

| Variável | Descrição |
|----------|-----------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/simulapronaf` |
| `DB_USERNAME` | Usuário do PostgreSQL |
| `DB_PASSWORD` | Senha do PostgreSQL |
| `GOOGLE_WEB_CLIENT_ID` | Client ID do projeto no Google Cloud |

Exemplo no terminal Linux/macOS:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/simulapronaf
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha
export GOOGLE_WEB_CLIENT_ID=seu_client_id.apps.googleusercontent.com
```

No Windows PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/simulapronaf"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="sua_senha"
$env:GOOGLE_WEB_CLIENT_ID="seu_client_id.apps.googleusercontent.com"
```

### 5. Gere as chaves RSA (obrigatório)

O projeto **não inclui as chaves no repositório** por segurança. Você precisa gerá-las uma vez com o OpenSSL antes de rodar a aplicação.

As chaves ficam em `src/main/resources/certs/` e são usadas pelo Spring Security para **assinar** (`app.key`) e **validar** (`app.pub`) os tokens JWT.

```bash
# 1. Gera a chave privada RSA de 2048 bits no formato PKCS#8
openssl genpkey -algorithm RSA -out app.key -pkeyopt rsa_keygen_bits:2048

# 2. Extrai a chave pública a partir da chave privada
openssl rsa -pubout -in app.key -out app.pub
```

Mova os dois arquivos gerados para `src/main/resources/certs/`:

```bash
mkdir -p src/main/resources/certs
mv app.key src/main/resources/certs/
mv app.pub src/main/resources/certs/
```

> O Spring Boot lê os caminhos `classpath:certs/app.key` e `classpath:certs/app.pub` configurados no `application.properties`. Se os arquivos não existirem, a aplicação falha na inicialização.

> **Nunca versione as chaves reais.** O `.gitignore` já deve ignorar o conteúdo de `certs/` em ambientes de produção. Para desenvolvimento local, as chaves geradas acima são suficientes.

---

### 6. Rodando sem Docker

Execute:

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. O Flyway aplica as migrations automaticamente.

---

### Rodando com Docker

#### 1. Gere a imagem

```bash
docker build -t simulapronaf-api .
```

#### 2. Suba o container

```bash
docker run -p 8080:8080 `
  -e DB_URL="jdbc:postgresql://host.docker.internal:5432/simulapronaf" `
  -e DB_USERNAME="postgres" `
  -e DB_PASSWORD="sua_senha" `
  -e GOOGLE_WEB_CLIENT_ID="seu-client-id.apps.googleusercontent.com" `
  -e JWT_PRIVATE_KEY_PATH="/run/secrets/app.key" `
  -e JWT_PUBLIC_KEY_PATH="/run/secrets/app.pub" `
  -v D:\simulapronaf\secrets:/run/secrets:ro `
  simulapronaf-api
```

Se estiver no Linux e o PostgreSQL estiver rodando na máquina host, o valor de `DB_URL` pode variar conforme sua rede Docker. Em muitos casos, `host.docker.internal` pode não funcionar por padrão.

Uma alternativa comum é usar o IP da máquina host ou rodar o banco em outro container/rede compartilhada.

#### 3. Verifique a aplicação

A API ficará disponível em:

```text
http://localhost:8080
```

---

### Comandos úteis com Docker

Parar o container:

```bash
docker stop simulapronaf-api
```

Iniciar novamente:

```bash
docker start simulapronaf-api
```

Ver logs:

```bash
docker logs -f simulapronaf-api
```

Remover container:

```bash
docker rm -f simulapronaf-api
```

---

### Observações importantes

- O banco PostgreSQL precisa estar acessível pela aplicação, tanto no modo tradicional quanto no modo Docker.
- O Flyway aplica as migrations automaticamente na inicialização.
- O `.dockerignore` evita enviar arquivos desnecessários para o build da imagem.
- **Nunca versione chaves RSA reais** nem credenciais de banco em produção.

---

## Autor

**Wagner Quadros**  
Gerente Agro + Desenvolvedor Backend  
Profissional com experiência no mercado de crédito rural e desenvolvimento de software, unindo visão de negócio do agronegócio com tecnologia moderna.

[![GitHub](https://img.shields.io/badge/GitHub-wagnerquadros-181717?style=flat-square&logo=github)](https://github.com/wagnerquadros)

---

<div align="center">

Feito com ❤️ para o campo brasileiro 🌱

</div>
