/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author eduardofigueiredo
 */
public class BeB {
    ArrayList<String> resposta = new ArrayList<>();
    No raiz;
    public String entrada;
    DecimalFormat decimal = new DecimalFormat("0.00");
    int tamanho;
    
    public BeB(String entrada, ArrayList<String> respostasbb,int linha){
        //tamanho quantidade de variaveis de decisao existem
        tamanho = linha;
        executarInicio(entrada,respostasbb);
    }
    
    public void executarInicio(String entrada, ArrayList<String> respostasbb){
        this.entrada = entrada;
        //so tenta executar se for diferente de nao
        if(respostasbb.get(0).equals("sim")){
            for(int i=1;i<respostasbb.size();i++){
                String[] separando = respostasbb.get(i).split("=");//separando x2=3.2
                String[] valorpositivo = separando[1].split(",");//pegando parte inteira de 3.2
                double valor = Double.parseDouble(valorpositivo[0]);//pegar o valor positivo 3.0
                separando[1] = separando[1].replace(",", ".");
                double valorinicial = Double.parseDouble(separando[1]);//pegar o valor completo 3.2
                
                int valorinteiro = (int)valor;
                String auxilia = decimal.format(valorinicial);
                auxilia = auxilia.replace(",", ".");

                if(Double.parseDouble(auxilia)-valorinteiro != 0){//verifica se o valor é diferente de positivo
                    raiz = new No(respostasbb.get(i));
                    raiz.inserirOsdoislados(raiz, separando[0] + "=" + separando[1].replace(".", ","));
                    i = respostasbb.size();

                    //executa no simplex com o no da esquerda e dpois o no da direita, se existir esquerda sempre existira direita
                    if(raiz.esq != null){
                        //System.out.println("\nesquerda ----------\n");
                        executar(raiz.esq,this.entrada);
                    }
                    if(raiz.dir != null){
                        //System.out.println("\ndireita -----------\n");
                        executar(raiz.dir,this.entrada);
                    }
                }
            }
        }
    }
    
    public void executar2parte(ArrayList<String> respostasbb, No no, String novaentrada){
        if(respostasbb.get(0).equals("sim")){
            for(int i=1;i<respostasbb.size();i++){
                String[] separando = respostasbb.get(i).split("=");//separando x2=3.2
                String[] valorpositivo = separando[1].split(",");//pegando parte inteira de 3.2
                double valor = Double.parseDouble(valorpositivo[0]);//pegar o valor positivo 3.0
                separando[1] = separando[1].replace(",", ".");
                double valorinicial = Double.parseDouble(separando[1]);//pegar o valor completo 3.2
      
                int valorinteiro = (int)valor;
                String auxilia = decimal.format(valorinicial);
                auxilia = auxilia.replace(",", ".");
                
                if(Double.parseDouble(auxilia)-valorinteiro != 0){//verifica se o valor é diferente de positivo
                    no = new No(separando[0] + "=" + separando[1].replace(".", ","));
                    no.inserirOsdoislados(no, separando[0] + "=" + separando[1].replace(".", ","));
                    i = respostasbb.size();

                    //executa no simplex com o no da esquerda e dpois o no da direita, se existir esquerda sempre existira direita
                    if(no.esq != null){
                        //System.out.println("\nesquerda ----------\n");
                        executar(no.esq,novaentrada);
                    }
                    if(no.dir != null){
                        //System.out.println("\ndireita -----------\n");
                        executar(no.dir,novaentrada);
                    }
                }
            }
        }
    }
    
    public void executar(No no,String entrada){
        int cont = 0;
        String restricaofinal = "";
        Simplex a1 = new Simplex();
        
        String[] explode = no.restricao.split(">=");
        //remove espaco da restricao para tratar parte a parte = x2<=3.2
        if(explode.length > 1){
            explode[0] = explode[0].trim();
            explode[1] = explode[1].trim();
            cont = 1;
        }else{
            String[] eplodindo = no.restricao.split("<=");
            explode = eplodindo;
            explode[0] = explode[0].trim();
            explode[1] = explode[1].trim();
            cont = 2;
        }
        
        //trata a restricao final gerando as demais restricoes zeradas, trata a nova entrada do no para acrescetar na entrada inicial.
        //primeiro if cuida se o valor for diferente de 0 e o else se valor for 0
        for(int i=0;i<this.tamanho;i++){
            String aux = "1X"+ (i+1);
            if(aux.compareTo("1" + explode[0]) == 0){
                if(i > 0 && i<this.tamanho-1){
                    restricaofinal = restricaofinal + "" + aux + " + ";
                }else if(i==0){
                    restricaofinal = restricaofinal + "" + aux + " + ";
                }else{
                    restricaofinal = restricaofinal + "" + aux + "";
                }
            }else{
                if(i > 0 && i<this.tamanho-1){
                    restricaofinal = restricaofinal + "0X" + (i+1) + " + ";
                }else if(i==0){
                    restricaofinal = restricaofinal + "0X" + (i+1) + " + ";
                }else{
                    restricaofinal = restricaofinal + "0X" + (i+1) + "";
                }
            }
        }
        
        String auxilia = "";
        for(int i=0;i<resposta.size();i++){
            if(resposta.get(i).equals(explode[1])){
                if(i == resposta.size()-1){
                    auxilia = ".0000" + auxilia + "1";
                }else{
                    auxilia += "0";
                }
            }
        }
        resposta.add(explode[1] + "" + auxilia);
        
        //converte 0 em um numero muito pequeno
        if(cont == 1){
            if(explode[1].compareTo("0")==0){
                explode[1] = "0.00001";
            }
            restricaofinal = restricaofinal + " <= " + explode[1] + "" + auxilia;
        }else{
            if(explode[1].compareTo("0")==0){
                explode[1] = "0.00001";
            }
            restricaofinal = restricaofinal + " >= " + explode[1] + "" + auxilia;
        }
        
        String aux = entrada + restricaofinal + "&END";
        //System.out.println(aux);

        //Lista q contem as respostas equivalentes do simplex as variaveis de decisao
        ArrayList <String> array = a1.execucao(aux);
        
        //percorre o array setando o novo valor de x1 ou  alguma coisa
        for(int i=0;i<array.size();i++){
            String[] explo = array.get(i).split("=");
            if(explo[0].compareTo(explode[0]) == 0){
                array.remove(i);
                array.add(i, explo[0] + "=" + explode[1]);
                i = array.size();
            }
        }
        //manda no array com as respostas, com a respota altera, manda o no corrente, e a entrada modificada para o proximo no apenas acrescentar
        executar2parte(array, no, entrada + restricaofinal + "&");
    }
}
