package br.com.fiap.game_finder.controllers;

import br.com.fiap.game_finder.entities.Game;
import br.com.fiap.game_finder.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final PagedResourcesAssembler<Game> pagedAssembler;

    @GetMapping
    public PagedModel<EntityModel<Game>> getAll(
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Game> page = gameService.findAll(pageable);
        return pagedAssembler.toModel(page, game -> buildGameModel(game));
    }

    @GetMapping("/{id}")
    public EntityModel<Game> getById(@PathVariable Long id) {
        Game game = gameService.findById(id);
        return buildGameModel(game);
    }

    @GetMapping("/genres/{genreId}")
    public PagedModel<EntityModel<Game>> getByGenre(
            @PathVariable Long genreId,
            @PageableDefault(size = 6, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Game> page = gameService.findByGenre(genreId, pageable);
        return pagedAssembler.toModel(page, this::buildGameModel);
    }

    @GetMapping("/platforms/{platformId}")
    public PagedModel<EntityModel<Game>> getByPlatform(
            @PathVariable Long platformId,
            @PageableDefault(size = 6, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Game> page = gameService.findByPlatform(platformId, pageable);
        return pagedAssembler.toModel(page, this::buildGameModel);
    }

    @GetMapping("/wishlist/{id}")
    public EntityModel<Game> toggleWishlist(@PathVariable Long id) {
        Game game = gameService.toggleWishlist(id);
        return buildGameModel(game);
    }

    private EntityModel<Game> buildGameModel(Game game) {
        Link selfLink = linkTo(methodOn(GameController.class).getById(game.getId()))
                .withSelfRel()
                .withTitle(game.getTitle());

        Link genreLink = linkTo(methodOn(GameController.class)
                .getByGenre(game.getGenre().getId(), null))
                .withRel("same-genre")
                .withTitle("Games in " + game.getGenre().getName() + " genre");

        Link platformLink = linkTo(methodOn(GameController.class)
                .getByPlatform(game.getPlatform().getId(), null))
                .withRel("same-platform")
                .withTitle("Games on " + game.getPlatform().getName());

        String wishlistRel = game.isInWishlist() ? "remove-from-wishlist" : "add-to-wishlist";
        String wishlistTitle = (game.isInWishlist() ? "Remove " : "Add ") + game.getTitle()
                + (game.isInWishlist() ? " from wishlist" : " to wishlist");

        Link wishlistLink = linkTo(methodOn(GameController.class).toggleWishlist(game.getId()))
                .withRel(wishlistRel)
                .withTitle(wishlistTitle)
                .withType("GET");

        return EntityModel.of(game, selfLink, genreLink, platformLink, wishlistLink);
    }
}