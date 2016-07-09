/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extratorradoc;

/**
 *
 * @author Rony
 */
public class Regex {
    public static final String TITULO_DO_TRABALHO_ATIVIDADES_ORIENTACAO = "(Titulo do trabalho:[a-zA-Z :\\-.]*(Tabela:))";
    public static final String TABELA_ATIVIDADES_ORIENTACAO = "(Tabela:[a-zA-Z : -]*(Orientando:))+";
    public static final String CHA_ATIVIDADES_ORIENTACAO = "(CHA:\\s+[0-9]*)";
    public static final String DATA_INICIO_ATIVIDADES_ORIENTACAO = "(Data inicio:\\s+[0-9/]*)";
    public static final String DATA_TERMINO_ATIVIDADES_ORIENTACAO = "(Data termino:\\s+[0-9/]*)";
    
    public static final String TABELA_ATIVIDADES_ESPECIAIS = "(Tabela:[a-zA-Z :\\-.]*)(CHA:)";
    public static final String CHA_ATIVIDADES_ESPECIAIS = "(CHA:\\s+[0-9]*)";
    public static final String DATA_INICIO_ATIVIDADES_ESPECIAIS = "(Data inicio:\\s+[0-9/]*)";
    public static final String DATA_TERMINO_ATIVIDADES_ESPECIAIS = "(Data termino:\\s+[0-9/]*)";
    
    public static final String REGEX_TITULO_ATIVIDADES_EXTENSAO = "(Tabela:\\s*[a-zA-Z:_ .-[,]]*\\s*CHA:)";
    public static final String REGEX_DESCRICAO_ATIVIDADES_EXTENSAO = "(Descricao da atividade:\\s*[a-zA-Z0-9:_ .-[(][)]]*Descricao da clientela:)";
    public static final String REGEX_DESCRICAO_CLIENTELA_ATIVIDADES_EXTENSAO = "(Descricao da clientela:\\s+([a-zA-Z -])*(Tabela:)?)+";
    public static final String REGEX_CHA_ATIVIDADES = "(CHA:\\s*\\d+)";
    public static final String REGEX_DATA_INICIO_ATIVIDADES = "(Data\\s*inicio:\\s*\\d{2}\\/\\d{2}\\/\\d{4})";
    public static final String REGEX_DATA_TERMINO_ATIVIDADES = "(Data\\s*termino:\\s*\\d{2}\\/\\d{2}\\/\\d{4})";
}
