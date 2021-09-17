package com.kalah.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.kalah.exception.GameNotFoundException;
import com.kalah.model.Game;

@Repository
public class GameRepositoryImpl implements GameRepository {

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	@Override
	public Game find(String id) {
		Game game = dynamoDBMapper.load(Game.class, id);
		if (game == null) {
			throw new GameNotFoundException(id);
		}
		return game;
	}

	@Override
	public Game save(Game game) {
		dynamoDBMapper.save(game);
		return game;
	}
}