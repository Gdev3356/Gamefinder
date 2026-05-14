package br.com.fiap.game_finder.repository;

import br.com.fiap.game_finder.entities.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    Page<Game> findAll(Pageable pageable);

    Page<Game> findByGenreId(Long genreId, Pageable pageable);

    Page<Game> findByPlatformId(Long platformId, Pageable pageable);

}