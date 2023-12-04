package ufrn.repository;

import ufrn.model.Conta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ContaCorrenteRepository {

    private static final Map<Long, Conta> database = new ConcurrentHashMap<>();

    public Long save(Conta conta) {
        if (Objects.isNull(conta.getId())) {
            conta.setId(DataBaseUtil.getNextId());
        }
        database.put(conta.getId(), conta);
        return conta.getId();
    }

    public void delete(Long id) {
        database.remove(id);
    }

    public Conta getById(Long id) {
        return database.get(id);
    }

    public List<Conta> getAll() {
        return database.values().stream().toList();
    }
}
