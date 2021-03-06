package io.github.oliviercailloux.pdf_number_pages.services.saver;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * If error during save, this runnable terminates.
 * </p>
 *
 * @author Olivier Cailloux
 *
 */
public class SaverRunnable implements Callable<Void> {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SaverRunnable.class);

	private SaveJob job;

	private final PdfSaver pdfSaver = new PdfSaver();

	public SaverRunnable(SaveJob job) {
		this.job = requireNonNull(job);
	}

	@Override
	public Void call() throws Exception {
		LOGGER.debug("Proceeding to save: {}.", job);
		pdfSaver.save(job);
		if (!pdfSaver.succeeded()) {
			throw new SaveFailedException(pdfSaver.getErrorMessage());
		}
		return null;
	}

}
