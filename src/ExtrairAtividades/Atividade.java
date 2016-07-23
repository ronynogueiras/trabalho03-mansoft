/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ExtrairAtividades;

/**
 *
 * @author alunoinf
 */
public class Atividade {
    private int id;
    private int cargaHoraria;
    private String tipo;
    private String descricao;
    private String dataInicio;
    private String dataTermino;
    private String codGrupoPontuacao;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the cargaHoraria
     */
    public int getCargaHoraria() {
        return cargaHoraria;
    }

    /**
     * @param cargaHoraria the cargaHoraria to set
     */
    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the dataInicio
     */
    public String getDataInicio() {
        return dataInicio;
    }

    /**
     * @param dataInicio the dataInicio to set
     */
    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @return the dataTermino
     */
    public String getDataTermino() {
        return dataTermino;
    }

    /**
     * @param dataTermino the dataTermino to set
     */
    public void setDataTermino(String dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getCodGrupoPontuacao() {
        return codGrupoPontuacao;
    }

    public void setCodGrupoPontuacao(String codGrupoPontuacao) {
        this.codGrupoPontuacao = codGrupoPontuacao;
    }
    @Override
    public String toString(){
        String str = "ID: "+this.id+"\n";
        str += "Tipo: "+this.tipo+"\n";
        str += "Codigo Grupo: "+this.codGrupoPontuacao+"\n";
        str += "Carga Horaria: "+this.cargaHoraria+"\n";
        str += "Descricao: "+this.descricao+"\n";
        str += "Data Inicio: "+this.dataInicio+"\n";
        str += "Data Termino:"+this.dataTermino+"\n";
        str += "-------------------------\n";
        return str;
    }

}
