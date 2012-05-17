package com.collibra.dgc.core.dto.stats;

import java.util.Collection;
import java.util.Comparator;

import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.user.Member;

/**
 * Result entry for {@link Member}s count per {@link Representation} statistics. Note that in future this can be
 * expanded beyond the count of members.
 * @author amarnath
 * 
 */
public class RepresentationStatsEntry {
	private Representation representation;
	private int count;

	public RepresentationStatsEntry(Representation representation, int count) {
		setRepresentation(representation);
		setCount(count);
	}

	public Representation getRepresentation() {
		return representation;
	}

	public void setRepresentation(Representation representation) {
		this.representation = representation;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return representation.verbalise() + " : " + count;
	}

	/**
	 * Comparator for sorting {@link Collection} of {@link RepresentationStatsEntry} based on the count.
	 */
	public static class CountComparator implements Comparator<RepresentationStatsEntry> {
		private boolean ascending = true;

		public CountComparator(boolean ascending) {
			this.ascending = ascending;
		}

		public int compare(RepresentationStatsEntry o1, RepresentationStatsEntry o2) {
			if (ascending) {
				return o1.count - o2.count;
			} else {
				return o2.count - o1.count;
			}
		}
	}
}
