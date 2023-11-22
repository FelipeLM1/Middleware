package ufrn.repository;

import ufrn.model.ContaCorrente;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ContaCorrenteRepository {

    private static final Map<Long, ContaCorrente> database = new ConcurrentHashMap<>();

    public Long save(ContaCorrente contaCorrente) {
        if (Objects.isNull(contaCorrente.getId())) {
            contaCorrente.setId(DataBaseUtil.getNextId());
        }
        database.put(contaCorrente.getId(), contaCorrente);
        return contaCorrente.getId();
    }

    public void delete(Long id) {
        database.remove(id);
    }

    public ContaCorrente getById(Long id) {
        return database.get(id);
    }

    public List<ContaCorrente> getAll() {
        return database.values().stream().toList();
    }
}
