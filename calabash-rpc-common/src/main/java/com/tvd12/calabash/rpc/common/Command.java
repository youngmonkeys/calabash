package com.tvd12.calabash.rpc.common;

import java.util.Map;

import com.tvd12.ezyfox.util.EzyEnums;

import lombok.Getter;

public enum Command {

	MAP_GET_ID(Commands.MAP_GET_ID_VALUE),
	MAP_SET("1"),
	MAP_PUT("2"),
	MAP_PUT_ALL("3"),
	MAP_GET_ONE("4"),
	MAP_GET_MANY("5"),
	MAP_GET_BY_QUERY("6"),
	MAP_CONTAINS_KEY("7"),
	MAP_REMOVE_ONE("8"),
	MAP_REMOVE_MANY("9"),
	MAP_CLEAR("a"),
	ATOMIC_LONG_GET_ID("b"),
	ATOMIC_LONG_GET("c"),
	ATOMIC_LONG_INCREMENT_AND_GET("d"),
	ATOMIC_LONG_ADD_AND_GET("e"),
	CHANNEL_GET_ID("f"),
	PUBLISH("g"),
	SUBSCRIBE("h");
	
	@Getter
	private final String value;
	
	private static final Map<String, Command> COMMAND_BY_VALUE =
			EzyEnums.enumMap(Command.class, Command::getValue);
	
	private Command(String value) {
		this.value = value;
	}
	
	public Command of(String value) {
		Command command = COMMAND_BY_VALUE.get(value);
		if(command != null)
			return command;
		throw new IllegalArgumentException("has no command with value: " + value);
	}
	
}
