package br.com.alura.screensound.repository;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.model.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

/**
 * Repository (Repositório) para a entidade Artista.
 * 
 * O que é um Repository?
 * - É uma interface que fornece métodos prontos para acessar o banco de dados
 * - NÃO precisa escrever SQL manualmente!
 * - O Spring Data JPA cria a implementação automaticamente
 * 
 * Por que usar Repository?
 * - Evita código repetitivo (boilerplate)
 * - Métodos prontos: save(), findAll(), findById(), delete(), etc.
 * - Pode criar métodos customizados seguindo convenções de nomenclatura
 * 
 * JpaRepository<Artista, Long>:
 * - Artista: Tipo da entidade que este repository gerencia
 * - Long: Tipo da chave primária (ID) da entidade
 */
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    
    /**
     * Método customizado para buscar artista por nome (parcial e case-insensitive).
     * 
     * DERIVED QUERY METHOD (Método de Consulta Derivada):
     * O Spring Data JPA cria a query SQL automaticamente baseado no NOME do método!
     * 
     * Decomposição do nome do método:
     * - findBy: Buscar por
     * - Nome: Campo "nome" da entidade Artista
     * - Containing: Contém (usa LIKE no SQL)
     * - IgnoreCase: Ignora maiúsculas/minúsculas
     * 
     * SQL gerado automaticamente:
     * SELECT * FROM artistas WHERE LOWER(nome) LIKE LOWER('%valor%')
     * 
     * Exemplo de uso:
     * - Buscar "mad" encontra "Madonna"
     * - Buscar "BEAT" encontra "Beatles"
     * - Buscar "cold" encontra "Coldplay"
     * 
     * Por que Optional<Artista>?
     * - Optional é um container que pode ou não conter um valor
     * - Evita NullPointerException
     * - Força o programador a tratar o caso de "não encontrado"
     * 
     * Exemplo de uso no código:
     * Optional<Artista> resultado = repository.findByNomeContainingIgnoreCase("madonna");
     * if (resultado.isPresent()) {
     *     Artista artista = resultado.get();
     *     System.out.println(artista.getNome());
     * } else {
     *     System.out.println("Artista não encontrado");
     * }
     * 
     * @param nome - nome ou parte do nome do artista a ser buscado
     * @return Optional contendo o artista encontrado, ou vazio se não encontrar
     */
    Optional<Artista> findByNomeContainingIgnoreCase(String nome);
    
    /**
     * Método customizado com @Query para buscar músicas por nome do artista.
     * 
     * ABORDAGEM COM @Query (Query Manual com JPQL):
     * Você escreve a query manualmente usando JPQL (Java Persistence Query Language).
     * 
     * JPQL vs SQL:
     * - JPQL usa NOMES DE CLASSES e ATRIBUTOS (Artista, musicas)
     * - SQL usa NOMES DE TABELAS e COLUNAS (artistas, musicas)
     * 
     * Decomposição da query:
     * - SELECT m: Seleciona as músicas (não o artista)
     * - FROM Artista a: Da tabela Artista (alias 'a')
     * - JOIN a.musicas m: Faz JOIN com a lista de músicas do artista (alias 'm')
     * - WHERE a.nome ILIKE %:nome%: Filtra por nome do artista (case-insensitive)
     * 
     * ILIKE vs LIKE:
     * - ILIKE: Case-insensitive (PostgreSQL)
     * - LIKE: Case-sensitive
     * 
     * :nome - Parâmetro nomeado (substitui pelo valor passado no método)
     * 
     * SQL gerado (aproximado):
     * SELECT m.* FROM artistas a 
     * JOIN musicas m ON a.id = m.artista_id 
     * WHERE a.nome ILIKE '%valor%'
     * 
     * VANTAGENS desta abordagem:
     * - Busca músicas diretamente (sem precisar buscar o artista primeiro)
     * - Apenas 1 query ao banco (mais eficiente)
     * - Mais controle sobre a query
     * 
     * DESVANTAGENS:
     * - Precisa escrever JPQL manualmente
     * - Mais propenso a erros de sintaxe
     * - Menos legível para iniciantes
     * 
     * Exemplo de uso:
     * List<Musica> musicas = repository.buscaMusicasPorArtista("madonna");
     * musicas.forEach(System.out::println);
     * 
     * @param nome - nome ou parte do nome do artista
     * @return Lista de músicas do artista (vazia se não encontrar)
     */
    @Query("SELECT m FROM Artista a JOIN a.musicas m WHERE a.nome ILIKE %:nome%")
    List<Musica> buscaMusicasPorArtista(String nome);
    
    /**
     * Métodos herdados do JpaRepository (não precisam ser declarados):
     * 
     * - save(Artista artista) - Salva ou atualiza um artista
     * - findAll() - Busca todos os artistas
     * - findById(Long id) - Busca artista por ID
     * - deleteById(Long id) - Deleta artista por ID
     * - count() - Conta quantos artistas existem
     * - existsById(Long id) - Verifica se existe artista com este ID
     * 
     * E muitos outros métodos prontos para usar!
     */
}
