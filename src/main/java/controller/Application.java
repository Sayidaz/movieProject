package controller;

import model.Film;
import model.Image;
import model.OCR.OCRManager;
import model.repository.FilmRepository;

import java.time.LocalDate;
import java.util.List;

public class Application {

    public static void main(String[] args) {

        String imagePath = "../Heat.jpg";

        Image image = new Image(imagePath);


        OCRManager ocrManager = new OCRManager();

        try {
            String extractedText = ocrManager.extractText(image);
            if (extractedText != null && !extractedText.isEmpty()) {
                System.out.println("Texte extrait :\n" + extractedText);
            } else {
                System.out.println("Aucun texte détecté dans l'image.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'extraction du texte : " + e.getMessage());
        }



        FilmRepository filmRepository = new FilmRepository();

        // pour vérifier qu'un film se sauve bien quand je lance la méthode de savesearch
        Film film = new Film(
                "Interstellar",
                "Christopher Nolan",
                List.of("Matthew McConaughey"),
                "Science Fiction",
                LocalDate.of(2014, 11, 7),
                "https://example.com/interstellar-trailer"
        );

        filmRepository.saveSearchHistory(film);
        System.out.println("Film ajouté");
        
    }

}

