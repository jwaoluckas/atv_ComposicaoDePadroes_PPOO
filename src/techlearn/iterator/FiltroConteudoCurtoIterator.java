package techlearn.iterator;

import techlearn.composite.Aula;
import techlearn.composite.ComponenteCatalogo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Percorre toda a árvore do catálogo (em profundidade) e entrega apenas as
 * Aulas com duração menor ou igual a 45 minutos. Módulos e aulas longas são
 * pulados silenciosamente.
 *
 * A descida na árvore usa somente criarIterator() de cada componente, então o
 * filtro não conhece as coleções internas do Composite.
 */
public class FiltroConteudoCurtoIterator implements Iterator<ComponenteCatalogo> {
    public static final int LIMITE_MINUTOS = 45;

    private final Deque<Iterator<ComponenteCatalogo>> pilha = new ArrayDeque<>();
    private ComponenteCatalogo proximo;

    public FiltroConteudoCurtoIterator(List<ComponenteCatalogo> itens) {
        this(new ModuloIterator(itens));
    }

    public FiltroConteudoCurtoIterator(Iterator<ComponenteCatalogo> iteradorRaiz) {
        pilha.push(iteradorRaiz);
        avancar();
    }

    @Override
    public boolean hasNext() {
        return proximo != null;
    }

    @Override
    public ComponenteCatalogo next() {
        if (proximo == null) {
            throw new NoSuchElementException("Não há mais aulas curtas no catálogo.");
        }
        ComponenteCatalogo atual = proximo;
        avancar();
        return atual;
    }

    // Busca antecipadamente o próximo item que satisfaz o filtro.
    private void avancar() {
        proximo = null;
        while (!pilha.isEmpty()) {
            Iterator<ComponenteCatalogo> iteradorAtual = pilha.peek();
            if (!iteradorAtual.hasNext()) {
                pilha.pop();
                continue;
            }
            ComponenteCatalogo componente = iteradorAtual.next();
            // Módulos devolvem os filhos; Aulas devolvem NullIterator.
            pilha.push(componente.criarIterator());
            if (ehAulaCurta(componente)) {
                proximo = componente;
                return;
            }
        }
    }

    private boolean ehAulaCurta(ComponenteCatalogo componente) {
        return componente instanceof Aula && componente.getDuracaoMinutos() <= LIMITE_MINUTOS;
    }
}
