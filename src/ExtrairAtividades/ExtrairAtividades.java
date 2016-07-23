package ExtrairAtividades;

import com.snowtide.pdf.OutputTarget;
import com.snowtide.pdf.Document;
import com.snowtide.PDF;

import java.io.*;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.binding.StringBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Extrai atividades de arquivo RADOC
 */
public class ExtrairAtividades {


    public static HashMap<String,String[]> atividadesGrupo;

    /**
     * @param args, arquivos passados por linha de comando
     */
    public static void main(String[] args) throws IOException {
        atividadesGrupo = carregaAtividades("config/atividades.json");
        if(args.length>0){
            for(String arquivo : args){
                parse(arquivo);
            }
        }else{
            parse(null);
            System.out.println("Você deve informar o nome do arquivo RADOC");
        }
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
            for(Atividade atividade : getProdutos(texto)){
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
        List<String> codGrupo = getCodGrupoPontuacao(text,Regex.TABELA);
        List<String> CHA = getValores(Regex.CHA, text);
        List<String> DATA_INICIO = getValores(Regex.DATA_INICIO, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA_TERMINO, text);
        int i = 0;
        for(Atividade atividade : atividades){
            atividade.setCargaHoraria(Integer.valueOf(CHA.get(i)));
            atividade.setCodGrupoPontuacao(codGrupo.get(i));
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
            i++;
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
        List<String> codGrupo = getCodGrupoPontuacao(text,Regex.TABELA);
        List<String> TABELA = getValores(Regex.TABELA, text);
        List<String> CHA = getValores(Regex.CHA, text);
        List<String> DATA_INICIO = getValores(Regex.DATA_INICIO, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA_TERMINO, text);
        int i = 0;
        for(Atividade atividade : atividades){
            String descricao = atividade.getDescricao() +", "+ TABELA.get(i);
            atividade.setCargaHoraria(Integer.valueOf(CHA.get(i)));
            atividade.setDescricao(descricao);
            atividade.setCodGrupoPontuacao(codGrupo.get(i));
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
            i++;
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
        List<String> codGrupo = getCodGrupoPontuacao(text,Regex.TABELA);
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
            atividade.setCodGrupoPontuacao(codGrupo.get(i));
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
            i++;
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
        List<String> codGrupo = getCodGrupoPontuacao(text,Regex.TABELA);
        List<String> DESCRICAO = getValores(Regex.DESCRICAO, text);
        List<String> CHA = getValores(Regex.CHA, text);
        List<String> DATA_INICIO = getValores(Regex.DATA_INICIO, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA_TERMINO, text);
        int i = 0;
        for(Atividade atividade : atividades){
            String descricao = atividade.getDescricao() +", "+ DESCRICAO.get(i);
            atividade.setCargaHoraria(Integer.valueOf(CHA.get(i)));
            atividade.setDescricao(descricao);
            atividade.setCodGrupoPontuacao(codGrupo.get(i));
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
            i++;
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
        List<String> codGrupo = getCodGrupoPontuacao(text,Regex.TABELA);
        List<String> DESCRICAO = getValores(Regex.DESCRICAO, text);
        List<String> CHA = getValores(Regex.CHA, text);
        List<String> DATA_INICIO = getValores(Regex.DATA_INICIO, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA_TERMINO, text);
        int i = 0;
        for(Atividade atividade : atividades){
            String descricao = atividade.getDescricao() +", "+ DESCRICAO.get(i);
            atividade.setCargaHoraria(Integer.valueOf(CHA.get(i)));
            atividade.setCodGrupoPontuacao(codGrupo.get(i));
            atividade.setDescricao(descricao);
            atividade.setDataInicio(DATA_INICIO.get(i));
            atividade.setDataTermino(DATA_TERMINO.get(i));
            i++;
        }
        return atividades;
    }

    /**
     * Restorna lista de produtos
     * @param text, texto do arquivo pdf
     * @return List<Atividade>
     */
    public static List<Atividade> getProdutos(String text){
        int escopoInicio = text.toLowerCase().indexOf("produtos");
        int escopoFinal = text.length();
        text = text.substring(escopoInicio,escopoFinal);
        List<Atividade> atividades = getAtividades(Regex.DESCRICAO_PRODUTO, text, "produtos");
        List<String> DESCRICAO = getValores(Regex.TITULO_PRODUTO, text);
        List<String> DATA_INICIO = getValores(Regex.DATA, text);
        List<String> DATA_TERMINO = getValores(Regex.DATA, text);

        int i = 0;
        for(Atividade atividade : atividades){
            String descricao = atividade.getDescricao() +", "+ DESCRICAO.get(i);
            if(DATA_INICIO.get(i) == DATA_TERMINO.get(i)){
                atividade.setCargaHoraria(8);
            }
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
                .replaceAll("(?i)(data:(\\s*)?([0-9]{2}\\/)([0-9]{2}\\/)([0-9]{4})(\\s*)?([0-9]{2}:){2}([0-9]{2})(.*?)(pagina)(\\s*)?([0-9]*(\\s*)?\\/(\\s*)?[0-9]*))", "") //Remove footer
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
        File novoArquivo = new File(caminho.replace(".pdf",""));
        if (!novoArquivo.exists()) {
            novoArquivo.createNewFile();
        }
        FileWriter fw = new FileWriter(novoArquivo.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        return bw;
    }

    public static HashMap<String,String[]> carregaAtividades(String caminho) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        HashMap<String, String[]> atividades = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String linha = br.readLine();

            while (linha != null) {
                stringBuilder.append(linha);
                stringBuilder.append(System.lineSeparator());
                linha = br.readLine();
            }
            String leitura = stringBuilder.toString();
            JSONObject config = new JSONObject(leitura);
            JSONArray itens = config.getJSONArray("atividades");
            int idx;
            atividades = new HashMap<>(itens.length());
            for (idx = 0; idx < itens.length(); idx++) {
                JSONObject item = itens.getJSONObject(idx);
                String chave = item.getString("idunico");
                String codigo = item.getString("codigo");
                String pontos = item.getString("pontos");
                String[] dados = new String[]{codigo,pontos};
                atividades.put(chave, dados);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return atividades;
    }

    public static List<String> getCodGrupoPontuacao(String text,String regex)
    {
        List<String> listaCodGrupos = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            String valor = matcher.group().replaceAll(Regex.CAMPOS,"").trim();
            String codGrupo = atividadesGrupo.get(valor)[0]!= null ? atividadesGrupo.get(valor)[0] : "000000000000";
            listaCodGrupos.add(codGrupo);
            System.out.println(codGrupo);
            System.out.println(valor);
        }
        return listaCodGrupos;
    }
}
