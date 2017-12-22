/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;


/**
 *
 * @author eduardofigueiredo
 */
public class No {
    public No esq;
    public No dir;
    public String restricao;
    
    public No(String restricao) {
        esq = null;
        dir = null;
        this.restricao = restricao;
    }
     
    public void inserirOsdoislados(No no, String restricao){
        String[] palavra = restricao.split("=");
        String valores[] = palavra[1].split(",");
        double valor1 = Double.parseDouble(valores[0]);
        double valor2 = valor1 + 1;

        //insere parte inteira da restricao na esquerda e inteira +1 na direita
        if(no.esq == null){
            no.esq = new No(palavra[0] + " >= " + (int)valor1);
            no.dir = new No(palavra[0] + " <= " + (int)valor2);
        }else{
            inserirOsdoislados(no.esq,restricao);
        }   
    }
}