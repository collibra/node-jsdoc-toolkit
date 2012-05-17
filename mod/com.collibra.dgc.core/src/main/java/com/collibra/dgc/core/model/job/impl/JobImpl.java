/**
 * 
 */
package com.collibra.dgc.core.model.job.impl;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.collibra.dgc.core.model.job.Job;

/**
 * Implementation of {@link Job}
 * @author dieterwachters
 */
@Entity
@Table(name = "JOBS")
public class JobImpl implements Job {
	private String id;
	private long creationDate;
	private long startDate;
	private long endDate;
	private EState state;
	private String message;
	private double percentage;
	private String owner;
	private boolean cancelable;

	protected JobImpl() {
	}

	public JobImpl(boolean cancelable, String owner) {
		creationDate = System.currentTimeMillis();
		state = EState.WAITING;
		this.cancelable = cancelable;
		this.owner = owner;
		this.id = UUID.randomUUID().toString();
	}

	@Override
	@Column(name = "id")
	@Id
	public String getId() {
		return id;
	}

	@Column(name = "CREATION_DATE")
	@Override
	public long getCreationDate() {
		return this.creationDate;
	}

	@Column(name = "START_DATE")
	@Override
	public long getStartDate() {
		return this.startDate;
	}

	@Column(name = "END_DATE")
	@Override
	public long getEndDate() {
		return this.endDate;
	}

	@Column(name = "STATE")
	@Override
	public EState getState() {
		return this.state;
	}

	@Column(name = "MESSAGE")
	@Override
	public String getMessage() {
		return this.message;
	}

	@Transient
	@Override
	public double getProgressPercentage() {
		return this.percentage;
	}

	@Override
	public void setState(EState state) {
		this.state = state;
	}

	@Override
	public void setProgress(double progress, String message) {
		percentage = progress;
		this.message = message;
	}

	@Override
	public void setError(String message) {
		state = EState.ERROR;
		this.message = message;
		endDate = System.currentTimeMillis();
	}

	@Override
	public void setCompleted(String message) {
		state = EState.COMPLETED;
		this.message = message;
		endDate = System.currentTimeMillis();
	}

	protected void setId(String id) {
		this.id = id;
	}

	protected void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	protected void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	protected void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	protected void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	@Override
	public void setCanceled(String message) {
		state = EState.CANCELED;
		this.message = message;
		endDate = System.currentTimeMillis();
	}

	@Column(name = "OWNER")
	@Override
	public String getOwner() {
		return owner;
	}

	@Column(name = "CANCELABLE")
	@Override
	public boolean isCancelable() {
		return cancelable;
	}

	protected void setOwner(String owner) {
		this.owner = owner;
	}

	protected void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}
}
