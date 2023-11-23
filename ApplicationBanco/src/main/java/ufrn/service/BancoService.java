package ufrn.service;

import ufrn.dto.TipoTransacao;
import ufrn.dto.TransacaoDto;
import ufrn.dto.TransferenciaDto;
import ufrn.exceptions.OperacaoIlegalException;
import ufrn.model.ContaCorrente;
import ufrn.model.Historico;
import ufrn.repository.ContaCorrenteRepository;

import java.time.ZonedDateTime;
import java.util.List;

public class BancoService {
    private final ContaCorrenteRepository repository;

    public BancoService(ContaCorrenteRepository repository) {
        this.repository = repository;
    }


    public Long salvar(String nome) {
        return repository.save(new ContaCorrente(nome));
    }

    public void remove(Long id) {
        repository.delete(id);
    }

    public List<ContaCorrente> getAll() {
        return repository.getAll();
    }

    public ContaCorrente getById(Long id) {
        return repository.getById(id);
    }

    public List<Historico> getHistorico(Long id) {
        return repository.getById(id).getHistorico();
    }

    public String getExtrato(Long id) {
        return repository.getById(id).extrato();
    }

    public Double transferir(TransferenciaDto dto) throws OperacaoIlegalException {
        var contaPrimaria = repository.getById(dto.idContaPrimaria());
        var contaDestino = repository.getById(dto.idContaDestino());
        var saltoAtual = sacar(new TransacaoDto(contaPrimaria.getId(), dto.valor(), ZonedDateTime.now().toString()), TipoTransacao.TRANSFERENCIA);
        depositar(new TransacaoDto(contaDestino.getId(), dto.valor(), ZonedDateTime.now().toString()));
        return saltoAtual;
    }

    public Double sacar(TransacaoDto dto, TipoTransacao tipoTransacao) throws OperacaoIlegalException {
        var conta = repository.getById(dto.idConta());
        if (dto.valor() < 0 || dto.valor() > conta.getSaldo()) throw new OperacaoIlegalException();

        conta.getHistorico().add((new Historico((ZonedDateTime.now().toString()), tipoTransacao.name(), dto.valor())));
        conta.setSaldo(-dto.valor());
        repository.save(conta);
        return conta.getSaldo();
    }

    public Double depositar(TransacaoDto dto) throws OperacaoIlegalException {
        var conta = repository.getById(dto.idConta());
        if (dto.valor() < 0) throw new OperacaoIlegalException();

        conta.getHistorico().add((new Historico(ZonedDateTime.now().toString(), TipoTransacao.DEPOSITO.name(), dto.valor())));
        conta.setSaldo(dto.valor());
        repository.save(conta);
        return conta.getSaldo();
    }

}
