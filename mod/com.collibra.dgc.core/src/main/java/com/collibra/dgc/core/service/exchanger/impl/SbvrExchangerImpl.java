package com.collibra.dgc.core.service.exchanger.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.collibra.dgc.core.dao.BinaryFactTypeDao;
import com.collibra.dgc.core.dao.CharacteristicDao;
import com.collibra.dgc.core.model.community.Community;
import com.collibra.dgc.core.model.meaning.MeaningFactory;
import com.collibra.dgc.core.model.representation.RepresentationFactory;
import com.collibra.dgc.core.model.representation.Vocabulary;
import com.collibra.dgc.core.service.RepresentationService;
import com.collibra.dgc.core.service.exchanger.SbvrExchanger;
import com.collibra.dgc.core.service.exchanger.exceptions.SbvrXmiException;
import com.collibra.dgc.core.service.exchanger.impl.importer.SbvrImporter;
import com.collibra.dgc.core.service.exchanger.options.SbvrImporterOptions;
import com.collibra.dgc.core.service.impl.ConstraintChecker;

/**
 * Implementation of the {@link SbvrExchanger}
 * 
 * @author dtrog
 * 
 */
@Service
public class SbvrExchangerImpl implements SbvrExchanger {
	private transient static final Logger log = LoggerFactory.getLogger(SbvrExchangerImpl.class);

	@Autowired
	private RepresentationService representationService;
	@Autowired
	private RepresentationFactory representationFactory;
	@Autowired
	private MeaningFactory meaningFactory;
	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private BinaryFactTypeDao binaryFactTypeDao;
	@Autowired
	private CharacteristicDao characteristicDao;
	@Autowired
	private ConstraintChecker constraintChecker;

	@Override
	public Vocabulary importSbvrVocabularyInputStream(InputStream is, Community community) {
		return importSbvrVocabularyInputStream(is, community, new SbvrImporterOptions());
	}

	@Override
	public Vocabulary importSbvrVocabularyInputStream(InputStream is, Community community, SbvrImporterOptions options) {
		SbvrImporter importer = new SbvrImporter(is, community, options, representationFactory, meaningFactory,
				binaryFactTypeDao, characteristicDao, constraintChecker);

		final Vocabulary vocabulary = importer.importSbvr();
		if (options.shouldPersistVocabulary()) {
			final TransactionTemplate tt = new TransactionTemplate(txManager);
			tt.execute(new TransactionCallback<Vocabulary>() {
				@Override
				public Vocabulary doInTransaction(TransactionStatus status) {
					return representationService.saveVocabulary(vocabulary);
				}
			});
		}
		return vocabulary;
	}

	@Override
	public Vocabulary importSbvrVocabularyFile(File sbvrFile, Community community) {
		return importSbvrVocabularyFile(sbvrFile, community, new SbvrImporterOptions());
	}

	@Override
	public Vocabulary importSbvrVocabularyFile(File sbvrFile, Community community, SbvrImporterOptions options) {
		try {
			return importSbvrVocabularyInputStream(new FileInputStream(sbvrFile), community, options);
		} catch (IOException e) {
			throw new SbvrXmiException("Error reading file", e);
		}
	}

	// TODO PORT
	// public Vocabulary importSbvrVocabularyAttachment(Attachment sbvrAttachment, Community community) {
	// return importSbvrVocabularyAttachment(sbvrAttachment, community, new SbvrImporterOptions());
	// }
	//
	// public Vocabulary importSbvrVocabularyAttachment(Attachment sbvrAttachment, Community community,
	// SbvrImporterOptions options) {
	// try {
	// return importSbvrVocabularyInputStream(new ByteArrayInputStream(sbvrAttachment.getContentAsBytes()),
	// speech, options);
	// } catch (XWikiException e) {
	// throw new SbvrXmiException("Error reading attachment", e);
	// }
	// }

	@Override
	public void exportSbvrVocabulary(Vocabulary vocabulary) {
		// no export yet
	}

}
