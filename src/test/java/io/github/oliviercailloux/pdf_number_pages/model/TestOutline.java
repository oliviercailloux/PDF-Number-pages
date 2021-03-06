package io.github.oliviercailloux.pdf_number_pages.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class TestOutline {

	@Test
	public void test() {
		final Outline outline = new Outline();
		final OutlineNode n0 = OutlineNode.newOutline(new PdfBookmark("n0", 0));
		outline.addChild(0, n0);
		assertEquals(outline, n0.getParent().get());
		assertEquals(0, n0.getLocalOrder().get().intValue());

		final OutlineNode n01 = OutlineNode.newOutline(new PdfBookmark("n01", 1));
		n0.addAsLastChild(n01);
		assertEquals(n0, n01.getParent().get());
		n0.remove(n01.getLocalOrder().get());

		final OutlineNode n3 = OutlineNode.newOutline(new PdfBookmark("n3", 3));
		outline.addChild(1, n3);
		final OutlineNode n2 = OutlineNode.newOutline(new PdfBookmark("n2", 2));
		outline.addChild(1, n2);
		final OutlineNode n1 = OutlineNode.newOutline(new PdfBookmark("n1", 1));
		outline.addChild(1, n1);
		assertEquals(ImmutableList.of(n0, n1, n2, n3), outline.getChildren());
		assertEquals(0, n0.getLocalOrder().get().intValue());
		assertEquals(1, n1.getLocalOrder().get().intValue());
		assertEquals(2, n2.getLocalOrder().get().intValue());
		assertEquals(3, n3.getLocalOrder().get().intValue());

		outline.remove(n1.getLocalOrder().get());
		/** Now: n0, n2, n3. */
		assertEquals(ImmutableList.of(n0, n2, n3), outline.getChildren());
		assertEquals(0, n0.getLocalOrder().get().intValue());
		assertEquals(1, n2.getLocalOrder().get().intValue());
		assertEquals(2, n3.getLocalOrder().get().intValue());
		assertFalse(n1.getLocalOrder().isPresent());
		assertFalse(n1.getParent().isPresent());

		n2.setAsNextSiblingOf(n3);
		/** Now: n0, n3, n2. */
		assertEquals(ImmutableList.of(n0, n3, n2), outline.getChildren());
		assertEquals(0, n0.getLocalOrder().get().intValue());
		assertEquals(1, n3.getLocalOrder().get().intValue());
		assertEquals(2, n2.getLocalOrder().get().intValue());

		n3.changeParent(n0);
		/** Now: n0 -> n3, n2. */
		assertEquals(n0, outline.getChildren().get(0));
		assertEquals(n2, outline.getChildren().get(1));
		assertEquals(ImmutableList.of(n0, n2), outline.getChildren());
		assertEquals(0, n0.getLocalOrder().get().intValue());
		assertEquals(1, n2.getLocalOrder().get().intValue());
		assertEquals(ImmutableList.of(n3), n0.getChildren());
		assertEquals(0, n3.getLocalOrder().get().intValue());
		assertEquals(n0, n3.getParent().get());
		assertEquals(outline, n2.getParent().get());

		assertFalse(outline.equals(new Outline()));
		outline.clear();
		assertEquals(ImmutableList.of(), outline.getChildren());

		assertTrue(outline.equals(new Outline()));
		final OutlineNode n0prime = OutlineNode.newOutline(new PdfBookmark("n0", 0));
		final OutlineNode n3prime = OutlineNode.newOutline(new PdfBookmark("n3", 3));
		n0prime.addChild(0, n3prime);
		assertTrue(n0prime.equals(n0));
	}

	@Test
	public void testEq() {
		final OutlineNode n0 = OutlineNode.newOutline(new PdfBookmark());
		final OutlineNode n0prime = OutlineNode.newOutline(new PdfBookmark());
		final OutlineNode n1 = OutlineNode.newOutline(new PdfBookmark("n1", 10));
		final OutlineNode n1prime = OutlineNode.newOutline(new PdfBookmark("n1", 10));
		assertTrue(n0.equals(n0prime));
		assertFalse(n0.equals(n1));
		n0.addAsLastChild(n1);
		n1prime.changeParent(n0prime);
		assertTrue(n0.equals(n0prime));
		assertTrue(n1.equals(n1prime));
		assertFalse(n0prime.equals(n1prime));
	}

	@Test
	public void testWithEmpty() {
		/**
		 * This test is useful because when all data is equal, it is more difficult to
		 * distinguish nodes.
		 */
		final Outline outline = new Outline();
		final OutlineNode n0 = OutlineNode.newOutline(new PdfBookmark());
		outline.addAsLastChild(n0);
		assertEquals(outline, n0.getParent().get());
		assertEquals(0, n0.getLocalOrder().get().intValue());
		final OutlineNode n3 = OutlineNode.newOutline(new PdfBookmark());
		outline.addAsLastChild(n3);
		/** Now: n0, n3. */
		assertEquals(ImmutableList.of(n0, n3), outline.getChildren());
		assertEquals(0, n0.getLocalOrder().get().intValue());
		assertEquals(1, n3.getLocalOrder().get().intValue());
		assertEquals(outline, n0.getParent().get());
		assertEquals(outline, n3.getParent().get());

		assertEquals(n0, outline.getChildren().get(0));
		final boolean changed = n3.changeParent(n0);
		assertTrue(changed);

		/** Now: n0 -> n3. */
		assertEquals(ImmutableList.of(n3), n0.getChildren());
		assertEquals(outline, n0.getParent().get());
		assertEquals(n0, n3.getParent().get());
		assertEquals(n0, outline.getChildren().get(0));
		assertEquals(ImmutableList.of(n0), outline.getChildren());
		assertEquals(0, n0.getLocalOrder().get().intValue());
		assertEquals(ImmutableList.of(n3), n0.getChildren());
		assertEquals(0, n3.getLocalOrder().get().intValue());
	}

}
