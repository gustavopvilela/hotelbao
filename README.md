# HotelBão: sistema de gerenciamento de estadias em Spring Boot

## Introdução
HotelBão é um sistema de gerenciamento de hotéis, desenvolvido como uma API baseada nos princípios RESTful. Ele oferece funcionalidades para administração de quartos, clientes e estadias, além de recursos de autenticação e autorização baseados nos papéis (_roles_) do usuário. O projeto também contam com uma interface de linha de comando (CLI) para interagir com a API.

## Funcionalidades principais
O projeto é dividido em módulos que representam as principais entidades do sistema.

### Usuários
- **CRUD de usuários**: criação, leitura, atualização e exclusão de usuários.

- **Autenticação e autorização:**
	
	- Sistema de login com autenticação via OAuth2 e JWT;
	- Dois níveis de acesso: `ROLE_ADMIN` e `ROLE_CLIENTE`.
- **Recuperação de senha**: funcionalidade para solicitar um token de recuperação e redefinir a senha por e-mail.

### Quartos
- **CRUD de quartos**: gerenciamento completo dos quartos do hotel, incluindo descrição, valor da diária e imagem.

### Estadias
- **CRUD de estadias**: lançamento e gerenciamento de estadias, associando um cliente a um quarto em um período de um dia;
- **Relatórios**: listagem de todas as estadias de um cliente; consulta da estadia de maior e menor valor por um cliente; cálculo do valor total gasto por um cliente;
- **Emissão de nota fiscal**: geração de uma nota fiscal com os detalhes das estadias e o valor total.

### Interface de linha de comando (CLI)
- **Menus interativos**: menus distintos para administradores e clientes, permitindo a interação com todas as funcionalidades da API diretamente pelo terminal.

## Tecnologias utilizadas

Para o *backend*, foram utilizados:

- **Java 21**;
- **Spring Boot 3.4.5**;
- **Spring Data JPA**: para persistência dos dados;
- **Spring Security**: para autenticação e autorização com o OAuth2;
- **Spring Mail**: para envio de e-mails de recuperação de senha;

Para o banco de dados:

- **H2 Database**: banco de dados em memória para ambiente de desenvolvimento e testes.

Para a documentação:

-  **Springdoc OpenAPI (*Swagger*)**: para documentação interativa da API.

Build e dependências:

- **Maven**.

## Endpoints da API

A seguir, uma visão geral dos principais endpoints da API. Para a documentação completa e interativa, acesse `/swagger-ui/index.html` com a aplicação em execução.

### Operações padrão de CRUD
![GET](https://img.shields.io/badge//usuario-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) ![POST](https://img.shields.io/badge//usuario-4A4A4A?style=flat-square&label=POST&labelColor=fcd12a&logo=spring&logoColor=4a4a4a&logoSize=auto) ![PUT](https://img.shields.io/badge//usuario/{id}-4A4A4A?style=flat-square&label=PUT&labelColor=4984B8&logo=spring&logoColor=white&logoSize=auto) ![DELETE](https://img.shields.io/badge//usuario/{id}-4A4A4A?style=flat-square&label=DELETE&labelColor=d21f3c&logo=spring&logoColor=white&logoSize=auto) 
![GET](https://img.shields.io/badge//quarto-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) ![POST](https://img.shields.io/badge//quarto-4A4A4A?style=flat-square&label=POST&labelColor=fcd12a&logo=spring&logoColor=4a4a4a&logoSize=auto) ![PUT](https://img.shields.io/badge//quarto/{id}-4A4A4A?style=flat-square&label=PUT&labelColor=4984B8&logo=spring&logoColor=white&logoSize=auto) ![DELETE](https://img.shields.io/badge//quarto/{id}-4A4A4A?style=flat-square&label=DELETE&labelColor=d21f3c&logo=spring&logoColor=white&logoSize=auto)
![GET](https://img.shields.io/badge//estadia-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) ![POST](https://img.shields.io/badge//estadia-4A4A4A?style=flat-square&label=POST&labelColor=fcd12a&logo=spring&logoColor=4a4a4a&logoSize=auto) ![PUT](https://img.shields.io/badge//estadia/{id}-4A4A4A?style=flat-square&label=PUT&labelColor=4984B8&logo=spring&logoColor=white&logoSize=auto) ![DELETE](https://img.shields.io/badge//estadia/{id}-4A4A4A?style=flat-square&label=DELETE&labelColor=d21f3c&logo=spring&logoColor=white&logoSize=auto)

### Operações especiais
- ![GET](https://img.shields.io/badge//usuario/clientes-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) Lista todos os usuários com papel de cliente;
- ![GET](https://img.shields.io/badge//estadia/cliente/{id}-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) Lista estadias por cliente;
- ![GET](https://img.shields.io/badge//estadia/maior/{id}-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) e ![GET](https://img.shields.io/badge//estadia/menor/{id}-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) Retorna a estadia mais cara e mais barata de um cliente;
- ![GET](https://img.shields.io/badge//estadia/total/{id}-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) Calcula o total gasto por um cliente;
- ![GET](https://img.shields.io/badge//estadia/nota--fiscal/{id}-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) Emite a nota fiscal de um cliente;
- ![GET](https://img.shields.io/badge//estadia/{data}/{quarto__id}-4A4A4A?style=flat-square&label=GET&labelColor=2c9f56&logo=spring&logoColor=white&logoSize=auto) Verifica se um quarto já está reservado em determinada data;
- ![POST](https://img.shields.io/badge//auth/recover--token-4A4A4A?style=flat-square&label=POST&labelColor=fcd12a&logo=spring&logoColor=4a4a4a&logoSize=auto) Inicia o processo de recuperação de senha;
- ![POST](https://img.shields.io/badge//auth/new--password-4A4A4A?style=flat-square&label=POST&labelColor=fcd12a&logo=spring&logoColor=4a4a4a&logoSize=auto) Define uma nova senha a partir de um token de recuperação;
- ![POST](https://img.shields.io/badge//oauth2/token-4A4A4A?style=flat-square&label=POST&labelColor=fcd12a&logo=spring&logoColor=4a4a4a&logoSize=auto) Endpoint de autenticação para obter um token JWT;
- ![POST](https://img.shields.io/badge//usuario/signup-4A4A4A?style=flat-square&label=POST&labelColor=fcd12a&logo=spring&logoColor=4a4a4a&logoSize=auto) Permite com que um novo cliente se cadastre;
- ![DELETE](https://img.shields.io/badge//database/clear-4A4A4A?style=flat-square&label=DELETE&labelColor=d21f3c&logo=spring&logoColor=white&logoSize=auto) Limpa todos os dados do banco (é necessário permissão especial de administrador).

## Como executar o projeto
1. **Pré-requisitos**: Java 21+, Maven 3.9.9+;
2. **Clonando o repositório**:
```bash
git clone https://github.com/gustavopvilela/hotelbao.git
cd hotelbao/backend
```
3. **Executando a aplicação**: caso não queira executar diretamente em sua IDE de preferência, execute:
```bash
mvn spring-boot:run
```
4. **API**: estará disponível em `http://localhost:8080`;
5. **Documentação** _**Swagger**_: estará disponível em `http://localhost:8080/swagger-ui/index.html`;
6. **Console do banco H2**: estará disponível em `http://localhost:8080/h2-console`, com a possibilidade de mudar as credenciais em `application-test.properties`;

## Dados iniciais
O sistema é inicializado com um conjunto de dados para facilitar os testes, incluindo:

- **Roles**: `ROLE_ADMIN` e `ROLE_CLIENTE`;
- **Usuários**: diversos usuários dentre administradores e clientes, com senhas pré-definidas;
- **Quartos**: uma variedade de quartos com diferentes descrições e preços;
- **Estadias**: registros de estadias pré-agendadas para os clientes de teste