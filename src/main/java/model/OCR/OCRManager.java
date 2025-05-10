package model.OCR;

import model.Image;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.nio.file.Path;
import java.util.List;

public class OCRManager {
    private Tesseract ocrLibrary;
    private List<String> supportedLanguages;

    public OCRManager() {
        this.ocrLibrary = new Tesseract();
        this.supportedLanguages = List.of("eng", "fra", "spa");

        try {

            ClassLoader classLoader = OCRManager.class.getClassLoader();
            String dataDirectory = Path.of(classLoader.getResource("data").toURI()).toString();
            this.ocrLibrary.setDatapath(dataDirectory);
        } catch (Exception e) {
            System.err.println("Erreur lors de la configuration de tessdata : " + e.getMessage());
        }

        this.ocrLibrary.setLanguage("eng");
    }

    public String extractText(Image image) {
        if (!image.exists()) {
            System.out.println("Erreur : le fichier image " + image.getPath() + " n'existe pas.");
            return null ;
        }
        try {
            Path imagePath = Path.of(image.getPath());
            return ocrLibrary.doOCR(imagePath.toFile());
        } catch (TesseractException e) {
            System.err.println("Erreur lors de l'extraction OCR : " + e.getMessage());
            return null;
        }
    }

    public void setLanguage(String language) {
        if (supportedLanguages.contains(language)) {
            ocrLibrary.setLanguage(language);
        } else {
            System.err.println("Langue non prise en charge : " + language);
        }
    }

    public void addSupportedLanguage(String language) {
        this.supportedLanguages.add(language);
    }
}



