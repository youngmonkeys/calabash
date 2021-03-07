package com.tvd12.calabash.server.controller;

import com.tvd12.calabash.core.IAtomicLong;
import com.tvd12.calabash.rpc.common.Commands;
import com.tvd12.calabash.rpc.common.ErrorCodes;
import com.tvd12.calabash.rpc.common.request.AtomicLongAddAndGetRequest;
import com.tvd12.calabash.rpc.common.request.AtomicLongGetIdRequest;
import com.tvd12.calabash.rpc.common.request.AtomicLongGetRequest;
import com.tvd12.calabash.server.core.CalabashServerContext;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.exception.BadRequestException;
import com.tvd12.quick.rpc.server.annotation.Rpc;
import com.tvd12.quick.rpc.server.annotation.RpcController;

import lombok.Setter;

@Setter
@RpcController
public class AtomicLongRequestController {

	@EzyAutoBind
	private CalabashServerContext serverContext;
	
	@Rpc(Commands.ATOMIC_LONG_GET_ID)
	public int atomicLongGetId(AtomicLongGetIdRequest request) {
		return serverContext.getAtomicLongId(request.getAtomicLongName());
	}
	
	@Rpc(Commands.ATOMIC_LONG_GET)
	public long atomicLongGet(AtomicLongGetRequest request) {
		IAtomicLong atomicLong = getAtomicLong(request.getAtomicLongId());
		return atomicLong.get();
	}
	
	@Rpc(Commands.ATOMIC_LONG_INCREMENT_AND_GET)
	public long atomicLongIncrementAndGet(AtomicLongGetRequest request) {
		IAtomicLong atomicLong = getAtomicLong(request.getAtomicLongId());
		return atomicLong.incrementAndGet();
	}
	
	@Rpc(Commands.ATOMIC_LONG_ADD_AND_GET)
	public long atomicLongAddAndGet(AtomicLongAddAndGetRequest request) {
		IAtomicLong atomicLong = getAtomicLong(request.getAtomicLongId());
		return atomicLong.addAndGet(request.getDelta());
	}
	
	private IAtomicLong getAtomicLong(int atomicLongId) {
		IAtomicLong atomicLong = serverContext.getAtomicLong(atomicLongId);
		if(atomicLong != null)
			return atomicLong;
		throw new BadRequestException(ErrorCodes.INVALID_ATOMIC_LONG_ID, "there is no atomicLong with id: " + atomicLongId);
	}
}
