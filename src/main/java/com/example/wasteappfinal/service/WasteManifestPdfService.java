package com.example.wasteappfinal.service;

import com.example.wasteappfinal.entity.WasteManifest;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * Servis za generiranje PDF dokumenta pratećeg lista.
 *
 * Koristi OpenPDF (iText 2.1.7) za izradu PDF-a iz podataka radnog naloga.
 * PDF se generira u memoriji i vraća kao byte[] kako bi se mogao poslati
 * kroz HTTP odgovor bez spremanja na disk.
 */
@Service
public class WasteManifestPdfService {

    /** Format datuma i vremena prikazan u PDF-u. */
    private static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

    /**
     * Generira PDF dokument pratećeg lista.
     *
     * @param manifest entitet pratećeg lista s povezanim radnim nalogom
     * @return byte[] sadržaj PDF dokumenta
     */
    public byte[] generate(WasteManifest manifest) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            // Postavke dokumenta: A4 format, margine 40 px
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, output);
            document.open();

            // Fontovi s CP1250 encodingom (podrška za hrvatske znakove)
            BaseFont baseFont = BaseFont.createFont(
                    BaseFont.HELVETICA,
                    BaseFont.CP1250,
                    BaseFont.NOT_EMBEDDED
            );

            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font headingFont = new Font(baseFont, 12, Font.BOLD);
            Font normalFont = new Font(baseFont, 10, Font.NORMAL);

            // Naslov dokumenta
            Paragraph title = new Paragraph("PRATEĆI LIST OTPADA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(18);
            document.add(title);

            var order = manifest.getWorkOrder();

            // Sekcija: Podaci o dokumentu
            addSection(document, "Podaci o dokumentu", headingFont, normalFont, new String[][]{
                    {"Broj pratećeg lista", manifest.getManifestNumber()},
                    {"Datum izdavanja", manifest.getIssuedAt().format(DATE_TIME)},
                    {"Broj radnog naloga", String.valueOf(order.getId())},
                    {"Status naloga", order.getStatus().name()}
            });

            // Sekcija: Podaci o korisniku
            addSection(document, "Podaci o korisniku", headingFont, normalFont, new String[][]{
                    {"Korisnik", safe(order.getUser().getDisplayName())},
                    {"Adresa e-pošte", order.getUser().getEmail()},
                    {"OIB", safe(order.getUser().getOib())},
                    {"Adresa preuzimanja", order.getPickupAddress()}
            });

            // Sekcija: Podaci o otpadu
            addSection(document, "Podaci o otpadu", headingFont, normalFont, new String[][]{
                    {"Vrsta otpada", order.getWasteType().getName()},
                    {"Šifra", order.getWasteType().getCode()},
                    {"Količina", order.getQuantity().toPlainString()},
                    {"Mjerna jedinica", order.getUnit().name()},
                    {"Planirani termin", order.getScheduledFor() == null
                            ? "Nije određen"
                            : order.getScheduledFor().format(DATE_TIME)},
                    {"Napomena naloga", safe(order.getNote())},
                    {"Napomena pratećeg lista", safe(manifest.getNote())}
            });

            // Footer poruka
            Paragraph source = new Paragraph(
                    "Dokument je generiran u aplikaciji Evidencija zbrinjavanja otpada.",
                    normalFont
            );
            source.setSpacingBefore(20);
            source.setAlignment(Element.ALIGN_CENTER);
            document.add(source);

            document.close();
            return output.toByteArray();

        } catch (Exception ex) {
            throw new IllegalStateException("PDF pratećeg lista nije moguće generirati.", ex);
        }
    }

    /**
     * Dodaje sekciju s naslovom i tablicom (labela + vrijednost).
     */
    private void addSection(Document document, String title, Font heading, Font normal, String[][] rows)
            throws DocumentException {

        Paragraph paragraph = new Paragraph(title, heading);
        paragraph.setSpacingBefore(8);
        paragraph.setSpacingAfter(6);
        document.add(paragraph);

        PdfPTable table = new PdfPTable(new float[]{1.1f, 2.2f});
        table.setWidthPercentage(100);

        for (String[] row : rows) {
            PdfPCell label = new PdfPCell(new Phrase(row[0], heading));
            label.setPadding(6);
            label.setBackgroundColor(new Color(240, 240, 240));

            PdfPCell value = new PdfPCell(new Phrase(row[1], normal));
            value.setPadding(6);

            table.addCell(label);
            table.addCell(value);
        }

        document.add(table);
    }

    /**
     * Vraća "Nije navedeno" ako je vrijednost null ili prazna.
     */
    private String safe(String value) {
        return value == null || value.isBlank() ? "Nije navedeno" : value;
    }
}
