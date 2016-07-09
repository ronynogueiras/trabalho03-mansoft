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
public class AtividadesExtensao {

    private int id;
    private int hours;
    private String description;
    private String initDate;
    private String finalDate;

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
     * @return the hours
     */
    public int getHours() {
        return hours;
    }

    /**
     * @param hours the hours to set
     */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }    

    /**
     * @return the initDate
     */
    public String getInitDate() {
        return initDate;
    }

    /**
     * @param initDate the initDate to set
     */
    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    /**
     * @return the finalDate
     */
    public String getFinalDate() {
        return finalDate;
    }

    /**
     * @param finalDate the finalDate to set
     */
    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }
    @Override
    public String toString(){
        String str = "ID: "+this.id;
        str += "\nHoras: "+this.hours;
        str += "\nDescricao: "+this.description;
        str += "\nData inicio: "+this.initDate;
        str += "\nData termino: "+this.finalDate;
        return str;
    }
}
