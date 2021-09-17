package com.kalah.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameTest {

  @Test
  public void testInitialization() {
    final Game givenGame = new Game();

    Assert.assertNotNull(givenGame.getBoard());
    Assert.assertNull(givenGame.getTurn());
    Assert.assertNull(givenGame.getWinner());
  }

  @Test
  public void testWinner() {
    final Game givenGame = new Game();
    givenGame.setWinner(Player.PLAYER_A);

    Assert.assertEquals(Player.PLAYER_A, givenGame.getWinner());

    givenGame.setWinner(Player.PLAYER_B);

    Assert.assertEquals(Player.PLAYER_B, givenGame.getWinner());
  }

  @Test
  public void testTurn() {
    final Game givenGame = new Game();
    givenGame.setTurn(Player.PLAYER_A);

    Assert.assertEquals(Player.PLAYER_A, givenGame.getTurn());

    givenGame.setTurn(Player.PLAYER_B);

    Assert.assertEquals(Player.PLAYER_B, givenGame.getTurn());
  }
}
