package br.com.fiap.game_finder.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "t_game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

            private String description;

            LocalDate releaseDate;

            Double rating;

            @ManyToOne
            @JoinColumn(name = "genre_id")
            private Genre genre;

            @ManyToOne
            @JoinColumn(name = "platform_id")
            private Platform platform;


            String coverUrl;

            String backdropUrl;

            boolean inWishlist;
}
