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


    private static HashMap<String,String> atividadesGrupo;

    /**
     * @param args, arquivos passados por linha de comando
     */
    public static void main(String[] args) throws IOException {
        if(args.length>0){
            atividadesGrupo = carregaAtividades("config/atividades.json");
            for(String arquivo : args){
                parse(arquivo);
            }
        }else{
            System.out.println("Você deve informar o nome do arquivo RADOC");
        }
    }

    /**
     * Inicia parser no arquivo Radoc.pdf
     * @param arquivo, caminho para arquivo radoc.pdf
     */
    private static void parse(String arquivo) throws IOException{
        String texto = getPdfTexto(arquivo);
        texto = tratarPDF(texto);
        BufferedWriter bw = criarNovoArquivo("AtividadesExtraidas_"+arquivo+".txt");
        List<Atividade> atividades = getAtividadesOrientacao(texto);
        int id = 1;
        for(Atividade atividade : atividades){
            atividade.setId(id);
            bw.write(atividade.toString());
            id++;
        }
        atividades = getAtividadesExtensao(texto);
        for(Atividade atividade : atividades){
            atividade.setId(id);
            bw.write(atividade.toString());
            id++;
        }
        atividades = getAtividadesQualificao(texto);
        for(Atividade atividade : atividades){
            atividade.setId(id);
            bw.write(atividade.toString());
            id++;
        }
        atividades = getAtividadesEspeciais(texto);
        for(Atividade atividade : atividades){
            atividade.setId(id);
            bw.write(atividade.toString());
            id++;
        }
        atividades  = getAtividadesAdministrativas(texto);
        for(Atividade atividade : atividades){
            atividade.setId(id);
            bw.write(atividade.toString());
            id++;
        }
        bw.close();
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
        //Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        //Matcher matcher = pattern.matcher(texto);
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
    private static List<Atividade> getAtividadesEspeciais(String text){
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
    private static List<Atividade> getAtividadesOrientacao(String text){
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
    private static List<Atividade> getAtividadesExtensao(String text) {
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
    private static List<Atividade> getAtividadesQualificao(String text){
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
    private static List<Atividade> getAtividadesAdministrativas(String text){
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
     * Remove espaços muito longos, quebras de linha, o cabeçario e o footer
     * @param texto, texto do pdf
     * @return texto devidamente tratado
     */
    private static String tratarPDF(String texto) {
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
    private static String removeAcentos(String string) {
        return Normalizer.normalize(string, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Retorna texto do pdf
     * @param caminho, do arquivo
     * @return texto do arquivo
     */
    private static String getPdfTexto(String caminho) throws IOException {
        Document pdf = PDF.open(caminho);
        StringBuilder texto = new StringBuilder(1024);
        pdf.pipe(new OutputTarget(texto));
        pdf.close();
        return texto.toString();
    }

    private static List<String> getCodGrupoPontuacao(String text,String regex)
    {
        List<String> listaCodGrupos = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            String valor = matcher.group().replaceAll(Regex.CAMPOS,"").trim();
            String codGrupo = atividadesGrupo.get(valor)!= null ? atividadesGrupo.get(valor) : "000000000000";
            listaCodGrupos.add(codGrupo);
            System.out.println(codGrupo);
            System.out.println(valor);
        }
        return listaCodGrupos;
    }

    /**
     * Cria novo arquivo ou escreve em cima de arquivo existente,
     * com informações adquiridas das atividades
     * @param caminho, do arquivo
     * @return BufferedWriter do arquivo
     */
    private static BufferedWriter criarNovoArquivo(String caminho) throws IOException {
        File novoArquivo = new File(caminho.replace(".pdf",""));
        if (!novoArquivo.exists()) {
            novoArquivo.createNewFile();
        }
        FileWriter fw = new FileWriter(novoArquivo.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        return bw;
    }

    private static HashMap<String,String> carregaAtividades(String caminho) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        HashMap<String, String> atividades = null;
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
                String valor = item.getString("codigo");
                atividades.put(chave, valor);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return atividades;
    }
}
