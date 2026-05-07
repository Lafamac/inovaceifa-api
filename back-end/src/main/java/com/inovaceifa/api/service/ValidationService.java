package com.inovaceifa.api.service;

import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.repository.FazendaRepository;
import com.inovaceifa.api.repository.FuncionarioRepository;
import com.inovaceifa.api.repository.ProprietarioRepository;
import com.inovaceifa.api.validation.DocumentoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final ProprietarioRepository proprietarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FazendaRepository fazendaRepository;

    /* =========================================================
       VERIFICAR CPF GLOBAL
       ========================================================= */
    public boolean cpfDisponivel(String cpfInformado) {

        String cpf = DocumentoUtil.somenteNumeros(cpfInformado);

        if (!DocumentoUtil.cpfValido(cpf)) {
            throw new AuthException("CPF inválido");
        }

        boolean existeEmProprietario = proprietarioRepository.existsByCpf(cpf);
        boolean existeEmFuncionario = funcionarioRepository.existsByCpf(cpf);

        return !(existeEmProprietario || existeEmFuncionario);
    }

    /* =========================================================
       VERIFICAR CNPJ GLOBAL
       ========================================================= */
    public boolean cnpjDisponivel(String cnpjInformado) {

        String cnpj = DocumentoUtil.somenteNumeros(cnpjInformado);

        if (!DocumentoUtil.cnpjValido(cnpj)) {
            throw new AuthException("CNPJ inválido");
        }

        return !fazendaRepository.existsByCnpj(cnpj);
    }
}
