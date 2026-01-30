# 📚 Screen Sound Músicas - Guia Didático

Este documento explica de forma didática os conceitos aplicados no projeto Screen Sound Músicas.

## 📖 Índice

1. [Estrutura do Projeto](#estrutura-do-projeto)
2. [Entidades JPA](#entidades-jpa)
3. [Relacionamentos](#relacionamentos)
4. [Repositories](#repositories)
5. [Configurações](#configurações)

---

## 🏗️ Estrutura do Projeto

### Arquitetura em Camadas

```
📦 screensound
├── 📂 model          → Entidades (classes que viram tabelas)
├── 📂 repository     → Acesso ao banco de dados
├── 📂 principal      → Lógica de negócio e menu
└── 📂 service        → Serviços externos (APIs)
```

**Por que essa estrutura?**
- **Separação de responsabilidades**: cada camada tem uma função específica
- **Facilita manutenção**: mudanças em uma camada não afetam as outras
- **Reutilização de código**: repositories podem ser usados em diferentes partes

---

## 🎯 Entidades JPA

### O que é uma Entidade?

Uma **entidade** é uma classe Java que representa uma **tabela no banco de dados**.

### Exemplo: Artista.java

```java
@Entity                              // ← Marca como entidade JPA
@Table(name = "artistas")            // ← Nome da tabela no banco
public class Artista {
    
    @Id                              // ← Chave primária
    @GeneratedValue(strategy = IDENTITY) // ← Auto incremento
    private Long id;
    
    @Column(unique = true)           // ← Coluna única (não pode repetir)
    private String nome;
    
    @Enumerated(EnumType.STRING)     // ← Salva o enum como texto
    private TipoArtista tipo;
    
    @OneToMany(mappedBy = "artista", // ← Relacionamento 1:N
               cascade = CascadeType.ALL,
               fetch = FetchType.EAGER)
    private List<Musica> musicas;
}
```

### Anotações Importantes

| Anotação | Função |
|----------|--------|
| `@Entity` | Define que a classe é uma entidade JPA |
| `@Table` | Define o nome da tabela no banco |
| `@Id` | Define a chave primária |
| `@GeneratedValue` | Define como o ID será gerado (auto incremento) |
| `@Column` | Configura propriedades da coluna |
| `@Enumerated` | Define como o enum será salvo (STRING ou ORDINAL) |

---

## 🔗 Relacionamentos

### OneToMany (Um para Muitos)

**Conceito:** Um artista pode ter várias músicas.

```java
// Na classe Artista
@OneToMany(mappedBy = "artista",     // ← Campo na classe Musica
           cascade = CascadeType.ALL, // ← Operações em cascata
           fetch = FetchType.EAGER)   // ← Carrega músicas junto
private List<Musica> musicas;
```

**O que significa cada parâmetro?**

- `mappedBy = "artista"`: O relacionamento é mapeado pelo campo `artista` na classe `Musica`
- `cascade = CascadeType.ALL`: Ao salvar/deletar artista, salva/deleta músicas também
- `fetch = FetchType.EAGER`: Carrega as músicas automaticamente ao buscar o artista

### ManyToOne (Muitos para Um)

**Conceito:** Várias músicas pertencem a um artista.

```java
// Na classe Musica
@ManyToOne                           // ← Muitas músicas para um artista
@JoinColumn(name = "artista_id")     // ← Nome da coluna FK no banco
private Artista artista;
```

**Resultado no Banco:**

```
Tabela: musicas
+----+------------------+-------------+
| id | titulo           | artista_id  |
+----+------------------+-------------+
| 1  | Like a Prayer    | 1           |
| 2  | Material Girl    | 1           |
+----+------------------+-------------+
```

### Diagrama do Relacionamento

```
┌─────────────────┐         ┌─────────────────┐
│    Artista      │ 1     N │     Musica      │
├─────────────────┤─────────├─────────────────┤
│ id (PK)         │         │ id (PK)         │
│ nome            │         │ titulo          │
│ tipo            │         │ artista_id (FK) │
└─────────────────┘         └─────────────────┘
```

---

## 🗂️ Repositories

### O que é um Repository?

Um **Repository** é uma interface que fornece métodos prontos para acessar o banco de dados, **sem precisar escrever SQL**.

### Exemplo: ArtistaRepository.java

```java
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    
    // Método customizado - Spring cria a query automaticamente!
    Optional<Artista> findByNomeContainingIgnoreCase(String nome);
}
```

### Métodos Herdados do JpaRepository

| Método | Função |
|--------|--------|
| `save(artista)` | Salva ou atualiza um artista |
| `findAll()` | Busca todos os artistas |
| `findById(id)` | Busca artista por ID |
| `deleteById(id)` | Deleta artista por ID |
| `count()` | Conta quantos artistas existem |

### Derived Query Methods

O Spring Data JPA cria queries automaticamente baseado no **nome do método**:

```java
// Busca por nome (ignora maiúsculas/minúsculas)
findByNomeContainingIgnoreCase(String nome)

// SQL gerado automaticamente:
// SELECT * FROM artistas WHERE LOWER(nome) LIKE LOWER('%nome%')
```

**Palavras-chave:**

- `findBy`: Buscar por
- `Containing`: Contém (LIKE)
- `IgnoreCase`: Ignora maiúsculas/minúsculas
- `And`: E lógico
- `Or`: Ou lógico

### Queries Customizadas com @Query

Para queries mais complexas, use @Query com JPQL:

```java
@Query("SELECT m FROM Artista a JOIN a.musicas m WHERE a.nome ILIKE %:nome%")
List<Musica> buscaMusicasPorArtista(String nome);
```

**Diferenças:**

| Abordagem | Vantagens | Desvantagens |
|-----------|-----------|-------------|
| Derived Query | Simples, sem SQL manual | Limitado a queries simples |
| @Query | Controle total, queries complexas | Precisa escrever JPQL manualmente |

**Quando usar cada um?**
- Derived Query: Queries simples (1-2 campos)
- @Query: Queries complexas, JOINs, otimização

---

## ⚙️ Configurações

### application.properties

```properties
# URL de conexão
spring.datasource.url=jdbc:postgresql://localhost:5433/alura_musicas

# Credenciais
spring.datasource.username=postgres
spring.datasource.password=1234

# DDL Auto
spring.jpa.hibernate.ddl-auto=update
```

### O que é DDL Auto?

Controla como o Hibernate gerencia o schema do banco:

| Valor | Comportamento |
|-------|---------------|
| `create` | **APAGA** e recria as tabelas (PERDE DADOS!) |
| `create-drop` | Cria ao iniciar, apaga ao fechar |
| `update` | **Atualiza** as tabelas (adiciona colunas, não apaga) |
| `validate` | Apenas valida se o schema está correto |
| `none` | Não faz nada |

**Recomendação:**
- Desenvolvimento: `update`
- Produção: `validate` ou `none`

---

## 🎓 Conceitos Importantes

### 1. Cascade (Operações em Cascata)

```java
@OneToMany(cascade = CascadeType.ALL)
```

**O que faz?**
- Ao salvar um artista, salva suas músicas automaticamente
- Ao deletar um artista, deleta suas músicas também

**Tipos de Cascade:**
- `ALL`: Todas as operações
- `PERSIST`: Apenas salvar
- `REMOVE`: Apenas deletar
- `MERGE`: Apenas atualizar

### 2. Fetch Type (Tipo de Carregamento)

```java
@OneToMany(fetch = FetchType.EAGER)
```

**Diferença:**

| Tipo | Comportamento |
|------|---------------|
| `EAGER` | Carrega as músicas **imediatamente** ao buscar o artista |
| `LAZY` | Carrega as músicas **apenas quando acessadas** |

**Quando usar?**
- `EAGER`: Quando sempre precisa das músicas
- `LAZY`: Quando raramente precisa das músicas (economiza memória)

### 3. Enum em Entidades

```java
@Enumerated(EnumType.STRING)
private TipoArtista tipo;
```

**Diferença:**

| Tipo | Salva no Banco | Vantagem | Desvantagem |
|------|----------------|----------|-------------|
| `STRING` | "SOLO", "BANDA" | Legível, não quebra se mudar ordem | Ocupa mais espaço |
| `ORDINAL` | 0, 1, 2 | Ocupa menos espaço | Quebra se mudar ordem do enum |

**Recomendação:** Use `STRING` sempre!

---

## 🔍 Fluxo de Execução

### Cadastrar um Artista

```
1. Usuário digita nome e tipo
   ↓
2. Cria objeto Artista
   ↓
3. artistaRepository.save(artista)
   ↓
4. Hibernate gera SQL INSERT
   ↓
5. PostgreSQL salva no banco
   ↓
6. Retorna artista com ID gerado
```

### Buscar Músicas por Artista

```
1. Usuário digita nome do artista
   ↓
2. artistaRepository.findByNomeContainingIgnoreCase(nome)
   ↓
3. Hibernate gera SQL SELECT com LIKE
   ↓
4. PostgreSQL retorna o artista
   ↓
5. musicaRepository.findByArtista(artista)
   ↓
6. Hibernate gera SQL SELECT com JOIN
   ↓
7. PostgreSQL retorna as músicas
   ↓
8. Exibe na tela
```

---

## 💡 Dicas e Boas Práticas

### ✅ Faça

- Use `@Column(unique = true)` para campos que não podem repetir
- Use `EnumType.STRING` para enums
- Use `Optional<>` em métodos que podem não encontrar resultado
- Configure `.env` para credenciais sensíveis
- Use `cascade` com cuidado (pode deletar dados sem querer)

### ❌ Evite

- Usar `ddl-auto=create` em produção (apaga tudo!)
- Usar `EnumType.ORDINAL` (quebra se mudar ordem)
- Fazer commit do arquivo `.env`
- Usar `FetchType.EAGER` em tudo (pode causar lentidão)

---

## 🎯 Resumo dos Aprendizados

✅ Mapear entidades JPA com anotações  
✅ Mapear Enums com @Enumerated(EnumType.STRING)  
✅ Criar relacionamentos OneToMany e ManyToOne  
✅ Usar Cascade e FetchType corretamente  
✅ Criar repositories com Spring Data JPA  
✅ Usar Derived Query Methods  
✅ Criar queries customizadas com @Query e JPQL  
✅ Comparar Derived Queries vs @Query  
✅ Configurar PostgreSQL com Spring Boot  
✅ Entender Optional e tratamento de exceções  
✅ Proteger credenciais com .env  
✅ Usar métodos estáticos para conversão de tipos  
✅ Aplicar programação funcional (forEach, method reference)  

---

📌 **Próximos Passos:**

- Adicionar validações com Bean Validation
- Criar queries customizadas com @Query
- Implementar paginação e ordenação
- Adicionar testes unitários
- Criar API REST com Spring Web

---

💬 **Dúvidas?** Consulte a documentação oficial:
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate](https://hibernate.org/orm/documentation/)
