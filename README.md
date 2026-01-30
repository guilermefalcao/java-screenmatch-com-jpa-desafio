# 🎵 Screen Sound Músicas

Aplicação Java para gerenciamento de artistas e músicas utilizando Spring Boot e Spring Data JPA.

## 📋 Sobre o Projeto

Screen Sound Músicas é uma aplicação de console que permite cadastrar artistas, suas músicas e realizar consultas no banco de dados PostgreSQL. Desenvolvido como desafio do curso de Spring Data JPA da Alura.

## 🚀 Funcionalidades

- ✅ Cadastrar artistas (solo, dupla ou banda)
- ✅ Cadastrar músicas vinculadas a artistas
- ✅ Listar todas as músicas cadastradas
- ✅ Buscar músicas por artista específico
- ✅ Persistência de dados com PostgreSQL

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.1.1**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Hibernate**

## 📦 Estrutura do Projeto

```
src/main/java/br/com/alura/screensound/
├── model/
│   ├── Artista.java          # Entidade Artista
│   ├── Musica.java           # Entidade Música
│   └── TipoArtista.java      # Enum (SOLO, DUPLA, BANDA)
├── repository/
│   ├── ArtistaRepository.java
│   └── MusicaRepository.java
├── principal/
│   └── Principal.java        # Menu interativo
└── ScreensoundApplication.java
```

## 🗄️ Modelo de Dados

### Relacionamento entre Entidades

- **Artista** `1:N` **Música**
  - Um artista pode ter várias músicas
  - Uma música pertence a um artista

### Tabelas

**artistas**
- `id` (PK)
- `nome` (UNIQUE)
- `tipo` (SOLO, DUPLA, BANDA)

**musicas**
- `id` (PK)
- `titulo`
- `artista_id` (FK)

## ⚙️ Configuração

### Pré-requisitos

- Java 17 ou superior
- Maven
- PostgreSQL instalado e rodando

### Banco de Dados

1. Crie o banco de dados:
```sql
CREATE DATABASE alura_musicas;
```

2. Configure as credenciais no arquivo `.env`:
```properties
DB_URL=jdbc:postgresql://localhost:5433/alura_musicas
DB_USERNAME=postgres
DB_PASSWORD=sua_senha
```

### Instalação

1. Clone o repositório:
```bash
git clone https://github.com/guilermefalcao/java-screenmatch-com-jpa-desafio.git
cd java-screenmatch-com-jpa-desafio
```

2. Execute a aplicação:
```bash
mvn clean install
mvn spring-boot:run
```

## 🎮 Como Usar

Ao executar a aplicação, você verá o menu:

```
*** Screen Sound Músicas ***

1- Cadastrar artistas
2- Cadastrar músicas
3- Listar músicas
4- Buscar músicas por artistas
5- Buscar músicas por artistas (usando @Query)

9- Sair
```

### Exemplo de Uso

1. **Cadastrar um artista:**
   - Escolha opção `1`
   - Informe o nome: `Madonna`
   - Informe o tipo: `solo`

2. **Cadastrar uma música:**
   - Escolha opção `2`
   - Informe o artista: `Madonna`
   - Informe o título: `Like a Prayer`

3. **Listar músicas:**
   - Escolha opção `3`
   - Visualize todas as músicas cadastradas

4. **Buscar músicas por artista (Derived Query):**
   - Escolha opção `4`
   - Informe o nome do artista
   - Usa 2 queries ao banco

5. **Buscar músicas por artista (@Query):**
   - Escolha opção `5`
   - Informe o nome do artista
   - Usa 1 query com JOIN (mais eficiente)

## 🔒 Segurança

- Arquivo `.env` contém credenciais sensíveis
- `.env` está incluído no `.gitignore`
- Nunca faça commit de senhas ou chaves de API

## 📚 Aprendizados

### Mapeamento JPA
- Mapeamento de entidades com anotações JPA (@Entity, @Table, @Id, @GeneratedValue)
- Mapeamento de Enums com @Enumerated(EnumType.STRING)
- Relacionamentos OneToMany e ManyToOne entre entidades
- Uso de Cascade e FetchType para controlar operações e carregamento

### Repositories e Consultas
- Criação de Repositories com Spring Data JPA
- Derived Query Methods - queries automáticas baseadas no nome do método
- JPQL com @Query para consultas customizadas
- Comparação entre Derived Queries e @Query

### Tratamento de Dados
- Uso de Optional para evitar NullPointerException
- Tratamento de exceções (DataIntegrityViolationException)
- Validação de dados com constraints (UNIQUE)

### Conceitos Avançados
- Injeção de Dependências com Spring
- Métodos estáticos para conversão de tipos
- Programação funcional (forEach, method reference)

## 👨‍💻 Autor

**Guilherme Falcão**

- GitHub: [@guilermefalcao](https://github.com/guilermefalcao)

## 📄 Licença

Este projeto foi desenvolvido como parte do curso de Spring Data JPA da Alura.

---

⭐ Se este projeto te ajudou, deixe uma estrela!
