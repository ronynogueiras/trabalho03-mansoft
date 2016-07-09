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
    public static final String REGEX_ATIVIDADES_ORIENTACAO = "(Titulo\\s+do\\s+trabalho:\\s*[a-zA-Z:_ .-]*\\s*Tabela:)|(CHA:\\s*\\d+\\s*(Data\\s*inicio:\\s*)?\\d{2}\\/\\d{2}\\/\\d{4}\\s*(Data\\s*termino:\\s*)?\\d{2}\\/\\d{2}\\/\\d{4})+";
    public static final String REGEX_REPLACE_ALL_ATIVIDADES_ORIENTACAO = "(Titulo do trabalho:\\s*)|(\\s*Tabela:)|(CHA:\\s*)|(Data\\s*inicio:\\s*)|(Data\\s*termino:\\s*)+";
    public static final String REGEX_TITULO_ATIVIDADES_EXTENSAO = "(Tabela:\\s*[a-zA-Z:_ .-[,]]*\\s*CHA:)";
    public static final String REGEX_DESCRICAO_ATIVIDADES_EXTENSAO = "(Descricao da atividade:\\s*[a-zA-Z0-9:_ .-[(][)]]*Descricao da clientela:)";
    public static final String REGEX_DESCRICAO_CLIENTELA_ATIVIDADES_EXTENSAO = "(Descricao da clientela:\\s*[a-zA-Z \\-\\(\\)]+(Tabela:)?)";
    public static final String REGEX_CHA_ATIVIDADES = "(CHA:\\s*\\d+)";
    public static final String REGEX_DATA_INICIO_ATIVIDADES = "(Data\\s*(de)?\\s*inicio:\\s*\\d{2}\\/\\d{2}\\/\\d{4})";
    public static final String REGEX_DATA_TERMINO_ATIVIDADES = "(Data\\s*(de)?\\s*termino:\\s*\\d{2}\\/\\d{2}\\/\\d{4})";
    public static final String REGEX_TITULO_ATIVIDADES_QUALIFICACAO = "(Tabela:\\s*[a-zA-Z \\-\\(\\),.]+(((Descricao:\\s*)?)|(Data:\\s*)?))+";
    public static final String REGEX_DESCRICAO_ATIVIDADES_QUALIFICACAO = "(Descricao:\\s*[a-zA-Z \\-\\(\\),.0-9]+CHA:)+";
}
