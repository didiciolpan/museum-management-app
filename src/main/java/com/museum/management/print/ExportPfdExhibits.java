package com.museum.management.print;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.museum.management.dto.ExhibitDto;

import java.io.OutputStream;
import java.util.List;

public class ExportPfdExhibits {

	private List<ExhibitDto> exhibits;

	public ExportPfdExhibits(List<ExhibitDto> exhibits) {
		this.exhibits = exhibits;
	}

	public void exportExhibitsToPdf(OutputStream outputStream) throws DocumentException {
		Document document = new Document(PageSize.A4_LANDSCAPE);
		
		PdfWriter.getInstance(document, outputStream);
		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(20);
		font.setColor(BaseColor.BLACK);
		Paragraph p = new Paragraph("List of exhibits", font);
		p.setAlignment(Paragraph.ALIGN_LEFT);
		document.add(p);
		document.add(Chunk.NEWLINE);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 33f, 33f, 33f, 33f, 33f, 33f });
		table.setSpacingBefore(10);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setPadding(5);
		Font font1 = FontFactory.getFont(FontFactory.COURIER_BOLD);
		font1.setSize(15);
		font1.setColor(BaseColor.BLACK);

		cell.setPhrase(new Phrase("Inventory no.", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Title", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Author", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Description", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Release year", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Value", font1));
		table.addCell(cell);

		populateTable(table);
		document.add(table);
		document.close();
	}

	private void populateTable(PdfPTable table) {
		for (int i = 0; i < exhibits.size(); i++) {
			ExhibitDto exhibit = exhibits.get(i);
			table.addCell(exhibit.getInventoryNumber());
			table.addCell(exhibit.getTitle());
			table.addCell(exhibit.getAuthor());
			table.addCell(exhibit.getDescription());
			table.addCell(exhibit.getReleaseYear().toString());
			table.addCell(exhibit.getValue().toString());
		}
	}
}
