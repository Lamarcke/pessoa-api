package pessoa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pessoa.api.model.entity.Endereco;
import pessoa.api.model.entity.Pessoa;
import pessoa.api.model.request.PessoaSearchRequest;
import pessoa.api.model.request.PessoaUpdateRequest;
import pessoa.api.service.PessoaService;
import pessoa.api.utils.PessoaSearchOptions;
import pessoa.api.utils.PlaceholderValues;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class PessoaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PessoaService pessoaService;

    @Test
    public void testPessoaRegister() throws Exception {
        Pessoa testPessoa = PlaceholderValues.getPlaceholderPessoa();
        mockMvc.perform(post("/pessoa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPessoa)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testPessoaGet() throws Exception {
        Pessoa pessoa = PlaceholderValues.getPlaceholderPessoa();
        pessoaService.registerPessoa(pessoa);

        mockMvc.perform(get("/pessoa")).andExpect(status().isOk());
    }

    @Test
    public void testPessoaGetById() throws Exception {
        Pessoa pessoa = PlaceholderValues.getPlaceholderPessoa();
        Pessoa savedPessoa = pessoaService.registerPessoa(pessoa);
        mockMvc.perform(get("/pessoa/" + savedPessoa.getId().toString())).andExpect(status().isOk());
    }

    @Test
    public void testPessoasGetByIds() throws Exception {
        String pessoasIdsString = "";

        for (int i = 0; i < 5; i++) {
            Pessoa pessoa = PlaceholderValues.getPlaceholderPessoa();
            Pessoa savedPessoa = pessoaService.registerPessoa(pessoa);
            pessoasIdsString += savedPessoa.getId() + ",";
        }

        mockMvc.perform(get("/pessoa?ids=" + pessoasIdsString)).andExpect(status().isOk());
    }

    @Test
    public void testPessoaUpdate() throws Exception {
        Pessoa pessoa = PlaceholderValues.getPlaceholderPessoa();
        Pessoa savedPessoa = pessoaService.registerPessoa(pessoa);

        PessoaUpdateRequest updateRequest = new PessoaUpdateRequest();
        updateRequest.setNome("João da Silva Nonato");
        updateRequest.setDataNascimento("01/01/2000");
        updateRequest.setEnderecos(List.of(PlaceholderValues.getPlaceholderEndereco()));
        String updateRequestAsJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/pessoa/" + savedPessoa.getId().toString()).contentType(MediaType.APPLICATION_JSON).content(updateRequestAsJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testPessoaDelete() throws Exception {
        Pessoa pessoa = PlaceholderValues.getPlaceholderPessoa();

        Pessoa savedPessoa = pessoaService.registerPessoa(pessoa);

        mockMvc.perform(delete("/pessoa/" + savedPessoa.getId().toString())).andExpect(status().isOk());
    }

    @Test
    public void testPessoaSearch() throws Exception {
        Pessoa pessoa = PlaceholderValues.getPlaceholderPessoa();
        Pessoa savedPessoa = pessoaService.registerPessoa(pessoa);
        PessoaSearchRequest searchRequest = new PessoaSearchRequest();
        searchRequest.setBy(PessoaSearchOptions.nome);
        searchRequest.setQuery(savedPessoa.getNome());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("by", searchRequest.getBy().toString());
        params.add("query", searchRequest.getQuery());

        mockMvc.perform(get("/pessoa/search").queryParams(params)).andExpect(status().isOk());

    }

    @Test
    public void testPessoaSearchWithEndereco() throws Exception {
        String cidadeTarget = "São Paulo";
        for (int i = 0; i < 3; i++) {
            Pessoa pessoa = PlaceholderValues.getPlaceholderPessoa();
            Endereco endereco = PlaceholderValues.getPlaceholderEndereco();
            endereco.setCidade(cidadeTarget);
            pessoa.setEnderecos(List.of(endereco));
            pessoaService.registerPessoa(pessoa);
        }

        PessoaSearchRequest searchRequest = new PessoaSearchRequest();
        searchRequest.setBy(PessoaSearchOptions.cidade);
        searchRequest.setQuery(cidadeTarget);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("by", searchRequest.getBy().toString());
        params.add("query", searchRequest.getQuery());


        mockMvc.perform(get("/pessoa/search").queryParams(params)).andExpect(status().isOk());
    }


}
