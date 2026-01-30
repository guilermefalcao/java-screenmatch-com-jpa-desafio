package br.com.alura.screensound.principal;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.model.Musica;
import br.com.alura.screensound.model.TipoArtista;
import br.com.alura.screensound.repository.ArtistaRepository;
import br.com.alura.screensound.repository.MusicaRepository;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Classe Principal - Contém a lógica de negócio e o menu interativo da aplicação.
 * 
 * Responsabilidades:
 * - Exibir menu para o usuário
 * - Capturar entradas do usuário
 * - Chamar os repositories para acessar o banco de dados
 * - Exibir resultados na tela
 * 
 * Padrão de projeto:
 * - Esta classe atua como um "Controller" (controlador)
 * - Faz a ponte entre o usuário e o banco de dados
 */
public class Principal {
    
    // Repositories injetados pelo construtor (Dependency Injection)
    private final ArtistaRepository artistaRepository;
    private final MusicaRepository musicaRepository;
    
    // Scanner para ler entradas do usuário via console
    private Scanner leitura = new Scanner(System.in);

    /**
     * Construtor que recebe os repositories.
     * 
     * Injeção de Dependência via Construtor:
     * - Os repositories são passados como parâmetros
     * - NÃO criamos os repositories aqui (new ArtistaRepository())
     * - Quem cria é o Spring (na classe ScreensoundApplication)
     * 
     * Vantagens:
     * - Facilita testes (pode passar repositories mockados)
     * - Desacopla a classe dos repositories
     * - Segue o princípio de Inversão de Dependência (SOLID)
     * 
     * @param artistaRepository - repository para acessar artistas no banco
     * @param musicaRepository - repository para acessar músicas no banco
     */
    public Principal(ArtistaRepository artistaRepository, MusicaRepository musicaRepository) {
        this.artistaRepository = artistaRepository;
        this.musicaRepository = musicaRepository;
    }

    /**
     * Método que exibe o menu principal e processa as opções do usuário.
     * 
     * Fluxo:
     * 1. Exibe o menu
     * 2. Lê a opção do usuário
     * 3. Executa a ação correspondente
     * 4. Repete até o usuário escolher sair (opção 9)
     */
    public void exibeMenu() {
        var opcao = -1;  // Inicializa com -1 para entrar no loop

        // Loop principal - continua até o usuário digitar 9
        while (opcao != 9) {
            // Text Block (""" ... """) - recurso do Java 15+
            // Permite escrever texto multi-linha de forma legível
            var menu = """
                    *** Screen Sound Músicas ***
                    
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                    4- Buscar músicas por artistas
                    5- Buscar músicas por artistas (usando @Query)
                    
                    9- Sair
                    """;

            System.out.println(menu);
            
            // Lê a opção como número inteiro
            opcao = leitura.nextInt();
            
            // Limpa o buffer do Scanner (remove o \n deixado pelo nextInt)
            leitura.nextLine();

            // Switch para executar a ação correspondente
            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtista();
                    break;
                case 5:
                    buscarMusicasPorArtistaComQuery();
                    break;
                case 9:
                    System.out.println("Encerrando a aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    /**
     * Método para cadastrar novos artistas no banco de dados.
     * 
     * Fluxo:
     * 1. Solicita nome do artista
     * 2. Solicita tipo do artista (solo, dupla ou banda)
     * 3. Converte o tipo de String para Enum
     * 4. Cria objeto Artista
     * 5. Salva no banco usando o repository
     * 6. Pergunta se quer cadastrar outro
     * 
     * Conceitos importantes:
     * - Loop while permite cadastrar vários artistas de uma vez
     * - TipoArtista.fromString() converte texto em enum
     * - artistaRepository.save() persiste no banco
     * - equalsIgnoreCase() ignora maiúsculas/minúsculas
     * - Try-catch trata erro de nome duplicado (UNIQUE constraint)
     */
    private void cadastrarArtistas() {
        var cadastrarNovo = "S";  // Inicializa com "S" para entrar no loop

        // Loop - continua enquanto o usuário responder "S" ou "s"
        while (cadastrarNovo.equalsIgnoreCase("s")) {
            System.out.println("Informe o nome desse artista: ");
            var nome = leitura.nextLine();

            System.out.println("Informe o tipo desse artista: (solo, dupla ou banda)");
            var tipo = leitura.nextLine();

            try {
                // Converte a String digitada em um valor do enum TipoArtista
                // Exemplo: "solo" -> TipoArtista.SOLO
                TipoArtista tipoArtista = TipoArtista.fromString(tipo);

                // Cria um novo objeto Artista
                Artista artista = new Artista(nome, tipoArtista);
                
                // Salva o artista no banco de dados
                // O Spring Data JPA gera automaticamente:
                // INSERT INTO artistas (nome, tipo) VALUES (?, ?)
                artistaRepository.save(artista);
                
                System.out.println("Artista cadastrado com sucesso!");
                
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                // Captura erro de nome duplicado (UNIQUE constraint)
                System.out.println("\nERRO: Já existe um artista com o nome '" + nome + "' cadastrado!");
                System.out.println("Por favor, escolha outro nome.\n");
            } catch (IllegalArgumentException e) {
                // Captura erro de tipo inválido
                System.out.println("\nERRO: Tipo de artista inválido!");
                System.out.println("Use: solo, dupla ou banda\n");
            }

            System.out.println("Cadastrar novo artista? (S/N)");
            cadastrarNovo = leitura.nextLine();
        }
    }

    /**
     * Método para cadastrar músicas vinculadas a um artista existente.
     * 
     * Fluxo:
     * 1. Solicita o nome do artista
     * 2. Busca o artista no banco (busca parcial, ignora maiúsculas)
     * 3. Se encontrar o artista:
     *    - Solicita o título da música
     *    - Cria objeto Musica vinculado ao artista
     *    - Adiciona a música na lista do artista
     *    - Salva o artista (cascade salva a música automaticamente)
     * 4. Se não encontrar, exibe mensagem de erro
     * 
     * Conceitos importantes:
     * - Optional<Artista>: Container que pode ou não conter um artista
     * - isPresent(): Verifica se o Optional contém um valor
     * - get(): Obtém o valor do Optional (só use após verificar isPresent())
     * - Cascade: Ao salvar o artista, salva a música automaticamente
     */
    private void cadastrarMusicas() {
        System.out.println("Cadastrar música de que artista? ");
        var nome = leitura.nextLine();
        
        // Busca o artista no banco usando busca parcial (LIKE)
        // Exemplo: "mad" encontra "Madonna"
        // Retorna Optional<Artista> (pode estar vazio se não encontrar)
        Optional<Artista> artista = artistaRepository.findByNomeContainingIgnoreCase(nome);

        // Verifica se encontrou o artista
        if (artista.isPresent()) {
            System.out.println("Informe o título da música: ");
            var nomeMusica = leitura.nextLine();
            
            // Cria nova música vinculada ao artista
            // artista.get() obtém o objeto Artista do Optional
            Musica musica = new Musica(nomeMusica, artista.get());
            
            // Adiciona a música na lista de músicas do artista
            artista.get().getMusicas().add(musica);
            
            // Salva o artista no banco
            // Devido ao cascade = CascadeType.ALL, a música é salva automaticamente!
            // SQL gerado:
            // INSERT INTO musicas (titulo, artista_id) VALUES (?, ?)
            artistaRepository.save(artista.get());
            
            System.out.println("Música cadastrada com sucesso!");
        } else {
            // Artista não encontrado no banco
            System.out.println("Artista não encontrado!");
        }
    }

    /**
     * Método para listar todas as músicas cadastradas no banco.
     * 
     * Fluxo:
     * 1. Busca todas as músicas no banco
     * 2. Exibe cada música na tela
     * 
     * Conceitos importantes:
     * - findAll(): Método herdado do JpaRepository
     *   - Retorna List<Musica> com todas as músicas
     *   - SQL gerado: SELECT * FROM musicas
     * 
     * - forEach(): Método funcional do Java 8+
     *   - Itera sobre cada elemento da lista
     *   - System.out::println é uma referência de método (method reference)
     *   - Equivalente a: musicas.forEach(m -> System.out.println(m))
     * 
     * - toString(): Cada música usa o método toString() personalizado
     *   - Definido na classe Musica
     *   - Formato: "Música: [titulo] - Artista: [nome]"
     */
    private void listarMusicas() {
        // Busca todas as músicas do banco
        List<Musica> musicas = musicaRepository.findAll();
        
        // Exibe cada música (chama toString() automaticamente)
        musicas.forEach(System.out::println);
    }

    /**
     * Método para buscar e listar todas as músicas de um artista específico.
     * 
     * Fluxo:
     * 1. Solicita o nome do artista
     * 2. Busca o artista no banco (busca parcial)
     * 3. Se encontrar o artista:
     *    - Busca todas as músicas desse artista
     *    - Exibe as músicas na tela
     * 4. Se não encontrar, exibe mensagem de erro
     * 
     * Conceitos importantes:
     * - Busca em duas etapas:
     *   1ª) Busca o artista por nome
     *   2ª) Busca as músicas do artista encontrado
     * 
     * - findByArtista(): Método customizado do MusicaRepository
     *   - Recebe um objeto Artista
     *   - Retorna List<Musica> com as músicas desse artista
     *   - SQL gerado: SELECT * FROM musicas WHERE artista_id = ?
     * 
     * - Optional: Garante tratamento seguro de "artista não encontrado"
     */
    private void buscarMusicasPorArtista() {
        System.out.println("Buscar músicas de que artista? ");
        var nome = leitura.nextLine();
        
        // Busca o artista no banco (busca parcial, ignora maiúsculas)
        Optional<Artista> artista = artistaRepository.findByNomeContainingIgnoreCase(nome);

        // Verifica se encontrou o artista
        if (artista.isPresent()) {
            // Busca todas as músicas do artista encontrado
            // artista.get() obtém o objeto Artista do Optional
            List<Musica> musicas = musicaRepository.findByArtista(artista.get());
            
            // Exibe cada música na tela
            musicas.forEach(System.out::println);
        } else {
            // Artista não encontrado
            System.out.println("Artista não encontrado!");
        }
    }
    
    /**
     * Método alternativo usando @Query para buscar músicas por artista.
     * 
     * DIFERENÇA em relação ao método anterior:
     * - Método anterior (buscarMusicasPorArtista):
     *   - Faz 2 queries: 1ª busca artista, 2ª busca músicas
     *   - Usa Derived Query Method
     *   - Mais simples e legível
     * 
     * - Este método (buscarMusicasPorArtistaComQuery):
     *   - Faz 1 query com JOIN
     *   - Usa @Query com JPQL
     *   - Mais eficiente (menos queries ao banco)
     *   - Busca músicas diretamente pelo nome do artista
     * 
     * Quando usar cada um?
     * - Use o anterior para código mais simples e legível
     * - Use este para melhor performance em grandes volumes de dados
     * 
     * Fluxo:
     * 1. Solicita o nome do artista
     * 2. Busca músicas diretamente usando @Query (1 query com JOIN)
     * 3. Exibe as músicas encontradas
     */
    private void buscarMusicasPorArtistaComQuery() {
        System.out.println("Buscar músicas de que artista? (usando @Query)");
        var nome = leitura.nextLine();
        
        // Busca músicas diretamente usando @Query
        // Faz JOIN entre artistas e músicas em uma única query
        List<Musica> musicas = artistaRepository.buscaMusicasPorArtista(nome);
        
        // Verifica se encontrou músicas
        if (musicas.isEmpty()) {
            System.out.println("Nenhuma música encontrada para este artista!");
        } else {
            // Exibe cada música
            musicas.forEach(System.out::println);
        }
    }

}
