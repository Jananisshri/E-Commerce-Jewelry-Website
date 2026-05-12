package com.tanishq.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.tanishq.dto.PriceDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private static final Logger log = LoggerFactory.getLogger(PdfService.class);

    private static final BaseColor LIGHT_GOLD    = new BaseColor(184, 150, 12);
    private static final BaseColor DARK_GREEN    = new BaseColor(10, 61, 46);
    private static final BaseColor SAGE_GREEN    = new BaseColor(138, 158, 126);
    private static final BaseColor TABLE_HEADER  = new BaseColor(10, 61, 46);
    private static final BaseColor TABLE_ROW_ODD = new BaseColor(200, 213, 185);

    public byte[] generatePriceQuotePdf(PriceDetailsDTO price) throws DocumentException {
        Document document = new Document(PageSize.A4, 50, 50, 60, 60);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        addHeader(document);
        addProductInfo(document, price);
        addPriceBreakdownTable(document, price);
        addFooter(document);

        document.close();
        return baos.toByteArray();
    }

    private void addHeader(Document document) throws DocumentException {

        // ── Logo Image — centered, no TANISHQ text ──
        try {
            ClassPathResource imgResource = new ClassPathResource("static/logo.png");
            Image logo = Image.getInstance(imgResource.getURL());
            logo.scaleToFit(120, 120);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            log.warn("Logo not found: {}", e.getMessage());
        }

        // ── A TATA PRODUCT ──
        Font tataFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, DARK_GREEN);
        Paragraph tata = new Paragraph("A TATA PRODUCT", tataFont);
        tata.setAlignment(Element.ALIGN_CENTER);
        tata.setSpacingAfter(8);
        document.add(tata);

        // ── Divider line ──
        LineSeparator line = new LineSeparator(1.5f, 100f, LIGHT_GOLD, Element.ALIGN_CENTER, -2);
        document.add(new Chunk(line));
        document.add(Chunk.NEWLINE);

        // ── PRICE QUOTATION subtitle ──
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, DARK_GREEN);
        Paragraph title = new Paragraph("PRICE QUOTATION", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4);
        document.add(title);

        // ── Generated date ──
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY);
        String dateStr = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        Paragraph date = new Paragraph("Generated on: " + dateStr, dateFont);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(20);
        document.add(date);
    }

    private void addProductInfo(Document document, PriceDetailsDTO price) throws DocumentException {
        Font labelFont   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, DARK_GREEN);
        Font valueFont   = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.DARK_GRAY);
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, DARK_GREEN);

        Paragraph section = new Paragraph("Product Details", sectionFont);
        section.setSpacingAfter(8);
        document.add(section);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{35f, 65f});
        infoTable.setSpacingBefore(5);
        infoTable.setSpacingAfter(20);

        addInfoRow(infoTable, "Product Name",  price.getProductName(),                  labelFont, valueFont);
        addInfoRow(infoTable, "Karatage",      price.getKaratage(),                     labelFont, valueFont);
        addInfoRow(infoTable, "Weight",        price.getWeight() + " grams",            labelFont, valueFont);
        addInfoRow(infoTable, "Materials",     price.getMaterials(),                    labelFont, valueFont);
        addInfoRow(infoTable, "Gold Price/gm", "Rs." + fmt(price.getGoldRatePerGram()) +
                              " (" + price.getKaratage() + ")",                         labelFont, valueFont);

        document.add(infoTable);
    }

    private void addPriceBreakdownTable(Document document, PriceDetailsDTO price) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, DARK_GREEN);
        Paragraph heading = new Paragraph("Price Breakdown", sectionFont);
        heading.setSpacingAfter(8);
        document.add(heading);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{30f, 35f, 35f});

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
        addTableHeader(table, "",                        headerFont);
        addTableHeader(table, "Percentage (%) / Grams",  headerFont);
        addTableHeader(table, "Cost (Rs)",               headerFont);

        Font rowFont   = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.DARK_GRAY);
        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, DARK_GREEN);

        addTableRow(table, "Weight",
                price.getWeight() + "g",
                "Rs." + fmt(price.getGoldValue()),
                rowFont, false);

        addTableRow(table, "Making",
                price.getMakingPercent() + "%",
                "Rs." + fmt(price.getMakingValue()),
                rowFont, true);

        addTableRow(table, "Wastage",
                price.getWastagePercent() + "%",
                "Rs." + fmt(price.getWastageValue()),
                rowFont, false);

        addTableRow(table, "Tax",
                price.getGstPercent() + "%",
                "Rs." + fmt(price.getGstAmount()),
                rowFont, true);

        // Total row — last 2 columns merged
        PdfPCell totalLabel = new PdfPCell(new Phrase("Total", totalFont));
        totalLabel.setBackgroundColor(TABLE_ROW_ODD);
        totalLabel.setPadding(8);
        totalLabel.setBorderColor(SAGE_GREEN);

        PdfPCell totalValue = new PdfPCell(new Phrase("Rs." + fmt(price.getTotalPrice()), totalFont));
        totalValue.setBackgroundColor(TABLE_ROW_ODD);
        totalValue.setPadding(8);
        totalValue.setColspan(2);
        totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValue.setBorderColor(SAGE_GREEN);

        table.addCell(totalLabel);
        table.addCell(totalValue);

        document.add(table);
    }

    private void addFooter(Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
        LineSeparator line = new LineSeparator(0.5f, 100f, LIGHT_GOLD, Element.ALIGN_CENTER, -2);
        document.add(new Chunk(line));

        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, BaseColor.GRAY);
        Paragraph footer = new Paragraph(
            "\nNote: Gold price is fetched live and may vary. Final price is subject to change.\n" +
            "This is a computer-generated quotation. For queries, contact your nearest Tanishq store.",
            footerFont
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(8);
        document.add(footer);
    }

    private void addInfoRow(PdfPTable table, String label, String value,
                             Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(6);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(6);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addTableHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(TABLE_HEADER);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTableRow(PdfPTable table, String label, String percent,
                              String cost, Font font, boolean odd) {
        BaseColor bg = odd ? TABLE_ROW_ODD : BaseColor.WHITE;

        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBackgroundColor(bg);
        labelCell.setPadding(7);
        labelCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell percentCell = new PdfPCell(new Phrase(percent, font));
        percentCell.setBackgroundColor(bg);
        percentCell.setPadding(7);
        percentCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell costCell = new PdfPCell(new Phrase(cost, font));
        costCell.setBackgroundColor(bg);
        costCell.setPadding(7);
        costCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(labelCell);
        table.addCell(percentCell);
        table.addCell(costCell);
    }

    private String fmt(BigDecimal value) {
        if (value == null) return "0.00";
        return String.format("%,.2f", value);
    }
}