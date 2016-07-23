/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExtrairAtividades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author raphael
 */
public class ExtrairAtividadesTest {
    
    public ExtrairAtividadesTest() {
    }
   

    /**
     * Test of getAtividadesEspeciais method, of class ExtrairAtividades.
     */
    @Test
    public void testGetAtividadesEspeciais() throws IOException {
        System.out.println("getAtividadesEspeciais");
        String texto = ExtrairAtividades.tratarPDF(ExtrairAtividades.getPdfTexto("files/Radoc-2011-Final.pdf"));
        List<Atividade> expResult = new ArrayList<>();
        Atividade atividade;
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("MEMBRO DE BANCA DE DEFESA DE DISSERTAÇÃO DE MESTRADO - NA INSTITUIÇÃO"));
        atividade.setCargaHoraria(12);
        atividade.setDataInicio("01/02/2011");
        atividade.setDataTermino("10/02/2011");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("MEMBRO DE BANCA DE DEFESA DE DISSERTAÇÃO DE MESTRADO - NA INSTITUIÇÃO"));
        atividade.setCargaHoraria(12);
        atividade.setDataInicio("01/06/2011");
        atividade.setDataTermino("15/06/2011");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("MEMBRO DE BANCA DE DEFESA DE DISSERTAÇÃO DE MESTRADO - EM OUTRA INSTITUIÇÃO"));
        atividade.setCargaHoraria(12);
        atividade.setDataInicio("01/11/2011");
        atividade.setDataTermino("22/11/2011");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("MEMBRO DE BANCA DE DEFESA DE DISSERTAÇÃO DE MESTRADO - EM OUTRA INSTITUIÇÃO"));
        atividade.setCargaHoraria(12);
        atividade.setDataInicio("01/02/2011");
        atividade.setDataTermino("10/02/2011");
        atividade.setDescricao(ExtrairAtividades.removeAcentos("MEMBRO DE BANCA DE DEFESA DE DISSERTAÇÃO DE MESTRADO - NA INSTITUIÇÃO"));
        atividade = new Atividade();
        atividade.setCargaHoraria(12);
        atividade.setDataInicio("01/08/2011");
        atividade.setDataTermino("26/08/2011");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("MEMBRO DE BANCA DE DEFESA DE MONOGRAFIA, PROJETO FINAL DE CURSO E OUTROS TIPOS DE BANCAS"));
        atividade.setCargaHoraria(4);
        atividade.setDataInicio("19/12/2011");
        atividade.setDataTermino("19/12/2011");
        expResult.add(atividade);
        
        List<Atividade> result = ExtrairAtividades.getAtividadesEspeciais(texto);
        int i = 0;
        for(Atividade a : expResult){
            assertEquals(expResult.get(i).getDescricao().toLowerCase(), 
                    result.get(i).getDescricao().toLowerCase());
            assertEquals(expResult.get(i).getCargaHoraria(), 
                    result.get(i).getCargaHoraria());
            assertEquals(expResult.get(i).getDataInicio(), 
                    result.get(i).getDataInicio());
            assertEquals(expResult.get(i).getDataTermino(), 
                    result.get(i).getDataTermino());
        }
    }

    /**
     * Test of getAtividadesOrientacao method, of class ExtrairAtividades.
     */
    @Test
    public void testGetAtividadesOrientacao() throws IOException {
        System.out.println("getAtividadesOrientacao");
        String texto = ExtrairAtividades.tratarPDF(ExtrairAtividades.getPdfTexto("files/Radoc-2012-Final.pdf"));
        List<Atividade> expResult = new ArrayList<>();
        Atividade atividade;
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("Evolução de uma Arquitetura para Frameworks de Aplicação de Sistemas de Informação - Uma Abordagem de Desenvolvimento Dirigido por Modelos., "
                + "ALUNO ORIENTADO EM DISSERTAÇÃO DE MESTRADO DEFENDIDA E APROVADA"));
        atividade.setCargaHoraria(48);
        atividade.setDataInicio("03/03/2010");
        atividade.setDataTermino("02/07/2012");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("UM ESTUDO COMPARATIVO DE FRAMEWORKS DE APLICAÇÃO PARA DESENVOLVIMENTO WEB, "
                + "ALUNO ORIENTADO EM PROJETO DE FINAL DE CURSO"));
        atividade.setCargaHoraria(11);
        atividade.setDataInicio("01/01/2012");
        atividade.setDataTermino("30/06/2012");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("UM ESTUDO SOBRE GOVERNANÇA DE TI: ASPECTOS DE CONTROLE FINANCEIRO, "
                + "ALUNO ORIENTADO EM PROJETO DE FINAL DE CURSO"));
        atividade.setCargaHoraria(11);
        atividade.setDataInicio("01/01/2012");
        atividade.setDataTermino("30/06/2012");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("GERAÇÃO DE BANCO DE DADOS TEMPORAIS COM MDD, "
                + "ALUNO ORIENTADO EM PROJETO DE FINAL DE CURSO"));
        atividade.setCargaHoraria(11);
        atividade.setDataInicio("01/01/2012");
        atividade.setDataTermino("30/12/2012");
        atividade.setDescricao(ExtrairAtividades.removeAcentos("MEMBRO DE BANCA DE DEFESA DE DISSERTAÇÃO DE MESTRADO - NA INSTITUIÇÃO"));
        
        List<Atividade> result = ExtrairAtividades.getAtividadesOrientacao(texto);
        int i = 0;
        for(Atividade a : expResult){
            assertEquals(expResult.get(i).getDescricao().toLowerCase(), 
                    result.get(i).getDescricao().toLowerCase());
            assertEquals(expResult.get(i).getCargaHoraria(), 
                    result.get(i).getCargaHoraria());
            assertEquals(expResult.get(i).getDataInicio(), 
                    result.get(i).getDataInicio());
            assertEquals(expResult.get(i).getDataTermino(), 
                    result.get(i).getDataTermino());
        }
    }

    /**
     * Test of getAtividadesExtensao method, of class ExtrairAtividades.
     */
    @Test
    public void testGetAtividadesExtensao() throws IOException {
        String texto = ExtrairAtividades.getPdfTexto("files/Radoc-2013-Final.pdf");
        texto = ExtrairAtividades.tratarPDF(texto);
        List<Atividade> expResult = new ArrayList<>();
        Atividade atividade;
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("PROMOÇÃO OU PRODUÇÃO DE EVENTOS ARTÍSTICOS E CIENTÍFICOS LOCAIS - COMISSÃO ORGANIZADORA, "
                + "XIII Jornada Goiana em Engenharia de Software, "
                + "Profissionais e estudantes da área de Tecnologia da Informação"));
        atividade.setCargaHoraria(40);
        atividade.setDataInicio("01/03/2013");
        atividade.setDataTermino("01/11/2013");
        expResult.add(atividade);
        
        List<Atividade> result = ExtrairAtividades.getAtividadesExtensao(texto);
        int i = 0;
        for(Atividade a : expResult){
            assertEquals(expResult.get(i).getDescricao().toLowerCase(), 
                    result.get(i).getDescricao().toLowerCase());
            assertEquals(expResult.get(i).getCargaHoraria(), 
                    result.get(i).getCargaHoraria());
            assertEquals(expResult.get(i).getDataInicio(), 
                    result.get(i).getDataInicio());
            assertEquals(expResult.get(i).getDataTermino(), 
                    result.get(i).getDataTermino());
        }
      
    
    }

    /**
     * Test of getAtividadesQualificao method, of class ExtrairAtividades.
     */
    @Test
    public void testGetAtividadesQualificao() throws IOException {
        String texto = ExtrairAtividades.tratarPDF(ExtrairAtividades.getPdfTexto("files/Radoc-2014-Final.pdf"));
        List<Atividade> expResult = new ArrayList<>();
        Atividade atividade;
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("Docente em licença para capacitação (Artigo 87, Lei N.8112), "
                + "Licença para Capacitação na área de Gestão de Serviços de Tecnologia da Informação, com fundamento no Artigo 87, da Lei nº 9527, de 10/12/97."));
        atividade.setCargaHoraria(440);
        atividade.setDataInicio("15/03/2014");
        atividade.setDataTermino("14/06/2014");
        expResult.add(atividade);
                atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("Participação em Congressos, Seminários, Encontros, Jornadas etc., "
                + "CNPq - 5o Encontro Técnico do RHAE Pesquisador na Empresa Brasília-DF"));
        atividade.setCargaHoraria(16);
        atividade.setDataInicio("11/11/2014");
        atividade.setDataTermino("12/11/2014");
        expResult.add(atividade);
        
        List<Atividade> result = ExtrairAtividades.getAtividadesQualificao(texto);
        int i = 0;
        for(Atividade a : expResult){
            assertEquals(expResult.get(i).getDescricao().toLowerCase(), 
                    result.get(i).getDescricao().toLowerCase());
            assertEquals(expResult.get(i).getCargaHoraria(), 
                    result.get(i).getCargaHoraria());
            assertEquals(expResult.get(i).getDataInicio(), 
                    result.get(i).getDataInicio());
            assertEquals(expResult.get(i).getDataTermino(), 
                    result.get(i).getDataTermino());
        }
      
       
    }

    /**
     * Test of getAtividadesAdministrativas method, of class ExtrairAtividades.
     */
    @Test
    public void testGetAtividadesAdministrativas() throws IOException {
        String texto = ExtrairAtividades.tratarPDF(ExtrairAtividades.getPdfTexto("files/Radoc-2015-Final.pdf"));
        List<Atividade> expResult = new ArrayList<>();
        Atividade atividade;
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.tratarPDF("Atividades acadêmicas e administrativas designadas por portaria do Reitor, Pró-Reitor ou Diretor de Unidade Acadêmica com carga horária >=150 horas, "
                + "Membro do Núcleo Docente Estruturante (NDE) do curso de Engenharia de Software"));
        atividade.setCargaHoraria(150);
        atividade.setDataInicio("01/01/2015");
        atividade.setDataTermino("31/12/2015");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.tratarPDF("Membros da CPPD ou da Comissão de Avaliação Institucional ou da Comissão Própria de Avaliação ou da CAD, "
                + "PRESIDENTE DA COMISSÃO DE AVALIAÇÃO DOCENTE - CAD - PARA TRATAR PRIORITARIAMENTE\n" +
                  "DOS PROCESSOS DE PROMOÇÃO À CLASSE D"));
        atividade.setCargaHoraria(120);
        atividade.setDataInicio("01/01/2015");
        atividade.setDataTermino("31/12/2015");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("Outras Atividades Administrativas e de Representação, "
                + "PARTICIPAÇÕES EM REUNIÕES DO CD 2015"));
        atividade.setCargaHoraria(9);
        atividade.setDataInicio("01/01/2015");
        atividade.setDataTermino("30/04/2015");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("Atividades acadêmicas e administrativas designadas por portaria do Reitor, Pró-Reitor ou Diretor de Unidade Acadêmica com carga horária >=150 horas, "
                + "Coordenador da Fábrica de Software do INF"));
        atividade.setCargaHoraria(150);
        atividade.setDataInicio("01/01/2015");
        atividade.setDataTermino("31/12/2015");
        expResult.add(atividade);
        atividade = new Atividade();
        atividade.setDescricao(ExtrairAtividades.removeAcentos("Outras Atividades Administrativas e de Representação, "
                + "Atualização do currículo na plataforma Lattes"));
        atividade.setCargaHoraria(8);
        atividade.setDataInicio("01/01/2015");
        atividade.setDataTermino("30/11/2015");
        expResult.add(atividade);
        
        List<Atividade> result = ExtrairAtividades.getAtividadesAdministrativas(texto);
        int i = 0;
        for(Atividade a : expResult){
            assertEquals(expResult.get(i).getDescricao().toLowerCase(), 
                    result.get(i).getDescricao().toLowerCase());
            assertEquals(expResult.get(i).getCargaHoraria(), 
                    result.get(i).getCargaHoraria());
            assertEquals(expResult.get(i).getDataInicio(), 
                    result.get(i).getDataInicio());
            assertEquals(expResult.get(i).getDataTermino(), 
                    result.get(i).getDataTermino());
        }
    }

    /**
     * Test of tratarPDF method, of class ExtrairAtividades.
     */
    @Test
    public void testTratarPDF() {
        assertEquals(
                ExtrairAtividades.tratarPDF("     Atividades acadêmicas e administrativas designadas por portaria do Reitor, Pró-Reitor ou Diretor de Unidade Acadêmica com carga horária >=150 horas \n  "),
                "atividades academicas e administrativas designadas por portaria do reitor, pro-reitor ou diretor de unidade academica com carga horaria >=150 horas");
    }

    /**
     * Test of removeAcentos method, of class ExtrairAtividades.
     */
    @Test
    public void testRemoveAcentos() {
        assertEquals(
                ExtrairAtividades.tratarPDF("Atividades acadêmicas e administrativas designadas por portaria do Reitor, Pró-Reitor ou Diretor de Unidade Acadêmica com carga horária >=150 horas"),
                "atividades academicas e administrativas designadas por portaria do reitor, pro-reitor ou diretor de unidade academica com carga horaria >=150 horas");
    }
    
}
