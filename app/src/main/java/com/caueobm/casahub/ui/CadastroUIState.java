package com.caueobm.casahub.ui;

// Estado para a UI de Cadastro
public abstract class CadastroUIState {
    private CadastroUIState() {
    } // Construtor privado para evitar instanciação direta

    public static final class Loading extends CadastroUIState {
        public Loading() {
        }
    }

    public static final class Success extends CadastroUIState {
        private final String token;

        public Success(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

    }

    public static final class Error extends CadastroUIState {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}