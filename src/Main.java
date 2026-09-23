import java.util.Scanner;

public class Main {

    static class Nodo {
        Character caractere;
        Nodo filhoEsquerdo;
        Nodo filhoDireito;
    }

    static class ArvoreBinariaMorse {

        Nodo raiz;

        void inicializar() {
            raiz = new Nodo();
        }

        void inserir(String codigoMorse, char caractere) {

            Nodo noAtual = raiz;

            for (char simbolo : codigoMorse.toCharArray()) {
                if (simbolo == '.') {
                    if (noAtual.filhoEsquerdo == null) {
                        noAtual.filhoEsquerdo = new Nodo();
                    }
                    noAtual = noAtual.filhoEsquerdo;
                } else if (simbolo == '-') {
                    if (noAtual.filhoDireito == null) {
                        noAtual.filhoDireito = new Nodo();
                    }
                    noAtual = noAtual.filhoDireito;
                }
            }
            //guarda o caractere
            noAtual.caractere = caractere;
        }

        // Decodifica mensagem inteira
        String buscar(String codigoMorse) {
            StringBuilder resultado = new StringBuilder();
            for (String codigo : codigoMorse.trim().split(" ")) {
                if (codigo.isEmpty()) continue;
                if (codigo.equals("/")) {
                    resultado.append(' ');
                    continue;
                }
                Character c = buscarCaractere(codigo);
                resultado.append(c == null ? '?' : c);   // '?' = codigo inexistente
            }
            return resultado.toString();
        }

        // Busca de um unico caractere
        Character buscarCaractere(String codigoMorse) {
            Nodo noAtual = raiz;
            for (char simbolo : codigoMorse.toCharArray()) {
                if (simbolo == '.') {
                    noAtual = noAtual.filhoEsquerdo;
                } else if (simbolo == '-') {
                    noAtual = noAtual.filhoDireito;
                } else {
                    return null;
                }
                if (noAtual == null) {
                    return null;
                }
            }
            return noAtual.caractere;
        }

        // Busca o caractere na arvore e retorna codigo Morse
        String buscarCodigo(char caractere) {
            return buscarCodigo(raiz, Character.toUpperCase(caractere), "");
        }

        private String buscarCodigo(Nodo no, char caractere, String caminho) {
            if (no == null) return null;

            if (no.caractere != null && no.caractere == caractere) return caminho;

            String esquerda = buscarCodigo(no.filhoEsquerdo, caractere, caminho + ".");

            if (esquerda != null) return esquerda;

            return buscarCodigo(no.filhoDireito, caractere, caminho + "-");
        }

        // Exibe a árvore
        void exibir() {
            System.out.println("(raiz)");
            exibir(raiz.filhoEsquerdo, "    ", ".");
            exibir(raiz.filhoDireito, "    ", "-");
        }

        private void exibir(Nodo no, String espacos, String codigo) {
            if (no == null) return;

            if (no.caractere != null) {
                System.out.println(espacos + no.caractere + " (" + codigo + ")");
            }

            exibir(no.filhoEsquerdo, espacos + "    ", codigo + ".");
            exibir(no.filhoDireito, espacos + "    ", codigo + "-");
        }
    }

    static final String[][] TABELA_MORSE = {
            {"A", ".-"},    {"B", "-..."},  {"C", "-.-."},  {"D", "-.."},   {"E", "."},
            {"F", "..-."},  {"G", "--."},   {"H", "...."},  {"I", ".."},    {"J", ".---"},
            {"K", "-.-"},   {"L", ".-.."},  {"M", "--"},    {"N", "-."},    {"O", "---"},
            {"P", ".--."},  {"Q", "--.-"},  {"R", ".-."},   {"S", "..."},   {"T", "-"},
            {"U", "..-"},   {"V", "...-"},  {"W", ".--"},   {"X", "-..-"},  {"Y", "-.--"},
            {"Z", "--.."},
            {"0", "-----"}, {"1", ".----"}, {"2", "..---"}, {"3", "...--"}, {"4", "....-"},
            {"5", "....."}, {"6", "-...."}, {"7", "--..."}, {"8", "---.."}, {"9", "----."}
    };

    static ArvoreBinariaMorse criarArvore() {
        ArvoreBinariaMorse arvore = new ArvoreBinariaMorse();
        arvore.inicializar();
        for (String[] linha : TABELA_MORSE) {
            arvore.inserir(linha[1], linha[0].charAt(0));
        }
        return arvore;
    }

    public static void main(String[] args) {
        ArvoreBinariaMorse arvore = criarArvore();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ARVORE MORSE =====");
            System.out.println("1 - Decodificar mensagem Morse");
            System.out.println("2 - Buscar codigo de um caractere");
            System.out.println("3 - Codificar texto para Morse");
            System.out.println("4 - Inserir novo caractere");
            System.out.println("5 - Exibir arvore");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");
            if (!sc.hasNextLine()) break;
            String opcao = sc.nextLine().trim();

            switch (opcao) {
                case "1": {
                    System.out.println("(letras separadas por espaco, palavras por ' / ')");
                    System.out.print("Morse: ");
                    System.out.println("Texto: " + arvore.buscar(sc.nextLine()));
                    break;
                }
                case "2": {
                    System.out.print("Caractere: ");
                    String entrada = sc.nextLine().trim();
                    String codigo = entrada.isEmpty() ? null : arvore.buscarCodigo(entrada.charAt(0));
                    if (codigo == null) {
                        System.out.println("Caractere nao encontrado.");
                    } else {
                        System.out.println(Character.toUpperCase(entrada.charAt(0)) + " = " + codigo);
                    }
                    break;
                }
                case "3": {
                    System.out.print("Texto: ");
                    String texto = sc.nextLine();
                    StringBuilder saida = new StringBuilder();
                    for (char c : texto.toCharArray()) {
                        String codigo = (c == ' ') ? "/" : arvore.buscarCodigo(c);
                        saida.append(codigo == null ? "?" : codigo).append(' ');
                    }
                    System.out.println("Morse: " + saida.toString().trim());
                    break;
                }
                case "4": {
                    System.out.print("Caractere: ");
                    String caractere = sc.nextLine().trim();
                    System.out.print("Codigo Morse: ");
                    String codigo = sc.nextLine().trim();
                    if (caractere.length() != 1 || !codigo.matches("[.-]+")) {
                        System.out.println("Entrada invalida.");
                    } else {
                        arvore.inserir(codigo, Character.toUpperCase(caractere.charAt(0)));
                        System.out.println("Inserido!");
                    }
                    break;
                }
                case "5":
                    arvore.exibir();
                    break;
                case "0":
                    sc.close();
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
        sc.close();
    }
}