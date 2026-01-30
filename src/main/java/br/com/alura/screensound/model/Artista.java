package br.com.alura.screensound.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade JPA que representa um Artista no banco de dados.
 * 
 * O que é uma Entidade?
 * - É uma classe Java mapeada para uma TABELA no banco de dados
 * - Cada objeto Artista é uma LINHA na tabela
 * - Cada atributo é uma COLUNA na tabela
 * 
 * Relacionamento:
 * - Um artista pode ter VÁRIAS músicas (OneToMany)
 * - Exemplo: Madonna tem "Like a Prayer", "Material Girl", etc.
 * 
 * Exemplo no banco:
 * +----+----------+-------+
 * | id | nome     | tipo  |
 * +----+----------+-------+
 * | 1  | Madonna  | SOLO  |
 * | 2  | Beatles  | BANDA |
 * +----+----------+-------+
 */
@Entity  // Marca esta classe como uma entidade JPA
@Table(name = "artistas")  // Define o nome da tabela no banco
public class Artista {
    
    /**
     * ID do artista (chave primária).
     * 
     * @Id - Define que este campo é a PRIMARY KEY
     * @GeneratedValue - Valor gerado automaticamente
     * IDENTITY - Usa auto incremento (1, 2, 3...)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do artista.
     * 
     * @Column(unique = true) - Garante que não existam dois artistas com o mesmo nome
     * Exemplo: Não pode cadastrar "Madonna" duas vezes
     */
    @Column(unique = true)
    private String nome;

    /**
     * Tipo do artista (SOLO, DUPLA ou BANDA).
     * 
     * @Enumerated(EnumType.STRING) - Salva o enum como texto no banco
     * - STRING: Salva "SOLO", "DUPLA", "BANDA" (legível)
     * - ORDINAL: Salvaria 0, 1, 2 (não recomendado, quebra se mudar ordem)
     * 
     * Por que STRING é melhor?
     * - Mais legível no banco de dados
     * - Não quebra se adicionar novos tipos no meio do enum
     */
    @Enumerated(EnumType.STRING)
    private TipoArtista tipo;

    /**
     * Lista de músicas do artista.
     * 
     * @OneToMany - Relacionamento Um para Muitos
     *   - Um artista (One) tem várias músicas (Many)
     *   - Exemplo: Madonna tem várias músicas
     * 
     * mappedBy = "artista" - Indica que o relacionamento é mapeado pelo campo
     *   "artista" na classe Musica. Ou seja, a classe Musica é a dona da relação.
     * 
     * cascade = CascadeType.ALL - Operações em cascata
     *   - Ao salvar o artista, salva as músicas automaticamente
     *   - Ao deletar o artista, deleta as músicas também
     *   - CUIDADO: Pode deletar dados sem querer!
     * 
     * fetch = FetchType.EAGER - Tipo de carregamento
     *   - EAGER: Carrega as músicas IMEDIATAMENTE ao buscar o artista
     *   - LAZY: Carrega as músicas APENAS quando acessadas (economiza memória)
     *   - Usamos EAGER aqui porque sempre precisamos das músicas
     */
    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Musica> musicas = new ArrayList<>();

    /**
     * Construtor padrão vazio.
     * Obrigatório para o JPA criar objetos via reflection.
     */
    public Artista() {}

    /**
     * Construtor com parâmetros.
     * Facilita a criação de novos artistas.
     * 
     * @param nome - nome do artista
     * @param tipo - tipo do artista (SOLO, DUPLA, BANDA)
     */
    public Artista(String nome, TipoArtista tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    // ========== GETTERS E SETTERS ==========
    // Permitem acessar e modificar os atributos privados

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoArtista getTipo() {
        return tipo;
    }

    public void setTipo(TipoArtista tipo) {
        this.tipo = tipo;
    }

    public List<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<Musica> musicas) {
        this.musicas = musicas;
    }

    /**
     * Método toString personalizado.
     * Define como o objeto será exibido quando impresso.
     * 
     * Exemplo de saída:
     * "Artista: Madonna (SOLO)"
     */
    @Override
    public String toString() {
        return "Artista: " + nome + " (" + tipo + ")";
    }
}
