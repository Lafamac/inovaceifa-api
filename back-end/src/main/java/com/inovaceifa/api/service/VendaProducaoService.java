package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.venda.*;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaProducaoService {

    private final VendaProducaoRepository repository;
    private final SafraTalhaoRepository safraTalhaoRepository;
    private final ContextoFazendaService contexto;

    public VendaProducaoResponseDTO criar(VendaProducaoCreateDTO dto) {

        SafraTalhao st = safraTalhaoRepository.findById(dto.getSafraTalhaoId())
                .orElseThrow(() -> new AuthException("SafraTalhão não encontrado"));

        VendaProducao v = new VendaProducao();

        v.setSafraTalhao(st);
        v.setQuantidade(dto.getQuantidade());
        v.setPrecoUnitario(dto.getPrecoUnitario());
        v.setDataVenda(dto.getDataVenda());

        v.setProprietario(contexto.getProprietario());
        v.setFazenda(contexto.getFazendaAtiva());
        v.setSafra(contexto.getSafraAtiva());

        BigDecimal total = dto.getQuantidade().multiply(dto.getPrecoUnitario());
        v.setValorTotal(total);

        v = repository.save(v);

        return toResponse(v);
    }

    public List<VendaProducaoResponseDTO> listarPorTalhao(Long safraTalhaoId) {

        return repository.findBySafraTalhao_Id(safraTalhaoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BigDecimal calcularReceitaTalhao(Long safraTalhaoId) {
        return repository.sumValorBySafraTalhao(safraTalhaoId); // ✅ CORRIGIDO
    }

    private VendaProducaoResponseDTO toResponse(VendaProducao v) {

        return VendaProducaoResponseDTO.builder()
                .id(v.getId())
                .talhaoNome(v.getSafraTalhao().getTalhao().getNome())
                .quantidade(v.getQuantidade())
                .precoUnitario(v.getPrecoUnitario())
                .valorTotal(v.getValorTotal())
                .dataVenda(v.getDataVenda())
                .build();
    }
}