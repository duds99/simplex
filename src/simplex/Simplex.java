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
public class Simplex {
    String auxiliar = "";
    ArrayList<ArrayList<Double>> fora = new ArrayList<ArrayList<Double>>();
    ArrayList<Double> dentro = new ArrayList<Double>();
    DecimalFormat decimal = new DecimalFormat("0.00");
    ArrayList<String> respostasbb = new ArrayList<>();
    double matriz[][]; //matriz do simplex
    int matrizresposta[][];//matriz onde salva as variaveis para ser mostrada na respost
    double matrizbaixo[][]; //matriz de apoio (parte de baixo do simplex)
    int salvalinha; //linha do pivor
    int salvacoluna; // coluna do pivor
    int linha; // quantidade de linhas
    int coluna; // quantidade de colunas
    
    //mostra a matriz gerada
    private void MostrarMatriz(){
        System.out.println("Matriz");
        for(int i=0;i<this.linha;i++){
            for(int j=0;j<this.coluna;j++){     
                System.out.print(decimal.format(this.matriz[i][j]) + "\t" );
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    //matriz de apoio, parte de baixo do simplex
    private void MostrarMatrizBaixo(){
        System.out.println("Matriz de baixo");
        for(int i=0;i<this.linha;i++){
            for(int j=0;j<this.coluna;j++){
                System.out.print(decimal.format(this.matrizbaixo[i][j]) + "\t" );
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    //le matrizes
    private void PrimeiroPasso(String palavras){
        String[] frases = palavras.split("&");
        String linha = frases[0];
        int cont = 0;
        int quebralinha = 0;
        dentro.add(0.0);
        fora.add(dentro);
        
        while(!linha.equals("END")){
            String explode[] = linha.split(" ");
     
            //le a funcao objetiva if == max e else == MIN 
            if(cont == 0){
                if(explode[0].equals("MAX")){
                    for(int i=1;i<explode.length;i++){
                        if(explode[i].equals("+") || explode[i].equals("-")){
                            if(explode[i].equals("-")){
                                i++;
                                quebralinha++;
                                dentro.add(-1 * Double.parseDouble(explode[i].substring(0,explode[i].length()-2)));          
                            }else{
                                quebralinha++;
                                i++;
                                dentro.add(Double.parseDouble(explode[i].substring(0,explode[i].length()-2)));
                            }
                        }else{
                          dentro.add(Double.parseDouble(explode[i].substring(0,explode[i].length()-2)));
                          quebralinha++;
                        }
                    }    
                //funcao objetiva MIN , le a funcao e a armazena dentro de uma list q sera utilizada para salvar em uma matriz
                }else{
                    for(int i=1;i<explode.length;i++){
                        if(explode[i].equals("+") || explode[i].equals("-")){
                           if(explode[i].equals("-")){
                                i++;
                                quebralinha++;
                                dentro.add(Double.parseDouble(explode[i].substring(0,explode[i].length()-2))); 
                            }else{
                                i++;
                                quebralinha++;
                                dentro.add(-1 * Double.parseDouble(explode[i].substring(0,explode[i].length()-2)));
                           }
                        }else{
                            quebralinha++;
                            dentro.add(-1 * Double.parseDouble(explode[i].substring(0,explode[i].length()-2)));
                        }
                    }
                }     
            }
            
            //le as restriçoes o final das restricoes apos o sinal
            if(cont >=1){
                for(int i=0;i<explode.length;i++){
                    //se restricao for menos nao faz mudanca de sinal dos demais valores, transforma inequacao em equacao
                    if(explode[i].equals("<=") || explode[i].equals("<")){
                        i++;
                        if(explode[i].equals("-")){
                            i++;
                            dentro.add(-1 * Double.parseDouble(explode[i]));
                        }else if(explode[i].equals("+")){
                            i++;
                            dentro.add(Double.parseDouble(explode[i]));
                        }else{
                            dentro.add(Double.parseDouble(explode[i]));
                        }
                    //realiza operacoes quando sinal é maior igual, para que transforme a inequacao em equacao
                    }else if(explode[i].equals(">=") || explode[i].equals(">")){
                        i++;
                        if(explode[i].equals("-")){
                            i++;
                            dentro.add(-1 * Double.parseDouble(explode[i]));
                        }else if(explode[i].equals("+")){
                            i++;
                            dentro.add(Double.parseDouble(explode[i]));
                        }else{
                            dentro.add(Double.parseDouble(explode[i]));
                        }  
                    //nao mexe na equacao, apenas adiciona a mesma na matriz
                    }else if(explode[i].equals("=")){
                        i++;
                        if(explode[i].equals("-")){
                            i++;
                            dentro.add(-1 * Double.parseDouble(explode[i]));
                        }else if(explode[i].equals("+")){
                            i++;
                            dentro.add(Double.parseDouble(explode[i]));
                        }else{
                            dentro.add(Double.parseDouble(explode[i]));
                        }
                    } 
                }
                
                //le as restricoes antes dos sinais
                for(int i=0;i<explode.length;i++){
                    if(explode[i].equals("-")){
                        i++;
                        dentro.add(-1 * Double.parseDouble(explode[i].substring(0,explode[i].length()-2)));
                    }else if(explode[i].equals("+")){
                        i++;
                        dentro.add(Double.parseDouble(explode[i].substring(0,explode[i].length()-2)));
                    }else if(explode[i].equals("<=") || explode[i].equals(">=") || explode[i].equals("=") || explode[i].equals(">") || explode[i].equals("<")){
                        if(explode[i].equals(">=") || explode[i].equals(">")){
                            int aux = cont*(quebralinha+1);
                            for(int j=aux;j<dentro.size();j++){
                               dentro.set(j,-1 * fora.get(0).get(j));
                            }
                        }                       
                        i = explode.length;
                    }else{
                        dentro.add(Double.parseDouble(explode[i].substring(0,explode[i].length()-2)));
                    }           
                }   
                         
            }
            //passa pra proxima linha da entrada
            cont++;
            linha = frases[cont];
        }   
        
        //transcreve o que foi lido para uma matriz com x linhas e y colunas
        this.linha = cont;
        this.coluna = quebralinha+1;
        this.matriz = new double[this.linha][coluna];
        this.matrizbaixo = new double[this.linha][coluna];
        matrizresposta = new int[this.linha][coluna];
        int aux = 0;
        for(int j=0;j<dentro.size();j++){
            if(j%(quebralinha+1)==0 && j!=0){
                aux++;
            }
            matriz[aux][j%(quebralinha+1)] = fora.get(0).get(j);
        }
        
        //inicializa matriz de resposta
        int valor = 1;
        for(int i=1;i<coluna;i++){
            matrizresposta[0][i] = valor;
            valor++;
        }
        
        for(int i=1;i<this.linha;i++){
            matrizresposta[i][0] = valor;
            valor++;
        }
    }
    
    
    //Calcula matriz de baixo para auxilio da proxima etapa se existir. Também gera as respostas
    private boolean SegundoPasso(){
        int posicaolinha[] = new int[linha]; //linha do pivor
        int posicaocoluna[] = new int[linha*coluna]; //coluna do pivor
        int negativolinha = 0; 
        int negativocoluna = 0;
        int negativocolunafx = 0;
        salvalinha = 0;
        salvacoluna = 0;
        
        //conta quantidade de numeros negativos existem na primeira coluna ML e marca posicao encontrada
        for(int i=1;i<linha;i++){
            if(matriz[i][0] < 0){
                posicaolinha[negativolinha] = i;
                negativolinha++;
                //verifica na linha encontrada acima a existencia de numeros negativos e grava a posicao do mesmo
                for(int j = 1;j<coluna;j++){
                    if(matriz[i][j] < 0){
                        posicaocoluna[negativocoluna] = j;
                        negativocoluna++;
                    }
                }
            }
        }
                
        //conta negativos na primeira linha f(x)
        for(int i=1;i<coluna;i++){
            if(matriz[0][i] < 0){
                negativocolunafx++;
            }
        }


        if(negativolinha == 0 && negativocolunafx == coluna-1){//todos positivos primeira coluna e todos negativos na primeira linha
            auxiliar = "Solucao Otima";
            respostasbb.add("sim");
            //System.out.println("Solucao Otima");
            System.out.println("TS2");
            return true;
        }else if(negativolinha != 0 && negativocoluna == 0){//existe negativo na coluna porem na existe negativo nas colunas
            respostasbb.add("nao");
            System.out.println("Sistema impossivel");
            return true;
        }else if(negativolinha == 0 && negativocolunafx < coluna-1){//todos positivos na primeira coluna e existe positivos na primeira linha exceto ML
            int contando = 0;
            for(int i=1;i<coluna;i++){
               if(matriz[0][i] > 0){
                   //conta negativos em uma determinada coluna onde o f(x) é positivo
                   for(int j=1;j<linha;j++){
                       if(matriz[j][i] <= 0){
                           contando++;
                       }
                   }
                   if(contando == linha-1){
                       respostasbb.add("nao");
                       System.out.println("Sistema com solucoes ilimitadas");
                       return true;
                   }else{
                       contando = 0;
                   }
                }
            }            
    
            //verifica se na linha f(x) existem apenas numeros negativos e 0
            int contarnegativo = 0;
            int conta0 = 0;
            for(int k=1;k<coluna;k++){
                if(matriz[0][k] < 0){
                    contarnegativo++;
                }
                if(matriz[0][k] == 0.0 || matriz[0][k] == -0.0){
                    conta0++;
                }
            }
            //verifica se f(x) possui apenas negativos e 0
            if((conta0 + contarnegativo) == coluna-1){
                respostasbb.add("nao");
                System.out.println("Multiplas solucoes");
                return true;
            }
            
            //primeira linha todos positivos e na 1 coluna existe algum positivo
            int colunaspositivas[] = new int[coluna];
            int aux = 0;
            for(int k=1;k<coluna;k++){
                if(matriz[0][k] > 0){
                    colunaspositivas[aux] = k;
                    aux++;
                }
            }
            //defini o pivor segunda fase
            double x = Double.POSITIVE_INFINITY;
            for(int k=1;k<linha;k++){
                for(int j=0;j<aux;j++){
                    if((matriz[k][0]<0 && matriz[k][colunaspositivas[j]]<0)){
                        if(x >= (matriz[k][0]/matriz[k][colunaspositivas[j]])){
                            x = matriz[k][0]/matriz[k][colunaspositivas[j]];
                            salvalinha = k;
                            salvacoluna = colunaspositivas[j];
                        }               
                    }else if((matriz[k][0]>0 && matriz[k][colunaspositivas[j]]>0)){
                        if(x >= (matriz[k][0]/matriz[k][colunaspositivas[j]])){
                            x = matriz[k][0]/matriz[k][colunaspositivas[j]];
                            salvalinha=k;
                            salvacoluna = colunaspositivas[j];
                        }
                    }
                }
            }
        }else{
            //existe negativos na primeira coluna (primeira fase), definindo pivor da primeira fase
            double x = Double.POSITIVE_INFINITY;
            for(int i=1;i<linha;i++){
                for(int j=0;j<negativocoluna;j++){
                    if((matriz[i][0]<0 && matriz[i][posicaocoluna[j]]<0)){
                        if(x > (matriz[i][0]/matriz[i][posicaocoluna[j]])){
                            x = matriz[i][0]/matriz[i][posicaocoluna[j]];
                            salvalinha = i;
                            salvacoluna = posicaocoluna[j];
                        }               
                    }else if((matriz[i][0]>0 && matriz[i][posicaocoluna[j]]>0)){
                        if(x > (matriz[i][0]/matriz[i][posicaocoluna[j]])){
                            x = matriz[i][0]/matriz[i][posicaocoluna[j]];
                            salvalinha=i;
                            salvacoluna = posicaocoluna[j];
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }
    
    /*
        Passo onde realiza as operacoes para calcular a matriz de baixo (segunda matriz)
        Inverte o ponto escolhido e realiza operacoes com o mesmo para descobrir os demais valores da matriz de baixo
    */
    private void TerceiroPasso(){
        //inverte pivor
        matrizbaixo[salvalinha][salvacoluna] = (1/matriz[salvalinha][salvacoluna]);
        
        for(int i=0;i<coluna;i++){
            if(i != salvacoluna){
                matrizbaixo[salvalinha][i] = matrizbaixo[salvalinha][salvacoluna] * matriz[salvalinha][i];
            }
        }

        for(int i=0;i<linha;i++){
            if(i != salvalinha){
            matrizbaixo[i][salvacoluna] = (-1 * matrizbaixo[salvalinha][salvacoluna]) * matriz[i][salvacoluna];
            }
        }
        
        for(int i=0;i<linha;i++){
            for(int j=0;j<coluna;j++){
                if(!(i == salvalinha || j == salvacoluna) && !(i == salvalinha && j == salvacoluna)){
                    matrizbaixo[i][j] = matriz[salvalinha][j] * matrizbaixo[i][salvacoluna];
                }
            }
        }   
    }
    
    /*
    Gera a nova matriz baseada na matriz principal e na matriz de baixo
    */
    private void QuartoPasso(){
        
        //swap de variaveis, para saber qual resposta é referente a qual variavel
        int aux = matrizresposta[0][salvacoluna];
        int aux2 = matrizresposta[salvalinha][0];
        matrizresposta[0][salvacoluna] = aux2;
        matrizresposta[salvalinha][0] = aux;
  
        //matriz de baixo vira matriz principal - coluna escolhida
        for(int i=0;i<linha;i++){
            matriz[i][salvacoluna] = matrizbaixo[i][salvacoluna];
        }
        //matriz de baixo vira matriz principal - linha escolhida
        for(int j=0;j<coluna;j++){
            matriz[salvalinha][j] = matrizbaixo[salvalinha][j];
        }
        
        //operacoes de encontrar os demais valores da matriz principal
        for(int i=0;i<linha;i++){
            for(int j=0;j<coluna;j++){
                if(i!=salvalinha && j!=salvacoluna){
                    matriz[i][j] = matriz[i][j] + matrizbaixo[i][j];
                }
            }
        }
    }
    
    //mostra os valores obtidos apos a execucao do simplex
    private void MostrarValores(){     
        for(int i=0;i<linha;i++){
            if(i==0){
                System.out.println("Z: " + decimal.format(this.matriz[i][0]));
            }else{
                System.out.println("X"+ matrizresposta[i][0] + ": " + decimal.format(this.matriz[i][0]));
                if(matrizresposta[i][0] < coluna){
                    respostasbb.add("X"+ matrizresposta[i][0] + "=" + decimal.format(this.matriz[i][0]));
                } 
            }
        }
    }
    
    /*
    ////ler a matriz da entrada
    //enquanto uma solucacao nao for encontrada realiza a operacao da primeira ou segunda fase, gera a matriz de baixo e logo
    //em seguida gera a nova matriz, se o a solucao nao for encontrada realiza os passos novamente ate que chegue nela.
    */
    public ArrayList<String> execucao(String linha){
        PrimeiroPasso(linha);
        
        while(!SegundoPasso()){
            TerceiroPasso();
            QuartoPasso();
            MostrarMatriz();
            MostrarMatrizBaixo();
        }
        MostrarValores();
        
        boolean ajuda = false;
        if(auxiliar.equals("Solucao Otima")){
            auxiliar = "";
            ajuda = true;
        }
        
        int contando = 0;
        //remover daqui pra baixo pro simplex comum
        for(int i=0;i<this.linha;i++){
            if(i==0){
                if(ajuda == true){
                    auxiliar += "Z: " + decimal.format(this.matriz[i][0]);
                }
                System.out.println("Z: " + decimal.format(this.matriz[i][0]));
            }else{
                if(ajuda == true){
                    auxiliar += "\t" + "X" + matrizresposta[i][0] + ": " + decimal.format(this.matriz[i][0]);
                }
                //System.out.println("X" + matrizresposta[i][0] + ": " + decimal.format(this.matriz[i][0]));
                
                //envia apenas as respostas das variaves colunas da FO
                if(matrizresposta[i][0] < coluna){
                    int valor = (int)this.matriz[i][0];
                    String auxilia = decimal.format(matriz[i][0]);
                    auxilia = auxilia.replace(",", ".");
                    
                    //verifica se o valor é inteiro se for conta, e se todos os valores forem inteiros o contando sera = coluna-1
                    if(Double.parseDouble(auxilia) - valor == 0){
                        contando++;
                    }
                    System.out.println("X" + matrizresposta[i][0] + ": " + decimal.format(this.matriz[i][0]));
                    respostasbb.add("X"+ matrizresposta[i][0] + "=" + decimal.format(this.matriz[i][0]));
                }
            }
        }
        
        //exibe respostas se todos as variaveis da fo sao inteiros
        if(ajuda == true){
            if(contando == coluna-1){
                System.out.println(auxiliar);
            }
        }
        return respostasbb;
    }
    
    
    public void beb(String args){
        String[] entrada = args.split("END");
        BeB beb = new BeB(entrada[0],respostasbb,coluna-1);  
    }

    //recebe a matriz de entrada e inicializa o objeto simplex
    public static void main(String[] args) {
        String linha = "MIN 7X1 + 8.5X2&0.6X1 + 0.8X2 >= 16&24X1 + 20X2 <= 1800&END";
        Simplex a1 = new Simplex();
        a1.execucao(linha);
        
        //remover pro simplex comum
        //a1.beb(linha);
    }     
}

//exemplo de entrada
/*
LISTA
MIN 7X1 + 8.5X2&0.6X1 + 0.8X2 >= 16&24X1 + 20X2 <= 1800&END
MAX 38X1 + 49X2&1X1 + 1.5X2 <= 160&2.5X1 + 2.5X2 <= 256&0X1 + 1X2 >= 40&END
MAX 14X1 + 22X2&2X1 + 4X2 <= 250&5X1 + 8X2 >= 460&1X1 + 0X2 <= 40&END

SLIDE
MAX 21X1 + 11X2&7X1 + 4X2 <= 13&END
*/