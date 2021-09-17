package com.kalah.service;

import com.kalah.model.Game;

public interface GameService {

	Game createGame();

	Game play(String gameId, Integer pitId);

	Game fetch(String gameId);
}