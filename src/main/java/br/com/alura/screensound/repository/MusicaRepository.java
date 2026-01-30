package br.com.alura.screensound.repository;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.model.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository (Repositório) para a entidade Musica.
 * 
 * O que é um Repository?
 * - Interface que fornece métodos prontos para acessar o banco de dados
 * - NÃO precisa escrever SQL!
 * - Spring Data JPA cria a implementação automaticamente
 * 
 * JpaRepository<Musica, Long>:
 * - Musica: Tipo da entidade gerenciada
 * - Long: Tipo da chave primária (ID)
 */
public interface MusicaRepository extends JpaRepository<Musica, Long> {
    
    /**
     * Método customizado para buscar todas as músicas de um artista específico.
     * 
     * DERIVED QUERY METHOD:
     * O Spring cria a query SQL automaticamente baseado no nome do método!
     * 
     * Decomposição do nome:
     * - findBy: Buscar por
     * - Artista: Campo "artista" da entidade Musica (que é um objeto Artista)
     * 
     * SQL gerado automaticamente:
     * SELECT * FROM musicas WHERE artista_id = ?
     * 
     * Como funciona?
     * 1. Recebe um objeto Artista como parâmetro
     * 2. Busca todas as músicas onde artista_id = artista.getId()
     * 3. Retorna uma lista de músicas
     * 
     * Exemplo de uso:
     * Artista madonna = artistaRepository.findById(1L).get();
     * List<Musica> musicas = musicaRepository.findByArtista(madonna);
     * // Retorna: ["Like a Prayer", "Material Girl", ...]
     * 
     * Por que List<Musica>?
     * - Um artista pode ter VÁRIAS músicas
     * - Retorna uma lista (pode estar vazia se o artista não tiver músicas)
     * 
     * @param artista - objeto Artista para buscar as músicas
     * @return Lista de músicas do artista (vazia se não tiver músicas)
     */
    List<Musica> findByArtista(Artista artista);
    
    /**
     * Métodos herdados do JpaRepository (prontos para usar):
     * 
     * - save(Musica musica) - Salva ou atualiza uma música
     * - findAll() - Busca todas as músicas
     * - findById(Long id) - Busca música por ID
     * - deleteById(Long id) - Deleta música por ID
     * - count() - Conta quantas músicas existem
     * 
     * E muitos outros!
     */
}
