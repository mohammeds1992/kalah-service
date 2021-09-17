package com.kalah.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@DynamoDBDocument
@AllArgsConstructor
@Data
public class Board {

	@DynamoDBIgnore
	public static int PIT_START_INDEX = 1;

	@DynamoDBIgnore
	public static int PIT_END_INDEX = 14;

	@Getter
	@DynamoDBAttribute
	private List<Pit> pits;

	public Board() {
		this.pits = new ArrayList<>();
		for (int i = Board.PIT_START_INDEX; i <= Board.PIT_END_INDEX; i++) {
			this.pits.add(new Pit(i));
		}
	}

	@DynamoDBIgnore
	public Pit getPit(final int index) {
		return this.pits.get((index - 1) % Board.PIT_END_INDEX);
	}

	@DynamoDBIgnore
	public int getStoneCount(final Player player, final boolean includeHouse) {
		return this.getPits().stream()
				.filter(pit -> (pit.getOwner().equals(player) && (includeHouse || !pit.isHouse())))
				.mapToInt(Pit::getStoneCount).sum();
	}
}