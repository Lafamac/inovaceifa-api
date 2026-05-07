package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.horamaquina.HoraMaquinaResponseDTO;
import com.inovaceifa.api.dto.horamaquina.HoraMaquinaCreateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.HoraMaquinaRepository;
import com.inovaceifa.api.repository.MaquinaRepository;
import com.inovaceifa.api.repository.OperacaoTalhaoRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HoraMaquinaService extends BaseCrudService<HoraMaquina, Long> {

    private final HoraMaquinaRepository horaMaquinaRepository;
    private final MaquinaRepository maquinaRepository;
    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final FuncionarioService funcionarioService;
    private final ContextoFazendaService contextoFazendaService;

    @Override
    protected JpaRepository<HoraMaquina, Long> getRepository() {
        return horaMaquinaRepository;
    }

    @Override
    protected void validarAcesso(HoraMaquina entity) {
        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        if (!entity.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Hora de máquina não pertence à fazenda ativa");
        }
    }

    public List<HoraMaquinaResponseDTO> listar() {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        return horaMaquinaRepository
                .findByFazendaIdAndSafraId(fazenda.getId(), safra.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PageResponseDTO<HoraMaquinaResponseDTO> listar(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        return PageUtils.toPageResponse(
                horaMaquinaRepository.findByFazendaIdAndSafraId(
                        fazenda.getId(),
                        safra.getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    @Transactional
    public HoraMaquinaResponseDTO criar(HoraMaquinaCreateDTO dto) {

        validarDados(dto);

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        Maquina maquina = buscarMaquinaValida(dto.getMaquinaId(), fazenda);

        Funcionario funcionario = null;

        if (dto.getFuncionarioId() != null) {
            funcionario = funcionarioService.buscarPorId(dto.getFuncionarioId());
        }

        OperacaoTalhao operacaoTalhao = null;

        if (dto.getOperacaoTalhaoId() != null) {
            operacaoTalhao = operacaoTalhaoRepository.findById(dto.getOperacaoTalhaoId())
                    .orElseThrow(() -> new AuthException("Operação de talhão não encontrada"));
        }

        BigDecimal horas = dto.getHorimetroFinal()
                .subtract(dto.getHorimetroInicial());

        HoraMaquina h = new HoraMaquina();

        h.setMaquina(maquina);
        h.setFazenda(fazenda);
        h.setSafra(safra);
        h.setFuncionario(funcionario);
        h.setOperacaoTalhao(operacaoTalhao);
        h.setServicoExec(dto.getServicoExec());
        h.setNroOs(dto.getNroOs());
        h.setDataExecucao(dto.getDataExecucao());
        h.setHorimetroInicial(dto.getHorimetroInicial());
        h.setHorimetroFinal(dto.getHorimetroFinal());
        h.setHorasTrabalhadas(horas);

        // 🔥 NOVO
        h.setCustoHora(dto.getCustoHora());

        h = super.salvarEntity(h);

        return toResponseDTO(h);
    }

    @Transactional
    public HoraMaquinaResponseDTO atualizar(Long id, HoraMaquinaCreateDTO dto) {

        validarDados(dto);

        HoraMaquina h = super.buscarEntity(id);

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        Maquina maquina = buscarMaquinaValida(dto.getMaquinaId(), fazenda);

        Funcionario funcionario = null;

        if (dto.getFuncionarioId() != null) {
            funcionario = funcionarioService.buscarPorId(dto.getFuncionarioId());
        }

        OperacaoTalhao operacaoTalhao = null;

        if (dto.getOperacaoTalhaoId() != null) {
            operacaoTalhao = operacaoTalhaoRepository.findById(dto.getOperacaoTalhaoId())
                    .orElseThrow(() -> new AuthException("Operação de talhão não encontrada"));
        }

        BigDecimal horas = dto.getHorimetroFinal()
                .subtract(dto.getHorimetroInicial());

        h.setMaquina(maquina);
        h.setFuncionario(funcionario);
        h.setOperacaoTalhao(operacaoTalhao);
        h.setServicoExec(dto.getServicoExec());
        h.setNroOs(dto.getNroOs());
        h.setDataExecucao(dto.getDataExecucao());
        h.setHorimetroInicial(dto.getHorimetroInicial());
        h.setHorimetroFinal(dto.getHorimetroFinal());
        h.setHorasTrabalhadas(horas);

        // 🔥 NOVO
        h.setCustoHora(dto.getCustoHora());

        h = super.salvarEntity(h);

        return toResponseDTO(h);
    }

    @Transactional
    public void excluir(Long id) {
        HoraMaquina h = super.buscarEntity(id);
        horaMaquinaRepository.delete(h);
    }

    private void validarDados(HoraMaquinaCreateDTO dto) {

        if (dto.getDataExecucao() == null || dto.getDataExecucao().isAfter(LocalDate.now())) {
            throw new AuthException("Data de execução inválida");
        }

        if (dto.getHorimetroInicial() == null || dto.getHorimetroFinal() == null) {
            throw new AuthException("Horímetros são obrigatórios");
        }

        if (dto.getHorimetroFinal().compareTo(dto.getHorimetroInicial()) <= 0) {
            throw new AuthException("Horímetro final deve ser maior");
        }
    }

    private Maquina buscarMaquinaValida(Long maquinaId, Fazenda fazenda) {

        Maquina maquina = maquinaRepository.findById(maquinaId)
                .orElseThrow(() -> new AuthException("Máquina não encontrada"));

        if (!maquina.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Máquina não pertence à fazenda ativa");
        }

        return maquina;
    }

    private HoraMaquinaResponseDTO toResponseDTO(HoraMaquina h) {

        return HoraMaquinaResponseDTO.builder()
                .id(h.getId())
                .maquinaId(h.getMaquina() != null ? h.getMaquina().getId() : null)
                .maquinaNome(h.getMaquina() != null ? h.getMaquina().getNome() : null)
                .fazendaId(h.getFazenda() != null ? h.getFazenda().getId() : null)
                .safraId(h.getSafra() != null ? h.getSafra().getId() : null)
                .funcionarioId(h.getFuncionario() != null ? h.getFuncionario().getId() : null)
                .operacaoTalhaoId(h.getOperacaoTalhao() != null ? h.getOperacaoTalhao().getId() : null)
                .servicoExec(h.getServicoExec())
                .nroOs(h.getNroOs())
                .dataExecucao(h.getDataExecucao())
                .horimetroInicial(h.getHorimetroInicial())
                .horimetroFinal(h.getHorimetroFinal())
                .horasTrabalhadas(h.getHorasTrabalhadas())
                .custoHora(h.getCustoHora()) // 🔥 NOVO
                .build();
    }
}