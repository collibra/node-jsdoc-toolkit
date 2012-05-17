package com.collibra.dgc.core.service.exchanger;

import java.io.File;
import java.io.InputStream;

import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.exchanger.options.SbvrImporterOptions;

/**
 * This component takes care of importing and exporting the SBVR XMI exchange format.
 * @author dtrog
 * 
 */
public interface SbvrExchanger {
	/**
	 * Imports the given xmi {@link File} and creates the necessary pages.
	 * 
	 * @param sbvrFile
	 */
	Vocabulary importSbvrVocabularyFile(File sbvrFile, Community community, SbvrImporterOptions options);

	// TODO PORT
	// /**
	// * Imports the given xmi {@link Attachment} and creates the necessary pages.
	// *
	// * @param sbvrAttachment
	// */
	// Vocabulary importSbvrVocabularyAttachment(Attachment sbvrAttachment, Community community,
	// SbvrImporterOptions options);

	/**
	 * Imports the given xmi {@link InputStream} and creates the necessary pages.
	 * @param is
	 */
	Vocabulary importSbvrVocabularyInputStream(InputStream is, Community community, SbvrImporterOptions options);

	/**
	 * Imports the given xmi {@link File} and creates the necessary pages.
	 * 
	 * @param sbvrFile
	 */
	Vocabulary importSbvrVocabularyFile(File sbvrFile, Community community);

	// TODO PORT
	// /**
	// * Imports the given xmi {@link Attachment} and creates the necessary pages.
	// *
	// * @param sbvrAttachment
	// */
	// Vocabulary importSbvrVocabularyAttachment(Attachment sbvrAttachment, SpeechCommunity speechCommunity);

	/**
	 * Imports the given xmi {@link InputStream} and creates the necessary pages.
	 * @param is
	 */
	Vocabulary importSbvrVocabularyInputStream(InputStream is, Community community);

	/**
	 * Exports the given vocabulary to xmi FIXME not implemented yet.
	 * @param vocabulary
	 */
	void exportSbvrVocabulary(Vocabulary vocabulary);

}
