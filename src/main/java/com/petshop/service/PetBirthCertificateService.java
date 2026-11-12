package com.petshop.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.petshop.domain.Client;
import com.petshop.domain.Pet;
import com.petshop.repository.PetRepository;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PetBirthCertificateService {

    private static final DateTimeFormatter DATE_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DeviceRgb ACCENT = new DeviceRgb(45, 106, 79);
    private static final DeviceRgb LIGHT_BG = new DeviceRgb(245, 247, 245);
    /** Metade de uma folha A4 (largura A4 × altura / 2). */
    private static final PageSize HALF_A4 = new PageSize(PageSize.A4.getWidth(), PageSize.A4.getHeight() / 2f);

    private final PetRepository petRepository;
    private final String shopName;

    public PetBirthCertificateService(
            PetRepository petRepository,
            @Value("${app.shop-name:Pet Shop}") String shopName) {
        this.petRepository = petRepository;
        this.shopName = shopName;
    }

    @Transactional(readOnly = true)
    public CertificatePdf generate(Long petId) {
        Pet pet = petRepository.findByIdWithClient(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet não encontrado"));
        Client guardian = pet.getClient();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(HALF_A4);
            Document document = new Document(pdf, HALF_A4);
            document.setMargins(22, 28, 22, 28);

            Border outer = new SolidBorder(ACCENT, 2f);
            Table frame = new Table(UnitValue.createPercentArray(new float[]{1}))
                    .useAllAvailableWidth()
                    .setBorder(outer)
                    .setPadding(10);

            Cell content = new Cell().setBorder(Border.NO_BORDER).setPadding(8).setBackgroundColor(ColorConstants.WHITE);

            content.add(new Paragraph(shopName)
                    .setFontSize(9)
                    .setFontColor(ACCENT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(2));

            content.add(new Paragraph("CERTIDÃO DE NASCIMENTO")
                    .setBold()
                    .setFontSize(14)
                    .setFontColor(ACCENT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(0));

            content.add(new Paragraph("ANIMAL")
                    .setBold()
                    .setFontSize(11)
                    .setFontColor(ACCENT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(8));

            String issuedAt = LocalDate.now(ZoneId.of("America/Sao_Paulo")).format(DATE_BR);
            content.add(metaLine("Registro nº", "PET-" + pet.getId()));
            content.add(metaLine("Emitida em", issuedAt).setMarginBottom(8));

            content.add(sectionTitle("DADOS DO ANIMAL"));
            content.add(fieldTable(new String[][]{
                    {"Nome", display(pet.getName())},
                    {"Espécie", speciesLabel(pet.getSpecies())},
                    {"Raça", display(pet.getBreed())},
                    {"Sexo", sexLabel(pet.getSex())},
                    {"Data de nascimento", formatDate(pet.getBirthDate())},
                    {"Peso", formatWeight(pet.getWeightKg())}
            }));

            content.add(sectionTitle("GUARDIÃO").setMarginTop(8));
            content.add(fieldTable(new String[][]{
                    {"Nome", display(guardian.getName())},
                    {"Endereço", display(guardian.getAddress())}
            }));

            content.add(new Paragraph("________________________________")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(14)
                    .setMarginBottom(1));
            content.add(new Paragraph("Assinatura / carimbo do pet shop")
                    .setFontSize(8)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER));

            frame.addCell(content);
            document.add(frame);
            document.close();

            String safeName = slugify(pet.getName());
            String filename = "certidao-" + safeName + "-" + pet.getId() + ".pdf";
            return new CertificatePdf(out.toByteArray(), filename);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Falha ao gerar certidão de nascimento",
                    ex);
        }
    }

    private Paragraph sectionTitle(String text) {
        return new Paragraph(text)
                .setBold()
                .setFontSize(9)
                .setFontColor(ACCENT)
                .setBackgroundColor(LIGHT_BG)
                .setPadding(4)
                .setMarginBottom(4);
    }

    private Paragraph metaLine(String label, String value) {
        return new Paragraph(label + ": " + value)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(1);
    }

    private Table fieldTable(String[][] rows) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{32, 68}))
                .useAllAvailableWidth()
                .setMarginBottom(2);

        for (String[] row : rows) {
            table.addCell(labelCell(row[0]));
            table.addCell(valueCell(row[1]));
        }
        return table;
    }

    private Cell labelCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold().setFontSize(8))
                .setBorder(Border.NO_BORDER)
                .setPaddingTop(2)
                .setPaddingBottom(2)
                .setPaddingRight(6);
    }

    private Cell valueCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setFontSize(8))
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(new DeviceRgb(220, 220, 220), 0.5f))
                .setPaddingTop(2)
                .setPaddingBottom(2);
    }

    private static String display(String value) {
        if (value == null || value.isBlank()) {
            return "—";
        }
        return value.trim();
    }

    private static String formatDate(LocalDate date) {
        if (date == null) {
            return "—";
        }
        return date.format(DATE_BR);
    }

    private static String formatWeight(BigDecimal weightKg) {
        if (weightKg == null) {
            return "—";
        }
        return weightKg.stripTrailingZeros().toPlainString() + " kg";
    }

    private static String speciesLabel(String species) {
        if (species == null || species.isBlank()) {
            return "—";
        }
        return switch (species) {
            case "CAO" -> "Cão";
            case "GATO" -> "Gato";
            case "OUTRO" -> "Outro";
            default -> species;
        };
    }

    private static String sexLabel(String sex) {
        if (sex == null || sex.isBlank()) {
            return "—";
        }
        return switch (sex) {
            case "MACHO" -> "Macho";
            case "FEMEA" -> "Fêmea";
            case "INDEFINIDO" -> "Indefinido";
            default -> sex;
        };
    }

    private static String slugify(String value) {
        if (value == null || value.isBlank()) {
            return "pet";
        }
        String normalized = Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return normalized.isBlank() ? "pet" : normalized;
    }

    public record CertificatePdf(byte[] content, String filename) {
    }
}
