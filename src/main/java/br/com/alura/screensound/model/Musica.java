package br.com.alura.screensound.model;

import jakarta.persistence.*;

/**
 * Entidade JPA que representa uma Música no banco de dados.
 * 
 * O que é uma Entidade?
 * - É uma classe Java que será mapeada para uma TABELA no banco de dados
 * - Cada objeto Musica representa uma LINHA na tabela
 * - Cada atributo representa uma COLUNA na tabela
 * 
 * Relacionamento:
 * - Uma música pertence a UM artista (ManyToOne)
 * - Várias músicas podem pertencer ao mesmo artista
 * 
 * Exemplo no banco:
 * +----+------------------+-------------+
 * | id | titulo           | artista_id  |
 * +----+------------------+-------------+
 * | 1  | Like a Prayer    | 1           |
 * | 2  | Material Girl    | 1           |
 * +----+------------------+-------------+
 */
@Entity  // Marca esta classe como uma entidade JPA
@Table(name = "musicas")  // Define o nome da tabela no banco de dados
public class Musica {
    
    /**
     * ID da música (chave primária).
     * 
     * @Id - Define que este campo é a chave primária (PRIMARY KEY)
     * @GeneratedValue - O valor é gerado automaticamente pelo banco
     * IDENTITY - Usa auto incremento do banco (1, 2, 3, 4...)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título da música.
     * Será uma coluna VARCHAR no banco de dados.
     */
    private String titulo;

    /**
     * Artista que canta esta música.
     * 
     * @ManyToOne - Relacionamento Muitos para Um
     *   - Várias músicas (Many) pertencem a um artista (One)
     *   - Exemplo: Madonna tem várias músicas
     * 
     * @JoinColumn - Define a coluna de junção (Foreign Key)
     *   - name = "artista_id" - Nome da coluna FK no banco
     *   - Esta coluna guarda o ID do artista
     */
    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

    /**
     * Construtor padrão vazio.
     * Obrigatório para o JPA criar objetos via reflection.
     */
    public Musica() {}

    /**
     * Construtor com parâmetros.
     * Facilita a criação de novas músicas.
     * 
     * @param titulo - título da música
     * @param artista - artista que canta a música
     */
    public Musica(String titulo, Artista artista) {
        this.titulo = titulo;
        this.artista = artista;
    }

    // ========== GETTERS E SETTERS ==========
    // Permitem acessar e modificar os atributos privados

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    /**
     * Método toString personalizado.
     * Define como o objeto será exibido quando impresso.
     * 
     * Exemplo de saída:
     * "Música: Like a Prayer - Artista: Madonna"
     */
    @Override
    public String toString() {
        return "Música: " + titulo + " - Artista: " + artista.getNome();
    }
}
