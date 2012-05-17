/**
 * 
 */
package com.collibra.dgc.bootstrap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.dbunit.dataset.stream.DataSetProducerAdapter;
import org.dbunit.dataset.stream.IDataSetConsumer;
import org.dbunit.util.xml.XmlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Manuel Laflamme
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 859 $ $Date: 2008-11-02 12:50:23 +0100 (dom, 02 nov 2008) $
 * @since 1.5.5 (Apr 19, 2003)
 */
public class FlatXmlWriter implements IDataSetConsumer {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(FlatXmlWriter.class);

	private static final String DATASET = "dataset";

	private final XmlWriter _xmlWriter;
	private ITableMetaData _activeMetaData;
	private int _activeRowCount;
	private boolean _includeEmptyTable = false;
	private String _systemId = null;

	public FlatXmlWriter(OutputStream out) throws IOException {
		this(out, null);
	}

	/**
	 * @param outputStream The stream to which the XML will be written.
	 * @param encoding The encoding to be used for the {@link XmlWriter}. Can be null. See
	 *            {@link XmlWriter#XmlWriter(OutputStream, String)}.
	 * @throws UnsupportedEncodingException
	 */
	public FlatXmlWriter(OutputStream outputStream, String encoding) throws UnsupportedEncodingException {
		_xmlWriter = new XmlWriter(outputStream, encoding);
		_xmlWriter.enablePrettyPrint(true);
	}

	public FlatXmlWriter(Writer writer) {
		_xmlWriter = new XmlWriter(writer);
		_xmlWriter.enablePrettyPrint(true);
	}

	public FlatXmlWriter(Writer writer, String encoding) {
		_xmlWriter = new XmlWriter(writer, encoding);
		_xmlWriter.enablePrettyPrint(true);
	}

	public void setIncludeEmptyTable(boolean includeEmptyTable) {
		_includeEmptyTable = includeEmptyTable;
	}

	public void setDocType(String systemId) {
		_systemId = systemId;
	}

	/**
	 * Enable or disable pretty print of the XML.
	 * @param enabled <code>true</code> to enable pretty print (which is the default). <code>false</code> otherwise.
	 * @since 2.4
	 */
	public void setPrettyPrint(boolean enabled) {
		_xmlWriter.enablePrettyPrint(enabled);
	}

	/**
	 * Writes the given {@link IDataSet} using this writer.
	 * @param dataSet The {@link IDataSet} to be written
	 * @throws DataSetException
	 */
	public void write(IDataSet dataSet) throws DataSetException {
		logger.debug("write(dataSet={}) - start", dataSet);

		DataSetProducerAdapter provider = new DataSetProducerAdapter(dataSet);
		provider.setConsumer(this);
		provider.produce();
	}

	// //////////////////////////////////////////////////////////////////////////
	// IDataSetConsumer interface

	public void startDataSet() throws DataSetException {
		logger.debug("startDataSet() - start");

		try {
			_xmlWriter.writeDeclaration();
			_xmlWriter.writeDoctype(_systemId, null);
			_xmlWriter.writeElement(DATASET);
		} catch (IOException e) {
			throw new DataSetException(e);
		}
	}

	public void endDataSet() throws DataSetException {
		logger.debug("endDataSet() - start");

		try {
			_xmlWriter.endElement();
			_xmlWriter.close();
		} catch (IOException e) {
			throw new DataSetException(e);
		}
	}

	public void startTable(ITableMetaData metaData) throws DataSetException {
		logger.debug("startTable(metaData={}) - start", metaData);

		_activeMetaData = metaData;
		_activeRowCount = 0;

		if (_activeMetaData.getTableName().equalsIgnoreCase("representations")) {
			rows.clear();
		}
	}

	public void endTable() throws DataSetException {
		logger.debug("endTable() - start");

		if (_activeMetaData.getTableName().equalsIgnoreCase("representations")) {
			// TODO reorder
			Column[] columns = _activeMetaData.getColumns();
			int col = 0;
			for (int i = 0; i < columns.length; i++) {
				String columnName = columns[i].getColumnName();
				if (columnName.equalsIgnoreCase("BSG_REP_TYPE")) {
					col = i;
					break;
				}
			}
			final int typeCol = col;
			Collections.sort(rows, new Comparator<Object[]>() {
				public int compare(Object[] o1, Object[] o2) {
					return ((String) o2[typeCol]).compareTo((String) o1[typeCol]);
				}
			});
			for (final Object[] values : rows) {
				writeRow(values);
			}
			rows.clear();
		}

		if (_includeEmptyTable && _activeRowCount == 0) {
			try {
				String tableName = _activeMetaData.getTableName();
				_xmlWriter.writeEmptyElement(tableName);
			} catch (IOException e) {
				throw new DataSetException(e);
			}
		}

		_activeMetaData = null;
	}

	private void writeRow(Object[] values) throws DataSetException {
		logger.debug("row(values={}) - start", values);
		try {
			String tableName = _activeMetaData.getTableName();
			_xmlWriter.writeElement(tableName);

			Column[] columns = _activeMetaData.getColumns();
			for (int i = 0; i < columns.length; i++) {
				String columnName = columns[i].getColumnName();
				Object value = values[i];

				// Skip null value
				if (value == null) {
					continue;
				}

				try {
					String stringValue = DataType.asString(value);
					_xmlWriter.writeAttribute(columnName, stringValue, true);
				} catch (TypeCastException e) {
					throw new DataSetException("table=" + _activeMetaData.getTableName() + ", row=" + i + ", column="
							+ columnName + ", value=" + value, e);
				}
			}

			_activeRowCount++;
			_xmlWriter.endElement();
		} catch (IOException e) {
			throw new DataSetException(e);
		}
	}

	private final List<Object[]> rows = new ArrayList<Object[]>();

	public void row(Object[] values) throws DataSetException {

		if (_activeMetaData.getTableName().equalsIgnoreCase("representations")) {
			rows.add(values);
		} else {
			writeRow(values);
		}
	}
}
