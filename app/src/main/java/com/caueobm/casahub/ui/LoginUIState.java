package com.caueobm.casahub.ui; // Ou um pacote de sua escolha

public abstract class LoginUIState {
    private LoginUIState() {} // Construtor privado para simular uma sealed class

    public static final class Idle extends LoginUIState { // Estado inicial ou após uma ação
        public Idle() {}
    }
    public static final class Loading extends LoginUIState {
        public Loading() {}
    }
    public static final class Success extends LoginUIState {
        private final String token;
        public Success(String token) {
            this.token = token;
        }
        public String getToken() { return token; }

    }
    public static final class Error extends LoginUIState {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}