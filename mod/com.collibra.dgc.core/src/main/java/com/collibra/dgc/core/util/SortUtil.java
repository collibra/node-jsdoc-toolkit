package com.collibra.dgc.core.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Representation;
import com.collibra.dgc.core.model.representation.Vocabulary;

/**
 * Utility for sorting {@link List}s.
 * @author amarnath
 * 
 */
public class SortUtil {
	/**
	 * @return Sorted {@link Vocabulary}s in ascending order.
	 */
	public List<Vocabulary> sortVocabularies(List<Vocabulary> vocabularies) {
		return sortVocabularies(vocabularies, true);
	}

	/**
	 * @param asc The sorting order. True for ascending and false for descending.
	 * @return Sort {@link Vocabulary}s.
	 */
	public List<Vocabulary> sortVocabularies(List<Vocabulary> vocabularies, boolean asc) {
		Collections.sort(vocabularies, new VocabularySortComapartor(asc));
		return vocabularies;
	}

	/**
	 * @return Sorted {@link Community}s in ascending order.
	 */
	public List<? extends Community> sortCommunities(List<? extends Community> communities) {
		return sortCommunities(communities, true);
	}

	/**
	 * @param asc The sorting order. True for ascending and false for descending.
	 * @return Sorted {@link Community}s in ascending order.
	 */
	public List<? extends Community> sortCommunities(List<? extends Community> communities, boolean asc) {
		Collections.sort(communities, new CommunitySortComapartor(asc));
		return communities;
	}

	/**
	 * @param asc The sorting order. True for ascending and false for descending.
	 * @return Sorted {@link Representation}s in ascending order.
	 */
	public List<? extends Representation> sortRepresentations(List<? extends Representation> representations,
			boolean asc) {
		Collections.sort(representations, new RepresentationSortComparator(asc));
		return representations;
	}

	/**
	 * {@link Vocabulary} {@link Comparator} for sorting.
	 */
	class VocabularySortComapartor implements Comparator<Vocabulary> {
		private final boolean asc;

		public VocabularySortComapartor(boolean asc) {
			this.asc = asc;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Vocabulary arg0, Vocabulary arg1) {
			if (asc) {
				return arg0.getName().compareToIgnoreCase(arg1.getName());
			} else {
				return arg1.getName().compareToIgnoreCase(arg0.getName());
			}
		}
	}

	/**
	 * Community {@link Comparator} for sorting.
	 */
	class CommunitySortComapartor implements Comparator<Community> {
		private final boolean asc;

		public CommunitySortComapartor(boolean asc) {
			this.asc = asc;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Community arg0, Community arg1) {
			if (asc) {
				return arg0.getName().compareToIgnoreCase(arg1.getName());
			} else {
				return arg1.getName().compareToIgnoreCase(arg0.getName());
			}
		}
	}

	/**
	 * Representation {@link Comparator} for sorting.
	 */
	class RepresentationSortComparator implements Comparator<Representation> {

		private final boolean asc;

		public RepresentationSortComparator(boolean asc) {
			this.asc = asc;
		}

		public int compare(Representation arg0, Representation arg1) {
			String a0 = arg0.verbalise();
			String a1 = arg1.verbalise();
			if (a0 == null || a1 == null)
				return 0;

			if (asc) {
				return a0.compareToIgnoreCase(a1);
			} else {
				return a1.compareToIgnoreCase(a0);
			}
		}

	}
}
