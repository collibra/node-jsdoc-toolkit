package com.collibra.dgc.core.model.user;

import com.collibra.dgc.core.model.Resource;

public interface GroupMembership extends Resource {

	public abstract User getUser();

	public abstract Group getGroup();

}
