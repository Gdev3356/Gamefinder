package br.com.fiap.game_finder.services;

import br.com.fiap.game_finder.entities.Game;
import br.com.fiap.game_finder.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public Page<Game> findAll(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    public Game findById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }

    public Page<Game> findByGenre(Long genreId, Pageable pageable) {
        return gameRepository.findByGenreId(genreId, pageable);
    }

    public Page<Game> findByPlatform(Long platformId, Pageable pageable) {
        return gameRepository.findByPlatformId(platformId, pageable);
    }

    public Game toggleWishlist(Long id) {
        Game game = findById(id);
        game.setInWishlist(!game.isInWishlist());
        return gameRepository.save(game);
    }
}