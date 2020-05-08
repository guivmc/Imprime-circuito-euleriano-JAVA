# Algoritmo para mostar o Circuito Euleriano de um grafo direcionado ou não direcionado, em JAVA

Os grafos são representados por um Map<String, String[]>, onde a chave é o nome do vertice e o array representa as arestas que esse vértice possui.

Para representar os grafos não-direcionados as arestas são iguais as do direcionado porém acrescentando uma aresta a mais de volta, o algoritmo já sabe que não se deve acrescentar arestas repetidas.

Para dizer se o grafo direcionado é euleriano verificamos se não há vértices disconexos e se nenhum vértice tem mais graus de saída do que de entrada e vice-versa, ou seja, tem o mesmo grau de saída e entrada.

Para dizer se o grafo não-direcionado é euleriano verificamos se não há vértices disconexos e se nenhum vértice possui grau ímpar.

Para montar o circuito sempre começamos pelo vértice de maior grau, no caso não-direcionado, ou de maior grau de saída, no caso direcionado, depois percorremos cada aresta removendo-as e colocando-as em uma pilha que representa o caminho percorrido.

Para utilizar o projeto use o txt grafo onde:

1° linha: 1 para grafo direcionado, 0 para não-direcionado.

2° linha: total de vértices.

3° linha até total de vértices - 1: os nomes dos vértices.

total de vértices - 1 linha até onde seu coração desejar: as arestas, exemplos: A,B (A-B e B-A ou A->B) ou A,B,C (A-B e B-A e A-C e C-A ou A->B e A->C).

OBS: Esse algoritmo foi feito para atender a um exercício e portanto existem otimizações que podem ser feitas.
