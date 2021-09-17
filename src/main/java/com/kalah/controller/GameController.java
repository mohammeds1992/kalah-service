package com.kalah.controller;

import java.net.InetAddress;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kalah.model.Game;
import com.kalah.model.Pit;
import com.kalah.model.dto.GameResponse;
import com.kalah.service.GameService;

@RestController
@RequestMapping(path = "/v1/games")
public class GameController {

	@Autowired
	private  GameService gameService;

	@Autowired
	private  Environment environment;

	@PostMapping(produces = "application/json")
	public ResponseEntity<GameResponse> createGame() {
		final Game game = gameService.createGame();
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(GameResponse
						.builder()
						.id(game.getId())
						.status(game.getBoard()
									.getPits()
									.stream()
									.collect(Collectors.toMap(Pit::getId, 
													   		  value -> Integer.toString(value.getStoneCount()))))
						.url(getUrl(game.getId()))
						.build());
	}

    @PutMapping(path= "/{gameId}/pits/{pitId}", produces = "application/json")
    public ResponseEntity<GameResponse> playGame(@PathVariable final String gameId, @PathVariable final Integer pitId) {
        final Game game = gameService.play(gameId, pitId);
    	return ResponseEntity
				.status(HttpStatus.OK)
				.body(GameResponse
						.builder()
						.id(game.getId())
						.status(game.getBoard()
									.getPits()
									.stream()
									.collect(Collectors.toMap(Pit::getId, 
													   		  value -> Integer.toString(value.getStoneCount()))))
						.url(getUrl(game.getId()))
						.build());
    }

    @GetMapping(path= "/{gameId}", produces = "application/json")
    public ResponseEntity<GameResponse> getGame(@PathVariable final String gameId) {
        final Game game = gameService.fetch(gameId);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(GameResponse
						.builder()
						.id(game.getId())
						.status(game.getBoard()
									.getPits()
									.stream()
									.collect(Collectors.toMap(Pit::getId, 
													   value -> Integer.toString(value.getStoneCount()))))
						.url(getUrl(game.getId()))
						.build());
    }

    private String getUrl(final String gameId) {
        final int port = environment.getProperty("server.port", Integer.class, 8080);
        return String.format("http://%s:%s/games/%s", InetAddress.getLoopbackAddress().getHostName(),
                port, gameId);
    }
}