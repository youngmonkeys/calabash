package com.tvd12.calabash.rpc.common;

import java.util.Map;

import com.tvd12.ezyfox.util.EzyEnums;

import lombok.Getter;

public enum Command {

	MAP_GET_ID(Commands.MAP_GET_ID_VALUE),
	MAP_SET(Commands.MAP_SET),
	MAP_PUT(Commands.MAP_PUT),
	MAP_PUT_ALL(Commands.MAP_PUT_ALL),
	MAP_GET_ONE(Commands.MAP_GET_ONE),
	MAP_GET_MANY(Commands.MAP_GET_MANY),
	MAP_GET_BY_QUERY(Commands.MAP_GET_BY_QUERY),
	MAP_CONTAINS_KEY(Commands.MAP_CONTAINS_KEY),
	MAP_REMOVE_ONE(Commands.MAP_REMOVE_ONE),
	MAP_REMOVE_MANY(Commands.MAP_REMOVE_MANY),
	MAP_CLEAR(Commands.MAP_CLEAR),
	ATOMIC_LONG_GET_ID(Commands.ATOMIC_LONG_GET_ID),
	ATOMIC_LONG_GET(Commands.ATOMIC_LONG_GET),
	ATOMIC_LONG_INCREMENT_AND_GET(Commands.ATOMIC_LONG_INCREMENT_AND_GET),
	ATOMIC_LONG_ADD_AND_GET(Commands.ATOMIC_LONG_ADD_AND_GET),
	CHANNEL_GET_ID(Commands.CHANNEL_GET_ID),
	PUBLISH(Commands.PUBLISH),
	SUBSCRIBE(Commands.SUBSCRIBE);
	
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
