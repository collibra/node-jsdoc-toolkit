package com.collibra.dgc.service.workflow;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbIdGenerator;

/**
 * This is a piece of code created during the support call with Camunda.
 * <p>
 * For mroe details visit: https://collibra03.firstserved.net/docs/DOC-2468
 * 
 * @author amarnath
 * 
 */
public class GlossaryDbIdGenerator extends DbIdGenerator {
	public GlossaryDbIdGenerator() {
		this.idBlockSize = 100;
	}

	public void reset() {
		this.nextId = 0;
		this.lastId = -1;
	}

	@Override
	public synchronized String getNextId() {
		this.commandExecutor = Context.getProcessEngineConfiguration().getCommandExecutorTxRequiresNew();
		return super.getNextId();
	}
}
