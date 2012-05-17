package com.collibra.dgc.core.service;


/**
 * Service for fixing data inconsistencies.
 * 
 * @author amarnath
 * 
 */
public interface DataConsistencyService {
	// /**
	// * Analysis 0001 : Terms without concept type.
	// * <p>
	// * Get all {@link Term}s not having {@link Concept} type.
	// * @return The {@link Term}s.
	// */
	// Collection<Term> findTermsWithoutConceptType();
	//
	// /**
	// * Analysis 0002 : Every latest vocabulary should only contain latest {@link Term}s.
	// * <p>
	// * Get all {@link Vocabulary}s containing non-latest {@link TermImpl}s.
	// * @return The {@link Vocabulary}s
	// */
	// MultiValueMap findVocabulariesWithNonLatestTerms();
	//
	// /**
	// * Analysis 0003 : A latest representation should only have one status attribute.
	// * <p>
	// * Get all {@link Representation}s having more than one status.
	// * @return {@link Map} of {@link Representation} to statuses count.
	// */
	// Map<Representation, Long> findLatestRepresentationsWithMorethanOneStatus();
	//
	// /**
	// * Analysis 0004 : All terms must have a steward.
	// * <p>
	// * Get all {@link Term}s not having {@link Member} with specified role. Note that the query does not search
	// through
	// * Collibra SBVR Vocabularies.
	// * @param roleName The {@link Role} name.
	// * @return The {@link Term}s.
	// */
	// Collection<Term> findTermsHavingNoMemberWithSpecifiedRole(String roleName);
	//
	// /**
	// * Analysis 0005 : All terms must have a meaning (uuid) present.
	// * <p>
	// * Get all {@link Term}s having no {@link Meaning}.
	// * @return The {@link Term}s.
	// */
	// Collection<Term> findTermsWithoutMeaning();
	//
	// /**
	// * Analysis 0006 : All terms must have a meaning (uuid) that is latest.
	// * <p>
	// * Get all latest {@link Term}s with non-latest {@link Meaning}.
	// * @return The {@link Term}s.
	// */
	// Collection<Term> findLatestTermsWithNonLatestMeaning();
	//
	// /**
	// * Analysis 0007 : All representations with multiple versions can only have one that is latest.
	// * <p>
	// * Get all {@link Representation}s with muliple latest versions.
	// * @return The {@link Representation}.
	// */
	// Map<Representation, Long> findMultipleLatestVersions();
	//
	// /**
	// * Analysis 0009 : A representation should have at least one status attribute.
	// * <p>
	// * Get all {@link Representation}s without status. Note that the search is limited to {@link Term},
	// * {@link BinaryFactTypeForm}, {@link CharacteristicForm} and {@link Name}.
	// * @return The {@link Representation}s.
	// */
	// Collection<Representation> findLatestRepresentationsWithoutStatus();
	//
	// /**
	// * Analysis 0010 : A representation that is latest should have latest attributes.
	// * <p>
	// * Get all latest {@link Representation}s that have non latest {@link Attribute}s.
	// * @return The {@link Representation} to {@link Attribute}s map.
	// */
	// Collection<SemanticCommunity> findAllSemanticCommunitiesWithoutMetaVocabulary();
	//
	// /**
	// * Analysis 0011 : A representation (latest or not) should only have one status attribute.
	// * <p>
	// * Get all {@link Representation}s (including latest and old) having more than one status.
	// * @return The {@link Map} of {@link Representation} to statuses count.
	// */
	// Map<Representation, Map<Long, Long>> findRepresentationsWithMorethanOneStatus();
	//
	// /**
	// * Analysis 0012 : Every semantic community should have exactly one meta-speech community.
	// * <p>
	// * Get all {@link SemanticCommunity}s (old or latest) that do not have meta {@link SpeechCommunity}.
	// * @return The {@link SemanticCommunity}s.
	// */
	// Collection<SemanticCommunity> findAllSemanticCommunitiesWithoutMetaSpeechCommunity();
	//
	// /**
	// * Analysis 0014 : Every speech community should have exactly one semantic community.
	// * <p>
	// * Get all {@link SpeechCommunity}s (old or latest) that do not have parent {@link SemanticCommunity}.
	// * @return The {@link SpeechCommunity}s.
	// */
	// Collection<SpeechCommunity> findAllSpeechCommunitiesWithoutParentCommuntiy();
	//
	// /**
	// * Analysis 0015 : Every Vocabulary should have exactly one speech community.
	// * <p>
	// * Get all {@link Vocabulary}s (old or latest) that do not have parent {@link SpeechCommunity}.
	// * @return The {@link Vocabulary}s.
	// */
	// Collection<Vocabulary> findAllVocabulariesWithoutParentCommuntiy();
	//
	// /**
	// * Analysis 0016 : Every Representation should have exactly one Vocabulary.
	// * <p>
	// * Get all {@link Representation}s (old or latest) that do not have {@link Vocabulary}. The {@link
	// Representation}s
	// * searched include {@link Term}s, {@link BinaryFactTypeForm}s, {@link CharacteristicForm}s and {@link Name}s.
	// * @return The {@link Representation}.
	// */
	// Collection<Representation> findAllRepresentationsWithoutVocabulary();
	//
	// /**
	// * Analysis 0017 : Every attribute should have exactly one owner.
	// * <p>
	// * Get all {@link Attribute}s without owner {@link Representation}.
	// * @return The {@link Attribute}s.
	// */
	// Collection<Attribute> findAllAttributesWithoutOwner();
	//
	// /**
	// * Analysis 0018 : Every attribute should have exactly one latest label.
	// * <p>
	// * Get all {@link Attribute}s without label.
	// * @return The {@link Attribute}s.
	// */
	// Collection<Attribute> findAllAttributesWithoutLabel();
	//
	// /**
	// * Analysis 0019 : The type of a concept that is latest should always be latest.
	// * <p>
	// * Get all {@link Concept}s with non latest {@link Concept} types.
	// * @return
	// */
	// Collection<Concept> findLatestConceptWithNonLatestType();
	//
	// /**
	// * Analysis 0014 : Every latest speech community should have exactly one semantic community.
	// * <p>
	// * Get all latest {@link SpeechCommunity}s that do not have parent {@link SemanticCommunity}.
	// * @return The {@link SpeechCommunity}s.
	// */
	// Collection<SpeechCommunity> findAllLatestSpeechCommunitiesWithoutParentCommuntiy();
	//
	// /**
	// * Analysis 0015 : Every latest Vocabulary should have exactly one speech community.
	// * <p>
	// * Get all {@link Vocabulary}s latest vocabularies that do not have parent {@link SpeechCommunity}.
	// * @return The {@link Vocabulary}s.
	// */
	// Collection<Vocabulary> findAllLatestVocabulariesWithoutParentCommuntiy();
	//
	// /**
	// * Analysis 0018 : Every latest attribute should have exactly one latest label.
	// * <p>
	// * Get all latest {@link Attribute}s without label.
	// * @return The {@link Attribute}s.
	// */
	// Collection<Attribute> findAllLatestAttributesWithoutLabel();
	//
	// /**
	// * Analysis 0021 : A speech community should not have duplicate vocabularies.
	// * <p>
	// * Get all latest {@link SpeechCommunity}s that have duplicate vocabularies.
	// * @return The
	// */
	// Collection<SpeechCommunity> findSpeechCommunitiesWithDuplicateVocabularies();
	//
	// /**
	// * Analysis 0010 : A representation that is latest should have latest attributes.
	// * <p>
	// * Get all latest {@link Representation}s that have non latest {@link Attribute}s.
	// * @return The {@link Representation} to {@link Attribute}s map.
	// */
	// MultiValueMap findLatestRepresentationsWithNonLatestAttributes();
	//
	// /**
	// * @return Get all non-latest {@link Vocabulary}s in latest {@link SpeechCommunity}.
	// */
	// Collection<Vocabulary> findNonlatestVocabulariesInLatestCommunity();
	//
	// /**
	// * For the given {@link SpeechCommunity} find the inconsistencies in vocabularies and terms and fix them.
	// */
	// void fixSpeechCommunity(SpeechCommunity speechCommunity);
	//
	// /**
	// * For the given {@link Vocabulary} find the inconsistencies in the terms and fix them.
	// */
	// void fixVocabulary(Vocabulary vocabulary);
	//
	// /**
	// * Fix all non-latest {@link Vocabulary}s in latest {@link SpeechCommunity}
	// * @return {@link Vocabulary}s fixed.
	// */
	// Collection<Vocabulary> fixNonlatestVocabulariesInLatestCommunity();
}
