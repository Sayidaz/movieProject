package controller;

import model.repository.FilmRepository;

public class UserManager {

    private static UserManager instance;
    private final FilmRepository filmRepository;

    private UserManager() {
        this.filmRepository = FilmRepository.getInstance();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean createAccount(String username, String password) {
        return filmRepository.createUser(username, password);
    }

    public boolean login(String username, String password) {
        return filmRepository.loginUser(username, password);
    }
}