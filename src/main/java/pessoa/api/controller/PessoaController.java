package pessoa.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pessoa.api.model.entity.Pessoa;
import pessoa.api.model.request.PessoaSearchRequest;
import pessoa.api.model.request.PessoaUpdateRequest;
import pessoa.api.service.PessoaService;

import java.util.List;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<List<Pessoa>> getPessoas(@RequestParam(required = false) List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return ResponseEntity.ok(this.pessoaService.getPessoas());
        } else {
            return ResponseEntity.ok(this.pessoaService.getPessoas(ids));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> getPessoa(@PathVariable int id) {
        return ResponseEntity.ok(pessoaService.getPessoa(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa registerPessoa(@Valid @RequestBody Pessoa pessoa) {
        return this.pessoaService.registerPessoa(pessoa);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePessoa(@PathVariable int id, @Valid @RequestBody PessoaUpdateRequest updateRequest) {
        this.pessoaService.updatePessoa(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePessoa(@PathVariable int id) {
        this.pessoaService.deletePessoa(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Pessoa>> searchPessoas(@Valid PessoaSearchRequest searchRequest) {
        List<Pessoa> results = this.pessoaService.searchPessoas(searchRequest.getBy(), searchRequest.getQuery());
        return ResponseEntity.ok(results);
    }


}
