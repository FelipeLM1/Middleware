package ufrn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(BancoController.class);


    @PostMapping
    public ResponseEntity<?> criarNovaConta(@RequestBody CriarConta criarConta) {
        long id = service.salvar(criarConta.nome());
        logger.info("Conta criada!");
        return new ResponseEntity<>(200, "Salvo com sucesso!", id);
    }

    @PostMapping("/depositar")
    public ResponseEntity<?> depositar(@RequestBody TransacaoDto dto) throws OperacaoIlegalException {
        Double saldoRestante = service.depositar(dto);
        logger.info("Deposito de R${} feito com sucesso!", dto.valor());
        return new ResponseEntity<>(200, "Salvo com sucesso!", saldoRestante);
    }

    @PostMapping("/sacar")
    public ResponseEntity<?> sacar(@RequestBody TransacaoDto dto) throws OperacaoIlegalException {
        Double saldoRestante = service.sacar(dto, TipoTransacao.SAQUE);
        logger.info("Saque de R${} realizado!", dto.valor());
        return new ResponseEntity<>(200, "Salvo com sucesso!", saldoRestante);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaDto dto) throws OperacaoIlegalException {
        Double saldoRestante = service.transferir(dto);
        logger.info("Transferencia realizada!", dto.valor());
        return new ResponseEntity<>(200, "Salvo com sucesso!", saldoRestante);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(200, "Listagem das contas!", service.getAll());
    }

    @PostMapping("/remover")
    public ResponseEntity<?> remover(@RequestBody IdReq dto) {
        service.remove(dto.id());
        logger.info("Removeu usuário com id {} ", dto.id());
        return new ResponseEntity<>(200, "Salvo com sucesso!", true);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getById(@RequestParam(name = "id") Long id) {
        var conta = service.getById(id);
        logger.info("Busca do usuário com id {} ", id);
        return new ResponseEntity<>(200, "Busca realizada com sucesso!", conta);
    }

    @GetMapping("/extrato")
    public ResponseEntity<?> getExtrato(@RequestParam(name = "id") Long id) {
        var extrato = service.getExtrato(id);
        logger.info("Busca do extrato com id {} ", id);
        return new ResponseEntity<>(200, "Busca realizada com sucesso!", extrato);
    }

}