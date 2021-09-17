package com.kalah.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBDocument
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pit {

	@Getter
    @DynamoDBAttribute
	private int id;

	@Getter
	@Setter
    @DynamoDBAttribute
	private int stoneCount;

	@JsonCreator
	public Pit(@JsonProperty("id") final int id) {
		this.id = id;
		if (!this.isHouse()) {
			this.setStoneCount(6);
		}
	}

	@DynamoDBIgnore
	public Player getOwner() {
		if (this.getId() <= Player.PLAYER_A.getHouseIndex()) {
			return Player.PLAYER_A;
		} else {
			return Player.PLAYER_B;
		}
	}

	@DynamoDBIgnore
	public boolean isDistributable(final Player turn) {
		return (!turn.equals(Player.PLAYER_A) || (this.getId() != Player.PLAYER_B.getHouseIndex()))
				&& (!turn.equals(Player.PLAYER_B) || (this.getId() != Player.PLAYER_A.getHouseIndex()));
	}

	@DynamoDBIgnore
	public boolean isHouse() {
		return (this.getId() == Player.PLAYER_A.getHouseIndex())
				|| (this.getId() == Player.PLAYER_B.getHouseIndex());
	}
}