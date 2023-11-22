package ufrn.controller;

import ufrn.annotations.http.GetMapping;
import ufrn.annotations.http.PostMapping;
import ufrn.annotations.http.RequestBody;
import ufrn.annotations.http.RequestParam;
import ufrn.dto.TipoTransacao;
import ufrn.dto.TransacaoDto;
import ufrn.dto.TransferenciaDto;
import ufrn.dto.requests.CriarConta;
import ufrn.dto.requests.IdReq;
import ufrn.exceptions.OperacaoIlegalException;
import ufrn.repository.ContaCorrenteRepository;
import ufrn.service.BancoService;
import ufrn.utils.ResponseEntity;

public class BancoController {

    BancoService service = new BancoService(new ContaCorrenteRepository());

    @PostMapping
    public ResponseEntity<?> criarNovaConta(@RequestBody CriarConta criarConta) {
        long id = service.salvar(criarConta.nome());
        return new ResponseEntity<>(200, "Salvo com sucesso!", id);
    }

    @PostMapping("/depositar")
    public ResponseEntity<?> depositar(@RequestBody TransacaoDto dto) throws OperacaoIlegalException {
        Double saldoRestante = service.depositar(dto);
        return new ResponseEntity<>(200, "Salvo com sucesso!", saldoRestante);
    }

    @PostMapping("/sacar")
    public ResponseEntity<?> sacar(@RequestBody TransacaoDto dto) throws OperacaoIlegalException {
        Double saldoRestante = service.sacar(dto, TipoTransacao.SAQUE);
        return new ResponseEntity<>(200, "Salvo com sucesso!", saldoRestante);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaDto dto) throws OperacaoIlegalException {
        Double saldoRestante = service.transferir(dto);
        return new ResponseEntity<>(200, "Salvo com sucesso!", saldoRestante);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(200, "Listagem das contas!", service.getAll());
    }

    @PostMapping("/remover")
    public ResponseEntity<?> remover(@RequestBody IdReq dto) {
        service.remove(dto.id());
        return new ResponseEntity<>(200, "Salvo com sucesso!", true);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getById(@RequestParam(name = "id") Long id) {
        var conta = service.getById(id);
        return new ResponseEntity<>(200, "Salvo com sucesso!", conta);
    }

}