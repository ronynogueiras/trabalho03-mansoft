package ExtrairAtividades;

import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.Document;
import com.snowtide.PDF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.binding.StringBinding;

/**
 * Extrai atividades de arquivo RADOC
 */
public class ExtrairAtividades {

    /**
     * @param args, arquivos passados por linha de comando
     */
    public static void main(String[] args) throws IOException {
        parse(args.length != 0 ? args[0] : null);
    }

    /**
     * Inicia parser no arquivo Radoc.pdf
     * @param path, caminho para arquivo radoc.pdf
     */
    public static void parse(String path) throws IOException{
        String[] files;
        if(path != null){
                files = new String[]{path};
        }
        else {
                files = new String[]{
                    "files/Radoc-2011-Final.pdf",
                    "files/Radoc-2012-Final.pdf",
                    "files/Radoc-2013-Final.pdf",
                    "files/Radoc-2014-Final.pdf",
                    "files/Radoc-2015-Final.pdf"
            };
        }
        for(String file : files){
            String texto = tratarPDF(getPdfTexto(file));
            BufferedWriter bw = criarNovoArquivo(file);
            for(Atividade atividade : getAtividadesOrientacao(texto)){
                bw.write(atividade.toString());
            }
            for(Atividade atividade : getAtividadesExtensao(texto)){
                bw.write(atividade.toString());
            }
            for(Atividade atividade : getAtividadesQualificao(texto)){
                bw.write(atividade.toString());
            }
            for(Atividade atividade : getAtividadesEspeciais(texto)){
                bw.write(atividade.toString());
            }
            for(Atividade atividade : getAtividadesAdministrativas(texto)){
                bw.write(atividade.toString());
            }
            bw.close();
        }
    }

    /**
     * Retorna lista de valores encontrador por regex
     * já devidamente tratados
     * @param regex, para encontrar os valores
     * @param texto, para ser analisado
     * @return List<String>, lista de valores
     */
    private static List<String> getValores(String regex, String texto){
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(texto);
        List<String> valores = new ArrayList<>();
        while(matcher.find()){
            String valor = matcher.group().replaceAll(Regex.CAMPOS,"").trim();
            valores.add(valor);
        }
        return valores;
    }

    /**
     * Retorna lista de atividades apartir de um regex e um texto
     * @param regex, para encontrar as atividades
     * @param texto, para ser analisado
     * @param tipo, das atividades
     * @return List<Atividade>, lista de atividades
     */
    private static List<Atividade> getAtividades(String regex, String texto, String tipo){
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(texto);
        List<Atividade> atividades = new ArrayList<>();
        Atividade atividade;
        int i = 0;
        List<String> valores = getValores(regex,texto);
        for(String valor : valores){
            atividade = new Atividade();
            atividade.setTipo(tipo);
            atividade.setId(i);
            atividade.setDescricao(valor);
            i++;
            atividades.add(atividade);
        }
        return atividades;
    }

    /**
     * Restorna lista de Atividades Academicas Especiais
     * @param text, texto do arquivo pdf
     * @return List<Atividade>
     */
    public static List<Atividade> getAtividadesEspeciais(String text){
        int escopoInicio = text.indexOf("atividades academicas especiais");
        int escopoFinal = text.indexOf("atividades administrativas");
        text = text.substring(escopoInicio,escopoFinal);
        List<Atividade> atividades = getAtividades(Regex.TABELA, text, "Acadêmicas Especiais");
        List<String> CHA = getValores(Regex.CHA, text);
        List<String> DATA_INICIO = getValores(Regex.DATA_INICIO, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA_TERMINO, text);
        int i = 0;
        for(Atividade atividade : atividades){
            atividade.setCargaHoraria(Integer.valueOf(CHA.get(i)));
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
        }
        return atividades;
    }

    /**
     * Restorna lista de Atividades de Orientacao
     * @param text, texto do arquivo pdf
     * @return List<Atividade>
     */
    public static List<Atividade> getAtividadesOrientacao(String text){
        int escopoInicio = text.indexOf("atividades de orientacao");
        int escopoFinal = text.indexOf("atividades em projetos");
        text = text.substring(escopoInicio,escopoFinal);
        List<Atividade> atividades = getAtividades(Regex.TITULO_DO_TRABALHO, text, "Orientação");
        List<String> TABELA = getValores(Regex.TABELA, text);
        List<String> CHA = getValores(Regex.CHA, text);
        List<String> DATA_INICIO = getValores(Regex.DATA_INICIO, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA_TERMINO, text);
        int i = 0;
        for(Atividade atividade : atividades){
            String descricao = atividade.getDescricao() +", "+ TABELA.get(i);
            atividade.setCargaHoraria(Integer.valueOf(CHA.get(i)));
            atividade.setDescricao(descricao);
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
        }
        return atividades;
    }

    /**
     * Restorna lista de Atividades Extensao
     * @param text, texto do arquivo pdf
     * @return List<Atividade>
     */
    public static List<Atividade> getAtividadesExtensao(String text) {
        int escopoInicio = text.indexOf("atividades de extensao");
        int escopoFinal = text.indexOf("atividades de qualificacao");
        StringBuilder customRegex = new StringBuilder(Regex.DESCRICAO_CLIENTELA
                .replace("(.*?)","[a-zA-Z ,.\\-()]*"));
        customRegex.setCharAt(customRegex.length() - 1, '|');
        text = text.substring(escopoInicio,escopoFinal);
        List<Atividade> atividades = getAtividades(Regex.TABELA, text, "Extensão");
        List<String> DESCRICAO_ATIVIDADE = getValores(Regex.DESCRICAO_ATIVIDADE, text);
        List<String> DESCRICAO_CLIENTELA = getValores(customRegex.toString() + ")", text);
        
        List<String> CHA = getValores(Regex.CHA, text);
        List<String> DATA_INICIO = getValores(Regex.DATA_INICIO, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA_TERMINO, text);
        int i = 0;
        for(Atividade atividade : atividades){
            String descricao = atividade.getDescricao() +
                    ", "+ DESCRICAO_ATIVIDADE.get(i) +
                    ", "+ DESCRICAO_CLIENTELA.get(i);
            atividade.setCargaHoraria(Integer.valueOf(CHA.get(i)));
            atividade.setDescricao(descricao);
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
        }
        return atividades;
    }

    /**
     * Restorna lista de Atividades de Qualificao
     * @param text, texto do arquivo pdf
     * @return List<Atividade>
     */
    public static List<Atividade> getAtividadesQualificao(String text){
        int escopoInicio = text.indexOf("atividades de qualificacao");
        int escopoFinal = text.indexOf("atividades academicas especiais");
        text = text.substring(escopoInicio,escopoFinal);
        List<Atividade> atividades = getAtividades(Regex.TABELA, text, "Qualificacao");
        List<String> DESCRICAO = getValores(Regex.DESCRICAO, text);
        List<String> CHA = getValores(Regex.CHA, text);
        List<String> DATA_INICIO = getValores(Regex.DATA_INICIO, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA_TERMINO, text);
        int i = 0;
        for(Atividade atividade : atividades){
            String descricao = atividade.getDescricao() +", "+ DESCRICAO.get(i);
            atividade.setCargaHoraria(Integer.valueOf(CHA.get(i)));
            atividade.setDescricao(descricao);
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
        }
        return atividades;
    }

    /**
     * Restorna lista de Atividades Administrativas
     * @param text, texto do arquivo pdf
     * @return List<Atividade>
     */
    public static List<Atividade> getAtividadesAdministrativas(String text){
        int escopoInicio = text.toLowerCase().indexOf("atividades administrativas");
        int escopoFinal = text.toLowerCase().indexOf("produtos descricao do produto:");
        text = text.substring(escopoInicio,escopoFinal);
        List<Atividade> atividades = getAtividades(Regex.TABELA, text, "Administração");
        List<String> DESCRICAO = getValores(Regex.DESCRICAO, text);
        List<String> CHA = getValores(Regex.CHA, text);
        List<String> DATA_INICIO = getValores(Regex.DATA_INICIO, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA_TERMINO, text);
        int i = 0;
        for(Atividade atividade : atividades){
            String descricao = atividade.getDescricao() +", "+ DESCRICAO.get(i);
            atividade.setCargaHoraria(Integer.valueOf(CHA.get(i)));
            atividade.setDescricao(descricao);
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
        }
        return atividades;
    }

    /**
     * Remove espaços muito longos, quebras de linha, o cabeçario e o footer
     * @param texto, texto do pdf
     * @return texto devidamente tratado
     */
    public static String tratarPDF(String texto) {
        return removeAcentos(texto.trim().replaceAll("(\\s{2})+|([\n\r])+"," ")) //Remove espaços extras e quebras de linhas
                .replaceAll("(?i)Data:[ 0-9/: a-zA-Z]*(Pagina[ 0-9/]*)", "") //Remove footer
                .replaceAll("(?i)UNIVERSIDADE(\\s+)FEDERAL(\\s+)DE(\\s+)GOIAS(\\s+)SISTEMA(\\s+)DE(\\s+)CADASTRO(\\s+)DE(\\s+)ATIVIDADES(\\s+)DOCENTES(\\s+)EXTRATO(\\s+)DAS(\\s+)ATIVIDADES(\\s+)-(\\s+)ANO(\\s+)BASE:(\\s+)[0-9]{4}","") // Remove cabeçario
                .toLowerCase();
    }

    /**
     * Remove acentos de string
     * @param string, texto com acentos
     * @return string sem acentos
     */
    public static String removeAcentos(String string) {
        return Normalizer.normalize(string, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Retorna texto do pdf
     * @param caminho, do arquivo
     * @return texto do arquivo
     */
    public static String getPdfTexto(String caminho) throws IOException {
        Document pdf = PDF.open(caminho);
        StringBuilder texto = new StringBuilder(1024);
        pdf.pipe(new OutputTarget(texto));
        pdf.close();
        return texto.toString();
    }

    /**
     * Cria novo arquivo ou escreve em cima de arquivo existente,
     * com informações adquiridas das atividades
     * @param caminho, do arquivo
     * @return BufferedWriter do arquivo
     */
    public static BufferedWriter criarNovoArquivo(String caminho) throws IOException {
        File novoArquivo = new File(caminho.replace(".pdf", ".txt"));
        if (!novoArquivo.exists()) {
            novoArquivo.createNewFile();
        }
        FileWriter fw = new FileWriter(novoArquivo.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        return bw;
    }
}
