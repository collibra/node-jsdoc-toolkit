package com.collibra.dgc.core.util;

/**
 * A Pair.
 * 
 * @author pmalarme
 */
public class Pair<A, B> implements Comparable<Pair<A, B>> {

	public final A first;
	public final B second;

	public Pair(A first, B second) {

		this.first = first;
		this.second = second;
	}

	@Override
	public String toString() {
		return "(" + first.toString() + ", " + second.toString() + ")";
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (!(obj instanceof Pair))
			return false;

		@SuppressWarnings("rawtypes")
		Pair pair = (Pair) obj;

		return equals(first, pair.first) && equals(second, pair.second);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Pair<A, B> pair) {

		if (pair != null) {

			// Compare the first object
			if (first instanceof Comparable) {

				final int sign = ((Comparable<A>) first).compareTo(pair.first);

				if (sign > 0)
					return 1;
				if (sign < 0)
					return -1;
			}

			// Compare the second object
			if (second instanceof Comparable) {

				final int sign = ((Comparable<B>) second).compareTo(pair.second);

				if (sign > 0)
					return 1;
				if (sign < 0)
					return -1;
			}
		}

		return 0;
	}

	@Override
	public int hashCode() {

		int hash = 1;

		hash = 31 * hash + (first == null ? 0 : first.hashCode());
		hash = 31 * hash + (second == null ? 0 : second.hashCode());

		return hash;
	}

	public static boolean equals(Object obj1, Object obj2) {

		return obj1 == null ? obj2 == null : obj1.equals(obj2);
	}

}
