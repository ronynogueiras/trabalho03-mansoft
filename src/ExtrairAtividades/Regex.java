/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExtrairAtividades;

/**
 * Regex's usados na aplicação
 * @author Raphael
 */

public class Regex {
    private static final String VALOR_QUALQUER = "(.*?)";
    private static final String VALOR_DATA = "[ 0-9/]*";
    private static final String VALOR_NUMERO = "\\s*\\d+";
    ;

    public static final String CAMPOS = "(?i)(" +
            "Tabela:|" +
            "CHA:|" +
            "Data termino:|" +
            "Data inicio:|" +
            "Descricao da atividade:|" +
            "Descricao Complementar:|" +
            "Descricao da Clientela:|" +
            "Descricao:|" +
            "Titulo do trabalho:|" +
            "Orientando:|" +
            "Orgao emissor(:?)|" +
            "Autoria:|" +
            "Ano de publicacao:|" +
            "Titulo do produto:|" +
            "Data:|" +
            "Data\\s*(de)?\\s*inicio:|" +
            "Data\\s*(de)?\\s*termino:|" +
            "Atividades de qualificação" +
            ")";
    public static final String TABELA = "(?i)Tabela:"+VALOR_QUALQUER+CAMPOS;
    public static final String TITULO_DO_TRABALHO = "(?i)Titulo do trabalho:"+VALOR_QUALQUER+CAMPOS;
    public static final String CHA = "(?i)CHA:"+VALOR_NUMERO;
    public static final String DATA_INICIO = "(?i)(Data inicio:|Data\\s*(de)?\\s*inicio:)"+VALOR_DATA;
    public static final String DATA_TERMINO = "(?i)(Data termino:|Data\\s*(de)?\\s*termino:)"+VALOR_DATA;
    public static final String DESCRICAO= "(?i)Descricao:"+VALOR_QUALQUER+CAMPOS;
    public static final String DESCRICAO_ATIVIDADE = "(?i)Descricao da atividade:"+VALOR_QUALQUER+CAMPOS;
    public static final String DESCRICAO_CLIENTELA = "(?i)Descricao da clientela:"+VALOR_QUALQUER+CAMPOS;
}
