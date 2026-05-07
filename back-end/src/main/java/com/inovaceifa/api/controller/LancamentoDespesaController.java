package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.financeiro.*;
import com.inovaceifa.api.dto.lancamento.*;
import com.inovaceifa.api.service.LancamentoDespesaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Tag(
        name = "Lançamentos de Despesas",
        description = "Controle de lançamentos de despesa"
)
@RestController
@RequestMapping("/financeiro/lancamentos")
@RequiredArgsConstructor
public class LancamentoDespesaController {

    private final LancamentoDespesaService service;

    /* =========================================================
       CRIAR
       ========================================================= */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LancamentoResponseDTO>> criar(
            @Valid @RequestBody LancamentoCreateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.criar(dto),
                        "Lançamento criado com sucesso"
                )
        );
    }

    /* =========================================================
       LISTAR (COM FILTROS)
       ========================================================= */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LancamentoResponseDTO>>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.listar(status, dataInicio, dataFim),
                        "Lançamentos listados com sucesso"
                )
        );
    }

    /* =========================================================
       MARCAR COMO PAGO
       ========================================================= */
    @PutMapping("/{id}/pagar")
    public ResponseEntity<ApiResponseDTO<Void>> pagar(@PathVariable Long id) {

        service.marcarComoPago(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Lançamento marcado como pago")
        );
    }

    /* =========================================================
       CANCELAR (SOFT DELETE)
       ========================================================= */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponseDTO<Void>> cancelar(@PathVariable Long id) {

        service.cancelar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Lançamento cancelado")
        );
    }

    /* =========================================================
       REATIVAR
       ========================================================= */
    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {

        service.reativar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Lançamento reativado")
        );
    }

    /* =========================================================
       RESUMO (COM OU SEM PERÍODO)
       ========================================================= */
    @GetMapping("/resumo")
    public ResponseEntity<ApiResponseDTO<FinanceiroResumoDTO>> resumo(
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.gerarResumoPorPeriodo(dataInicio, dataFim),
                        "Resumo financeiro gerado com sucesso"
                )
        );
    }

    /* =========================================================
       DASHBOARD POR CATEGORIA
       ========================================================= */
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponseDTO<DashboardFinanceiroDTO>> dashboard() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.gerarDashboard(),
                        "Dashboard financeiro gerado com sucesso"
                )
        );
    }

    /* =========================================================
       DASHBOARD MENSAL (NOVO)
       ========================================================= */
    @GetMapping("/dashboard-mensal")
    public ResponseEntity<ApiResponseDTO<List<DashboardMensalDTO>>> dashboardMensal() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.gerarDashboardMensal(),
                        "Dashboard mensal gerado com sucesso"
                )
        );
    }

    /* =========================================================
   CUSTO POR HECTARE
   ========================================================= */
    @GetMapping("/custo-hectare")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> custoPorHectare() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.calcularCustoPorHectare(),
                        "Custo por hectare calculado com sucesso"
                )
        );
    }

    /* =========================================================
       ORÇADO VS REALIZADO
       ========================================================= */
    @GetMapping("/orcado-vs-realizado")
    public ResponseEntity<ApiResponseDTO<OrcadoVsRealizadoDTO>> orcadoVsRealizado() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.gerarOrcadoVsRealizado(),
                        "Orçado vs Realizado gerado com sucesso"
                )
        );
    }

    /* =========================================================
       PROJEÇÃO SAFRA
       ========================================================= */
    @GetMapping("/projecao")
    public ResponseEntity<ApiResponseDTO<ProjecaoSafraDTO>> projecaoSafra() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.gerarProjecaoSafra(),
                        "Projeção da safra gerada com sucesso"
                )
        );
    }
}