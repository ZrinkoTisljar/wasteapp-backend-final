package com.example.wasteappfinal.service;

import com.example.wasteappfinal.entity.WasteManifest;
//import com.lowagie.text.BaseColor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * Servis za generiranje PDF dokumenta pratećeg lista.
 *
 * Dokument je prilagođen potrebama aplikacije i sadrži:
 * - podatke o otpadu
 * - podatke o pošiljatelju
 * - prazna polja za prijevoznika
 * - prazna polja za primatelja
 * - mjesta za potpise
 *
 * PDF se generira u memoriji i ne sprema se na disk.
 */
@Service
public class WasteManifestPdfService {

    /**
     * Format datuma i vremena koji se prikazuje u PDF-u.
     */
    private static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

    /**
     * Generira PDF dokument iz pratećeg lista i povezanog radnog naloga.
     *
     * @param manifest prateći list s povezanim radnim nalogom
     * @return sadržaj PDF dokumenta kao niz bajtova
     */
    public byte[] generate(WasteManifest manifest) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            Document document = new Document(
                    PageSize.A4,
                    40,
                    40,
                    35,
                    35
            );

            PdfWriter.getInstance(document, output);
            document.open();

            /*
             * Font s podrškom za hrvatske znakove.
             */
            BaseFont baseFont = BaseFont.createFont(
                    BaseFont.HELVETICA,
                    BaseFont.CP1250,
                    BaseFont.NOT_EMBEDDED
            );

            Font titleFont = new Font(baseFont, 16, Font.BOLD);
            Font normalFont = new Font(baseFont, 9, Font.NORMAL);
            Font boldFont = new Font(baseFont, 9, Font.BOLD);
            Font sectionFont = new Font(
                    baseFont,
                    10,
                    Font.BOLD,
                    Color.WHITE
            );
            Font footerFont = new Font(
                    baseFont,
                    8,
                    Font.ITALIC
            );

            var order = manifest.getWorkOrder();
            var user = order.getUser();

            /*
             * Količina bez nepotrebnih završnih nula.
             *
             * Primjer:
             * 100.000 -> 100
             * 100.250 -> 100.25
             */
            String formattedQuantity = order.getQuantity() == null
                    ? "Nije navedeno"
                    : order.getQuantity()
                    .stripTrailingZeros()
                    .toPlainString();

            /*
             * Kontaktni podaci korisnika.
             */
            String contact = safe(user.getEmail());

            if (user.getPhone() != null && !user.getPhone().isBlank()) {
                contact = contact + " / " + user.getPhone();
            }

            /*
             * 1. Naslov dokumenta.
             */
            Paragraph title = new Paragraph(
                    "PRATEĆI LIST ZA OTPAD (PL-O)",
                    titleFont
            );

            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(16);
            document.add(title);

            /*
             * Osnovni podaci o dokumentu.
             */
            Paragraph manifestNumber = new Paragraph(
                    "BROJ PL-O: " + safe(manifest.getManifestNumber()),
                    boldFont
            );

            document.add(manifestNumber);

            String issuedAt = manifest.getIssuedAt() == null
                    ? "Nije navedeno"
                    : manifest.getIssuedAt().format(DATE_TIME);

            document.add(new Paragraph(
                    "Datum izdavanja: " + issuedAt,
                    normalFont
            ));

            document.add(new Paragraph(
                    "Broj radnog naloga: " + order.getId(),
                    normalFont
            ));

            document.add(Chunk.NEWLINE);

            /*
             * 2. Blok A – podaci o pošiljci otpada.
             */
            PdfPTable tableA = createSectionTable(
                    "A. POŠILJKA OTPADA",
                    sectionFont
            );

            addCell(
                    tableA,
                    "Šifra otpada:",
                    safe(order.getWasteType().getCode()),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableA,
                    "Naziv otpada:",
                    safe(order.getWasteType().getName()),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableA,
                    "Procijenjena količina:",
                    formattedQuantity + " " + formatUnit(order.getUnit().name()),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableA,
                    "Status radnog naloga:",
                    formatStatus(order.getStatus().name()),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableA,
                    "Planirani termin:",
                    order.getScheduledFor() == null
                            ? "Nije određen"
                            : order.getScheduledFor().format(DATE_TIME),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableA,
                    "Napomena:",
                    safe(order.getNote()),
                    boldFont,
                    normalFont
            );


            document.add(tableA);

            /*
             * 3. Blok B – podaci o pošiljatelju.
             */
            PdfPTable tableB = createSectionTable(
                    "B. POŠILJATELJ OTPADA",
                    sectionFont
            );

            addCell(
                    tableB,
                    "Naziv / Ime i prezime:",
                    safe(user.getDisplayName()),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableB,
                    "OIB:",
                    safe(user.getOib()),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableB,
                    "Adresa polazišta:",
                    safe(order.getPickupAddress()),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableB,
                    "Kontaktni podaci:",
                    contact,
                    boldFont,
                    normalFont
            );

            document.add(tableB);

            /*
             * 4. Blok C – prijevoznik.
             *
             * Polja se mogu naknadno ručno ispuniti.
             */
            PdfPTable tableC = createSectionTable(
                    "C. PRIJEVOZNIK – ISPUNJAVA PRIJEVOZNIK",
                    sectionFont
            );

            addCell(
                    tableC,
                    "Naziv prijevoznika:",
                    line(),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableC,
                    "OIB prijevoznika:",
                    line(),
                    boldFont,
                    normalFont
            );


            addCell(
                    tableC,
                    "Registarska oznaka vozila:",
                    line(),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableC,
                    "Ime i prezime vozača:",
                    line(),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableC,
                    "Datum preuzimanja:",
                    "______ . ______ . __________",
                    boldFont,
                    normalFont
            );

            document.add(tableC);

            /*
             * 5. Blok D – primatelj otpada.
             */
            PdfPTable tableD = createSectionTable(
                    "D. PRIMATELJ OTPADA – ISPUNJAVA PRIMATELJ",
                    sectionFont
            );

            addCell(
                    tableD,
                    "Naziv primatelja:",
                    line(),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableD,
                    "OIB primatelja:",
                    line(),
                    boldFont,
                    normalFont
            );


            addCell(
                    tableD,
                    "Adresa odredišta:",
                    line(),
                    boldFont,
                    normalFont
            );

            addCell(
                    tableD,
                    "Datum vaganja:",
                    "______ . ______ . __________",
                    boldFont,
                    normalFont
            );

            addCell(
                    tableD,
                    "Preuzeta količina:",
                    "________________ kg",
                    boldFont,
                    normalFont
            );

            document.add(tableD);

            /*
             * 6. Mjesta za potpise.
             */
            PdfPTable signatureTable = new PdfPTable(3);
            signatureTable.setWidthPercentage(100);
            signatureTable.setSpacingBefore(18);
            signatureTable.setKeepTogether(true);

            signatureTable.addCell(createSignatureCell(
                    "Potpis pošiljatelja (B)",
                    normalFont
            ));

            signatureTable.addCell(createSignatureCell(
                    "Potpis prijevoznika (C)",
                    normalFont
            ));

            signatureTable.addCell(createSignatureCell(
                    "Potpis primatelja (D)",
                    normalFont
            ));

            document.add(signatureTable);

            /*
             * 7. Podnožje dokumenta.
             */
            Paragraph footer = new Paragraph(
                    "Dokument je generiran u aplikaciji "
                            + "Evidencija zbrinjavanja otpada.",
                    footerFont
            );

            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(18);
            document.add(footer);

            document.close();

            return output.toByteArray();

        } catch (Exception ex) {
            throw new IllegalStateException(
                    "PDF pratećeg lista nije moguće generirati.",
                    ex
            );
        }
    }

    /**
     * Stvara tablicu sekcije s tamnim naslovnim retkom.
     */
    private PdfPTable createSectionTable(
            String title,
            Font sectionFont
    ) throws DocumentException {

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);
        table.setSpacingBefore(9);
        table.setWidths(new float[]{1.3f, 2.7f});
        table.setKeepTogether(true);

        PdfPCell titleCell = new PdfPCell(
                new Phrase(title, sectionFont)
        );

        titleCell.setColspan(2);
        titleCell.setBackgroundColor(Color.DARK_GRAY);
        titleCell.setPadding(5);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(titleCell);

        return table;
    }

    /**
     * Dodaje redak s nazivom podatka i njegovom vrijednošću.
     */
    private void addCell(
            PdfPTable table,
            String label,
            String value,
            Font labelFont,
            Font valueFont
    ) {
        PdfPCell labelCell = new PdfPCell(
                new Phrase(label, labelFont)
        );

        labelCell.setPadding(5);
        labelCell.setBackgroundColor(new Color(245, 245, 245));

        PdfPCell valueCell = new PdfPCell(
                new Phrase(safe(value), valueFont)
        );

        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    /**
     * Stvara ćeliju s prostorom za potpis.
     */
    private PdfPCell createSignatureCell(
            String label,
            Font font
    ) {
        PdfPCell cell = new PdfPCell(
                new Phrase(
                        label + ":\n\n\n________________________",
                        font
                )
        );

        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(8);

        return cell;
    }

    /**
     * Hrvatski prikaz statusa radnog naloga.
     */
    private String formatStatus(String status) {
        if (status == null) {
            return "Nije navedeno";
        }

        return switch (status) {
            case "CREATED" -> "Kreiran";
            case "SCHEDULED" -> "Zakazan";
            case "COMPLETED" -> "Završen";
            case "CANCELLED" -> "Otkazan";
            default -> status;
        };
    }

    /**
     * Prikaz mjerne jedinice prilagođen dokumentu.
     */
    private String formatUnit(String unit) {
        if (unit == null) {
            return "";
        }

        return switch (unit) {
            case "KG" -> "kg";
            case "T" -> "t";
            case "M3" -> "m³";
            default -> unit;
        };
    }

    /**
     * Crta za ručno ispunjavanje podatka.
     */
    private String line() {
        return "________________________________________";
    }

    /**
     * Zamjenjuje null i prazne vrijednosti jasnom porukom.
     */
    private String safe(String value) {
        return value == null || value.isBlank()
                ? "Nije navedeno"
                : value;
    }
}