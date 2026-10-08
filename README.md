# ConstruCerta

Sistema de **login seguro** (cadastro, autenticacao e autorizacao por
perfil) desenvolvido com **Java 17, Spring Boot 3, Spring Security,
Thymeleaf e MongoDB Atlas**, como atividade individual da disciplina
Aplicativos Web.

O nucleo de autenticacao foi pensado para ser **generico e reutilizavel**:
nao depende do tema "materiais de construcao" e pode ser adaptado para
outro projeto no futuro trocando apenas o enum `Role` e o tema visual.

## Funcionalidades

- Cadastro de usuarios com validacao de dados (nome, e-mail, senha) e
  hash de senha com **BCrypt** (nunca em texto puro).
- Login e logout com **Spring Security** (formulario customizado).
- **3 perfis de acesso**: `CLIENTE`, `VENDEDOR` e `ADMIN`.
- Controle de acesso por rota (`/cliente/**`, `/vendedor/**`,
  `/admin/**`) **e** por metodo (`@PreAuthorize` em `UserService` e
  `ProdutoService`), como defesa em profundidade.
- Painel administrativo para o `ADMIN` promover/revogar perfis e
  ativar/desativar contas.
- Catalogo basico de materiais de construcao: `VENDEDOR`/`ADMIN`
  cadastram produtos (nome, categoria, unidade, preco, estoque) e
  `CLIENTE` consulta o catalogo.
- Usuarios **e sessoes HTTP** armazenados no MongoDB Atlas (via
  Spring Session Data MongoDB).
- Tema visual (cores, nome exibido) **configuravel** por
  `application.properties` / variaveis de ambiente, sem precisar
  tocar em HTML, CSS ou na logica de negocio.

## Stack

| Camada          | Tecnologia                                   |
|-----------------|-----------------------------------------------|
| Linguagem       | Java 17                                        |
| Framework       | Spring Boot 3.3.4                              |
| Seguranca       | Spring Security 6 (BCrypt, CSRF habilitado)    |
| View            | Thymeleaf + thymeleaf-extras-springsecurity6   |
| Banco de dados  | MongoDB Atlas (Spring Data MongoDB)            |
| Sessao HTTP     | Spring Session Data MongoDB                    |
| Build           | Maven                                          |

## Estrutura do projeto

```
src/main/java/com/construcerta/
├── ConstruCertaApplication.java    # ponto de entrada
├── config/
│   ├── SecurityConfig.java         # autenticacao/autorizacao (Spring Security)
│   ├── MongoConfig.java            # auditoria Mongo + sessao Mongo
│   ├── ThemeProperties.java        # tema visual (application.properties: app.theme.*)
│   ├── GlobalModelAttributes.java  # injeta o tema em todas as telas
│   └── DataInitializer.java        # cria o ADMIN inicial no primeiro boot
├── model/
│   ├── User.java                   # documento MongoDB (colecao "users")
│   ├── Role.java                   # enum dos 3 perfis (CLIENTE, VENDEDOR, ADMIN)
│   └── Produto.java                # documento MongoDB (colecao "produtos")
├── repository/
│   ├── UserRepository.java         # Spring Data MongoDB
│   └── ProdutoRepository.java
├── security/
│   ├── CustomUserDetails.java      # adapta User -> UserDetails
│   └── CustomUserDetailsService.java
├── service/
│   ├── UserService.java            # regras de negocio (cadastro, promocao de perfil)
│   ├── ProdutoService.java         # regras de negocio do catalogo
│   └── exception/UsuarioJaExisteException.java
├── dto/
│   ├── RegisterForm.java           # formulario de cadastro (com validacao)
│   ├── AdminUserUpdateForm.java    # formulario do painel admin
│   └── ProdutoForm.java            # formulario de cadastro/edicao de produto
└── controller/
    ├── HomeController.java
    ├── AuthController.java         # /login, /register
    ├── ClienteController.java      # /cliente/**
    ├── VendedorController.java     # /vendedor/**
    └── AdminController.java        # /admin/**

src/main/resources/
├── application.properties
├── templates/                      # Thymeleaf (design separado da logica)
│   ├── fragments/head.html         # <head> + variaveis CSS do tema
│   ├── fragments/navbar.html       # menu, varia conforme o perfil logado
│   ├── auth/login.html, auth/register.html
│   ├── cliente/dashboard.html, vendedor/dashboard.html, admin/dashboard.html
│   └── error/403.html, error/404.html
└── static/css/theme.css            # cores lidas via variaveis CSS (tema laranja/terracota)
```

## Como funciona a integracao com o MongoDB Atlas

1. A aplicacao le a connection string do Atlas pela variavel de
   ambiente `MONGODB_URI` (propriedade `spring.data.mongodb.uri` em
   `application.properties`). **Nenhuma credencial fica no codigo-fonte.**
2. `UserRepository` (Spring Data MongoDB) persiste os usuarios na
   colecao `users`, com indices unicos em `username` e `email`
   (evita duplicidade direto no banco, alem da validacao na aplicacao).
3. `ProdutoRepository` persiste os itens do catalogo na colecao
   `produtos`.
4. `spring-session-data-mongodb` grava as sessoes HTTP autenticadas na
   colecao `sessions` do mesmo cluster Atlas - ou seja, o login
   sobrevive a um restart da aplicacao e pode ser compartilhado entre
   instancias, se no futuro o projeto escalar para mais de uma instancia.
5. No primeiro boot, se nao existir nenhum usuario `ADMIN` no banco,
   `DataInitializer` cria um automaticamente (credenciais tambem via
   variavel de ambiente - veja a secao de configuracao abaixo).

## Pre-requisitos

- JDK 17 instalado (`java -version`).
- Maven 3.9+ (ou use o wrapper, se adicionar um).
- Uma conta no [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
  (o plano gratuito M0 e suficiente).

## Configurando o MongoDB Atlas

1. Crie um cluster gratuito (M0) no Atlas.
2. Em **Database Access**, crie um usuario de banco com senha (nao e o
   seu login do Atlas, e um usuario especifico para a aplicacao se
   conectar).
3. Em **Network Access**, libere o IP da sua maquina (ou,
   para testes, `0.0.0.0/0` - menos seguro, evite em producao).
4. Em **Database > Connect > Drivers**, copie a connection string no
   formato:
   ```
   mongodb+srv://<usuario>:<senha>@<cluster>.mongodb.net/construcerta?retryWrites=true&w=majority
   ```
5. Defina essa string na variavel de ambiente `MONGODB_URI` (veja abaixo).

## Variaveis de ambiente

| Variavel          | Obrigatoria | Descricao                                             |
|--------------------|-------------|---------------------------------------------------------|
| `MONGODB_URI`       | sim (producao) | Connection string do MongoDB Atlas                   |
| `ADMIN_USERNAME`    | nao         | Usuario do ADMIN inicial (padrao: `admin`)              |
| `ADMIN_EMAIL`       | nao         | E-mail do ADMIN inicial                                 |
| `ADMIN_PASSWORD`    | nao         | Senha do ADMIN inicial (**troque apos o primeiro login**) |
| `SERVER_PORT`       | nao         | Porta HTTP (padrao: `8080`)                              |
| `THEME_PRIMARY_COLOR` e demais `THEME_*` | nao | Sobrescrevem o tema visual sem alterar codigo |

### No Windows (PowerShell)

```powershell
$env:MONGODB_URI = "mongodb+srv://usuario:senha@cluster.mongodb.net/construcerta?retryWrites=true&w=majority"
$env:ADMIN_PASSWORD = "SenhaForteDoAdmin123!"
```

### No Linux/macOS (bash)

```bash
export MONGODB_URI="mongodb+srv://usuario:senha@cluster.mongodb.net/construcerta?retryWrites=true&w=majority"
export ADMIN_PASSWORD="SenhaForteDoAdmin123!"
```

## Executando o sistema localmente

```bash
# 1. configure as variaveis de ambiente (secao acima)
# 2. compile e rode os testes
mvn clean verify

# 3. rode a aplicacao
mvn spring-boot:run
```

A aplicacao sobe em `http://localhost:8080`. Na primeira execucao,
o `DataInitializer` cria o usuario ADMIN (veja o console/log para a
mensagem de confirmacao).

Fluxo sugerido para testar os 3 perfis:

1. Acesse `http://localhost:8080/register` e crie um usuario comum
   (ele entra automaticamente como `CLIENTE`).
2. Faca login com o ADMIN (`ADMIN_USERNAME`/`ADMIN_PASSWORD`) e acesse
   `/admin/dashboard` para promover o usuario recem-criado a
   `VENDEDOR` (ou `ADMIN`), marcando o checkbox correspondente e
   clicando em "Salvar".
3. Faca login como `VENDEDOR` e cadastre um produto em
   `/vendedor/dashboard`.
4. Faca login como `CLIENTE` e veja o produto cadastrado em
   `/cliente/dashboard`.
5. Tente acessar `/admin/dashboard` logado como `CLIENTE` - deve cair
   na pagina 403.

## Testes automatizados

```bash
mvn test
```

Inclui testes de:
- Existencia dos 3 perfis minimos e do prefixo `ROLE_` usado pelo Spring Security.
- Hash de senha (BCrypt): garante que a senha nunca e igual ao hash
  armazenado e que senhas incorretas nao validam.

> Nota: os testes acima nao dependem de uma conexao real com o
> MongoDB. Para um teste de integracao completo (subindo o contexto
> Spring por inteiro), aponte `MONGODB_URI` para um cluster de teste
> no Atlas (ou um MongoDB local) antes de rodar `mvn spring-boot:run`.

## Como trocar o tema visual (sem tocar no codigo)

Edite (ou sobrescreva por variavel de ambiente) os valores
`app.theme.*` em `application.properties`:

```properties
app.theme.name=Novo Tema
app.theme.primary-color=#1f6f54
app.theme.secondary-color=#3fa884
app.theme.logo-text=MeuApp
```

Todas as telas leem essas cores via variaveis CSS (`--color-primary`,
etc.), definidas em `templates/fragments/head.html` e usadas em
`static/css/theme.css`.

## Reaproveitando este login em outro projeto

1. Troque os valores do enum `Role`.
2. Ajuste as regras de rota em `SecurityConfig` para os novos nomes
   de perfil.
3. Troque o tema visual via `application.properties`.
4. O restante (hash de senha, cadastro, login, logout, sessao no
   MongoDB, painel administrativo) continua funcionando sem mudanca
   estrutural, porque nenhuma dessas classes depende do dominio
   "materiais de construcao".

## Documentacao

A documentacao completa (estrutura do sistema, integracao com o
MongoDB Atlas e principais decisoes de design), em formato PDF e
normas ABNT, esta em [`docs/documentacao-construcerta.pdf`](docs/documentacao-construcerta.pdf).

## Gitflow

Este repositorio segue o fluxo de gitflow pedido pela atividade
(branch `develop`, branches `feature/*` e Pull Requests para
`develop`). O passo a passo exato dos comandos usados para publicar
este projeto esta em [`docs/guia-gitflow.md`](docs/guia-gitflow.md).
