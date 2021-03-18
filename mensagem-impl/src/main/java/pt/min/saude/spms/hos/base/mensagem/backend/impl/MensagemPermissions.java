package pt.min.saude.spms.hos.base.mensagem.backend.impl;

/**
 * Centraliza as constantes que representam as permissões existentes neste componente.
 * Estas constantes hierarquicas deverão ser usadas em cada serviço com controlo de autorização para construir
 * a lista de permissões a necessárias a validar.
 * <p>
 * A hierarquia é composta por:
 * DOMINIO -> AGREGADO -> ELEMENTO -> AÇÃO
 * <p>
 * NOTA: Os serviços só necessitam da acção "Executar", as restantes são necessárias apenas para renderização no frontend.
 */
public class MensagemPermissions {

    private static final String SEP = ".";

    private static final String PERM_EXEC = "x";
    private static final String PERM_READ = "r";
    private static final String PERM_CHANGE = "w";

    public static class Base {

        private static final String DOMINIO = "base";

        public static class Mensagem {

            private static final String AGREGADO = "mensagem";

            public static class Pesquisar {

                private static final String ELEMENTO = "pesquisar";

                public static final String Executar = DOMINIO + SEP + AGREGADO + SEP + ELEMENTO + SEP + PERM_EXEC;
                public static String Consultar = DOMINIO + SEP + AGREGADO + SEP + ELEMENTO + SEP + PERM_READ;
            }

            public static class Consultar {

                private static final String ELEMENTO = "consultar";

                public static final String Executar = DOMINIO + SEP + AGREGADO + SEP + ELEMENTO + SEP + PERM_EXEC;
                public static String Consultar = DOMINIO + SEP + AGREGADO + SEP + ELEMENTO + SEP + PERM_READ;
            }

            public static class Criar {

                private static final String ELEMENTO = "criar";

                public static final String Executar = DOMINIO + SEP + AGREGADO + SEP + ELEMENTO + SEP + PERM_EXEC;
                public static String Actualizar = DOMINIO + SEP + AGREGADO + SEP + ELEMENTO + SEP + PERM_CHANGE;
            }

            public static class Atualizar {

                private static final String ELEMENTO = "atualizar";

                public static final String Executar = DOMINIO + SEP + AGREGADO + SEP + ELEMENTO + SEP + PERM_EXEC;
                public static String Actualizar = DOMINIO + SEP + AGREGADO + SEP + ELEMENTO + SEP + PERM_CHANGE;
            }
        }
    }


}
