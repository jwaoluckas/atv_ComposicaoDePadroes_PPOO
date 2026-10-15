# Composição de Padrões: TechLearn

Atividade da disciplina de Padrões de Projeto (IFPE). O sistema de cursos da TechLearn usa três padrões juntos:

- **Singleton** (`LoggerService`): um único serviço de log para todo o sistema.
- **Composite** (`ComponenteCatalogo`, `Aula`, `Modulo`): cursos, módulos e aulas montados em forma de árvore e tratados do mesmo jeito.
- **Iterator** (`Iterator`, `ModuloIterator`, `NullIterator`): permite percorrer os itens sem mostrar como eles estão guardados.

## Desafio do Passo 5: Filtro de Conteúdo Curto

A classe `FiltroConteudoCurtoIterator` percorre o curso inteiro, inclusive os módulos que estão dentro de outros módulos, e entrega só as aulas de até 45 minutos. Módulos e aulas mais longas são pulados.

### Como rodar

Abra o projeto no IntelliJ e rode a classe `techlearn.Main`. Ou, pelo terminal, dentro da pasta `src`:

```
javac -encoding UTF-8 -d ../out techlearn/Main.java techlearn/*/*.java
java -cp ../out techlearn.Main
```

Resultado do filtro:

```
Aula curta encontrada: Aula 1.1: Coesão e Acoplamento (45 min)
Aula curta encontrada: Aula 2.1: Singleton na Prática (30 min)
Aula curta encontrada: Aula 2.3: Iterator para Coleções (40 min)
```

### Pergunta

> Como a separação entre a estrutura do Composite e a lógica de percurso do Iterator facilitou a adição dessa nova regra sem quebrar o código do cliente?

### Resposta

Como o projeto separa quem guarda os itens (o Composite, nas classes `Modulo` e `Aula`) de quem passa pelos itens (o Iterator), uma parte não interfere na outra.

Para resolver o desafio, bastou criar um novo "guia", o `FiltroConteudoCurtoIterator`. A cada chamada de `next()`, ele anda pelo curso e entrega só as aulas de até 45 minutos. Ele pula as aulas mais longas e também os módulos, que servem só para agrupar aulas. Quando encontra um módulo, ele não abre a lista interna dele. Ele só pede um novo guia com `criarIterator()` e segue por ele. Assim, o filtro funciona com módulos dentro de módulos, sem precisar saber como cada um guarda os seus itens.

Por isso, nenhuma linha de `Modulo`, `Aula`, `ComponenteCatalogo` ou da interface `Iterator` foi mudada. A montagem do curso continua igual, e a soma das durações e a exibição da ementa continuam funcionando. No `Main`, o laço `while (hasNext())` também é o mesmo. A única mudança foi qual guia ele usa. Se amanhã surgir outra regra, por exemplo listar só as aulas longas, basta criar outro guia, sem mexer no que já funciona.
