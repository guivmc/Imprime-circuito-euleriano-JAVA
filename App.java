package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//Membros:
//Guilherme (21002514)
public class App {

    //region nao-direcionado
    public static Map<String, String[]> criaGrafoNaoDirecionado(String[] arestas)
    {
        Map<String, String[]> grafo = new HashMap<>();

        for (int i = 0; i < arestas.length - 1; i++)
        {
            String[] listAresta = arestas[i].split(","); //Pega os vértices para montar as arestas

            for (int j = 0; j < listAresta.length; j++)//Loop para fazer a adição nos dois sentidos.
            {
                String vertice = listAresta[j];

                ArrayList<String> arestasAdicionar = new ArrayList<>();

                if (grafo.containsKey(vertice)) //Se grafo já tem o vértice, mantém as arestas.
                {
                    arestasAdicionar.addAll(Arrays.asList(grafo.get(vertice)));//mantém as arestas.
                }

                for (int k = 0; k < listAresta.length; k++) //Adiciona outras arestas.
                {
                    if(k != j && !arestasAdicionar.contains(listAresta[k])) //Evita fazer ligações repetidas entre os vertices.
                        arestasAdicionar.add(listAresta[k]);
                }

                grafo.put(vertice, arestasAdicionar.toArray(String[]::new));//Atualiza o grafo.
            }
        }

        return grafo;
    }

    public static boolean haCaminhoEulerianoParaGrafoNaoDirecionado(Map<String, String[]> grafo)
    {
        String[] keys = grafo.keySet().toArray(String[]::new); //Pega os vertices do grafo.

        boolean haVerticeDisconexo = false;

        boolean possuiImpar = false;

        int totalArestas;

        for (int i = 0; i < grafo.size() && !haVerticeDisconexo && !possuiImpar; i++)
        {
            totalArestas = grafo.get(keys[i]).length;

            if(totalArestas % 2 == 1) //Procura por grau impar.
                possuiImpar = true;

            if(totalArestas == 0) //Procura por vertice disconexo.
                haVerticeDisconexo = true;
        }

        //Um grafo não direcionado possui caminho euleriano
        // Sé todos os vertices tiverem grau par e não possuir vertices disconexo.
        return !possuiImpar && !haVerticeDisconexo;
    }

    public static String[] caminhoEurelianoParaGrafoNaoDirecionado(Map<String, String[]> grafo)
    {
        String[] keys = grafo.keySet().toArray(String[]::new); //Pega os vertices do grafo.

        int totalArestas = 0;

        //Soma o total de arestas.
        for (int i = 0; i < grafo.size(); i++)
        {
            totalArestas += grafo.get(keys[i]).length;
        }

        //Remove arestas duplicadas.
        totalArestas /= 2;

        ArrayList<String> pilha = new ArrayList<>();
        String keyTopo = ""; //Chave do topo da pilha.
        String vizinho = "";

        int maiorQuantidadeDeArestas = 0;

        //Procura pelo vertice com a maior quantidade de ligações de saida.
        for (int i = 0; i < grafo.size(); i++)
        {
            if(maiorQuantidadeDeArestas < grafo.get(keys[i]).length)
            {
                maiorQuantidadeDeArestas = grafo.get(keys[i]).length;
                keyTopo = keys[i];
            }
        }
        pilha.add(keyTopo);//Coloca vertice inicial na pilha.

        //Usamos 2 auxiliar para guardar as arestas dos vertices,
        // pois a forma como o grafo não direcionado foi montado é igual a de um direcionado sempre com os 2 sentidos entre os vertices.
        ArrayList<String> auxArestasIda = new ArrayList<>(); //Auxiliar para lista de arestas do vertice que estamos olhando.
        ArrayList<String> auxArestasVolta = new ArrayList<>(); //Auxiliar para lista de arestas do vizinho do vertice que estamos olhando.

        for (int i = 0; i < totalArestas; i++) //Percorre todas as arestas.
        {
            //Atuliza o topo
            keyTopo = pilha.get(pilha.size() - 1);

            //Pega as arestas do vertice que esta no topo da pilha.
            auxArestasIda.addAll(Arrays.asList(grafo.get(keyTopo)));

            //Pega as arestas do primeiro vizinho inserido.
            auxArestasVolta.addAll(Arrays.asList(grafo.get(auxArestasIda.get(0))));

            //Define o topo como sendo o primeiro vizinho.
            vizinho = auxArestasIda.get(0);
            pilha.add(vizinho);

            try
            {
                //Remove o primeiro vizinho.
                auxArestasIda.remove(0);

                //Atualiza as arestas do vertice que estamos olhando.
                grafo.put(keyTopo, auxArestasIda.toArray(String[]::new));

                if(!vizinho.equalsIgnoreCase(keyTopo)) //Se vizinho e o vertice atual não são os mesmos.
                {
                    //Remove o caminho de volta do vizinho.
                    auxArestasVolta.remove(keyTopo);
                    //Atualiza as arestas do vertice vizinho.
                    grafo.put(vizinho, auxArestasVolta.toArray(String[]::new));
                }
            }
            catch (Exception e)
            {
                //Ignora exceptions
            }

            //Limpa auxiliares
            auxArestasIda = new ArrayList<>();
            auxArestasVolta = new ArrayList<>();
        }

        return pilha.toArray(String[]::new);
    }
    //endregion

    //region direcionado
    public static Map<String, String[]> criaGrafoDirecionado(String[] arestas)
    {
        Map<String, String[]> grafo = new HashMap<>();

        for (int i = 0; i < arestas.length - 1; i++)
        {
            String[] listAresta = arestas[i].split(","); //Pega os vértices para montar as arestas

            String vertice = listAresta[0];

            ArrayList<String> arestasAdicionar = new ArrayList<>();

            if(grafo.containsKey(vertice)) //Se grafo já tem o vértice, adiciona novas arestas.
            {
                arestasAdicionar.addAll(Arrays.asList(grafo.get(vertice)));//Mantém as arestas originais.
                arestasAdicionar.addAll(Arrays.asList(Arrays.copyOfRange(listAresta, 1, listAresta.length))); //Adiciona novas arestas.
            }
            else {//Se não coloca vértice com as arestas listadas.
                arestasAdicionar.addAll(Arrays.asList(Arrays.copyOfRange(listAresta, 1, listAresta.length)));//Adiciona novas arestas.
            }

            grafo.put(vertice,  arestasAdicionar.toArray(String[]::new));//Atualiza o grafo.
        }

        return grafo;
    }

    public static boolean haCaminhoEurelianoParaGrafoDirecionado(Map<String, String[]> grafo)
    {
        String[] keys = grafo.keySet().toArray(String[]::new); //Pega os vertices do grafo.

        Map<String, Integer> totalDeArestas = new HashMap<>();

        boolean haVerticeDisconexo = false;
        boolean mesmoGrauDeEntradaESaida = true;

        int totalDeArestasDeSaida;

        //Calcula os vertices de Saida e procura por vertice disconexo.
        for (int i = 0; i < grafo.size() && !haVerticeDisconexo; i++)
        {
            totalDeArestasDeSaida = grafo.get(keys[i]).length;

            if(totalDeArestasDeSaida == 0) //Procura por vertice disconexo.
                haVerticeDisconexo = true;

            totalDeArestas.put(keys[i], totalDeArestasDeSaida);
        }

        if(!haVerticeDisconexo)
        {

            int totalDeArestasDeEntrada;

            //Calcula o total de arestas de entrada.
            for (int i = 0; i < grafo.size(); i++)
            {
                totalDeArestasDeEntrada = 0;
                for (int j = 0; j < grafo.get(keys[i]).length; j++)
                {
                    totalDeArestasDeEntrada += totalDeArestas.get(grafo.get(keys[i])[j]) + 1;
                    totalDeArestas.put(grafo.get(keys[i])[j], totalDeArestasDeEntrada);
                }
            }
        }
        else
        {
            return false;
        }

        //Verifica se os vertices tem mesmo grau de saida e entrada.
        for (int i = 0; i < grafo.size() && mesmoGrauDeEntradaESaida; i++)
        {
            if(totalDeArestas.get(keys[i]) % 2 == 1)
                mesmoGrauDeEntradaESaida = false;
        }

        //Um grafo direcionado só tem caminho eureliano se todos os vertices tiverem o mesmo grau de saida e entrada,
        //e tambem que todos os vertices sejam conexos.
        return mesmoGrauDeEntradaESaida;
    }

    public static String[] caminhoEurelianoParaGrafoDirecionado(Map<String, String[]> grafo)
    {
        String[] keys = grafo.keySet().toArray(String[]::new); //Pega os vertices do grafo.

        ArrayList<String> pilha = new ArrayList<>();

        String keyTopo = ""; //Chave do topo da pilha.
        String vizinho = "";

        int maiorQuantidadeDeArestas = 0;

        //Procura pelo vertice com a maior quantidade de ligações de saida.
        for (int i = 0; i < grafo.size(); i++)
        {
            if(maiorQuantidadeDeArestas < grafo.get(keys[i]).length)
            {
                maiorQuantidadeDeArestas = grafo.get(keys[i]).length;
                keyTopo = keys[i];
            }
        }
        pilha.add(keyTopo);//Coloca vertice inicial na pilha.

        int totalArestas = 0;

        //Soma o total de arestas.
        for (int i = 0; i < grafo.size(); i++)
        {
            totalArestas += grafo.get(keys[i]).length;
        }

        ArrayList<String> auxArestasIda = new ArrayList<>(); //Auxiliar para lista de arestas do vertice que estamos olhando.

        for (int i = 0; i < totalArestas; i++)
        {
            //Atuliza o topo
            keyTopo = pilha.get(pilha.size() - 1);

            //Pega as arestas do vertice que esta no topo da pilha.
            auxArestasIda.addAll(Arrays.asList(grafo.get(keyTopo)));

            //Define o topo como sendo o primeiro vizinho.
            vizinho = auxArestasIda.get(0);
            pilha.add(vizinho);

            try
            {
                //Remove o primeiro vizinho.
                auxArestasIda.remove(0);

                //Atualiza as arestas do vertice que estamos olhando.
                grafo.put(keyTopo, auxArestasIda.toArray(String[]::new));
            }
            catch (Exception e)
            {
                //Ignora exceptions
            }

            //Limpa auxiliares
            auxArestasIda = new ArrayList<>();
        }

        return pilha.toArray(String[]::new);
    }
    //endregion

    public static String formataOCaminhoEureliano(String[] caminho)
    {
        StringBuilder path = new StringBuilder();

        for (int i = 0; i < caminho.length; i++)
        {
            path.append(caminho[i]).append("-");
        }

        return path.toString();
    }


    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\grafo.txt"));

            StringBuilder sb = new StringBuilder();

            String line = br.readLine();
            boolean direcionado = line.equals("1"); //Lê se o grafo é direcionado ou não.

            line = br.readLine();
            int numVertices = Integer.parseInt(line);//Lê a quantidade de vértices.

            String[] vertices = new String[numVertices]; //Lê os vértices.
            for (int i = 0; i < numVertices; i++) {
                vertices[i] = br.readLine();
            }

            ArrayList<String> arestas = new ArrayList<>();//Lê as arrestas.
            while (line != null) {
                line = br.readLine();
                arestas.add(line);
            }

            br.close();//Fecha o arquivo.

            Map<String, String[]> grafo;
            String[] caminho;

            if(direcionado) {
                grafo = criaGrafoDirecionado(arestas.toArray(String[]::new));
                if(haCaminhoEurelianoParaGrafoDirecionado(grafo))
                {
                    caminho = caminhoEurelianoParaGrafoDirecionado(grafo);
                    System.out.println("Circuito encontrado: " + formataOCaminhoEureliano(caminho));
                }
                else
                {
                    System.out.println("O Grafo fornecido não é Euleriano");
                }

            }
            else {
                grafo = criaGrafoNaoDirecionado(arestas.toArray(String[]::new));
                if(haCaminhoEulerianoParaGrafoNaoDirecionado(grafo)){
                    caminho = caminhoEurelianoParaGrafoNaoDirecionado(grafo);
                    System.out.println("Circuito encontrado: " + formataOCaminhoEureliano(caminho));
                }
                else{
                    System.out.println("O Grafo fornecido não é Euleriano");
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException e) {
            System.out.println("Erro ao tenar ler uma linha ou ao tentar fechar o arquivo.");
        }
    }
}
