package com.kalah.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTypeConvertedEnum
public enum Player {

	@DynamoDBIgnore
	PLAYER_A(Board.PIT_END_INDEX / 2),

	@DynamoDBIgnore
	PLAYER_B(Board.PIT_END_INDEX);

	@Getter
	@Setter
    @DynamoDBAttribute
    @DynamoDBTyped(DynamoDBAttributeType.S)
	private int houseIndex;
}