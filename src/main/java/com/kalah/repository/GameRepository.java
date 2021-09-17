package com.kalah.repository;

import com.kalah.model.Game;

public interface GameRepository {

    Game find(final String id);

    Game save(final Game game);
}