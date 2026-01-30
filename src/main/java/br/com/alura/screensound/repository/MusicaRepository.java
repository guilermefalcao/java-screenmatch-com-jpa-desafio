package br.com.alura.screensound.repository;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.model.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MusicaRepository extends JpaRepository<Musica, Long> {
    List<Musica> findByArtista(Artista artista);
}
