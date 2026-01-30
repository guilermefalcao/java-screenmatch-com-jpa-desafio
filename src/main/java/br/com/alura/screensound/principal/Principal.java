package br.com.alura.screensound.principal;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.model.Musica;
import br.com.alura.screensound.model.TipoArtista;
import br.com.alura.screensound.repository.ArtistaRepository;
import br.com.alura.screensound.repository.MusicaRepository;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final ArtistaRepository artistaRepository;
    private final MusicaRepository musicaRepository;
    private Scanner leitura = new Scanner(System.in);

    public Principal(ArtistaRepository artistaRepository, MusicaRepository musicaRepository) {
        this.artistaRepository = artistaRepository;
        this.musicaRepository = musicaRepository;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 9) {
            var menu = """
                    *** Screen Sound Músicas ***
                    
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                    4- Buscar músicas por artistas
                    
                    9- Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

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
                case 9:
                    System.out.println("Encerrando a aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarArtistas() {
        var cadastrarNovo = "S";

        while (cadastrarNovo.equalsIgnoreCase("s")) {
            System.out.println("Informe o nome desse artista: ");
            var nome = leitura.nextLine();

            System.out.println("Informe o tipo desse artista: (solo, dupla ou banda)");
            var tipo = leitura.nextLine();

            TipoArtista tipoArtista = TipoArtista.fromString(tipo);

            Artista artista = new Artista(nome, tipoArtista);
            artistaRepository.save(artista);
            System.out.println("Artista cadastrado com sucesso!");

            System.out.println("Cadastrar novo artista? (S/N)");
            cadastrarNovo = leitura.nextLine();
        }
    }

    private void cadastrarMusicas() {
        System.out.println("Cadastrar música de que artista? ");
        var nome = leitura.nextLine();
        Optional<Artista> artista = artistaRepository.findByNomeContainingIgnoreCase(nome);

        if (artista.isPresent()) {
            System.out.println("Informe o título da música: ");
            var nomeMusica = leitura.nextLine();
            Musica musica = new Musica(nomeMusica, artista.get());
            artista.get().getMusicas().add(musica);
            artistaRepository.save(artista.get());
            System.out.println("Música cadastrada com sucesso!");
        } else {
            System.out.println("Artista não encontrado!");
        }
    }

    private void listarMusicas() {
        List<Musica> musicas = musicaRepository.findAll();
        musicas.forEach(System.out::println);
    }

    private void buscarMusicasPorArtista() {
        System.out.println("Buscar músicas de que artista? ");
        var nome = leitura.nextLine();
        Optional<Artista> artista = artistaRepository.findByNomeContainingIgnoreCase(nome);

        if (artista.isPresent()) {
            List<Musica> musicas = musicaRepository.findByArtista(artista.get());
            musicas.forEach(System.out::println);
        } else {
            System.out.println("Artista não encontrado!");
        }
    }

}
