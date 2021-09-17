package com.kalah.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalah.exception.IllegalMoveException;
import com.kalah.model.Board;
import com.kalah.model.Game;
import com.kalah.model.Pit;
import com.kalah.model.Player;
import com.kalah.repository.GameRepository;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository repository;

    @Autowired
    public GameServiceImpl(final GameRepository repository) {
        this.repository = repository;
    }

    @Override
	public Game createGame() {
        return this.repository.save(new Game());
    }

    @Override
    public Game play(final String gameId, final Integer pitId) {
        final Game game = this.repository.find(gameId);
        distributeStones(game, pitId);
        checkGameOver(game);
        this.repository.save(game);
        return game;
    }

	@Override
	public Game fetch(String gameId) {
		return this.repository.find(gameId);
	}

    private void resetBoard(final Game game) {
        game.getBoard().getPits().parallelStream()
                .filter(pit -> (Player.PLAYER_A.getHouseIndex() != pit.getId())
                               && (Player.PLAYER_B.getHouseIndex() != pit.getId()))
                .forEach(pit -> pit.setStoneCount(0));
    }

    private void distributeStones(final Game game, int pitId) {
        final Pit startPit = game.getBoard().getPit(pitId);
        validateMove(game, pitId);
        int stoneToDistribute = startPit.getStoneCount();
        startPit.setStoneCount(0);
        while (stoneToDistribute > 0) {
            final Pit currentPit = game.getBoard().getPit(++pitId);
            if (currentPit.isDistributable(game.getTurn())) {
                currentPit.setStoneCount(currentPit.getStoneCount() + 1);
                stoneToDistribute--;
            }
        }
        lastEmptyPit(game, pitId);
        decidePlayerTurn(game, pitId);
    }

    private void checkGameOver(final Game game) {
        final int playerAPitStoneCount = game.getBoard().getStoneCount(Player.PLAYER_A, false);
        final int playerBPitStoneCount = game.getBoard().getStoneCount(Player.PLAYER_B, false);
        if ((playerAPitStoneCount == 0) || (playerBPitStoneCount == 0)) {
            final Pit houseA = game.getBoard().getPit(Player.PLAYER_A.getHouseIndex());
            final Pit houseB = game.getBoard().getPit(Player.PLAYER_B.getHouseIndex());
            houseA.setStoneCount(houseA.getStoneCount() + playerAPitStoneCount);
            houseB.setStoneCount(houseB.getStoneCount() + playerBPitStoneCount);
            determineWinner(game);
            resetBoard(game);
        }
    }

    private void determineWinner(final Game game) {
        final int houseAStoneCount = game.getBoard().getStoneCount(Player.PLAYER_A, true);
        final int houseBStoneCount = game.getBoard().getStoneCount(Player.PLAYER_B, true);
        if (houseAStoneCount > houseBStoneCount) {
            game.setWinner(Player.PLAYER_A);
        } else if (houseAStoneCount < houseBStoneCount) {
            game.setWinner(Player.PLAYER_B);
        }
    }

    private void lastEmptyPit(final Game game, final int endPitId) {
        final Pit endPit = game.getBoard().getPit(endPitId);
        if (!endPit.isHouse() && endPit.getOwner().equals(game.getTurn())
            && (endPit.getStoneCount() == 1)) {
            final Pit oppositePit = game.getBoard().getPit(Board.PIT_END_INDEX - endPit.getId());
            if (oppositePit.getStoneCount() > 0) {
                final Pit house = game.getBoard().getPit(endPit.getOwner().getHouseIndex());
                house.setStoneCount(
                        (house.getStoneCount() + oppositePit.getStoneCount()) + endPit.getStoneCount());
                oppositePit.setStoneCount(0);
                endPit.setStoneCount(0);
            }
        }
    }

    private void decidePlayerTurn(final Game game, final int pitId) {
        final Pit pit = game.getBoard().getPit(pitId);
        if (pit.isHouse() && Player.PLAYER_A.equals(pit.getOwner())
            && Player.PLAYER_A.equals(game.getTurn())) {
            game.setTurn(Player.PLAYER_A);
        } else if (pit.isHouse() && Player.PLAYER_B.equals(pit.getOwner())
                   && Player.PLAYER_B.equals(game.getTurn())) {
            game.setTurn(Player.PLAYER_B);
        } else {
            if (Player.PLAYER_A.equals(game.getTurn())) {
                game.setTurn(Player.PLAYER_B);
            } else {
                game.setTurn(Player.PLAYER_A);
            }
        }
    }

    private void validateMove(final Game game, final int startPitId) {
        final Pit startPit = game.getBoard().getPit(startPitId);
        if (startPit.isHouse()) {
            throw new IllegalMoveException("Cannot start from house");
        }
        if (Player.PLAYER_A.equals(game.getTurn())
            && !Player.PLAYER_A.equals(startPit.getOwner())) {
            throw new IllegalMoveException("It's Player A's turn");
        }
        if (Player.PLAYER_B.equals(game.getTurn())
            && !Player.PLAYER_B.equals(startPit.getOwner())) {
            throw new IllegalMoveException("It's Player B's turn");
        }
        if (startPit.getStoneCount() == 0) {
            throw new IllegalMoveException("Cannot start from empty pit");
        }
        if (game.getTurn() == null) {
            if (Player.PLAYER_A.equals(startPit.getOwner())) {
                game.setTurn(Player.PLAYER_A);
            } else {
                game.setTurn(Player.PLAYER_B);
            }
        }
    }
}