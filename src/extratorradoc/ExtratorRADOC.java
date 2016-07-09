package extratorradoc;

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

public class ExtratorRADOC {

    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        parse();        
    }
    public static void parse() throws IOException
    {
        String[] files = new String[]{
            "files/Radoc-2011-Final.pdf"};
        
        for(String file : files){
            Document pdf = PDF.open(file);
            StringBuilder texto = new StringBuilder(1024);
            pdf.pipe(new OutputTarget(texto));
            pdf.close();
            File novoArquivo = new File(file.replace(".pdf", ".txt"));


            if (!novoArquivo.exists()) {
                    novoArquivo.createNewFile();
            }

            FileWriter fw = new FileWriter(novoArquivo.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            String novoTexto = removeAcentos(texto.toString());
            novoTexto = novoTexto.trim().replaceAll("(\\s{2})+|([\n\r])+"," ");
            bw.write(novoTexto);
            bw.close();
            
            //getAtividadesOrientacao(newText);
            getAtividadesExtensao(novoTexto);
            List<Atividade> listOrientacao = getAtividadesOrientacao(novoTexto);
            for(Atividade a : listOrientacao){
                System.out.println(a.toString());
            }
            List<Atividade> listEspeciais = getAtividadesEspeciais(novoTexto);
            for(Atividade a : listEspeciais){
                System.out.println(a.toString());
            }
            //for(AtividadesExtensao s: list){
            //    System.out.println(s.toString());
            //}
            //System.out.println(file);
            //System.out.println(text);
        }
    }
    
    public static List<Atividade> getAtividadesEspeciais(String text){
        int aux = text.toLowerCase().indexOf("atividades academicas especiais");
        int aux2 = text.toLowerCase().indexOf("atividades administrativas");
        text = text.substring(aux,aux2).replace("\n", " ").replace("\r", " ");
        Pattern pattern = Pattern.compile(Regex.TITULO_DO_TRABALHO_ATIVIDADES_ORIENTACAO,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        List<Atividade> listMatches = new ArrayList<>();
        Atividade atividade;
        int i = 0;
        while(matcher.find()){
            atividade = new Atividade();
            atividade.setTipo("Acadêmicas Especiais");
            String descricao = matcher.group().replaceAll("(Tabela:)","").trim();
            atividade.setId(i);
            atividade.setDescricao(descricao);
            i++;
            listMatches.add(atividade);
        }
        return listMatches;
    }
    
    public static List<Atividade> getAtividadesOrientacao(String text)
    {
        int aux = text.indexOf("Atividades de orientacao");
        int aux2 = text.indexOf("Atividades em projetos");
        text = text.substring(aux,aux2).replace("\n", " ").replace("\r", " ");
        Pattern pattern = Pattern.compile(Regex.TITULO_DO_TRABALHO_ATIVIDADES_ORIENTACAO,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        List<Atividade> listMatches = new ArrayList<>();
        Atividade atividade;
        int i = 0;
        while (matcher.find()) {
            atividade = new Atividade();
            atividade.setTipo("Orientação");
            String descricao = matcher.group().replaceAll("(Titulo do trabalho:|Tabela:|Orientador Nivel:)","").trim();
            atividade.setId(i);
            atividade.setDescricao(descricao);
            i++;
            listMatches.add(atividade);
        }
        pattern = Pattern.compile(Regex.TABELA_ATIVIDADES_ORIENTACAO,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        i = 0;
        while (matcher.find()) {
            atividade = listMatches.get(i);
            String temp = matcher.group().replaceAll("(Titulo do trabalho:|Tabela:|Orientador Nivel:)","").trim();
            String descricao = atividade.getDescricao() + ", " + temp;
            atividade.setDescricao(descricao);
            i++;
        }
        pattern = Pattern.compile(Regex.CHA_ATIVIDADES_ORIENTACAO,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        i = 0;
        while (matcher.find()) {
            atividade = listMatches.get(i);
            int cha = Integer.valueOf(matcher.group().replaceAll("(CHA:)","").trim());
            atividade.setCargaHoraria(cha);
            i++;
        }
        pattern = Pattern.compile(Regex.DATA_INICIO_ATIVIDADES_ORIENTACAO,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        i = 0;
        while (matcher.find()) {
            atividade = listMatches.get(i);
            String dataInicio = matcher.group().replaceAll("(Data inicio:)","").trim();
            atividade.setDataInicio(dataInicio);
            i++;
        }
        pattern = Pattern.compile(Regex.DATA_TERMINO_ATIVIDADES_ORIENTACAO,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        i = 0;
        while (matcher.find()) {
            atividade = listMatches.get(i);
            String dataTermino = matcher.group().replaceAll("(Data termino:)","").trim();
            atividade.setDataTermino(dataTermino);
            i++;
        }
        return listMatches;
    }
    
    public static void getAtividadesExtensao(String text){
        int escopoInicio = text.toLowerCase().indexOf("atividades de extensao");
        int escopoFinal = text.toLowerCase().indexOf("atividades de qualificacao");
        String escopoTexto = text.substring(escopoInicio,escopoFinal);
        Pattern padrao = Pattern.compile(Regex.REGEX_TITULO_ATIVIDADES_EXTENSAO,Pattern.CASE_INSENSITIVE);
        Matcher correspondente = padrao.matcher(escopoTexto);
        List<Atividade> listaAtividades = new ArrayList<>();
        Atividade atividade;
        int i=0;
        while(correspondente.find()){
            String temp = correspondente.group().replaceAll("(\\s*Tabela:\\s*)|(\\s*CHA:)","");
            atividade = new Atividade();
            atividade.setId(i);
            atividade.setDescricao(temp);
            listaAtividades.add(atividade);
            ++i;
            System.out.println(temp);
        }
        padrao = Pattern.compile(Regex.REGEX_CHA_ATIVIDADES,Pattern.CASE_INSENSITIVE);
        correspondente = padrao.matcher(escopoTexto);
        i=0;
        while(correspondente.find()){
            int temp = Integer.valueOf(correspondente.group().replaceAll("(\\s*CHA:)","").trim());
            atividade = listaAtividades.get(i);
            atividade.setCargaHoraria(temp);
            ++i;
            System.out.println(temp);
        }
        padrao = Pattern.compile(Regex.REGEX_DESCRICAO_ATIVIDADES_EXTENSAO,Pattern.CASE_INSENSITIVE);
        correspondente = padrao.matcher(escopoTexto);
        i=0;
        while(correspondente.find()){
            String temp = correspondente.group().replaceAll("(\\s*Descricao da atividade:\\s*)|(\\s*Descricao da clientela:\\s*)","").trim();
            atividade = listaAtividades.get(i);
            temp += ", ";
            temp += atividade.getDescricao();
            atividade.setDescricao(temp);
            ++i;
            System.out.println(temp);
        }
        padrao = Pattern.compile(Regex.REGEX_DESCRICAO_CLIENTELA_ATIVIDADES_EXTENSAO,Pattern.CASE_INSENSITIVE);
        correspondente = padrao.matcher(escopoTexto);
        i=0;
        while(correspondente.find()){
            String temp = correspondente.group().replaceAll("(\\s*Descricao da clientela:\\s*)|(\\s*Tabela:\\s*)","").trim();
            System.out.println(temp);
            //atividade = listaAtividades.get(i);
            //temp += ", ";
            //temp += atividade.getDescricao();
            //atividade.setDescricao(temp);
            //++i;
        }
        padrao = Pattern.compile(Regex.REGEX_DATA_INICIO_ATIVIDADES,Pattern.CASE_INSENSITIVE);
        correspondente = padrao.matcher(escopoTexto);
        i=0;
        while(correspondente.find()){
            String temp = correspondente.group().replaceAll("(\\s*Data\\s*inicio:\\s*)","").trim();
            atividade = listaAtividades.get(i);
            atividade.setDataInicio(temp);
            ++i;
            System.out.println(temp);
        }
        padrao = Pattern.compile(Regex.REGEX_DATA_TERMINO_ATIVIDADES,Pattern.CASE_INSENSITIVE);
        correspondente = padrao.matcher(escopoTexto);
        i=0;
        while(correspondente.find()){
            String temp = correspondente.group().replaceAll("(\\s*Data\\s*termino:\\s*)","").trim();
            atividade = listaAtividades.get(i);
            atividade.setDataTermino(temp);
            ++i;
            System.out.println(temp);
        }
        //return listaAtividades.size()>0 ? listaAtividades : null;
    }
    
    public static String removeAcentos(String string) {
        return Normalizer.normalize(string, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
