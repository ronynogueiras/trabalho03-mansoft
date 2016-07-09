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
            "files/Radoc-2011-Final.pdf"
            ,"files/Radoc-2012-Final.pdf"
            ,"files/Radoc-2013-Final.pdf"
            ,"files/Radoc-2014-Final.pdf"
            ,"files/Radoc-2015-Final.pdf"};
        
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
            bw.write(newText.trim().replaceAll("(\\s{2})+"," "));
            bw.close();
            
            getAtividadesOrientacao(newText);
            //System.out.println(file);
            //System.out.println(text);
        }
    }
    
    
    public static void getAtividadesOrientacao(String text)
    {
        int aux = text.indexOf("Atividades de orientacao");
        System.out.println(aux);
        int aux2 = text.indexOf("Atividades em projetos");
        System.out.println(aux2);
        text = text.substring(aux,aux2);
        Pattern pattern = Pattern.compile("(Titulo\\s+do\\s+trabalho:\\s*[a-zA-Z:_ .-]*\\s*Tabela:)|(CHA:\\s*\\d+\\s*(Data\\s*inicio:\\s*)?\\d{2}\\/\\d{2}\\/\\d{4}\\s*(Data\\s*termino:\\s*)?\\d{2}\\/\\d{2}\\/\\d{4})+",Pattern.CASE_INSENSITIVE);
        // in case you would like to ignore case sensitivity,
        // you could use this statement:
        // Pattern pattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        // check all occurance
        StringBuilder str = new StringBuilder();
        int index=1;
        int auxIdx=1;
        while (matcher.find()) {
            String temp = matcher.group().replaceAll("(Titulo do trabalho:\\s*)|(\\s*Tabela:)|(CHA:\\s*)|(Data\\s*inicio:\\s*)|(Data\\s*termino:\\s*)+","");
            temp = temp.replaceAll("(\\s{2})+",";");
            str.append(temp).append(";");
        }
        System.out.println(str.toString());
    }
    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
