package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.ReferenciaDTO;
import com.inovaceifa.api.model.RefPedidoCompraStatus;
import com.inovaceifa.api.repository.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inovaceifa.api.model.RefParametro;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.model.RefParametro;
import com.inovaceifa.api.repository.RefTipoRateioRepository;
import com.inovaceifa.api.model.RefTipoRateio;

import java.util.List;

@RestController
@RequestMapping("/referencias")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReferenciaController {

    private final RefCulturaRepository refculturaRepository;
    private final RefResFerrugemRepository refresFerrugemRepository;
    private final RefStCultivoRepository stCultivoRepository;
    private final ContaGerencialRepository contaGerencialRepository;
    private final RefDespesaRepository refDespesaRepository;
    private final RefTipoMaquinaRepository refTipoMaquinaRepository;
    private final RefTipoMovProdutoRepository refTipoMovProdutoRepository;
    private final RefTipoGastoMaquinaRepository refTipoGastoMaquinaRepository;
    private final RefCentroCustoRepository refCentroCustoRepository;
    private final RefTipoPosseMaquinaRepository refTipoPosseMaquinaRepository;
    private final RefGrupoRepository refGrupoRepository;
    private final RefFamiliaRepository refFamiliaRepository;
    private final RefTipoPagamentoRepository refTipoPagamentoRepository;
    private final RefParametroRepository refParametroRepository;
    private final RefPedidoCompraStatusRepository refPedidoCompraStatusRepository;
    private final RefTipoRateioRepository refTipoRateioRepository;
    private final RefOperacaoTalhaoRepository refOperacaoTalhaoRepository;

    /* =========================================================
       CULTURAS
       ========================================================= */

    @GetMapping("/culturas")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> culturas() {

        var list = refculturaRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(c -> new ReferenciaDTO(c.getId(), c.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Culturas carregadas com sucesso")
        );
    }

    /* =========================================================
       RESISTÊNCIA À FERRUGEM
       ========================================================= */

    @GetMapping("/res-ferrugem")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> resFerrugem() {

        var list = refresFerrugemRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(r -> new ReferenciaDTO(r.getId(), r.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Resistências à ferrugem carregadas com sucesso")
        );
    }

        /* =========================================================
       OPERAÇÃO TALHÃO
       ========================================================= */

    @GetMapping("/operacao-talhao")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> operacaoTalhao() {

        var list = refOperacaoTalhaoRepository
                .findAll()
                .stream()
                .filter(r -> Boolean.TRUE.equals(r.getAtivo()))
                .sorted((a, b) ->
                        a.getDescricao().compareToIgnoreCase(b.getDescricao()))
                .map(r -> new ReferenciaDTO(
                        r.getId(),
                        r.getDescricao()
                ))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        list,
                        "Operações de talhão carregadas com sucesso"
                )
        );
    }

    /* =========================================================
       SISTEMA DE CULTIVO
       ========================================================= */

    @GetMapping("/st-cultivo")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> stCultivo() {

        var list = stCultivoRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(s -> new ReferenciaDTO(s.getId(), s.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Sistemas de cultivo carregados com sucesso")
        );
    }

    /* =========================================================
       CENTRO DE CUSTO
       ========================================================= */

    @GetMapping("/centro-custo")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> centroCusto() {

        var list = refCentroCustoRepository
                .findAllByAtivoTrueOrderByDescricaoAsc()
                .stream()
                .map(c -> new ReferenciaDTO(c.getId(), c.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Centros de custo carregados com sucesso")
        );
    }

    /* =========================================================
       GRUPO
       ========================================================= */

    @GetMapping("/grupo")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> grupo() {

        var list = refGrupoRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(g -> new ReferenciaDTO(g.getId(), g.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Grupos carregados com sucesso")
        );
    }

    /* =========================================================
   TIPO RATEIO
   ========================================================= */

    @GetMapping("/tipo-rateio")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> tipoRateio() {

        List<ReferenciaDTO> list = refTipoRateioRepository
                .findAllByAtivoTrueOrderByDescricaoAsc()
                .stream()
                .map((RefTipoRateio t) ->
                        new ReferenciaDTO(t.getId(), t.getDescricao())
                )
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Tipos de rateio carregados com sucesso")
        );
    }

    /* =========================================================
       FAMÍLIA
       ========================================================= */

    @GetMapping("/familia")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> familia() {

        var list = refFamiliaRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(f -> new ReferenciaDTO(f.getId(), f.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Famílias carregadas com sucesso")
        );
    }

    @GetMapping("/parametro-completo")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaResponseDTO>>> parametroCompleto() {

        var list = refParametroRepository.findAll()
                .stream()
                .filter(RefParametro::getAtivo)
                .sorted((a, b) -> a.getDescricao().compareToIgnoreCase(b.getDescricao()))
                .map(p -> ReferenciaResponseDTO.builder()
                        .id(p.getId())
                        .descricao(p.getDescricao())
                        .ativo(p.getAtivo())
                        .build()
                        .extra("chave", p.getChave())
                        .extra("valor", p.getValor())
                )
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Parâmetros carregados com valor")
        );
    }

    /* =========================================================
       CONTA GERENCIAL
       ========================================================= */

    @GetMapping("/conta-gerencial")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> contaGerencial() {

        var list = contaGerencialRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(c -> new ReferenciaDTO(c.getId(), c.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Contas gerenciais carregadas com sucesso")
        );
    }

    /* =========================================================
       DESPESA
       ========================================================= */

    @GetMapping("/despesa")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> despesa() {

        var list = refDespesaRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(d -> new ReferenciaDTO(d.getId(), d.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Despesas carregadas com sucesso")
        );
    }

    /* =========================================================
       TIPO PAGAMENTO
       ========================================================= */

    @GetMapping("/tipo-pagamento")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> tipoPagamento() {

        var list = refTipoPagamentoRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(t -> new ReferenciaDTO(t.getId(), t.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Tipos de pagamento carregados com sucesso")
        );
    }

    /* =========================================================
       TIPO POSSE MÁQUINA
       ========================================================= */

    @GetMapping("/tipo-posse-maquina")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> tipoPosseMaquina() {

        var list = refTipoPosseMaquinaRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(t -> new ReferenciaDTO(t.getId(), t.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Tipos de posse de máquina carregados com sucesso")
        );
    }

    /* =========================================================
       TIPO DE MÁQUINA
       ========================================================= */

    @GetMapping("/tipo-maquina")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> tipoMaquina() {

        var list = refTipoMaquinaRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(t -> new ReferenciaDTO(t.getId(), t.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Tipos de máquina carregados com sucesso")
        );
    }

    /* =========================================================
       TIPO MOVIMENTO PRODUTO
       ========================================================= */

    @GetMapping("/tipo-mov-produto")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> tipoMovProduto() {

        var list = refTipoMovProdutoRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(t -> new ReferenciaDTO(t.getId(), t.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Tipos de movimento de produto carregados com sucesso")
        );
    }

    /* =========================================================
       TIPO GASTO MÁQUINA
       ========================================================= */

    @GetMapping("/tipo-gasto-maquina")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> tipoGastoMaquina() {

        var list = refTipoGastoMaquinaRepository.findAllByOrderByDescricaoAsc()
                .stream()
                .map(t -> new ReferenciaDTO(t.getId(), t.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Tipos de gasto de máquina carregados com sucesso")
        );
    }

    @GetMapping("/parametro")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> parametro() {

        var list = refParametroRepository.findAll()
                .stream()
                .filter(RefParametro::getAtivo)
                .sorted((a, b) -> a.getDescricao().compareToIgnoreCase(b.getDescricao()))
                .map(p -> new ReferenciaDTO(p.getId(), p.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Parâmetros carregados com sucesso")
        );
    }

    /* =========================================================
       STATUS PEDIDO COMPRA (🔥 NOVO)
       ========================================================= */

    @GetMapping("/pedido-compra-status")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaDTO>>> pedidoCompraStatus() {

        var list = refPedidoCompraStatusRepository.findAll()
                .stream()
                .filter(RefPedidoCompraStatus::getAtivo)
                .sorted((a, b) -> a.getDescricao().compareToIgnoreCase(b.getDescricao()))
                .map(s -> new ReferenciaDTO(s.getId(), s.getDescricao()))
                .toList();

        return ResponseEntity.ok(
                ApiResponseDTO.success(list, "Status de pedido de compra carregados com sucesso")
        );
    }
}