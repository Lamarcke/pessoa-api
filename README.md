# Pessoa API

Uma API para criar, editar e excluir pessoas, escrita em Java usando Spring Boot.

## Funções

- Criar uma pessoa
- Editar uma pessoa
- Excluir uma pessoa
- Listar todas as pessoas
- Listar uma pessoa pelo ID
- Pesquisar pessoas pelo nome
- Pesquisar pessoas usando parte do endereço (logradouro, cidade, cep)

## Tecnologias

- JDK 17
- Spring Boot
- Spring Data JPA
- H2 (banco de dados em memória)
- Lombok

## Desenvolvimento

Basta clonar este repositório e executar em sua IDE de preferência.
`git clone https://github.com/Lamarcke/pessoa-api.git`

## Testes

Para executar os testes, basta executar o comando abaixo:
`mvn test`
Ou utilizar sua IDE preferida para executar os testes.

## Deployment

O repostório contém um arquivo Dockerfile que pode ser utilizado para hospedar a aplicação
em um serviço de sua preferência. (Fly.io, Heroku, etc...)

Também é possível hospedar a aplicação manualmente em um VPS.

## Autor

Cássio Lamarck Silva Freitas
Desenvolvedor Fullstack Junior

