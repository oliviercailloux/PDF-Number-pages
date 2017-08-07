package io.github.oliviercailloux.pdf_number_pages.gui.outline_component;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;

import io.github.oliviercailloux.pdf_number_pages.model.IOutlineNode;
import io.github.oliviercailloux.pdf_number_pages.model.Outline;
import io.github.oliviercailloux.pdf_number_pages.model.OutlineNode;

public class OutlineTreeContentProvider implements ITreeContentProvider {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(OutlineTreeContentProvider.class);

	@Override
	public Object[] getChildren(Object parentElement) {
		final OutlineNode outline = (OutlineNode) parentElement;
		LOGGER.debug("Returning children of {}.", parentElement);
		return Iterables.toArray(outline.getChildren(), OutlineNode.class);
	}

	@Override
	public Object[] getElements(Object inputElement) {
//		final DummyOutlineRootNode dummyOutline = (DummyOutlineRootNode) inputElement;
		final Outline outline = (Outline) inputElement;
		LOGGER.debug("Returning elements of {}.", inputElement);
		return Iterables.toArray(outline.getChildren(), OutlineNode.class);
	}

	@Override
	public Object getParent(Object element) {
		final IOutlineNode outlineNode = (IOutlineNode) element;
		return outlineNode.getParent().orElse(null);
	}

	@Override
	public boolean hasChildren(Object element) {
		final IOutlineNode outlineNode = (IOutlineNode) element;
		LOGGER.debug("Returning has children for {}.", outlineNode);
		return !Iterables.isEmpty(outlineNode.getChildren());
	}

}
