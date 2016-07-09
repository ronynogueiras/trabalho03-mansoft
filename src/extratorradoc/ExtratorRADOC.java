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
            StringBuilder text = new StringBuilder(1024);
            pdf.pipe(new OutputTarget(text));
            pdf.close();
            File newFile = new File(file.replace(".pdf", ".txt"));


            if (!newFile.exists()) {
                    newFile.createNewFile();
            }

            FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            String newText = removeDiacriticalMarks(text.toString());
            bw.write(newText.trim().replaceAll("(\\s{2})+|([\n])+"," "));
            bw.close();
            
            //getAtividadesOrientacao(newText);
            List<AtividadesExtensao> listExtencao = getAtividadesExtensao(newText);
            List<AtividadesOrientacao> listOrientacao = getAtividadesOrientacao(newText);
            //for(AtividadesExtensao s: list){
            //    System.out.println(s.toString());
            //}
            //System.out.println(file);
            //System.out.println(text);
        }
    }
    
    
    public static List<AtividadesOrientacao> getAtividadesOrientacao(String text)
    {
        int aux = text.indexOf("Atividades de orientacao");
        System.out.println(aux);
        int aux2 = text.indexOf("Atividades em projetos");
        System.out.println(aux2);
        text = text.substring(aux,aux2).replace("\n", " ").replace("\r", " ");
        Pattern pattern = Pattern.compile(Regex.TITULO_DO_TRABALHO_ATIVIDADES_ORIENTACAO,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        List<AtividadesOrientacao> listMatches = new ArrayList<>();
        AtividadesOrientacao atividade;
        int i = 0;
        while (matcher.find()) {
            atividade = new AtividadesOrientacao();
            String temp = matcher.group().replaceAll("(Titulo do trabalho:|Tabela:|Orientador Nivel:)","");
            atividade.setDescricao(temp);
            atividade.set
            i++;
            //temp = temp.replaceAll("(\\s{2})+",";");
            listMatches.add(atividade);
        }
        return listMatches;
    }
    
    public static List<AtividadesExtensao> getAtividadesExtensao(String text){
        int initScope = text.toLowerCase().indexOf("atividades de extensao");
        int finalScope = text.toLowerCase().indexOf("atividades de qualificacao");
        String scopeText = text.substring(initScope, finalScope).replace("\n", " ").replace("\r", " ");
        Pattern pattern = Pattern.compile(Regex.REGEX_TITULO_ATIVIDADES_EXTENSAO,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(scopeText);
        List<AtividadesExtensao> listMatches = new ArrayList<>();
        AtividadesExtensao atividadesExtensao;
        int i=0;
        while(matcher.find()){
            String descricao = matcher.group().replaceAll("(\\s*Tabela:\\s*)|(\\s*CHA:)","");
            atividadesExtensao = new AtividadesExtensao();;
            atividadesExtensao.setId(i);
            atividadesExtensao.setDescription(descricao);
            listMatches.add(atividadesExtensao);
            ++i;
            //System.out.println(temp);
        }
        pattern = Pattern.compile(Regex.REGEX_CHA_ATIVIDADES,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(scopeText);
        i=0;
        while(matcher.find()){
            int cha = Integer.valueOf(matcher.group().replaceAll("(\\s*CHA:)","").trim());
            atividadesExtensao = listMatches.get(i);
            atividadesExtensao.setHours(cha);
            ++i;
            //System.out.println(temp);
        }
        pattern = Pattern.compile(Regex.REGEX_DESCRICAO_ATIVIDADES_EXTENSAO,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(scopeText);
        i=0;
        while(matcher.find()){
            String temp = matcher.group().replaceAll("(\\s*Descricao da atividade:\\s*)|(\\s*Descricao da clientela:\\s*)","").trim();
            atividadesExtensao = listMatches.get(i);
            temp += ", ";
            temp += atividadesExtensao.getDescription();
            atividadesExtensao.setDescription(temp);
            ++i;
            //System.out.println(temp);
        }
        pattern = Pattern.compile(Regex.REGEX_DESCRICAO_CLIENTELA_ATIVIDADES_EXTENSAO,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(scopeText);
        i=0;
        while(matcher.find()){
            String temp = matcher.group().replaceAll("(\\s*Descricao da clientela:\\s*)|(\\s*Tabela:\\s*)","").trim();
            atividadesExtensao = listMatches.get(i);
            temp += ", ";
            temp += atividadesExtensao.getDescription();
            atividadesExtensao.setDescription(temp);
            ++i;
        }
        pattern = Pattern.compile(Regex.REGEX_DATA_INICIO_ATIVIDADES,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(scopeText);
        i=0;
        while(matcher.find()){
            String temp = matcher.group().replaceAll("(\\s*Data\\s*inicio:\\s*)","").trim();
            atividadesExtensao = listMatches.get(i);
            atividadesExtensao.setInitDate(temp);
            ++i;
            System.out.println(temp);
        }
        pattern = Pattern.compile(Regex.REGEX_DATA_TERMINO_ATIVIDADES,Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(scopeText);
        i=0;
        while(matcher.find()){
            String temp = matcher.group().replaceAll("(\\s*Data\\s*termino:\\s*)","").trim();
            //atividadesExtensao = listMatches.get(i);
            //atividadesExtensao.setFinalDate(temp);
            //++i;
            System.out.println(temp);
        }
        return listMatches.size()>0 ? listMatches : null;
    }
    
    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
